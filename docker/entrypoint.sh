#!/bin/bash

if [ -z "$RSS_CLIENT_ID" ]; then
    echo "RSS_CLIENT_ID not defined"
    exit 1
fi

if [ -z "$RSS_SECRET" ]; then
    echo "RSS_SECRET not defined"
    exit 1
fi

if [ -z "$RSS_URL" ]; then
    echo "RSS_URL not defined"
    exit 1
fi

sed -i "s/config.client_id=.*$/config.client_id=$RSS_CLIENT_ID/" /etc/default/rss/oauth.properties
sed -i "s/config.client_secret=.*$/config.client_secret=$RSS_SECRET/" /etc/default/rss/oauth.properties
sed -i "s|config.callbackURL=.*$|config.callbackURL=$RSS_URL/fiware-rss/callback|" /etc/default/rss/oauth.properties

# Waiting for DB
sleep 15
set -m

exec "$@" &

echo "Waiting for creation of tables in DB"
sleep 60

echo "INSERT INTO \`bm_currency\` (NU_CURRENCY_ID, TX_ISO4217_CODE, TX_DESCRIPTION, TC_SYMBOL, TX_ISO4217_CODE_NUM, NU_ISO4217_DECIMALS) VALUES (1,'EUR','Euro','€','978',2),(2,'GBP','Esterlina','£','826',2),(3,'BRL','Verdadero brasileno','R$','986',2),(4,'ARS','Peso argentino','\$a','032',2),(5,'MXN','Peso mexicano','$','484',2),(6,'CLP','Peso chileno','$','152',2),(7,'PEN','Nuevo sol','S/.','604',2),(8,'VEF','Bolivar fuerte','Bs.','937',2),(9,'COP','Peso colombiano','$','170',2),(10,'USD','US Dolar','$','840',2),(11,'NIO','Cordoba oro','C$','558',2),(12,'GTQ','Quetzal','Q','320',2),(13,'SVC','El Salvador Colon','¢','222',2),(14,'PAB','Balboa','B/.','590',2),(15,'UYU','Peso Uruguayo','$','858',2),(16,'MYR','Malaysian ringgit','RM','458',2),(17,'NOK','Norwegian krone','kr','578',2),(18,'HUF','Hungarian forint','Ft','348',2);" | mysql -h rss_db -u root -pmy-secret-pw rss 

echo "Tables has been created"
fg 1
