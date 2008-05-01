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

/*
 * A simple bean class contains a single value to be displayed in a selection list. 
 * Custom Select Data Sources that implement the I_WidgetSelectSource interface must return
 * a List containing instances of these objects.
 */
public class CustomSelectValue {

	// Option Name (display value)
	protected String m_strName;
	// Option Value (stored value)
	protected String m_strValue;

	/**
	 * Object Constructor
	 * 
	 * @param Name Option Name (Display Value)
	 * @param Value Option Value (Stored Value)
	 */
	public CustomSelectValue(String Name, String Value) {
		m_strName = Name;
		m_strValue = Value;
	}
	
	/**
	 * Returns the Name
	 * @return String Name
	 */
	public String getName() {
		return m_strName;
	}
	
	/**
	 * Sets the Name
	 * @param Name
	 */
	public void setName(String Name) {
		m_strName = Name;
	}

	/**
	 * Returns the Value
	 * @return String Value
	 */
	public String getValue() {
		return m_strValue;
	}
	
	/**
	 * Sets the Value
	 * @param Value
	 */
	public void setValue(String Value) {
		m_strValue = Value;
	}
}
