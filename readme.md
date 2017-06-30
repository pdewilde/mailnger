#Mailnger (Mail Manager)

#####A simple Java EE app for managing mailing lists

###Features:
* Online User Sign-Up and Unsubscribe
* MySQL Database Backend
* Privliged and Unprivliged Accounts to allow for restricted mailing lists

## Getting It working
Mailnger was developed on InteliJ with an Apache Tomcat server as the intended target

---

Beyond the code in the repository you will need the [Connector/J MySQL JDBC driver](https://dev.mysql.com/downloads/connector/j/)
and an intallation of Apache Tomcat.

The Connector/J jar **MUST** be placed in the $CATALINA$/lib folder of your Tomcat installation and
you need to modify $CATALINA$/config/server.xml file as follows:

```
<Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener" />
```
Needs to be changed to 
```
<Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener" driverManagerProtection="false" />
```

This teak fixes the No suitable drivers found error. It comes from [this stackoverflow post](https://stackoverflow.com/questions/15926654/drivers-loaded-in-apache-tomcat-6-0-36-lib-but-still-get-no-suitable-driver-foun)

---

Currently, you must put your server credentials in the constants at the beginning of the
Updater class located at /src/mailnger/Updater.java. The credentials.txt file in the root
currently is not working with tomcat distributions.

You are welcome to use the default database as a test server. I do not run it and provide
no garuntee of its continued maitance.
___

### MySQL Database

I will in the future develop a tool to set up the database, but for now you can create the tables manually.

There are three tables in the following Format:

USERS
* EMAIL (VARCHAR 255) Primary Key
* MEMBER (BOOL) Defaults to 0

LISTS
* LIST_NAME (varchar 100) Primary Key
* RESTRICTED (BOOL) Defaults to 1

SUBSCRIPTIONS
* LIST_NAME (varchar 100) Primary Key
* EMAIL (varchar 255) Primary Key

---
###Tomcat Setup
[I setup with these instructions](https://www.linode.com/docs/development/frameworks/apache-tomcat-on-ubuntu-16-04)

You can deploy the WAR using the web manager, but if you want your app to be root add the
following lines inside the <Host> section in server.xml.

```
<Context path="" docBase="myAPP"/>
<Context path="ROOT" docBase="ROOT"/>
```

Additionally, to use port 80, remove comment on the #AUTHBIND=no in /etc/default/tomcat7
and set the value to yes.

## Future Improvements

* Switch To DataSource from DataManager implementation for best practices
* Create Management Client or Web Interface