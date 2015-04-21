/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
 *
 * Copyright (C) 2015, CoNWeT Lab., Universidad Politécnica de Madrid
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package es.tid.fiware.rss.controller;

import es.tid.fiware.rss.model.Aggregator;
import es.tid.fiware.rss.model.Algorithm;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.transaction.annotation.Transactional;

import es.tid.fiware.rss.model.AppProviderParameter;
import es.tid.fiware.rss.model.DbeAppProvider;
import es.tid.fiware.rss.model.DbeTransaction;
import es.tid.fiware.rss.model.RSSFile;
import es.tid.fiware.rss.model.RSSModel;
import es.tid.fiware.rss.model.RSSProvider;
import es.tid.fiware.rss.model.SetRevenueShareConf;
import es.tid.fiware.rss.oauth.model.OauthLoginWebSessionData;
import es.tid.fiware.rss.service.RSSModelsManager;
import es.tid.fiware.rss.service.SettlementManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SettlementController {

    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(SettlementController.class);
    /**
     * User session attribute.
     */
    private final String USER_SESSION = "userSession";

    @Autowired
    private SettlementManager settlementManager;

    @Autowired
    private RSSModelsManager modelsManager;

    @Resource(name = "rssProps")
    private Properties rssProps;

    /**
     * Main page redirection.
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/settlement")
    @Transactional
    public String settlement(HttpServletRequest request, ModelMap model) {
        try {
            OauthLoginWebSessionData session = (OauthLoginWebSessionData)
                request.getSession().getAttribute(USER_SESSION);
            String aggregatorId = null;
            if (session != null) {
                aggregatorId = session.getAggregatorId();
            }
            model.addAttribute("providers", settlementManager.getProviders(aggregatorId));
            model.addAttribute("aggregators", settlementManager.getAggregators());
            model.addAttribute("pentahoReportsUrl", rssProps.get("pentahoReportsUrl"));
            return "settlement";
        } catch (Exception e) {
            model.addAttribute("message", "Settlement:"  + e.getMessage());
            logger.error(e.getMessage(), e);
            return "error";
        }
    }

    /**
     * Returns Web view for the creation of Revenue Sharing models
     * @param request
     * @param model
     * @return The page to be rendered
     */
    @RequestMapping("/RSModels")
    public String rsModelsView(HttpServletRequest request, ModelMap model) {
        String result = null;
        try {
            OauthLoginWebSessionData session = (OauthLoginWebSessionData)
                request.getSession().getAttribute(USER_SESSION);

            return "RSModels";
        } catch (Exception e) {
            model.addAttribute("message", "RS Models:"  + e.getMessage());
            logger.error(e.getMessage(), e);
            result = "error";
        }
        return result;
    }
    
    /**
     * Do settlement.
     * 
     * @param dateFrom
     * @param dateTo
     * @param aggregatorId
     * @param model
     * @return the model and view
     */
    @RequestMapping(value = "/doSettlement", headers = "Accept=*/*", produces = "application/json")
    @ResponseBody
    public JsonResponse doSettlement(@QueryParam("dateFrom") String dateFrom,
        @QueryParam("dateTo") String dateTo, @QueryParam("aggregatorId") String aggregatorId,
        @QueryParam("providerId") String providerId, ModelMap model) {
        try {
            logger.debug("doSettlement - Provider: {} , aggregator: {}", providerId, aggregatorId);
            logger.debug("doSettlement - Start: Init" + dateFrom + ",End:" + dateTo);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat getDate = new SimpleDateFormat("MM/yyyy");
            String initDate = "";
            String endDate = "";
            if (dateFrom != null && !"".equalsIgnoreCase(dateFrom) && dateTo != null && !"".equalsIgnoreCase(dateTo)) {
                Date from = getDate.parse(dateFrom);
                Date to = getDate.parse(dateTo);
                initDate = format.format(from);
                endDate = format.format(to);
            } else {
                // By default use the current month
                GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
                cal.setTime(new Date());
                cal.set(Calendar.DAY_OF_MONTH, 1);
                initDate = format.format(cal.getTime());
                cal.add(Calendar.MONTH, 1);
                endDate = format.format(cal.getTime());
            }
            // Calculate settlement.
            settlementManager.runSettlement(initDate, endDate, aggregatorId, providerId);
            JsonResponse response = new JsonResponse();
            response.setMessage("Settlement proccess launched correctly.");
            response.setSuccess(true);
            return response;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            JsonResponse response = new JsonResponse();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            return response;
        }
    }

    /**
     * View RSS Files.
     * 
     * @param aggregatorId
     * @param model
     * @return
     */
    @RequestMapping("/viewFiles")
    public String viewFiles(@QueryParam("aggregatorId") String aggregatorId, ModelMap model) {
        try {
            logger.debug("viewFiles - Start");

            List<RSSFile> rssFilesList = settlementManager.getSettlementFiles(aggregatorId);

            model.addAttribute("RSSFilesList", rssFilesList);
            return "viewReportsList";

        } catch (Exception e) {
            model.addAttribute("message", "View files:"  + e.getMessage());
            logger.error(e.getMessage(), e);
            return "error";
        }
    }

    /**
     * View file.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/viewFile")
    public void viewfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String output_file_name = request.getParameter("rssname");
            logger.debug("Downloading file: " + output_file_name);

            File file = new File(output_file_name);
            FileInputStream is = new FileInputStream(file);

            response.setContentType("application/xls");
            response.setContentLength(new Long(file.length()).intValue());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            OutputStream os = response.getOutputStream();

            FileCopyUtils.copy(is, os);
            is.close();
            os.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return;
    }

    /**
     * View transactions.
     * 
     * @param aggregatorId
     * @param model
     * @return
     */
    @RequestMapping("/viewTransactions")
    public String viewTransactions(@QueryParam("aggregatorId") String aggregatorId, ModelMap model) {
        try {
            logger.debug("viewTransactions - Start");
            // List<String> result = settlementManager.runSelectTransactions();
            List<DbeTransaction> result = settlementManager.runSelectTransactions(aggregatorId);
            model.addAttribute("transactions", result);
            return "viewTransactions";

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("message","View Transactions:"  + e.getMessage());
            return "error";
        }
    }

    /**
     * View RS models.
     * 
     * @param aggregatorId
     * @param model
     * @return
     */
    @RequestMapping("/viewRS")
    public String viewRS(@QueryParam("aggregatorId") String aggregatorId, ModelMap model) {
        try {
            logger.debug("viewRS - Start");

            // List<String> result = settlementManager.runSelectRS();
            List<SetRevenueShareConf> result = settlementManager.getRSModels(aggregatorId);
            model.addAttribute("rsList", result);
            return "viewRS";

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("message", "View RS Models:"  + e.getMessage());
            return "error";
        }
    }

    /**
     * Create Model.
     * 
     * @param rsModel
     * @param model
     * @return
     */
    @RequestMapping(
            value = "/createRSModel",
            headers = "Accept=*/*",
            produces = "application/json",
            consumes ="application/json",
            method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse createRSModel(@RequestBody RSSModel rsModel, ModelMap model) {
        try {
            logger.debug("Creating RS model Aggregator: "
                    + rsModel.getAggregatorId() + " Provider: "
                    + rsModel.getOwnerProviderId() + " Class: "
                    + rsModel.getProductClass());

            modelsManager.createRssModel(rsModel);
            logger.debug("RS Model Created.");
            JsonResponse response = new JsonResponse();
            response.setMessage("RS Model Created.");
            response.setSuccess(true);
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            JsonResponse response = new JsonResponse();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            return response;
        }
    }

    /**
     * View Providers.
     * 
     * @param model
     * @return
     */
    @RequestMapping("/viewProviders")
    public String viewProviders(@QueryParam("aggregatorId") String aggregatorId, ModelMap model) {
        try {
            logger.debug("viewProviders - Start");
            List<DbeAppProvider> result = settlementManager.getProviders(aggregatorId);
            logger.debug("Provider list size: {}", result.size());
            // List<String> result = settlementManager.runSelectProviders();
            model.addAttribute("providersList", result);
            return "viewProviders";

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("message", "View Providers:"  + e.getMessage());
            return "error";
        }
    }

    /**
     * Create Provider.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "/createProvider", headers = "Accept=*/*", produces = "application/json")
    @ResponseBody
    public JsonResponse createProvider(@QueryParam("providerId") String providerId,
        @QueryParam("providerName") String providerName, @QueryParam("aggregatorId") String aggregatorId,
        ModelMap model) {
        try {
            logger.debug("createProvider.ProviderId:{} providerName:{} aggregatorId:{}", providerId, providerName,
                aggregatorId);
            if (null == providerName || "".equalsIgnoreCase(providerName) ||
                null == providerId || "".equalsIgnoreCase(providerId)) {
                logger.error("No necesary data provided");
                JsonResponse response = new JsonResponse();
                response.setMessage("No necesary data provided");
                response.setSuccess(false);
                return response;
            }
            settlementManager.runCreateProvider(providerId, providerName, aggregatorId);
            logger.debug("Provider Created.");
            JsonResponse response = new JsonResponse();
            response.setMessage("Provider Created.");
            response.setSuccess(true);
            return response;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            JsonResponse response = new JsonResponse();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            return response;
        }
    }

    /**
     * Clean transactions.
     * 
     * @param provider
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/clean", headers = "Accept=*/*", produces = "application/json")
    @ResponseBody
    public JsonResponse clean(@ModelAttribute("provider") @Valid AppProviderParameter provider, BindingResult result,
        ModelMap model) {
        try {
            logger.debug("clean - Start");
            if (result.hasErrors()) {
                JsonResponse response = new JsonResponse();
                response.setMessage("Error deleting");
                response.setSuccess(false);
                return response;
            }
            logger.debug("Deleting for provider: " + provider.getName());
            settlementManager.runClean(provider.getName());
            JsonResponse response = new JsonResponse();
            response.setMessage("Deleting Done.");
            response.setSuccess(true);
            return response;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            JsonResponse response = new JsonResponse();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            return response;
        }
    }

    /**
     * Logout.
     * 
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        try {
            logger.debug("logout");
            request.getSession().setAttribute(USER_SESSION, null);
            return "index";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    /**
     * Create Aggregator.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = "/createAggregator", headers = "Accept=*/*", produces = "application/json")
    @ResponseBody
    public JsonResponse createAggregator(@QueryParam("aggregatorId") String aggregatorId,
        @QueryParam("aggregatorName") String aggregatorName, ModelMap model) {
        try {
            logger.debug("createAggregator.AggregatorId:{} aggregatorName:{}", aggregatorId, aggregatorName);
            if (null == aggregatorName || "".equalsIgnoreCase(aggregatorName) ||
                null == aggregatorId || "".equalsIgnoreCase(aggregatorId)) {
                logger.error("No necesary data provided");
                JsonResponse response = new JsonResponse();
                response.setMessage("No necesary data provided");
                response.setSuccess(false);
                return response;
            }
            settlementManager.runCreateAggretator(aggregatorId, aggregatorName);
            logger.debug("Aggregator Created.");
            JsonResponse response = new JsonResponse();
            response.setMessage("Store Created.");
            response.setSuccess(true);
            return response;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            JsonResponse response = new JsonResponse();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            return response;
        }
    }

    @RequestMapping(value="/aggregators", headers = "Accept=*/*", produces = "application/json")
    @ResponseBody
    public List<Aggregator> getAggregators(ModelMap model) {
        List<Aggregator> result = new ArrayList<>();
        try {
            result = settlementManager.getAPIAggregators();
        } catch (Exception e) {
        }
        return result;
    }

    @RequestMapping(value="/providers", headers = "Accept=*/*", produces = "application/json")
    @ResponseBody
    public List<RSSProvider> getProviders(@QueryParam("aggregatorId") String aggregatorId,
            ModelMap model) {
        List<RSSProvider> result = new ArrayList<>();
        try {
            result = settlementManager.getAPIProviders(aggregatorId);
        } catch (Exception e) {
        }
        return result;
    }

    @RequestMapping(value="/algorithms", headers = "Accept=*/*", produces = "application/json")
    @ResponseBody
    public List<Algorithm> getAlgorithms(ModelMap model) {
        List<Algorithm> algorithms = new ArrayList<>();
        try {
            algorithms = modelsManager.getRSAlgorithms();
        } catch (Exception e) {
        }
        return algorithms;
    }
}
