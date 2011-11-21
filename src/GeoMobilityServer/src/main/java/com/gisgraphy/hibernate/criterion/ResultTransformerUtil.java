/**
 * ResultTransformerUtil.java
 * 
 * Mercer Inc.
 * JBossMHR
 * Copyright 2008 All Rights Reserved
 * @since 1.0 May 14, 2008
 * =============================================================================================
 * $Id: ResultTransformerUtil.java,v 1.1 2008/05/14 14:44:23 abhishekm Exp $
 * =============================================================================================
 */
package com.gisgraphy.hibernate.criterion;

import geomobility.data.osm.entity.Roads;
import geomobility.data.osm.entity.result.PointsResult;
import geomobility.data.osm.entity.result.RoadsResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;

import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.domain.valueobject.StreetDistance;

/**
 * The Class ResultTransformerUtil.
 * 
 * @author Abhishek Mirge
 */
public class ResultTransformerUtil<T> {

	/**
	 * Transform to bean. See bug
	 * http://opensource.atlassian.com/projects/hibernate/browse/HHH-2463
	 * 
	 * @param aliasList
	 *            the alias list
	 * @param resultList
	 *            the result list
	 * 
	 * @return the list of GisFeatureDistance
	 */
	public static List<GisFeatureDistance> transformToGisFeatureDistance(
			String aliasList[], List<?> resultList,
			Map<Long, List<String>> featureIdToZipCodesMap) {
		List<GisFeatureDistance> results = new ArrayList<GisFeatureDistance>();
		if (aliasList != null && !resultList.isEmpty()) {
			ResultTransformer tr = new AliasToBeanResultTransformer(
					GisFeatureDistance.class);
			Iterator<?> it = resultList.iterator();
			Object[] obj;
			GisFeatureDistance gisFeatureDistance;
			while (it.hasNext()) {
				obj = (Object[]) it.next();
				gisFeatureDistance = (GisFeatureDistance) tr.transformTuple(
						obj, aliasList);
				int indexInList = results.indexOf(gisFeatureDistance);
				if (indexInList == -1) {
					gisFeatureDistance.updateFields();
					results.add(gisFeatureDistance);
					if (featureIdToZipCodesMap != null) {
						gisFeatureDistance.setZipCodes(featureIdToZipCodesMap
								.get(gisFeatureDistance.getId()));
					}
				}
			}
		}

		return results;
	}

	public static List<PointsResult> transformToPoints(String aliasList[],
			List<?> resultList) {
		List<PointsResult> results = new ArrayList<PointsResult>();
		if (aliasList != null && !resultList.isEmpty()) {
			ResultTransformer tr = new AliasToBeanResultTransformer(
					PointsResult.class);
			Iterator<?> it = resultList.iterator();
			Object[] obj;
			PointsResult t;
			while (it.hasNext()) {
				obj = (Object[]) it.next();
				t = (PointsResult) tr.transformTuple(obj, aliasList);

				int indexInList = results.indexOf(t);
				if (indexInList == -1) {
					results.add(t);
				}
			}
		}

		return results;
	}

	public static Roads transformToRoads(String aliasList[], Object[] obj) {
		if (aliasList != null && obj != null) {
			ResultTransformer tr = new AliasToBeanResultTransformer(Roads.class);
			Roads t = (Roads) tr.transformTuple(obj, aliasList);

			return t;
		}

		return null;
	}

	public static List<RoadsResult> transformToListRoadsResult(
			String aliasList[], List<?> resultList, int positionCentroid) {
		List<RoadsResult> results = new ArrayList<RoadsResult>();
		if (aliasList != null && !resultList.isEmpty()) {
			ResultTransformer tr = new AliasToBeanResultTransformer(RoadsResult.class);
			Iterator<?> it = resultList.iterator();
			Object[] obj;
			RoadsResult t;
			while (it.hasNext()) {
				obj = (Object[]) it.next();

				t = (RoadsResult) tr.transformTuple(obj, aliasList);
				t.setInterpolateStr(obj[positionCentroid] + "");
				int indexInList = results.indexOf(t);
				if (indexInList == -1) {
					results.add(t);
				}
			}
		}

		return results;
	}

	public static List<Roads> transformToListRoads(String aliasList[],
			List<?> resultList) {
		List<Roads> results = new ArrayList<Roads>();
		if (aliasList != null && !resultList.isEmpty()) {
			ResultTransformer tr = new AliasToBeanResultTransformer(Roads.class);
			Iterator<?> it = resultList.iterator();
			Object[] obj;
			Roads t;
			while (it.hasNext()) {
				obj = (Object[]) it.next();

				t = (Roads) tr.transformTuple(obj, aliasList);
				int indexInList = results.indexOf(t);
				if (indexInList == -1) {
					results.add(t);
				}
			}
		}

		return results;
	}

	/**
	 * Transform to bean. See bug
	 * http://opensource.atlassian.com/projects/hibernate/browse/HHH-2463
	 * 
	 * @param aliasList
	 *            the alias list
	 * @param resultList
	 *            the result list
	 * 
	 * @return the list of {@link StreetDistance}
	 */
	public static List<StreetDistance> transformToStreetDistance(
			String aliasList[], List<?> resultList) {
		List<StreetDistance> transformList = new ArrayList<StreetDistance>();
		if (aliasList != null && !resultList.isEmpty()) {
			AliasToBeanResultTransformer tr = new AliasToBeanResultTransformer(
					StreetDistance.class);
			Iterator<?> it = resultList.iterator();
			Object[] obj;
			while (it.hasNext()) {
				obj = (Object[]) it.next();
				StreetDistance streetDistance = (StreetDistance) tr
						.transformTuple(obj, aliasList);
				streetDistance.updateFields();
				transformList.add(streetDistance);
			}
		}
		return transformList;
	}

}
