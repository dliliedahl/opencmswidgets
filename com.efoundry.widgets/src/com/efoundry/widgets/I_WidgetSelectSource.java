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

import java.util.List;

import org.opencms.file.CmsObject;

/**
 * This interface is used to retrieve a list of option values for a select object. The returned list
 * must contain instances of <code>{@link SelectOptionValue}</code> objects.
 */
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
