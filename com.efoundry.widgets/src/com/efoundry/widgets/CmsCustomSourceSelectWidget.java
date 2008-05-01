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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;
import org.opencms.widgets.CmsSelectWidget;
import org.opencms.widgets.I_CmsWidget;
import org.opencms.widgets.I_CmsWidgetDialog;
import org.opencms.widgets.I_CmsWidgetParameter;

import com.efoundry.widgets.sources.DefaultDS;

/**
 * This is a custom widget which builds a SELECT option list using a user configurable data source.
 * The data source is configured in the widget's configuration option field:<p>
 * Example:<p> 
 *	<code>
 *      &lt;layout element="SomeField" widget="CustomSourceSelectWidget"
 *				configuration="source='com.efoundry.widgets.sources.ContentTypesDS'"/&gt;
 * </code>
 * <p>
 * The value of the source must be contained within single quotes as indicated. Custom Data
 * source implementations are passed the configuration string before retrieving the source
 * content. Additional configuration options specific to the data source may also be passed
 * in this parameter. It is up to the data source implementation to specify the format of any
 * additional configuration data.
 * The specified class must implement the <code>{@link I_WidgetSelectSource}</code> interface.
 * 
 */
public class CmsCustomSourceSelectWidget extends CmsSelectWidget {

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsCustomSourceSelectWidget.class);

	/** The list of select values */
    private List<SelectOptionValue> m_selectOptions = null;

    /** The configuration options */
    CustomSourceConfiguration m_config;
    
    /** The widget data source */
    I_WidgetSelectSource m_iDataSource = null;

    /**
     * Creates a new select widget.<p>
     */
    public CmsCustomSourceSelectWidget() {
        super();
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#newInstance()
     */
    @Override
	public I_CmsWidget newInstance() {
		// return a new instance of the widget
        return new CmsCustomSourceSelectWidget();
	}
    
	/**
	 * @see org.opencms.widgets.A_CmsWidget#setConfiguration(java.lang.String)
	 */
	@Override
	public void setConfiguration(String configuration) {
		
		super.setConfiguration(configuration);

    	if (m_iDataSource == null) {
            m_config = new CustomSourceConfiguration(configuration);
            String strClassName = m_config.getConfigValue("source");
            
            // read the class name for the data source and instantiate it
            Class sourceClazz;
            try {
                sourceClazz = Class.forName(strClassName);
                m_iDataSource = (I_WidgetSelectSource)sourceClazz.newInstance();
            } catch (Exception e) {
            	// Log the error
                LOG.error(Messages.get().getBundle().key(Messages.LOG_DATASOURCE_INIT_ERROR_2, 
                		strClassName), e);
                
           		// since it failed use the default source provider to be nice
           		m_iDataSource = new DefaultDS();
            }
            // set the configuration
            m_iDataSource.setConfiguration(m_config);
        }
	}

    /**
     * @see org.opencms.widgets.I_CmsWidget#getDialogWidget(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog, org.opencms.widgets.I_CmsWidgetParameter)
     */
	@Override
	public String getDialogWidget(CmsObject cms, I_CmsWidgetDialog widgetDialog, I_CmsWidgetParameter param) {
        String id = param.getId();

        // build the SELECT HTML
        StringBuffer result = new StringBuffer(16);
        result.append("<td class=\"xmlTd\" style=\"height: 25px;\"><select class=\"xmlInput");
        if (param.hasError()) {
            result.append(" xmlInputError");
        }
        result.append("\" name=\"");
        result.append(id);
        result.append("\" id=\"");
        result.append(id);
        result.append("\">");

    	// read the option data values
        getSelectOptionData(cms);
        
        // finish the HTML
        if (null != m_selectOptions) {
	        String selected = getSelectedValue(cms, param);
	        Iterator<SelectOptionValue> i = m_selectOptions.iterator();
	        while (i.hasNext()) {
	            SelectOptionValue option = (SelectOptionValue) i.next();
	            // create the option
	            result.append("<option value=\"");
	            result.append(option.getValue());
	            result.append("\"");
	            if ((selected != null) && selected.equals(option.getValue())) {
	                result.append(" selected=\"selected\"");
	            }
	            result.append(">");
	            result.append(option.getName());
	            result.append("</option>");
	        }
        }
	
        result.append("</select>");
        result.append("</td>");

        return result.toString();
	}

    /**
     * Reads the option values from the data source
     * 
     * @param cms CmsObject instance
     * @return List of SelectOptionValue objects read from the data source
     */
	protected List<SelectOptionValue> getSelectOptionData(CmsObject cms) {
        // set the configuration in the data source
        m_iDataSource.setConfiguration(m_config);

        // read the option values
        // data values are not cached by default, but can be cached
		// by setting the configuration option "cacheData='true'"
		String strCache = m_config.getConfigValue("cacheData");
		if (null != strCache && strCache.equalsIgnoreCase("true")) {
	        if (m_selectOptions == null) {
		        m_selectOptions = m_iDataSource.getValues(cms);
		    }
		    return m_selectOptions;
		} else {
			// not cached, re-read
			m_selectOptions = m_iDataSource.getValues(cms);
			return m_selectOptions;
		}
	}
}
