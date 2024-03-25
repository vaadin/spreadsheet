Vaadin Spreadsheet ${project.version}
================================
Vaadin Spreadsheet is an add-on for the Vaadin Framework. Vaadin Spreadsheet
provides a Vaadin Component for displaying and editing Excel spreadsheets
within a Vaadin application.

Installation
============
The add-on works like normal Vaadin Add-ons. Note, that the package also has
client side extensions, so make sure that you compile your widgetset after
installation!

!!!!! NOTE !!!!!
Widget set compilation will fail unless you have a license for Vaadin
Spreadsheet. See https://vaadin.com/pricing for details.
Please find instructions for how to install the license at
https://vaadin.com/docs/v8/framework/addons

Maven
-----

Dependency snippet for Maven users:

<dependency>
<groupId>com.vaadin</groupId>
<artifactId>vaadin-spreadsheet</artifactId>
<version>${project.version}</version>
</dependency>

Prereleases are available in Vaadin Prereleases repository:

<repository>
<id>vaadin-prereleases</id>
<url>https://maven.vaadin.com/vaadin-prereleases</url>
</repository>

Ivy
---

IVY dependency snippet:

<dependency org="com.vaadin" name="vaadin-spreadsheet" rev="${project.version}" conf="default->default" />

Using plain Jar
---------------

If you wan't to use the add-on jar directly, add it to your classpath. Please be sure to 
add all libraries from the lib folder to your classpath also.

Licensing
=========

Vaadin Spreadsheet is a commercial product. You must either acquire a license or
stop using it. More information about Vaadin Commercial License and Service 
Terms is available at https://vaadin.com/commercial-license-and-service-terms.

You may obtain a valid license by subscribing to Vaadin Pro.
See https://vaadin.com/pricing for details.

Once you have the subscription, the license key can be found in
https://vaadin.com/myaccount/licenses#classic

Register your copy of Vaadin Spreadsheet by creating a file named
.vaadin.spreadsheet.developer.license containing the license key in your home
directory or by setting the vaadin.spreadsheet.developer.license=license_key
system property to disable the license warning message.

Vaadin Spreadsheet (version 1.2.0 or later) supports Vaadin Charts, making it
possible to open Excel files with charts in it. To enable this feature, you
need to add a vaadin-spreadsheet-charts dependency to your project. Vaadin
Charts is distributed under Vaadin Commercial License and Service Terms, 
see https://vaadin.com/commercial-license-and-service-terms for details. To
use the vaadin-spreadsheet-charts package, you need to have valid Vaadin
Spreadsheet and Vaadin Charts licenses.

Third Party Licensing
=====================

This Add-on component depends on a few external open source libraries, of
which the Apache 2.0 licensed Apache POI library is the most important.
You can find details about these dependencies in the license.html file.

Links
=====

Homepage:
https://vaadin.com/directory/component/vaadin-spreadsheet

Apache POI Homepage:
http://poi.apache.org/

Code and usage examples:
http://demo.vaadin.com/spreadsheet/

Issue tracker:
https://github.com/vaadin/spreadsheet/issues

Documentation:
https://vaadin.com/docs/v8/spreadsheet/spreadsheet-overview

SCM (Git):
https://github.com/vaadin/spreadsheet