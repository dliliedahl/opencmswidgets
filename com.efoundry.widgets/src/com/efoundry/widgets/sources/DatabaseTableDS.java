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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.opencms.file.CmsObject;

import com.efoundry.widgets.CustomSourceConfiguration;
import com.efoundry.widgets.I_WidgetSelectSource;
import com.efoundry.widgets.SelectOptionValue;

/**
 * This class implements a Custom Data source for the <code>{@link CmsDataSourceSelectWidget}</code>. The class
 * retrieves the values using a database query. This data source may be configured in the following way:
 * <p>
 *	<code>
 *      &lt;layout element="SomeField" widget="DataSourceSelectWidget"
 *				configuration="source='com.efoundry.widgets.sources.DatabaseTableDS'|jndiname='jdbc/mySource'|qry='select name,value from some_table'"/&gt;
 * </code>
 * <p>
 * The 'source', 'jndiname', and 'qry' parameters are delimited with a <code>|</code> character. The 'jndiname' parameter
 * contains a name of the JNDI resource that is used for the data connection, and the qry contains the query to be used.
 * The data source obtains the name of the option value from the first returned column, and the value from the second one.
 * Any other columns used in the query are ignored.
 */
public class DatabaseTableDS implements I_WidgetSelectSource {

	/** some constants */
	protected static final String JNDINAME = "jndiname";
	protected static final String QRY = "qry";
	
	// Name of the JNDI datasource
	String m_strJNDIName;
	
	// Query to be used
	String m_strQuery;
	
	public List<SelectOptionValue> getValues(CmsObject cms) {
		// list of return values
		ArrayList<SelectOptionValue> aVals = new ArrayList<SelectOptionValue>();

		// open the data-source and retrieve the values
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context) initContext.lookup("java:/comp/env");
	    	// use the provided jndi datasource name
			DataSource ds = (DataSource) envContext.lookup(m_strJNDIName);
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			try {
				// run the query
			    ResultSet rs = stmt.executeQuery(m_strQuery);
			    try {
			        while ( rs.next() ) {
			        	// Fieldname is in column 1, Value is in column 2
		        		aVals.add(new SelectOptionValue((String) rs.getObject(2), (String) rs.getObject(2)));
			        }
			    } finally {
			        rs.close();
			    }
			} finally {
			    stmt.close();
			    conn.close();
			}	
	    } catch (Exception e) {
	    	// return the error in the select list
	    	aVals.add(new SelectOptionValue(e.getMessage(), e.getMessage()));
	   	}
		
		return aVals;
	}

	public void setConfiguration(CustomSourceConfiguration config) {
		m_strJNDIName = config.getConfigValue(JNDINAME);
		this.m_strQuery = config.getConfigValue(QRY);
	}

}
