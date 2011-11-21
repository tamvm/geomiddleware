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

package com.gisgraphy.hibernate.criterion;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;

import com.gisgraphy.domain.valueobject.SRID;
import com.vividsolutions.jts.geom.Point;

/**
 * An implementation of the <code>Criterion</code> interface that implements
 * restriction for a psql intersects restriction
 * 
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class IntersectsRestrictionWithPoint implements Criterion {

	private Point point;

	private String columnName;

	private boolean isTransfrom = false;

	private static final long serialVersionUID = 1L;

	/**
	 * @param columnName
	 *            the name of the column that hold GIS information (typically
	 *            shape column)
	 * @param p
	 *            The shape we want to see if the column data intersects
	 */
	public IntersectsRestrictionWithPoint(String columnName, Point p) {
		if (columnName == null) {
			throw new IllegalArgumentException(
					"collumname is required  for IntersectsRestriction");
		}
		if (p == null) {
			throw new IllegalArgumentException(
					"polygon is required  for IntersectsRestriction");
		}
		this.columnName = columnName;
		this.point = p;
	}

	public IntersectsRestrictionWithPoint(String columnName, Point p,
			boolean isTransfrom) {
		this(columnName, p);
		this.isTransfrom = isTransfrom;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.criterion.Criterion#getTypedValues(org.hibernate.Criteria,
	 * org.hibernate.criterion.CriteriaQuery)
	 */
	// public TypedValue[] getTypedValues(Criteria criteria,
	// CriteriaQuery criteriaQuery) throws HibernateException {
	// return new TypedValue[] { criteriaQuery.getTypedValue(criteria,
	// Roads.WAY, point) };
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.criterion.Criterion#toSqlString(org.hibernate.Criteria,
	 * org.hibernate.criterion.CriteriaQuery)
	 */
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException {
		// Tam
		String columnName = criteriaQuery.getColumn(criteria, this.columnName);
		columnName = "GeometryFromText(" + columnName + ","
				+ SRID.WGS84_SRID.getSRID() + ")";

		StringBuffer result = new StringBuffer();
		if (!isTransfrom)
			result = result.append(columnName).append(" && ").append(
					"GeometryFromText( '" + point.toString() + "' ,"
							+ SRID.WGS84_SRID.getSRID() + ")");
		else
			result = result.append(columnName).append(" && ").append(
					"GeometryFromText( '" + point.toString() + "' ,"
							+ SRID.WGS84_SRID.getSRID() + ")");
		// String columnName = criteriaQuery.getColumn(criteria,
		// this.columnName);
		// StringBuffer result = new StringBuffer(columnName).append(" && ")
		// .append(" ? ");
		return result.toString();
	}

	public TypedValue[] getTypedValues(Criteria criteria,
			CriteriaQuery criteriaQuery) throws HibernateException {
		return new TypedValue[0];
	}

}
