
# Revenue Settlement and Sharing System [![Build Status](https://travis-ci.org/telefonicaid/fiware-rss.svg?branch=develop)](https://travis-ci.org/telefonicaid/fiware-rss?branch=develop) [![Coverage Status](https://coveralls.io/repos/telefonicaid/fiware-rss/badge.png?branch=develop)](https://coveralls.io/r/telefonicaid/fiware-rss?branch=develop)

This is the code repository for the Revenue Settlement and Sharing System, a Java implementation developed as a part of the FI-WARE project (http://www.fi-ware.org/).

You find all the information on Revenue Settlement and Sharing System in its page in the FI-WARE Catalogue:

http://catalogue.fi-ware.org/enablers/revenue-settlement-and-sharing-system

Note that you don't need this repository code if you install it using the packages at FI-WARE repository: https://forge.fi-ware.org/frs/?group_id=7  


## Installing and Using the Revenue Settlement and Sharing System

The administration and programming manuals for Revenue Settlement and Sharing System are found in the FI-WARE Catalogue page,
under the "Documentation" tab.

http://catalogue.fi-ware.org/enablers/revenue-settlement-and-sharing-system/documentation


## Building Revenue Settlement and Sharing System

This section includes indications on how to build Revenue Settlement and Sharing System from this code repository.
The final result of this step will be 2 wars files: 
* one for RSS (fiware-rss.war) located at ${base_code}/fiware-rss/fiware-rss/target
* other for Expenditure Limits(expenditureLimit.war) located at ${base_code}/fiware-rss/rss-expendLimit/el-server/target.

where ${base_code} is the folder where the code will be downloaded from the repository.


Revenue Settlement and Sharing System is a Java 6 project built using for its management the software project tool Apache Maven.This means that it is required to install them. The following installation  has been done using as base O.S. Ubuntu 12.04.2 LTS. To do it:

* Install Java 6

```
sudo apt-get install openjdk-6-jdk
```

* Install Apache Maven

```
sudo apt-get install maven
```

The best way to obtain the code is by cloning the respository using git tool. 

* Install Git

```
sudo apt-get install git
```

* Obtain the code

```
git clone https://github.com/telefonicaid/fiware-rss
```

The first thing to do, it is compiling the code, avoiding running the test, to download dependencies. 
To do it, run the following command inside the folder download in the previous step.

```
mvn install -Dmaven.test.skip=true
```

## Testing Revenue Settlement and Sharing System

For testing purpose, a Mysql database will be necessary. To install it, use the following command:

```
sudo apt-get install mysql-server-5.5
```

During the installation you will be asked for a password: use root, that is the one use by default by testing.

Create a database for test purpose using the following command:

```
mysql -uroot -proot -e 'create database FIWARE_SETTLEMENT;'
```

Create the necessary tables using the following command from the directory ${base_code}/fiware-rss/

```
mysql -uroot -proot FIWARE_SETTLEMENT < ./rss-core/resources/datamodel/dbmodel.sql
```

Finally run the following command to execute the test:

```
mvn test -fae
```


## License

Revenue Settlement and Sharing System is licensed under Affero General Public License (GPL) version 3 (http://www.gnu.org/licenses/agpl-3.0.html).
