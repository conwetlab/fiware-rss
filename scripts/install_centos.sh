#!/bin/bash

sudo yum update
sudo yum install -y openjdk-7-jdk tomcat7 mysql-client mysql-server

export TOMCATUSR="tomcat"
export TOMCATPATH=/usr/share/tomcat7/webapps

# Install maven if needed
if [ -d "$INSPWD/fiware-rss" ]; then
    sudo yum install -y  install maven
fi

service mysqld restart
/usr/bin/mysql_secure_installation
