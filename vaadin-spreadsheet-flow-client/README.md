

### Exported Spreadsheet

This module compiles and exposed the client part of the Spreadsheet component which has been developed by using GWT.

### Building

For building it you just need to run `mvn package` and the result JS will be installed in the resources folder of the `vaadin-spreadsheet-flow` component.

### Debugging

GWT provides a code-server for serving the generated JS with their source-maps as well as for recompiling the module on demand once you do changes in java code, also known as Super Dev Mode.

So for debugging the GWT code you need to:

- run `mvn gwt:run -Psdm` from this folder
- open this module in your favourite java IDE
- open the url http://localhost:9876 and install the 'Dev Mode On' bookmark in your browser (this only need to be performed once)
- run your application containing the `vaadin-spreadsheet` in localhost
- visit your application in localhost e.g. http://localhost:8080
- perform changes in Java code, and push the bookmark when ready.






