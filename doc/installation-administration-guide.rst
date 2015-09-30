=====================================
Installation and Administration Guide
=====================================

------------
Introduction
------------

This Installation and Administration Guide covers RSS-RI version 4.4.3. Any feedback on this document is highly welcomed, including bugs, typos or things you think should be included but aren't. Please send it to the "Contact Person" email that appears in the `Catalogue page for this GEi <http://catalogue.fiware.org/enablers/revenue-settlement-and-sharing-system-rss-ri>`__.

-------------------
System Requirements
-------------------

Hardware Requirements
=====================

The following table contains the minimum resource requirements for running the RSS: 

* CPU: 1-2 cores with at least 2.0 GHZ
* Physical RAM: 2GB
* Disk Space: 10GB The actual disk space depends on the amount of transactions strored in the database.

Operating System Support
========================
The RSS has been tested in the following Operating Systems:

* Ubuntu 12.04, 14.04
* CentOS 6.3, 6.5, 7.0

Software Requirements
===================== 
In order to have the RSS running, the following software is needed. However, these dependencies are not meant to be installed manually in this step, as they will be installed throughout the documentation:

* Java JDK 7
* Apache Tomcat 7
* MySQL >= 5.5

---------------------
Software Installation
---------------------

Getting the RSS Software
========================

The packaged version of the RSS software can be downloaded from:

* `The FIWARE catalgue <http://catalogue.fiware.org/enablers/revenue-settlement-and-sharing-system-rss-ri/downloads>`__.

This package contains the war files of the RSS as well as the installation scripts used in this document.

Alternatively, it is possible to install the RSS from the sources published in GitHub. To clone the repository, the git package is needed: ::

    # Ubuntu/Debian
    $ apt-get install git

    # CentOS
    $ yum -y install git


To download the source code usig git, execute the following command: ::

    $ git clone https://github.com/conwetlab/fiware-rss.git

Installing the RSS Using Scripts
================================

In order to facilitate the installation of the RSS, the script *install.sh* has been provided. This script installs all needed dependencies, configures the RSS and deploys it.

.. note::
    The script *install.sh* installs java and tomcat. If you have those systems already installed, you may want to install the RSS manually as explained in the next section.


To use the installation script execute the following command: ::

    $ ./install.sh

The installation script, installs MySQL and creates the root user. During this process you will be asked to provide a password for this user:

* Ubuntu/Debian

.. image:: /images/installation/rss_inst.png

* CentOS ::

    NOTE: RUNNING ALL PARTS OF THIS SCRIPT IS RECOMMENDED FOR ALL MySQL
    SERVERS IN PRODUCTION USE!  PLEASE READ EACH STEP CAREFULLY!

    In order to log into MySQL to secure it, we'll need the current
    password for the root user.  If you've just installed MySQL, and
    you haven't set the root password yet, the password will be blank,
    so you should just press enter here.

    Enter current password for root (enter for none):

    OK, successfully used password, moving on...

    Setting the root password ensures that nobody can log into the MySQL
    root user without the proper authorisation.

    Set root password? [Y/n] y
    New password: 
    Re-enter new password: 
    Password updated successfully!
    Reloading privilege tables..
        ... Success!

    By default, a MySQL installation has an anonymous user, allowing anyone
    to log into MySQL without having to have a user account created for
    them.  This is intended only for testing, and to make the installation
    go a bit smoother.  You should remove them before moving into a
    production environment.

    Remove anonymous users? [Y/n]

    Normally, root should only be allowed to connect from 'localhost'.  This
    ensures that someone cannot guess at the root password from the network.

    Disallow root login remotely? [Y/n] 

    ... skipping.

    By default, MySQL comes with a database named 'test' that anyone can
    access.  This is also intended only for testing, and should be removed
    before moving into a production environment.

    Remove test database and access to it? [Y/n]

    Reloading the privilege tables will ensure that all changes made so far
    will take effect immediately.

    Reload privilege tables now? [Y/n]


Then, the installation script creates the `RSS` database. In order to be able to do that, you are asked to provide root MySQL credentials. ::

    'RSS' database is going to be created, Please introduce your mysql user and password with administration permissions (i.e root user).
    > user:
    root
    > Password:

The RSS uses the `FIWARE Identity Manager <https://account.lab.fiware.org/>`__  for authenticating user. In this regard, the installation script asks you to provide valid OAuth2 credentials for your application. Additionally, it is also required to include the URL where the service is going to run (only host and port): ::

    ------------------------------------------------------------------------
    The RSS requires a FIWARE IdM to authenticate users. Please provide valid FIWARE credentials for this application
    > FIWARE Client ID:
    {FIWARE CLIENT ID}
    > FIWARE Client Secret:
    {FIWARE CLIENT SECRET}
    > Include the URL (including port) where the RSS is going to run:
    http://[HOST]:[PORT]

During this installation process, the properties files are created in `/etc/default/rss` using the provided information.

Manually Installing the RSS
===========================

Installing Basic Dependencies
+++++++++++++++++++++++++++++

The basic dependencies of the RSS can be easily installed using `apt-get` or `yum`, depending on the system. 

* Ubuntu/Debian ::

  # apt-get install -y openjdk-7-jdk tomcat7 mysql-client mysql-server

* CentOS ::

  # yum install -y java-1.7.0-openjdk-devel tomcat
  # rpm -Uvh http://dev.mysql.com/get/mysql-community-release-el7-5.noarch.rpm
  # yum -y install mysql-community-server
  # /usr/bin/systemctl enable mysqld

Compiling Source Code
+++++++++++++++++++++

If you have downloaded the source code of the RSS from its GIT repository, you will need to compile the source. To do that it is needed to have `maven` installed.

* Ubuntu/Debian ::

  # apt-get install maven

* CentOS ::

  # yum install maven

Once maven is installed, you can compile the source code executing the following command: ::

  # mvn install

.. note::
    In this case war files will be available at *fiware-rss/target/fiware-rss.war* and *rss-expendLimit/el-server/target/expenditureLimit.war*

Deploying the Software
----------------------

The RSS reads its properties from `database.properties` and `oauth.properties` files, located at `/etc/default/rss`. 

The concrete values contained in the properties files are described in *Configuration* section.

-------------
Configuration
-------------

This section explains how to configure the RSS. If you have used the provided script, you can skip this step as your properties files are already created. However, it is highly recomended to read this section in order to understand the existing preferences.

Database Configuration
======================

Database connection in configured in `/etc/default/rss/database.properties` which has the following structure: ::

    ## Filter usage
    database.url=jdbc:mysql://localhost:3306/RSS
    database.username=root
    database.password=root
    database.driverClassName=com.mysql.jdbc.Driver



OAuth2 Configuration
====================

OAuth2 information is configured in `/etc/default/rss/oauth.properties`, which has the following structure: ::

    ############## IDM configuration ################
    config.baseUrl=https://account.lab.fiware.org
    config.logoutPath=/auth/logout
    config.client_id=
    config.client_secret=
    config.callbackURL=http://localhost:8080/fiware-rss/callback
    config.callbackPath=/callback
    config.authorizeUrl=/oauth2/authorize
    config.accessTokenUrl=/oauth2/token
    config.userInfoUrl=/user?access_token=
    config.grantedRole=Provider

-----------------------
Sanity check procedures
-----------------------

The Sanity Check Procedures are those activities that a System Administrator has to perform to verify that an installation is ready to be tested. 
Therefore there is a preliminary set of tests to ensure that obvious or basic malfunctioning is fixed before proceeding to unit tests, integration tests and user validation.


End to End testing
==================

Although one End to End testing must be associated to the Integration Test, we can show here a quick testing to check that everything is up and running.
The first test step involves creating a new resource as well as the implicit creation of a collection. The second test step checks if meta information in different file formats can be obtained.

Step 1 - Create the Resource
----------------------------

Create a file named resource.xml with resource content like this. ::

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <resource>
	   <creator>Yo</creator>
	   <creationDate></creationDate>
	   <modificationDate></modificationDate>
	   <name>Resource Example</name>
	   <contentUrl>http://localhost:8080/FiwareRepository/v2/collec/collectionA/collectionB/ResourceExample</contentUrl>
	   <contentFileName>http://whereistheresource.com/ResourceExample</contentFileName>
    </resource>


Send the request: ::

    curl -v -H "Content-Type: application/xml" -X POST --data "@resource.xml" http://[SERVER_URL]:8080/FiwareRepository/v2/collec/


You should receive a HTTP/1.1 201 as status code

Create a file named resourceContent.txt with arbitrary content. ::

    curl -v -H "Content-Type: text/plain" -X PUT --data "@resourceContent.txt" http://localhost:8080/FiwareRepository/v2/collec/collectionA/collectionB/ResourceExample


You should receive a HTTP/1.1 200 as status code


Step 2 - Retrieve meta information
----------------------------------

Test HTML Response:

Open ``http://[SERVER_URL]:8080/FiwareRepository/v2/collec/collectionA/`` in your web browser. You should receive meta information about the implicit created collection in HTML format.

Test Text Response: ::

    curl -v -H "Content-Type: text/plain" -X GET http://[SERVER_URL]:8080/FiwareRepository/v2/collectionA/collectionB/ResourceExample


You should receive meta information about the implicit created collection in text format. 
You may use curl to also test the other supported content types (``application/json``, ``application/rdf+xml``, ``text/turtle``, ``text/n3``, ``text/html``, ``text/plain``, ``application/xml``)

List of Running Processes
=========================

You can execute the command ``ps -ax | grep 'tomcat\|mongo\|virtuoso'`` to check that the Tomcat web server, the Mongo database, and Virtuoso Triple Store are running. It should show a message text similar to the following: ::

     1048 ?        Ssl    0:51 /usr/bin/mongod --config /etc/mongodb.conf
     1112 pts/1    SNl    0:01 virtuoso-t -f
     1152 ?        Sl     0:03 /usr/lib/jvm/java-8-oracle/bin/java -Djava.util.logging.config.file=/home/jortiz/conwet/Repository-RI/apache-tomcat-8.0.26/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Dhttp.nonProxyHosts=localhost|127.0.0.1|CONWETLABJORTIZ -Djava.endorsed.dirs=/home/jortiz/conwet/Repository-RI/apache-tomcat-8.0.26/endorsed -classpath /home/jortiz/conwet/Repository-RI/apache-tomcat-8.0.26/bin/bootstrap.jar:/home/jortiz/conwet/Repository-RI/apache-tomcat-8.0.26/bin/tomcat-juli.jar -Dcatalina.base=/home/jortiz/conwet/Repository-RI/apache-tomcat-8.0.26 -Dcatalina.home=/home/jortiz/conwet/Repository-RI/apache-tomcat-8.0.26 -Djava.io.tmpdir=/home/jortiz/conwet/Repository-RI/apache-tomcat-8.0.26/temp org.apache.catalina.startup.Bootstrap start
     2031 pts/1    S+     0:00 grep --color=auto --exclude-dir=.bzr --exclude-dir=.cvs --exclude-dir=.git --exclude-dir=.hg --exclude-dir=.svn tomcat\|mongo\|virtuoso


Network interfaces Up & Open
============================

To check whether the ports in use are listening, execute the command ``netstat -ntpl``. The expected results must be somehow similar to the following: ::

    tcp        0      0 127.0.0.1:28017         0.0.0.0:*               ESCUCHAR    -               
    tcp        0      0 127.0.1.1:53            0.0.0.0:*               ESCUCHAR    -               
    tcp        0      0 0.0.0.0:1111            0.0.0.0:*               ESCUCHAR    11271/virtuoso-t
    tcp        0      0 127.0.0.1:631           0.0.0.0:*               ESCUCHAR    -               
    tcp        0      0 0.0.0.0:8890            0.0.0.0:*               ESCUCHAR    11271/virtuoso-t
    tcp        0      0 127.0.0.1:27017         0.0.0.0:*               ESCUCHAR    -               
    tcp6       0      0 :::8080                 :::*                    ESCUCHAR    11286/java      
    tcp6       0      0 ::1:631                 :::*                    ESCUCHAR    -               
    tcp6       0      0 127.0.0.1:8005          :::*                    ESCUCHAR    11286/java      
    tcp6       0      0 :::8009                 :::*                    ESCUCHAR    11286/java      


Databases
=========

The last step in the sanity check (once that we have identified the processes and ports) is to check the databases that has to be up and accept queries. For that, we execute the following commands:

* MongoDb ::

    $ mongo
    MongoDB shell version: 2.4.9
    connecting to: test
    Welcome to the MongoDB shell.
    For interactive help, type "help".
    For more comprehensive documentation, see
    http://docs.mongodb.org/
    Questions? Try the support group
    http://groups.google.com/group/mongodb-user
    > db


It should show a message text similar to the following: ::

    test


* Virtuoso ::
    
    $isql
    OpenLink Interactive SQL (Virtuoso), version 0.9849b.
    Type HELP; for help and EXIT; to exit.
    SQL> SPARQL SELECT DISTINCT ?g WHERE { GRAPH ?g { ?s ?q ?l }};


It should show a message text similar to the following: ::

    g
    LONG VARCHAR
    _______________________________________________________________________________

    http://www.openlinksw.com/schemas/virtrdf#
    http://www.w3.org/ns/ldp#
    http://localhost:8890/sparql
    http://localhost:8890/DAV/
    http://www.w3.org/2002/07/owl#

    5 Rows. -- 90 msec.


--------------------
Diagnosis Procedures
--------------------

The Diagnosis Procedures are the first steps that a System Administrator has to take to locate the source of an error in a GE. Once the nature of the error is identified by these tests, the system admin can resort to more concrete and specific testing to pinpoint the exact point of error and a possible solution.

The resource load of the Repository-RI strongly depends on the number of concurrent requests received as well as on the free main memory and disk space:

* Mimimum available main memory: 1 GB
* Mimimum available hard disk space: 2 GB

Resource availability
=====================

State the amount of available resources in terms of RAM and hard disk that are necessary to have a healthy enabler. This means that bellow these thresholds the enabler is likely to experience problems or bad performance.

Resource consumption
====================

Resource consumption strongly depends on the load, especially on the number of concurrent requests.

The main memory consumption of the Tomcat application server should be between 48MB and 1024MB. These numbers can vary significantly if you use a different application server.

I/O flows
=========

The only expected I/O flow is of type HTTP or HTTPS, on ports defined in Apache Tomcat configuration files, inbound and outbound. Requests interactivity should be low.
