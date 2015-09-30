#!/bin/bash

export INSPWD=$PWD

if [ -f "/etc/centos-release" ]; then
        export SYS_V=centos
elif [ -f "/etc/issue" ]; then
    # This file can exist in Debian and centos
    CONTENT=$(cat /etc/issue)
    if [[ $CONTENT == *CentOS* ]]; then
        export SYS_V=centos
    elif [[ $CONTENT == *Ubuntu* ]]; then
        export SYS_V=ubuntu
    elif [[ $CONTENT == *Debian* ]]; then
        export SYS_V=debian
    fi
fi

if [[ $SYS_V == "centos" ]] ; then
    ./scripts/install_centos.sh
elif [[ $SYS_V == "ubuntu" || $SYS_V == "debian" ]]; then
    ./scripts/install_debian.sh
else
    echo "Operating system not supported"
    exit 1
fi

set -e
# Compile source code is needed
if [ -d "$INSPWD/fiware-rss" ]; then
    mvn clean
    mvn install -DskipTests=true
    cp $INSPWD/fiware-rss/target/fiware-rss.war $INSPWD/
    cp $INSPWD/rss-expendLimit/el-server/target/expenditureLimit.war $INSPWD/
fi

# Create properties directory
sudo mkdir /etc/default/rss/
sudo cp $INSPWD/properties/database.properties /etc/default/rss/database.properties
sudo cp $INSPWD/properties/oauth.properties /etc/default/rss/oauth.properties
sudo chown -R $TOMCATUSR:$TOMCATUSR /etc/default/rss

# Retrieve database preferences
echo "------------------------------------------------------------------------"
echo "'RSS' database is going to be created, Please introduce your mysql user and password with administration permissions (i.e root user)."
echo "> User:"
read MYSQLUSR 
echo "> Password:"
read -s MYSQLPASS

set +e

mysqladmin -u $MYSQLUSR -p$MYSQLPASS create rss

set -e
# Update database.properties
sudo sed -i "s|database.username=.*$|database.username=$MYSQLUSR|" /etc/default/rss/database.properties
sudo sed -i "s|database.password=.*$|database.password=$MYSQLPASS|" /etc/default/rss/database.properties


# Retrieve OAUth2 preferences
echo "------------------------------------------------------------------------"
echo "The RSS requires a FIWARE IdM to authenticate users. Please provide valid FIWARE credentials for this application"
echo "> FIWARE Client ID:"
read RSS_CLIENT_ID
echo "> FIWARE Client Secret:"
read RSS_SECRET
echo "> Include the URL (including port) where the RSS is going to run:"
read RSS_URL

# Update oauth.properties
sudo sed -i "s/config.client_id=.*$/config.client_id=$RSS_CLIENT_ID/" /etc/default/rss/oauth.properties
sudo sed -i "s/config.client_secret=.*$/config.client_secret=$RSS_SECRET/" /etc/default/rss/oauth.properties
sudo sed -i "s|config.callbackURL=.*$|config.callbackURL=$RSS_URL/fiware-rss/callback|" /etc/default/rss/oauth.properties


# Deploy war files
sudo cp $INSPWD/fiware-rss.war $TOMCATPATH/fiware-rss.war
sudo cp $INSPWD/expenditureLimit.war $TOMCATPATH/expenditureLimit.war

service tomcat7 restart

echo "Waiting for table creation"
sleep 60
mysql rss -u $MYSQLUSR -p$MYSQLPASS < scripts/currencies.sql

echo "Successfully Installed the RSS. You can update your properties located in /etc/default/rss"

