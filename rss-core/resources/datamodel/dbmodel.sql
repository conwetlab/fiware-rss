#
# Structure for table "bm_currency"
#

DROP TABLE IF EXISTS `bm_currency`;
CREATE TABLE `bm_currency` (
  `nu_currency_id` bigint(10) NOT NULL,
  `tx_iso4217_code` varchar(3) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_description` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tc_symbol` varchar(5) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_iso4217_code_num` varchar(3) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `nu_iso4217_decimals` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`nu_currency_id`),
  UNIQUE KEY `uq_bm_currency_iso4217` (`tx_iso4217_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_language"
#

DROP TABLE IF EXISTS `bm_language`;
CREATE TABLE `bm_language` (
  `nu_language_id` bigint(10) NOT NULL,
  `tx_name` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_iso639_1_code` char(3) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_iso639_code` char(2) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`nu_language_id`),
  UNIQUE KEY `uq_bm_language_iso639` (`tx_iso639_1_code`),
  UNIQUE KEY `uq_bm_language_name` (`tx_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_country"
#

DROP TABLE IF EXISTS `bm_country`;
CREATE TABLE `bm_country` (
  `nu_country_id` bigint(10) NOT NULL,
  `tx_itu_t212_code` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_iso3166_code` char(2) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `nu_currency_id` bigint(10) NOT NULL,
  `nu_language_id` bigint(10) NOT NULL,
  PRIMARY KEY (`nu_country_id`),
  UNIQUE KEY `uq_bm_country_iso3166` (`tx_iso3166_code`),
  UNIQUE KEY `uq_bm_country_itu_t212` (`tx_itu_t212_code`),
  KEY `fk_bm_currency_bm_country` (`nu_currency_id`),
  KEY `fk_bm_language_bm_country` (`nu_language_id`),
  CONSTRAINT `fk_bm_currency_bm_country` FOREIGN KEY (`nu_currency_id`) REFERENCES `bm_currency` (`nu_currency_id`),
  CONSTRAINT `fk_bm_language_bm_country` FOREIGN KEY (`nu_language_id`) REFERENCES `bm_language` (`nu_language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_methods_of_payment"
#

DROP TABLE IF EXISTS `bm_methods_of_payment`;
CREATE TABLE `bm_methods_of_payment` (
  `nu_mop_id` bigint(10) NOT NULL,
  `tx_name` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_description` varchar(250) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_code` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`nu_mop_id`),
  UNIQUE KEY `uq_mop_name` (`tx_name`),
  UNIQUE KEY `uq_mop_code` (`tx_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_customer_type"
#

DROP TABLE IF EXISTS `bm_customer_type`;
CREATE TABLE `bm_customer_type` (
  `nu_customer_type_id` bigint(10) NOT NULL,
  `tx_name` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_description` varchar(250) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `nu_mop_id` bigint(10) NOT NULL,
  PRIMARY KEY (`nu_customer_type_id`),
  UNIQUE KEY `uq_customer_type_name` (`tx_name`),
  KEY `fk_bm_methofpay_bm_custom_type` (`nu_mop_id`),
  CONSTRAINT `fk_bm_methofpay_bm_custom_type` FOREIGN KEY (`nu_mop_id`) REFERENCES `bm_methods_of_payment` (`nu_mop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_ob"
#

DROP TABLE IF EXISTS `bm_ob`;
CREATE TABLE `bm_ob` (
  `nu_ob_id` bigint(10) NOT NULL,
  `tx_name` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `nu_country_id` bigint(10) NOT NULL,
  `tx_brand` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`nu_ob_id`),
  UNIQUE KEY `uq_bm_ob_name` (`tx_name`),
  KEY `fk_bm_country_bm_ob` (`nu_country_id`),
  CONSTRAINT `fk_bm_country_bm_ob` FOREIGN KEY (`nu_country_id`) REFERENCES `bm_country` (`nu_country_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_ob_country"
#

DROP TABLE IF EXISTS `bm_ob_country`;
CREATE TABLE `bm_ob_country` (
  `nu_ob_id` bigint(10) NOT NULL,
  `nu_country_id` bigint(10) NOT NULL,
  `tc_pricepoints_yn` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_mnc_itu_t212` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`nu_ob_id`,`nu_country_id`),
  KEY `fk_bm_country_bm_ob_country` (`nu_country_id`),
  KEY `inx_mnc_itu_t212` (`tx_mnc_itu_t212`),
  CONSTRAINT `fk_bm_country_bm_ob_country` FOREIGN KEY (`nu_country_id`) REFERENCES `bm_country` (`nu_country_id`),
  CONSTRAINT `fk_bm_ob_bm_ob_country` FOREIGN KEY (`nu_ob_id`) REFERENCES `bm_ob` (`nu_ob_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_paymentbroker"
#

DROP TABLE IF EXISTS `bm_paymentbroker`;
CREATE TABLE `bm_paymentbroker` (
  `bm_pb_id` bigint(10) NOT NULL,
  `tx_name` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`bm_pb_id`),
  UNIQUE KEY `uq_bm_paymentbroker_name` (`tx_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_ob_mop"
#

DROP TABLE IF EXISTS `bm_ob_mop`;
CREATE TABLE `bm_ob_mop` (
  `nu_ob_id` bigint(10) NOT NULL,
  `nu_country_id` bigint(10) NOT NULL,
  `nu_mop_id` bigint(10) NOT NULL,
  `bm_pb_id` bigint(10) DEFAULT NULL,
  `dt_start_date` datetime NOT NULL,
  PRIMARY KEY (`nu_mop_id`,`nu_country_id`,`nu_ob_id`),
  KEY `fk_bm_ob_country_bm_ob_mop` (`nu_ob_id`,`nu_country_id`),
  KEY `fk_bm_paymentbroker_bm_ob_mop` (`bm_pb_id`),
  CONSTRAINT `fk_bm_methofpay_bm_ob_mop` FOREIGN KEY (`nu_mop_id`) REFERENCES `bm_methods_of_payment` (`nu_mop_id`),
  CONSTRAINT `fk_bm_ob_country_bm_ob_mop` FOREIGN KEY (`nu_ob_id`, `nu_country_id`) REFERENCES `bm_ob_country` (`nu_ob_id`, `nu_country_id`),
  CONSTRAINT `fk_bm_paymentbroker_bm_ob_mop` FOREIGN KEY (`bm_pb_id`) REFERENCES `bm_paymentbroker` (`bm_pb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_pb_mop"
#

DROP TABLE IF EXISTS `bm_pb_mop`;
CREATE TABLE `bm_pb_mop` (
  `bm_pb_id` bigint(10) NOT NULL,
  `nu_mop_id` bigint(10) NOT NULL,
  `tx_paymentmethod_code` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  PRIMARY KEY (`bm_pb_id`,`nu_mop_id`),
  KEY `fk_bm_methofpay_bm_pb_mop` (`nu_mop_id`),
  CONSTRAINT `fk_bm_methofpay_bm_pb_mop` FOREIGN KEY (`nu_mop_id`) REFERENCES `bm_methods_of_payment` (`nu_mop_id`),
  CONSTRAINT `fk_bm_paymentbroker_bm_pb_mop` FOREIGN KEY (`bm_pb_id`) REFERENCES `bm_paymentbroker` (`bm_pb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_price_point"
#

DROP TABLE IF EXISTS `bm_price_point`;
CREATE TABLE `bm_price_point` (
  `tx_price_point_id` varchar(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `nu_price` decimal(20,5) NOT NULL,
  `dt_edit_date` datetime NOT NULL,
  `nu_ob_id` bigint(10) NOT NULL,
  `nu_country_id` bigint(10) NOT NULL,
  `nu_currency_id` bigint(10) NOT NULL,
  PRIMARY KEY (`tx_price_point_id`,`nu_ob_id`,`nu_country_id`),
  KEY `fk_bm_ob_country_bm_pricepoint` (`nu_ob_id`,`nu_country_id`),
  KEY `fk_currency_vs_price_point` (`nu_currency_id`),
  CONSTRAINT `fk_bm_ob_country_bm_pricepoint` FOREIGN KEY (`nu_ob_id`, `nu_country_id`) REFERENCES `bm_ob_country` (`nu_ob_id`, `nu_country_id`),
  CONSTRAINT `fk_currency_vs_price_point` FOREIGN KEY (`nu_currency_id`) REFERENCES `bm_currency` (`nu_currency_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_service"
#

DROP TABLE IF EXISTS `bm_service`;
CREATE TABLE `bm_service` (
  `nu_service_id` bigint(10) NOT NULL,
  `tx_name` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_description` varchar(250) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tc_thirdparties_yn` char(1) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `dt_start_date` datetime NOT NULL,
  `tc_status` char(1) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tc_api_taxes_yn` char(1) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL DEFAULT 'N',
  PRIMARY KEY (`nu_service_id`),
  UNIQUE KEY `uq_bm_service_name` (`tx_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_service_deployment"
#

DROP TABLE IF EXISTS `bm_service_deployment`;
CREATE TABLE `bm_service_deployment` (
  `nu_deployment_id` bigint(10) NOT NULL,
  `nu_service_id` bigint(10) NOT NULL,
  `nu_ob_id` bigint(10) NOT NULL,
  `nu_country_id` bigint(10) NOT NULL,
  `dt_deployment_date` datetime DEFAULT NULL,
  `tc_status` char(1) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`nu_deployment_id`),
  UNIQUE KEY `uq_servobcount_deployment` (`nu_service_id`,`nu_ob_id`,`nu_country_id`),
  KEY `fk_bm_ob_count_bm_serv_deploy` (`nu_ob_id`,`nu_country_id`),
  CONSTRAINT `fk_bm_ob_count_bm_serv_deploy` FOREIGN KEY (`nu_ob_id`, `nu_country_id`) REFERENCES `bm_ob_country` (`nu_ob_id`, `nu_country_id`),
  CONSTRAINT `fk_bm_service_bm_service_deplo` FOREIGN KEY (`nu_service_id`) REFERENCES `bm_service` (`nu_service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_servdeploy_mop"
#

DROP TABLE IF EXISTS `bm_servdeploy_mop`;
CREATE TABLE `bm_servdeploy_mop` (
  `nu_service_mop_id` bigint(10) NOT NULL,
  `nu_mop_id` bigint(10) NOT NULL,
  `nu_customer_type_id` bigint(10) NOT NULL,
  `nu_deployment_id` bigint(10) NOT NULL,
  `tc_default_yn` char(1) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`nu_service_mop_id`),
  KEY `fk_bm_custo_type_bm_serdep_mop` (`nu_customer_type_id`),
  KEY `fk_bm_methofpay_bm_servdep_mop` (`nu_mop_id`),
  KEY `fk_bm_serv_depl_bm_servdep_mop` (`nu_deployment_id`),
  CONSTRAINT `fk_bm_custo_type_bm_serdep_mop` FOREIGN KEY (`nu_customer_type_id`) REFERENCES `bm_customer_type` (`nu_customer_type_id`),
  CONSTRAINT `fk_bm_methofpay_bm_servdep_mop` FOREIGN KEY (`nu_mop_id`) REFERENCES `bm_methods_of_payment` (`nu_mop_id`),
  CONSTRAINT `fk_bm_serv_depl_bm_servdep_mop` FOREIGN KEY (`nu_deployment_id`) REFERENCES `bm_service_deployment` (`nu_deployment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_service_product_type"
#

DROP TABLE IF EXISTS `bm_service_product_type`;
CREATE TABLE `bm_service_product_type` (
  `nu_service_product_type_id` bigint(10) NOT NULL,
  `tx_name` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_description` varchar(250) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `nu_service_id` bigint(10) NOT NULL,
  `tc_is_default_yn` char(1) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT 'N',
  PRIMARY KEY (`nu_service_id`,`nu_service_product_type_id`),
  UNIQUE KEY `uq_service_product_type_name` (`nu_service_id`,`tx_name`),
  CONSTRAINT `fk_bm_service_bm_serv_prod_typ` FOREIGN KEY (`nu_service_id`) REFERENCES `bm_service` (`nu_service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_product"
#

DROP TABLE IF EXISTS `bm_product`;
CREATE TABLE `bm_product` (
  `nu_product_id` bigint(10) NOT NULL,
  `tx_name` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_product_description` varchar(250) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `nu_service_id` bigint(10) NOT NULL,
  `tc_is_service_product_yn` char(1) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL DEFAULT 'N',
  `nu_service_product_type_id` bigint(10) DEFAULT NULL,
  PRIMARY KEY (`nu_product_id`),
  UNIQUE KEY `uq_product_id_and_service` (`nu_service_id`,`tx_name`),
  KEY `fk_bm_se_pr_type_bm_product` (`nu_service_id`,`nu_service_product_type_id`),
  CONSTRAINT `fk_bm_service_bm_product` FOREIGN KEY (`nu_service_id`) REFERENCES `bm_service` (`nu_service_id`),
  CONSTRAINT `fk_bm_se_pr_type_bm_product` FOREIGN KEY (`nu_service_id`, `nu_service_product_type_id`) REFERENCES `bm_service_product_type` (`nu_service_id`, `nu_service_product_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "bm_product_vs_ob"
#

DROP TABLE IF EXISTS `bm_product_vs_ob`;
CREATE TABLE `bm_product_vs_ob` (
  `nu_product_id` bigint(10) NOT NULL,
  `nu_deployment_id` bigint(10) NOT NULL,
  `nu_price` decimal(10,4) DEFAULT NULL,
  PRIMARY KEY (`nu_product_id`,`nu_deployment_id`),
  KEY `fk_bm_serv_depl_bm_prod_vs_ob` (`nu_deployment_id`),
  CONSTRAINT `fk_bm_product_bm_product_vs_ob` FOREIGN KEY (`nu_product_id`) REFERENCES `bm_product` (`nu_product_id`),
  CONSTRAINT `fk_bm_serv_depl_bm_prod_vs_ob` FOREIGN KEY (`nu_deployment_id`) REFERENCES `bm_service_deployment` (`nu_deployment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "dbe_aggregator"
#

DROP TABLE IF EXISTS `dbe_aggregator`;
CREATE TABLE `dbe_aggregator` (
  `tx_email` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL DEFAULT '',
  `tx_name` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`tx_email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for table "dbe_appprovider"
#

DROP TABLE IF EXISTS `dbe_appprovider`;
CREATE TABLE `dbe_appprovider` (
  `tx_appprovider_id` varchar(50) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_name` varchar(256) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`tx_appprovider_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "dbe_aggregator_appprovider"
#

DROP TABLE IF EXISTS `dbe_aggregator_appprovider`;
CREATE TABLE `dbe_aggregator_appprovider` (
  `tx_email` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL DEFAULT '',
  `tx_appprovider_id` varchar(50) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL DEFAULT '',
  KEY `FK_dbe_agg_prov_dbe_agg` (`tx_email`),
  KEY `FK_dbe_agg_prov_dbe_app` (`tx_appprovider_id`),
  CONSTRAINT `FK_dbe_agg_prov_dbe_agg` FOREIGN KEY (`tx_email`) REFERENCES `dbe_aggregator` (`tx_email`),
  CONSTRAINT `FK_dbe_agg_prov_dbe_app` FOREIGN KEY (`tx_appprovider_id`) REFERENCES `dbe_appprovider` (`tx_appprovider_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for table "dbe_appprovider_application"
#

DROP TABLE IF EXISTS `dbe_appprovider_application`;
CREATE TABLE `dbe_appprovider_application` (
  `tx_application_id` varchar(50) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_appprovider_id` varchar(50) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`tx_appprovider_id`,`tx_application_id`),
  CONSTRAINT `fk_appprov_vs_appprov_applicat` FOREIGN KEY (`tx_appprovider_id`) REFERENCES `dbe_appprovider` (`tx_appprovider_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "dbe_expend_control"
#

DROP TABLE IF EXISTS `dbe_expend_control`;
CREATE TABLE `dbe_expend_control` (
  `NU_SERVICE_ID` bigint(10) NOT NULL,
  `NU_CURRENCY_ID` bigint(10) NOT NULL,
  `TX_APPPROVIDER_ID` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `NU_OB_ID` bigint(10) NOT NULL,
  `NU_COUNTRY_ID` bigint(10) NOT NULL,
  `FT_EXPENSED_AMOUNT` decimal(12,4) DEFAULT NULL,
  `DT_NEXT_PERIOD_START` datetime DEFAULT NULL,
  `TX_NOTIFICATIONS` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `TX_EL_TYPE` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `TX_END_USER_ID` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`NU_SERVICE_ID`,`NU_CURRENCY_ID`,`NU_OB_ID`,`NU_COUNTRY_ID`,`TX_EL_TYPE`,`TX_END_USER_ID`,`TX_APPPROVIDER_ID`),
  KEY `FK_dbe_expend_control_bm_currency` (`NU_CURRENCY_ID`),
  KEY `FK_dbe_expend_control_bm_ob_country` (`NU_OB_ID`,`NU_COUNTRY_ID`),
  KEY `FK_dbe_expend_control_dbe_appprovider` (`TX_APPPROVIDER_ID`),
  CONSTRAINT `FK_dbe_expend_control_bm_currency` FOREIGN KEY (`NU_CURRENCY_ID`) REFERENCES `bm_currency` (`nu_currency_id`),
  CONSTRAINT `FK_dbe_expend_control_bm_ob_country` FOREIGN KEY (`NU_OB_ID`, `NU_COUNTRY_ID`) REFERENCES `bm_ob_country` (`nu_ob_id`, `nu_country_id`),
  CONSTRAINT `FK_dbe_expend_control_bm_service` FOREIGN KEY (`NU_SERVICE_ID`) REFERENCES `bm_service` (`nu_service_id`),
  CONSTRAINT `FK_dbe_expend_control_dbe_appprovider` FOREIGN KEY (`TX_APPPROVIDER_ID`) REFERENCES `dbe_appprovider` (`tx_appprovider_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "dbe_expend_limit"
#

DROP TABLE IF EXISTS `dbe_expend_limit`;
CREATE TABLE `dbe_expend_limit` (
  `TX_EL_TYPE` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `FT_MAX_AMOUNT` decimal(12,4) DEFAULT NULL,
  `TX_NOTIF_AMOUNTS` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `TX_END_USER_ID` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `TX_APPPROVIDER_ID` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `NU_OB_ID` bigint(10) NOT NULL,
  `NU_COUNTRY_ID` bigint(10) NOT NULL,
  `NU_SERVICE_ID` bigint(10) NOT NULL,
  `NU_CURRENCY_ID` bigint(10) NOT NULL,
  PRIMARY KEY (`NU_OB_ID`,`NU_CURRENCY_ID`,`TX_EL_TYPE`,`TX_END_USER_ID`,`NU_COUNTRY_ID`,`NU_SERVICE_ID`,`TX_APPPROVIDER_ID`),
  KEY `FK_dbe_expend_limit_bm_currency` (`NU_CURRENCY_ID`),
  KEY `FK_dbe_expend_limit_bm_ob_country` (`NU_OB_ID`,`NU_COUNTRY_ID`),
  KEY `FK_dbe_expend_limit_bm_service` (`NU_SERVICE_ID`),
  KEY `FK_dbe_expend_limit_dbe_appprovider` (`TX_APPPROVIDER_ID`),
  CONSTRAINT `FK_dbe_expend_limit_bm_currency` FOREIGN KEY (`NU_CURRENCY_ID`) REFERENCES `bm_currency` (`nu_currency_id`),
  CONSTRAINT `FK_dbe_expend_limit_bm_ob_country` FOREIGN KEY (`NU_OB_ID`, `NU_COUNTRY_ID`) REFERENCES `bm_ob_country` (`nu_ob_id`, `nu_country_id`),
  CONSTRAINT `FK_dbe_expend_limit_bm_service` FOREIGN KEY (`NU_SERVICE_ID`) REFERENCES `bm_service` (`nu_service_id`),
  CONSTRAINT `FK_dbe_expend_limit_dbe_appprovider` FOREIGN KEY (`TX_APPPROVIDER_ID`) REFERENCES `dbe_appprovider` (`tx_appprovider_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "dbe_system_properties"
#

DROP TABLE IF EXISTS `dbe_system_properties`;
CREATE TABLE `dbe_system_properties` (
  `tx_param_data_type` varchar(50) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_param_name` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_param_description` varchar(250) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_param_value` varchar(500) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_param_class` varchar(50) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  PRIMARY KEY (`tx_param_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "dbe_transaction"
#

DROP TABLE IF EXISTS `dbe_transaction`;
CREATE TABLE `dbe_transaction` (
  `tx_transaction_id` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `ts_request` datetime NOT NULL,
  `ts_response` datetime DEFAULT NULL,
  `tx_end_user_id` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tc_transaction_type` char(1) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_global_user_id` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tc_transaction_status` char(1) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_err_code` varchar(10) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_err_msg` varchar(250) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_reference_code` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_org_transaction_id` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_callback_url` varchar(300) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `ft_charged_total_amount` decimal(9,4) DEFAULT NULL,
  `ft_request_amount` decimal(9,4) DEFAULT NULL,
  `tx_request_code` varchar(64) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `nu_currency_id` bigint(10) DEFAULT NULL,
  `ft_request_tax_amount` decimal(9,4) DEFAULT NULL,
  `nu_service_id` bigint(10) NOT NULL,
  `nu_product_id` bigint(10) DEFAULT NULL,
  `ts_client_date` datetime DEFAULT NULL,
  `tx_op_attributes` varchar(1000) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_pb_correlation_id` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_partition` varchar(2) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_org_pb_correlation_id` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_request_amount_desc` varchar(250) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_oper_serv_desc` varchar(250) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_taxes_applied` varchar(250) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `nu_internal_ob_id` bigint(10) NOT NULL,
  `tx_pb_spec_data` varchar(250) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `nu_internal_country_id` bigint(10) NOT NULL,
  `nu_internal_mop_id` bigint(10) NOT NULL,
  `bm_pb_id` bigint(10) DEFAULT NULL,
  `tx_trans_status_desc` varchar(250) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `nu_request_mop_id` bigint(10) DEFAULT NULL,
  `ft_internal_total_amount` decimal(9,4) DEFAULT NULL,
  `ft_charged_tax_amount` decimal(9,4) DEFAULT NULL,
  `ft_internal_tax_amount` decimal(9,4) DEFAULT NULL,
  `tx_pb_result_code` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_pb_result_code_desc` varchar(256) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_masked_account_num` varchar(30) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `ft_request_total_amount` decimal(8,4) DEFAULT NULL,
  `tx_channel` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_application_id` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `tx_product_class` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_merchant_id` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_operation_nature` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tc_is_refunded_yn` char(1) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL DEFAULT 'N',
  `tc_is_captured_yn` char(1) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL DEFAULT 'N',
  `nu_client_country_id` bigint(10) DEFAULT NULL,
  `nu_client_ob_id` bigint(10) DEFAULT NULL,
  `tx_subtype_mop` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_client_zip_code` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `ts_status_date` datetime NOT NULL,
  `tx_app_provider` varchar(20) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_pb_url_redirection` varchar(512) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_refund_reason` varchar(40) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `tx_redirect_url` varchar(512) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
  `ft_internal_amount` decimal(9,4) DEFAULT NULL,
  `ft_charged_amount` decimal(9,4) DEFAULT NULL,
  PRIMARY KEY (`tx_transaction_id`),
  UNIQUE KEY `uq_reference_x_serv` (`tx_reference_code`,`nu_service_id`,`tx_application_id`),
  KEY `fk_bm_currency_dbe_transaction` (`nu_currency_id`),
  KEY `fk_bm_mop_dbe_transaction` (`nu_request_mop_id`),
  KEY `fk_bm_ob_country_dbe_transact` (`nu_client_ob_id`,`nu_client_country_id`),
  KEY `fk_bm_ob_mop_dbe_transaction` (`nu_internal_ob_id`,`nu_internal_country_id`,`nu_internal_mop_id`),
  KEY `fk_bm_pbroker_dbe_transaction` (`bm_pb_id`),
  KEY `fk_bm_product_dbe_transaction` (`nu_product_id`),
  KEY `fk_bm_service_dbe_transaction` (`nu_service_id`),
  CONSTRAINT `fk_bm_currency_dbe_transaction` FOREIGN KEY (`nu_currency_id`) REFERENCES `bm_currency` (`nu_currency_id`),
  CONSTRAINT `fk_bm_mop_dbe_transaction` FOREIGN KEY (`nu_request_mop_id`) REFERENCES `bm_methods_of_payment` (`nu_mop_id`),
  CONSTRAINT `fk_bm_ob_country_dbe_transact` FOREIGN KEY (`nu_client_ob_id`, `nu_client_country_id`) REFERENCES `bm_ob_country` (`nu_ob_id`, `nu_country_id`),
  CONSTRAINT `fk_bm_ob_mop_dbe_transaction` FOREIGN KEY (`nu_internal_ob_id`, `nu_internal_country_id`, `nu_internal_mop_id`) REFERENCES `bm_ob_mop` (`nu_ob_id`, `nu_country_id`, `nu_mop_id`),
  CONSTRAINT `fk_bm_pbroker_dbe_transaction` FOREIGN KEY (`bm_pb_id`) REFERENCES `bm_paymentbroker` (`bm_pb_id`),
  CONSTRAINT `fk_bm_product_dbe_transaction` FOREIGN KEY (`nu_product_id`) REFERENCES `bm_product` (`nu_product_id`),
  CONSTRAINT `fk_bm_service_dbe_transaction` FOREIGN KEY (`nu_service_id`) REFERENCES `bm_service` (`nu_service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

#
# Structure for table "set_revenue_share_conf"
#

DROP TABLE IF EXISTS `set_revenue_share_conf`;
CREATE TABLE `set_revenue_share_conf` (
  `tx_appprovider_id` varchar(50) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `nu_ob_id` bigint(10) NOT NULL,
  `nu_country_id` bigint(10) NOT NULL,
  `nu_perc_revenue_share` int(5) NOT NULL DEFAULT '0',
  `tx_product_class` varchar(40) NOT NULL DEFAULT '',
  PRIMARY KEY (`tx_appprovider_id`,`nu_ob_id`,`tx_product_class`,`nu_country_id`),
  KEY `fk_ob_coun_to_reveneu_share_c` (`nu_ob_id`,`nu_country_id`),
  CONSTRAINT `fk_appprov_to_reveneu_share_c` FOREIGN KEY (`tx_appprovider_id`) REFERENCES `dbe_appprovider` (`tx_appprovider_id`),
  CONSTRAINT `fk_ob_coun_to_reveneu_share_c` FOREIGN KEY (`nu_ob_id`, `nu_country_id`) REFERENCES `bm_ob_country` (`nu_ob_id`, `nu_country_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;