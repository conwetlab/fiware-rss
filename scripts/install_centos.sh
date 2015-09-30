#!/bin/bash

sudo yum update
sudo yum install -y openjdk-7-jdk tomcat7 mysql-client mysql-server

# Install maven if needed
if [ -d "$INSPWD/fiware-rss" ]; then
    sudo yum install -y  install maven
fi

sudo service mysqld restart
sudo /usr/bin/mysql_secure_installation
