Maintenance
=====

Updating copyright year range
-----
1. update <code>currentYear</code> in the every <code>pom.xml</code> as the first thing every year
    - yes, this is annoying, we should refactor the project to have actual module inheritance
1. run <code>mvn -P license license:format -DskipITs</code> for the root module to update the year range in all the Java files of all the modules
    - based on each module's <code>src/license/header.txt</code> and <code>currentYear</code>
    - root module doesn't have its own <code>header.txt</code> so it uses <code>vaadin-spreadsheet/src/license/header.txt</code>
1. check that all modules got updated <code>.java</code> files with the correct year
    - update the inclusion list for <code>com.mycila.license-maven-plugin</code> in that module's <code>pom.xml</code> if needed

Updating license name
-----
Java class license headers:
1. update contents of <code>license</code> tags in <code>vaadin-spreadsheet/pom.xml</code>
1. update the files <code>header.txt</code> and <code>license.txt</code> in all <code>vaadin-spreadsheet/src/license/</code>, <code>vaadin-spreadsheet-charts/src/license/</code>, and <code>vaadin-spreadsheet-testbench-api/src/license/</code>
    - triplicated because all jars need to contain the files
1. run <code>mvn -P license license:format -DskipITs</code> for the root module to update the license information and the year range in all the Java files of all the modules
1. check that all modules got updated <code>.java</code> files with the correct year

Other places where the licensing information should be updated:
1. <code>README.md</code>
1. <code>LICENSE.txt</code>
1. <code>AdVaaLicen</code> tag for <code>org.apache.maven.plugins.maven-jar-plugin</code> in <code>vaadin-spreadsheet/pom.xml</code>
1. Vaadin Spreadsheet documentation for [Vaadin 8](https://vaadin.com/docs/v8/spreadsheet/spreadsheet-overview) and [Vaadin 7](https://vaadin.com/docs/v7/spreadsheet/spreadsheet-overview), search the category for <code>license</code>
1. general add-on licensing documentation ([V8](https://vaadin.com/docs/v8/framework/addons) and [V7](https://vaadin.com/docs/v7/framework/addons))

Removal should be considered, but update while they still exist:
1. <code>license.html</code> and <code>README.txt</code> in <code>directory/assembly/</code>
1. the various <code>documentation/*.asciidoc</code> pages, search for <code>license</code>

License name update instructions should also be used if the decision is made to include the license version number again.