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
import java.util.List;

import org.opencms.file.CmsObject;

import com.efoundry.widgets.CustomSourceConfiguration;
import com.efoundry.widgets.I_WidgetSelectSource;
import com.efoundry.widgets.Messages;
import com.efoundry.widgets.SelectOptionValue;

/**
 * This is a default class for options
 */
public class DefaultDS implements I_WidgetSelectSource {

	/**
	 * Public constructor is necessary
	 */
	public DefaultDS() {
	}

	/**
	 * Returns a list that reports an error message
	 */
	public List<SelectOptionValue> getValues(CmsObject cms) {
		ArrayList<SelectOptionValue> aVals = new ArrayList<SelectOptionValue>();
		
		aVals.add(new SelectOptionValue(Messages.get().getBundle().key(Messages.DEFAULT_DS_ERROR_MSG_0), "null"));
		return aVals;
	}

	public void setConfiguration(CustomSourceConfiguration config) {
		// don't care
	}
}
