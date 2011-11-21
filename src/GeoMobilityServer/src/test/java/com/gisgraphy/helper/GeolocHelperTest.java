/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
package com.gisgraphy.helper;

import geomobility.core.exception.GeoException;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Provides useful methods for geolocalisation
 * 
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class GeolocHelperTest extends TestCase {
	public void testCreateSRS() throws GeoException {
		Assert.assertTrue(GeolocHelper.createCRS(4326).toString().contains("4326"));
		Assert.assertTrue(GeolocHelper.createCRS(900913).toString().contains("3785"));
	}

	public static void main(String[] args) {
		GeolocHelperTest test = new GeolocHelperTest();
		try {
			test.testCreateSRS();
		} catch (GeoException e) {
			e.printStackTrace();
		}
	}
}
