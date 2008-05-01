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

import org.opencms.util.CmsStringUtil;

import com.efoundry.widgets.I_WidgetSelectSource;

/**
 * This is a base class that widget data sources may use.
 */
public abstract class A_CustomDataSource implements I_WidgetSelectSource {

	/** character used to separate option values in the configuration string */
	protected final static String OPTION_DELIMITER = "|";
	
	/** sequence used to find the beginning of an option value */
	protected final static String OPTION_VALUE_BEGIN="='";
	
	/** character used to find the end of an option value */
	protected final static String OPTION_VALUE_END="'";
	
	/** The configuration string */
	protected String m_strConfig;
	
	/**
	 * Save the configuration string
	 */
	public void setConfiguration(String config) {
		m_strConfig = config;
	}
	
	/**
	 * This method retrieves the value of a configuration option with the given name. Configuration options
	 * must be delimited with a <code>|</code> character and surrounded with a <code>'</code> character.
	 * 
	 * @param ConfigName Name of the configuration option to retrieve
	 * @return Value of the configuration option if found, else null
	 */
	protected String getConfigValue(String ConfigName) {

        // split the options along the delimiter
        String[] parts = CmsStringUtil.splitAsArray(m_strConfig, OPTION_DELIMITER);
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.length() == 0) {
                // skip empty parts
                continue;
            }
            // find the =' token, indicating the start of the option value
            int nOptStart = part.indexOf(OPTION_VALUE_BEGIN);
            if (nOptStart != -1) {
            	String strOptName = part.substring(0, nOptStart);
            	if (strOptName.equals(ConfigName)) {
            		int nValStart = nOptStart + OPTION_VALUE_BEGIN.length();
            		int nValEnd = part.indexOf(OPTION_VALUE_END, nValStart);
            		String strOptValue = part.substring(nValStart, nValEnd);
            		return strOptValue;
            	}
            }
        }
		return null;
	}
}