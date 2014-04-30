#!/bin/sh
cd /home/fiware/pentaho/
/opt/pentaho/data-integration/pan.sh -file:cdr_to_DB.ktr -param:cdrpath=/home/fiware/bills/cdr_fixed/ -level=Minimal 
cd /home/fiware/bills/cdr_fixed/
mv * /home/fiware/bills/cdr_backup
# Pruebas
#mysql -u root -p12341234 -h 10.95.14.162 -e "CALL FIWARE_SETTLEMENT.refunded_yn();"
# Testbed
mysql -u root -proot -e "CALL FIWARE_SETTLEMENT.refunded_yn();"
