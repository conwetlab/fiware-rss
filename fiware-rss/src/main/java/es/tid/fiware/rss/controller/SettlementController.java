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

import es.tid.fiware.rss.model.AppProviderParameter;
import es.tid.fiware.rss.model.RSSFile;
import es.tid.fiware.rss.model.RSUser;
import es.tid.fiware.rss.service.SettlementManager;
import es.tid.fiware.rss.service.UserManager;

@Controller
public class SettlementController {

    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(SettlementController.class);
    /**
     * User session attribute.
     */

    @Autowired
    private SettlementManager settlementManager;

    @Autowired
    private UserManager userManager;

    @Resource(name = "rssProps")
    private Properties rssProps;

    /**
     * Main page redirection.
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/")
    public String settlement(HttpServletRequest request, ModelMap model) {
        try {

            RSUser currUser = this.userManager.getCurrentUser();
            String aggregatorId = currUser.getEmail();

            model.addAttribute("aggregatorId", aggregatorId);
            model.addAttribute("is_admin", this.userManager.isAdmin());
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
    @RequestMapping("/models")
    public String rsModelsView(HttpServletRequest request, ModelMap model) {
        String result = null;
        try {
            RSUser currUser = this.userManager.getCurrentUser();
            model.addAttribute("aggregatorId", currUser.getEmail());

            return "RSModels";
        } catch (Exception e) {
            model.addAttribute("message", "RS Models:"  + e.getMessage());
            logger.error(e.getMessage(), e);
            result = "error";
        }
        return result;
    }

    /**
     * View RS models.
     * 
     * @param model
     * @return
     */
    @RequestMapping("/models/list")
    public String viewRS(ModelMap model) {
        try {
            logger.debug("viewRS - Start");
            return "viewRS";

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("message", "View RS Models:"  + e.getMessage());
            return "error";
        }
    }

    /**
     * View RSS Files.
     * 
     * @param aggregatorId
     * @param model
     * @return
     */
    @RequestMapping("/files")
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
    @RequestMapping("/transactions")
    public String viewTransactions(@QueryParam("aggregatorId") String aggregatorId, ModelMap model) {
        try {
            logger.debug("viewTransactions - Start");
            return "viewTransactions";

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("message","View Transactions:"  + e.getMessage());
            return "error";
        }
    }

    /**
     * View Providers.
     * 
     * @param model
     * @return
     */
    @RequestMapping("/providers")
    public String viewProviders(ModelMap model) {
        try {
            logger.debug("viewProviders - Start");
            return "viewProviders";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            model.addAttribute("message", "View Providers:"  + e.getMessage());
            return "error";
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
}
