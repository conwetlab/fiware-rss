#!/bin/bash

cd /home/fiware/pentaho/

/opt/pentaho/data-integration/pan.sh -file:generic_report_from_dbe.ktr -param:appProviderId=$3 -param:appProviderName=$4 -param:baseDir=./reports -param:countryId=1 -param:countryName=ES -param:obId=1 -param:obName=FI-WARE -param:startPeriod=$1 -param:endPeriod=$2

mkdir reports/$4/FI-WARE_ES/formatted

/opt/pentaho/data-integration/pan.sh -file:specific_report.ktr -param:appProviderId=$3 -param:appProviderName=$4 -param:baseDir=./reports -param:countryId=1 -param:countryName=ES -param:obId=1 -param:obName=FI-WARE -param:startPeriod=$1 -param:endPeriod=$2 -param:genericFileName=generic_report_$4_FI-WARE_ES_$1_$2.csv -param:operatorId=1
