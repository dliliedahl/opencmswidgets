/*
 * This library is part of the code for the book: OpenCms for Developers
 *
 * Copyright (c) 2007, 2008 Dan Liliedahl
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.efoundry.widgets.sources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.main.CmsException;
import org.opencms.main.OpenCms;
import org.opencms.xml.content.CmsXmlContent;
import org.opencms.xml.content.CmsXmlContentFactory;
import org.opencms.xml.types.I_CmsXmlContentValue;

import com.efoundry.widgets.CustomSourceConfiguration;
import com.efoundry.widgets.I_WidgetSelectSource;
import com.efoundry.widgets.SelectOptionValue;

/**
 * This class retrieves select option values obtained from a repeating field in a structured content instance.
 * The option configuration string specifies where the data comes from, as shown below:<p>
 * <p>
 * <code>
 *      &lt;layout element="SomeField" widget="CustomSourceSelectWidget"
 *				configuration="source='com.efoundry.widgets.sources.ContentListDS'|
 *              contenttype='ValueList'|
 *              location='/Blogs/BlogCategories'|
 *              fieldname='Category'"/&gt;
 * </code>
 * <p>
 * <b>contenttype</b> - this must specify the structured content type that contains the list of values<p>
 * <b>location</b> - this specifies the location of the resource instance. The resource instance must be
 * of the specified contenttype<p>
 * <b>fieldname</b> - this specifies the name of the field within the contenttype containing the values. The
 * field must be a simple type containing a string value
 */
public class ContentFieldListDS implements I_WidgetSelectSource {

	/** The content type containing the list of values */
	String m_strContentType;
	
	/** VFS path to the instance of the resource */
	String m_strLocation;
	
	/** Name of the field within the resource */
	String m_strFieldname;
	
	/**
	 * Public constructor needed
	 */
	public ContentFieldListDS() {
		m_strContentType = null;
		m_strLocation = null;
		m_strFieldname = null;
	}

	/**
	 * Retrieve and saves the configuration option values
	 * @param config configuration information
	 */
	public void setConfiguration(CustomSourceConfiguration config) {
		
		// get the configuration options
		m_strContentType = config.getConfigValue("contenttype");
		m_strLocation = config.getConfigValue("location");
		m_strFieldname = config.getConfigValue("fieldname");
	}

	/**
	 * Returns the list of Values for our data source
	 * @param cms	CmsObject instance
	 * @return List of SelectOptionValue values to appear in the select list
	 */
	public List<SelectOptionValue> getValues(CmsObject cms) {
		ArrayList<SelectOptionValue> lstVals = new ArrayList<SelectOptionValue>();

		if (false == isConfigValid()) {
			lstVals.add(new SelectOptionValue("Missing or invalid configuration options!", "null"));
		} else {
			// read the resource containing the values, in the specified location
			try {
				CmsResource res = cms.readResource(m_strLocation);
				// check it against the desired type
				if (m_strContentType.equalsIgnoreCase(OpenCms.getResourceManager().getResourceType(res).getTypeName())) {
					// retrieve the values
					CmsXmlContent content = CmsXmlContentFactory.unmarshal(cms, cms.readFile(res));
					// using the specified fieldname
		    		List lVals = content.getValues(m_strFieldname, cms.getRequestContext().getLocale());
                    Iterator j = lVals.iterator();
                    while (j.hasNext()) {
                        I_CmsXmlContentValue iVal = (I_CmsXmlContentValue)j.next();
                        // add the value
    					lstVals.add(new SelectOptionValue(iVal.getStringValue(cms), iVal.getStringValue(cms)));
                    }
				} else {
					lstVals.add(new SelectOptionValue("Specified resource (" + m_strLocation + ") is not of type:" +
							m_strContentType, "null"));
				}
			} catch (CmsException e) {
				// return the error in the list to be nice
				lstVals.add(new SelectOptionValue("Error reading " + m_strLocation + ": " 
						+ e.getMessage(), "null"));
			}
		}
		return lstVals;
	}

	/**
	 * Determines if the configuration values are valid
	 * @return true if the configuration is valid, otherwise false
	 */
	private boolean isConfigValid() {
		return m_strContentType != null &&
			m_strLocation != null &&
			m_strFieldname != null;
	}
}
