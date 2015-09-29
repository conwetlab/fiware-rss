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

package es.upm.fiware.rss.controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import es.upm.fiware.rss.model.RSUser;
import es.upm.fiware.rss.service.UserManager;

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
    private UserManager userManager;


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

    @RequestMapping("/reports")
    public String viewReportsList(ModelMap model) {
        try {
            logger.debug("viewTransactions - Start");
            return "viewReportsList";

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
}
