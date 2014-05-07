#!/bin/bash

cd /home/fiware/pentaho/

echo "Run run_report_all script"
echo "Run run_report_all script" >> ./logs/reports.log

if [ $# -eq 2 ]
then
  echo "Geting info from mysql" >> ./logs/reports.log
 #mysql -u root -p12341234 --skip-column-names -e "SELECT tx_appprovider_id,tx_name FROM FIWARE_SETTLEMENT.dbe_appprovider;" > file.txt
 mysql -u root -proot --skip-column-names -e "SELECT tx_appprovider_id,tx_name FROM FIWARE_SETTLEMENT.dbe_appprovider;" > file.txt
 cat file.txt| while read line;
 do
  echo $line
  COLS=($line);
  length=${#COLS[@]}
  appProviderid=${COLS[0]}
  appProviderName=""
  if [ $length -ge 3 ]
  then
    for (( i=1; i<$length; i++ ))
    do
      if [ $i = 1 ]
      then
         appProviderName=${COLS[i]}
      else
         appProviderName="${appProviderName}_${COLS[i]}"
      fi
    done
  else
    appProviderName=${COLS[1]}
  fi
  echo "appProviderid:$appProviderid"
  echo "appProviderName:$appProviderName"
  echo "appProviderid:$appProviderid" >> ./logs/reports.log
  echo "appProviderName:$appProviderName" >> ./logs/reports.log
  ./run_report.sh $1 $2 $appProviderid $appProviderName >> ./logs/reports.log
 done
 rm file.txt
 echo "$0 : All reports generated. See logs/reports.log"

else
echo "$0 : You must give the period in the format YYYY-MM YYYY-MM"
echo "$0 : You must give the period in the format YYYY-MM YYYY-MM" >> ./logs/reports.log
exit 1
fi