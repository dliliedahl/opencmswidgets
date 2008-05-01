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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.opencms.file.CmsObject;
import org.opencms.i18n.CmsLocaleManager;
import org.opencms.i18n.CmsMessages;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.explorer.CmsExplorerTypeSettings;

import com.efoundry.widgets.CustomSourceConfiguration;
import com.efoundry.widgets.I_WidgetSelectSource;
import com.efoundry.widgets.SelectOptionValue;

/**
 * This class implements a Custom Data source for the <code>{@link CmsCustomSourceSelectWidget}</code>. The class
 * retrieves the list of available structured content types. This data source may be configured in the following way:
 * <p>
 *	<code>
 *      &lt;layout element="SomeField" widget="CustomSourceSelectWidget"
 *				configuration="source='com.efoundry.widgets.sources.ContentTypesDS'|exclude='structuredcontent'"/&gt;
 * </code>
 * <p>
 * The 'source' and 'exclude' parameters are delimited with a <code>|</code> character. The 'exclude' parameter
 * contains a comma separated list of content types that are to be excluded from the list.
 */
public class ContentTypesDS implements I_WidgetSelectSource {

	/** xmlpage content type */
	protected static final String XMLPAGE = "xmlpage";

	/** some constants */
	protected static final String FILEICON = "fileicon.xmlpage";
	protected static final String XMLCONTENT = "xmlcontent";
	protected static final String EXCLUDE = "exclude";
	
	/** List of exclusions */
	Hashtable<String, String> m_htExclusions;
	
	/**
	 * Constructor
	 */
	public ContentTypesDS() {
		// initialize the exclusions list
		m_htExclusions = new Hashtable<String, String>();
	}

	/**
	 * Retrieves the list of structured content types
	 */
	public List<SelectOptionValue> getValues(CmsObject cms) {
		// list of return values
		ArrayList<SelectOptionValue> aVals = new ArrayList<SelectOptionValue>();

		// get the message bundle for the workplace manager
		CmsMessages wpMessages = OpenCms.getWorkplaceManager().getMessages(CmsLocaleManager.getDefaultLocale());

		// The XML Page is structured content, but is not 'derived' from xmlcontent, so we special case it
    	if (null == m_htExclusions.get(XMLPAGE)) {
            // Only add it if it hasn't been excluded
    		aVals.add(new SelectOptionValue(wpMessages.key(FILEICON), XMLPAGE));
    	}
		
		// get a list of all configured resource types from the explorer config file
        Iterator i = OpenCms.getWorkplaceManager().getExplorerTypeSettings().iterator();
        while (i.hasNext()) {
            CmsExplorerTypeSettings settings = (CmsExplorerTypeSettings)i.next();
            // look for structured content types
            String strRef = settings.getReference();
            if (CmsStringUtil.isNotEmpty(strRef)) {
	            // we only care about structured content types (types that have a reference to 'xmlcontent'
	            if (strRef.equals(XMLCONTENT)) {
	            	// and that can be created in the UI
	            	if (CmsStringUtil.isEmpty(settings.getNewResourceUri())) {
	            		// no 'new resource' URI specified, don't allow it
	            		continue;
	            	}

	            	// don't add if it is excluded
	            	if (null == m_htExclusions.get(settings.getName())) {
			            // add found setting to list
		        		aVals.add(new SelectOptionValue(wpMessages.key(settings.getKey()), settings.getName()));
	            	}
	            }
            }
        }
        return aVals;
	}

	/**
	 * Retrieve and saves the configuration option values
	 * @param config configuration information
	 */
	public void setConfiguration(CustomSourceConfiguration config) {
		
		// read the list of exclusions
		String strExclusions = config.getConfigValue(EXCLUDE);
		if (null != strExclusions) {
	        String[] parts = CmsStringUtil.splitAsArray(strExclusions, ",");
	        for (int i = 0; i < parts.length; i++) {
	            String part = parts[i].trim();
	            if (part.length() == 0) {
	                // skip empty parts
	                continue;
	            }
	            // add the type to the list
		        m_htExclusions.put(part, part);
	        }
		}
	}
}
