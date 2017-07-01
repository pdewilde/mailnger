# Mailnger (Mail Manager)

##### A simple Java EE app for managing mailing lists

### Features:
* Online User Sign-Up and Unsubscribe
* MySQL Database Backend
* Privliged and Unprivliged Accounts to allow for restricted mailing lists

## Getting It working
Mailnger was developed on InteliJ with an Apache Tomcat server as the intended target

---

Beyond the code in the repository you will need the [Connector/J MySQL JDBC driver](https://dev.mysql.com/downloads/connector/j/)
and an intallation of Apache Tomcat.

The driver should be included in the artifact when building.

---

Currently, you must put your server credentials in the constants at the beginning of the
Updater class located at /src/mailnger/Updater.java. The credentials.txt file in the root
currently is not working with tomcat distributions.

You are welcome to use the default database as a test server. I do not run it and provide
no garuntee of its continued maintenance.
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
### Tomcat Setup (Ubuntu 16.04)
[I setup with these instructions](https://www.linode.com/docs/development/frameworks/apache-tomcat-on-ubuntu-16-04)

You can deploy the WAR using the web manager, but if you want your app to be root add the
following lines inside the \<Host\> section in server.xml located at /var/lib/tomcat8/conf .

```
<Context path="" docBase="mailnger_war"/>
<Context path="ROOT" docBase="ROOT"/>
```

Additionally, to use port 80, remove comment on the #AUTHBIND=no in /etc/default/tomcat8
and set the value to yes, and then change the server.xml file in /var/lib/tomcat8/conf by making the line

```<Connector connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443"/>``` 

say port 80 instead of 8080. Additionally, if you have apache running, you will need to stop the Apache service.

## Future Improvements

* Switch To DataSource from DataManager implementation for best practices
* Create Management Client or Web Interface