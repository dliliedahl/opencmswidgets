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
package com.efoundry.widgets;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;
import org.opencms.util.CmsStringUtil;
import org.opencms.widgets.Messages;

import com.efoundry.widgets.CustomSourceConfiguration;

/**
 * An option of a custom source select type widget.<p>
 * 
 * This class provides support for specifying option values to an instance of a custom source
 * select type widget. The option values must be specified in the XSD file for the widget and
 * follow this syntax:<p> 
 * 
 * <code>setting1='setting1val'|setting2='setting2val'</code><p>
 * 
 * For example:<p>  
 * 
 * <code>source='com.example.widget.MyDataSource'|exclude='someexclude'|type='sometype'</code><p>
 * 
 * The specified source must be a Java class that supports the I_WidgetSelectSource interface and must
 * be available in the JVM classpath.  
 * 
 * @author Dan Liliedahl  
 * 
 */
public class CustomSourceConfiguration {

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CustomSourceConfiguration.class);

	/** character used to separate option values in the configuration string */
	protected final static String OPTION_DELIMITER = "|";
	
	/** sequence used to find the beginning of an option value */
	protected final static String OPTION_VALUE_BEGIN="='";
	
	/** character used to find the end of an option value */
	protected final static String OPTION_VALUE_END="'";

	/** List of option values */
	private Hashtable<String, String> m_htOptions;

    /**
     * Constructor
     * @param Class Name of the class that provides the source for this select list
     */
	public CustomSourceConfiguration(String configuration) {
		parseOptions(configuration);
	}

    /**
     * Parses the widget configuration parameter<p> 
     * 
     * Please note: No exception is thrown in case the input is malformed, any malformed entries are 
     * silently ignored (but logged)<p>
     * 
     * @param input the widget input string to parse
     */
    private void parseOptions(String config) {
    	m_htOptions = new Hashtable<String, String>();

    	String [] aParms = CmsStringUtil.splitAsArray(config, OPTION_DELIMITER);
    	for (int i=0; i<aParms.length; i++) {
    		boolean bBadParamFormat = false;
    		// read the parameter name
    		String strParm = aParms[i];
    		int nParmNameBegin = 0;
	    	int nParmNameEnd = strParm.indexOf(OPTION_VALUE_BEGIN);
	    	if (-1 != nParmNameEnd) {
	    		// parameter name
	    		String strParmName = strParm.substring(nParmNameBegin, nParmNameEnd);
	    		// parameter value
	    		int nParmValueStart = nParmNameEnd + OPTION_VALUE_BEGIN.length();
	    		int nParmValueEnd = strParm.indexOf(OPTION_VALUE_END, nParmValueStart);
	    		if (-1 != nParmValueEnd) {
		    		String strParmVal = strParm.substring(nParmValueStart, nParmValueEnd);
		    		
		    		// add the param name-value pair
		    		m_htOptions.put(strParmName, strParmVal);
	    		} else {
	    			bBadParamFormat = true;
	    		}
	    	} else {
	    		bBadParamFormat = true;
	    	}
    		if (bBadParamFormat && LOG.isInfoEnabled()) {
    			LOG.info(Messages.get().getBundle().key(Messages.ERR_MALFORMED_SELECT_OPTIONS_1, strParm));
    		}
    	}
    }

	/**
	 * This method retrieves the value of a configuration option with the given name.
	 * 
	 * @param ConfigName Name of the configuration option to retrieve
	 * 
	 * @return Value of the config option if found, else null
	 */
	public String getConfigValue(String ConfigName) {
		return (String) m_htOptions.get(ConfigName);
	}
}
