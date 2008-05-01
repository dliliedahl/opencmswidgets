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

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;
import org.opencms.widgets.Messages;

import com.efoundry.widgets.CmsCustomSourceSelectWidgetOption;

/**
 * An option of a custom source select type widget.<p>
 * 
 * This class provides support for specifying option values to an instance of a custom source
 * select type widget. The option values must be specified in the XSD file for the widget and
 * follow this syntax:<p> 
 * 
 * <code>source='classfile'</code><p>
 * 
 * For example:<p>  
 * 
 * <code>source='com.example.widget.MyDataSource'</code><p>
 * 
 * The specified source must be a Java class that supports the I_WidgetSelectSource interface and must
 * be available in the JVM classpath.  
 * 
 * @author Dan Liliedahl  
 * 
 */
public class CmsCustomSourceSelectWidgetOption {

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsCustomSourceSelectWidgetOption.class);

    /** Key prefix for the 'source'. */
    private static final String KEY_SOURCE_BEGIN = "source='";
    private static final String KEY_SOURCE_END = "'";

    /** the data source class */
    private String m_strClassName;

    /**
     * Constructor
     * @param Class Name of the class that provides the source for this select list
     */
	public CmsCustomSourceSelectWidgetOption(String Class) {
		m_strClassName = Class;
	}

	/**
	 * Returns the configured class name
	 * @return String class name
	 */
	public String getClassName() {
		return m_strClassName;
	}

    /**
     * Parses a widget configuration String.<p> 
     * 
     * Please note: No exception is thrown in case the input is malformed, all malformed entries are silently ignored.<p>
     * 
     * @param input the widget input string to parse
     * 
     * @return a <code>{@link CmsCustomSourceSelectWidgetOption}</code> object
     */
    public static CmsCustomSourceSelectWidgetOption parseOptions(String input) {
        // default result for empty or mis-configured option
    	String strDataSource = "";

    	// find the location of the source=' string in the config string
    	int nSrcOffsetStart = input.indexOf(KEY_SOURCE_BEGIN);
    	if (-1 != nSrcOffsetStart) {
    		// now find the terminator
    		nSrcOffsetStart += KEY_SOURCE_BEGIN.length(); 
    		int nSrcOffsetEnd = input.indexOf(KEY_SOURCE_END, nSrcOffsetStart);
	        try {
	        	// extract the source
	            strDataSource = input.substring(nSrcOffsetStart, nSrcOffsetEnd);
	        } catch (Exception e) {
	            if (LOG.isInfoEnabled()) {
	                LOG.info(Messages.get().getBundle().key(Messages.ERR_MALFORMED_SELECT_OPTIONS_1, input));
	            }
	        }
    	}
        return new CmsCustomSourceSelectWidgetOption(strDataSource);
    }

}
