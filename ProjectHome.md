# This is a module for OpenCms that contains a flexible data source widget #

The module contains a user interface widget that allows select list pull-down data to be obtained from dynamic data sources. The module contains a single widget which is configurable and extensible by adding data sources. Three data sources are included in the module:

  * A data source that obtains data from a repeated field within a structured content item
  * A data source that returns a list of structured content types
  * A data soruce that returns selection data using a database query

The widget also contains a framework for adding additional data sources.

## Installing the module ##

  1. Download the binary module.
  1. Import the module into OpenCms using Administrator/Module management tools
  1. Register the widget with OpenCms:
```
 * Edit the file: <OPENCMS-APPDIR>\WEB-INF\config\opencms-vfs.xml
 * Locate the <widgets> section
 * Add the widget registration:
 <widget class="com.efoundry.widgets.CmsCustomSourceSelectWidget" alias="DataSourceSelectWidget"/>
 * Restart OpenCms
```

The /Widgets folder contains samples showing various ways to use the widgets.

==Using the widget
To use the widget, the layout section of the schema file must contain a reference to the widget. The configuration will look like this:

```
   <layout element="Choice" widget="DataSourceSelectWidget"
            configuration="source='DATA_SOURCE'" />
```

> DATA\_SOURCE must be the name of a Java class that implements the `I_WidgetSelectSource` interface. The module includes the following three data sources:

**com.efoundry.widgets.sources.ContentFieldListDS**

> This data source retrieves select option values obtained from a repeating field in a structured content instance. The configuration string specifies where the data comes from. The syntax of the configuration string is:
```
<layout element="SomeField" widget="DataSourceSelectWidget"
   configuration="source='com.efoundry.widgets.sources.ContentListDS'|
            contenttype='NAME_OF_CONTENT_TYPE'|
            location='LOCATION_OF_CONTENT'|
            fieldname='FIELD_NAME'" />
```
> contenttype - this must specify the structured content type that contains the list of values to be returned
> location - this specifies the location of the resource instance. The resource instance must be of the specified contenttype
> fieldname - this specifies the name of the field within the contenttype containing the values. The field must be a simple type containing a string value

A 'ChoiceList' content type is included in the module which may be used to contain select list values.

> Configuration Example:
configuration="source='com.efoundry.widgets.sources.ContentFieldListDS'|contenttype='ChoiceList'|location='/Widgets/Seminars'|fieldname='Value'"

> the contenttype is set to 'ChoiceList'
> the instance containing the data is located at '/Widgets/Seminars'
> the field within the content type is named 'Value'

**com.efoundry.widgets.sources.ContentTypesDS**

> Configuration Example:
configuration="source='com.efoundry.widgets.sources.ContentTypesDS|exclude='xmlpage'"

> This data source returns a selection list that contains a list of content types. The 'source' and 'exclude' parameters are delimited with a ```
|``` character. The 'exclude' parameter contains a comma separated list of content types that are to be excluded from the list.

> the exclude option is used to filter out the xmlpage content type

**com.efoundry.widgets.sources.DatabaseTableDS**

> This data source obtains select list values using a database query. The 'source', 'jndiname', and 'qry' parameters are delimited with a | character. The 'jndiname' parametercontains a name of the JNDI resource that is used for the data connection, and the qry contains the query to be used. The data source obtains the name of the option value from the first returned column, and the value from the second one. Any other columns used in the query are ignored.

> Configuration Example:
configuration="source='com.efoundry.widgets.sources.DatabaseTableDS'|jndiname='jdbc/states'|qry='select state,name from states'"

> the JNDI resource name is 'jdbc/states'
> the query is 'select state,name from states'

## Adding new data sources ##
Adding a new data source is simply a matter of creating a java class that implements the `I_WidgetSelectSource` interface.

```
public interface I_WidgetSelectSource {

	/**
	 * This method is called after the data source is constructed and before the option values are 
	 * retrieved. It passes along the configuration string to the widget.
	 * 
	 * @param config String value of the configuration string
	 */
	public void setConfiguration(CustomSourceConfiguration config);
	
	/**
	 * Returns a <code>List</code> of <code>{@link SelectOptionValue}</code> objects to be displayed in the 
	 * selection list.
	 */
	public List<SelectOptionValue> getValues(CmsObject cms);
}
```