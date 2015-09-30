#!/bin/bash

# Install basic dependencies
sudo apt-get update
sudo apt-get install -y  openjdk-7-jdk  tomcat7 mysql-client mysql-server

export TOMCATUSR="tomcat7"
export TOMCATPATH=/var/lib/tomcat7/webapps

# Install maven if needed
if [ -d "$INSPWD/fiware-rss" ]; then
    sudo apt-get install -y  maven
fi

sudo service mysql restart
