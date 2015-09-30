#!/bin/bash

sudo yum update
sudo yum install -y java-1.7.0-openjdk-devel tomcat

sudo rpm -Uvh http://dev.mysql.com/get/mysql-community-release-el7-5.noarch.rpm
sudo yum -y install mysql-community-server

# Install maven if needed
if [ -d "$INSPWD/fiware-rss" ]; then
    sudo yum install -y  install maven
fi

sudo /usr/bin/systemctl enable mysqld
sudo /usr/bin/systemctl start mysqld

sudo /usr/bin/mysql_secure_installation
