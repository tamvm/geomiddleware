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
/**
 *
 */
package geomobility.gisgraphy.builder;

import org.springframework.stereotype.Component;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FullTextSearchException;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQueryHttpBuilder;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.IFullTextSearchEngine;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.serializer.OutputFormat;

/**
 * A Fulltext Query builder. it build Fulltext query from HTTP Request
 * 
 * @see Pagination
 * @see Output
 * @see IFullTextSearchEngine
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@Component
public class FulltextQueryBuilder {

	private static FulltextQueryBuilder instance = new FulltextQueryBuilder();

	public static FulltextQueryBuilder getInstance() {
		return instance;

	}

	/**
	 * @param req
	 *            an HttpServletRequest to construct a
	 *            {@link FulltextQueryHttpBuilder}
	 */
	public FulltextQuery buildFromRequest(String country, String lang, String styleStr, String[] placetypes, 
			String q, int from, int maxResult, String indent, String spellchecking, String sortCriteria, boolean isAsc, String formatStr) {
		FulltextQuery query = null;
		String httpQueryParameter = q;
		if (httpQueryParameter != null) {
			query = new FulltextQuery(httpQueryParameter.trim());
		}
		if (httpQueryParameter == null || "".equals(httpQueryParameter.trim())) {
			throw new FullTextSearchException("query is not specified or empty");
		}
		if (httpQueryParameter.length() > FulltextQuery.QUERY_MAX_LENGTH) {
			throw new FullTextSearchException("query is limited to " + FulltextQuery.QUERY_MAX_LENGTH + "characters");
		}
		// pagination
		//pagination = Pagination.paginateWithMaxResults(query.getMaxLimitResult()).from(from).to(to).limitNumberOfResults(query.getMaxLimitResult());
		Pagination pagination = new Pagination(from, maxResult);
		
		// output
		OutputFormat format = OutputFormat.getFromString(formatStr);
		OutputStyle style = OutputStyle.getFromString(styleStr);
		String languageparam = lang;
		Output output = Output.withFormat(format).withLanguageCode(
				languageparam).withStyle(style);

		// placetype
		String[] placetypeParameters = placetypes;
		Class<? extends GisFeature>[] clazzs = null;
		if (placetypeParameters != null) {
			clazzs = new Class[placetypeParameters.length];
			for (int i = 0; i < placetypeParameters.length; i++) {
				Class<? extends GisFeature> classEntityFromString = GeolocHelper
						.getClassEntityFromString(placetypeParameters[i]);
				clazzs[i] = classEntityFromString;
			}
		}

		// countrycode
		String countrycodeParam = country;
		if (countrycodeParam == null) {
			query.limitToCountryCode(null);
		} else {
			query.limitToCountryCode(countrycodeParam.toUpperCase());

		}

		// indentation
		if ("true".equalsIgnoreCase(indent) || "on".equalsIgnoreCase(indent)) {
			output.withIndentation();
		}
		// spellchecking
		if ("true".equalsIgnoreCase(spellchecking)
				|| "on".equalsIgnoreCase(spellchecking)) {
			query.withSpellChecking();
		} else if ("false".equalsIgnoreCase(spellchecking)) {
			query.withoutSpellChecking();
		}

		query.withPagination(pagination);
		query.withPlaceTypes(clazzs);
		query.withOutput(output);

		query.setSortCriteria(sortCriteria);
		query.setAsc(isAsc);

		return query;
	}

}
