#!/bin/bash

# Install basic dependencies
sudo apt-get update
sudo apt-get install -y  openjdk-7-jdk  tomcat7 mysql-client mysql-server

# Install maven if needed
if [ -d "$INSPWD/fiware-rss" ]; then
    sudo apt-get install -y  maven
fi

sudo service mysql restart
