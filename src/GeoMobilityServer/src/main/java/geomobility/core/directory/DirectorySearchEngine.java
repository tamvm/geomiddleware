package geomobility.core.directory;

import geomobility.core.directory.dto.AttSearchResultDto;
import geomobility.core.directory.dto.LocSearchResultDto;
import geomobility.core.utils.Log;
import geomobility.core.utils.StaticFunc;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import tam.HibernateUtil;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.service.ServiceException;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FullTextSearchEngine;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery;
import com.gisgraphy.domain.geoloc.service.geoloc.GeolocQuery;
import com.gisgraphy.domain.geoloc.service.geoloc.GeolocSearchException;
import com.gisgraphy.domain.repository.GenericGisDao;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.service.IStatsUsageService;
import com.gisgraphy.stats.StatsUsageType;

/**
 * @author VoMinhTam (vo.mita.ov@gmail.com)
 * @see {@link FullTextSearchEngine}
 */
public class DirectorySearchEngine {
	protected static final Logger logger = LoggerFactory
			.getLogger(FullTextSearchEngine.class);
	private static final String TAG = DirectorySearchEngine.class.getSimpleName();
	private GenericGisDao gisDao;
	private IStatsUsageService statsUsageService;
	public DirectorySearchEngine() {
	}

	public DirectorySearchEngine(IStatsUsageService statsUsageService) {
		this.statsUsageService = statsUsageService;
	}

	public DirectorySearchEngine(GenericGisDao gisFeatureDao, IStatsUsageService statsUsageService) {
		this.gisDao = gisFeatureDao;
		this.statsUsageService = statsUsageService;
	}

	public AttSearchResultDto executeAttributeSearch(FulltextQuery query)
			throws ServiceException {
		if (statsUsageService != null)
			statsUsageService.increaseUsage(StatsUsageType.FULLTEXT);
		long start = System.currentTimeMillis();
		//Assert.notNull(query, "Can not execute a null query");
//		gisFeatureList = gisDao.listByName(query.getQuery(), false, 
//				query.getCountryCode(), query.getSortCriteria(), query.isAsc());
		
		String queryString = "from " + gisDao.getPersistenceClass().getSimpleName() + " as c ";
		
		ArrayList<String> listWhere = new ArrayList<String>();
		if (!StaticFunc.isNOE(query.getQuery())){
			String keyword = StaticFunc.removeSign(query.getQuery());
			listWhere.add("no_sign(c.name) ilike '%"+ keyword +"%' or no_sign(c.adm1Name) ilike '%"+keyword+"%'");
		}
		
		if (!StaticFunc.isNOE(query.getCountryCode()))
			listWhere.add("countrycode='" + query.getCountryCode() + "'");
		
		String orderBy = StaticFunc.isNOE(query.getSortCriteria()) ? "" : " order by " + query.getSortCriteria() 
			+ ((query.isAsc()) ? " asc " : " desc ");

		queryString += (listWhere.size()>0)? " where " + StaticFunc.join(listWhere, " and ") : "";
		queryString += orderBy;
		Log.d(TAG, queryString);

		Session session = HibernateUtil.getSessionFactory().openSession();
		Query qry = session.createQuery(queryString).setFirstResult(query.getFirstPaginationIndex())
				.setMaxResults(query.getMaxNumberOfResults());
		qry.setCacheable(true);

		List<GisFeature> gisFeatureList = qry.list();
		if (gisFeatureList == null) {
			gisFeatureList = new ArrayList();
		}
		return new AttSearchResultDto(gisFeatureList, System.currentTimeMillis() - start);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gisgraphy.domain.geoloc.service.geoloc.IGeolocSearchEngine#executeQuery
	 * (com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery)
	 */
	public LocSearchResultDto executeNearest(GeolocQuery query)
			throws ServiceException {
		statsUsageService.increaseUsage(StatsUsageType.GEOLOC);
		//Assert.notNull(query, "Can not execute a null query");
		long start = System.currentTimeMillis();
		Class<? extends GisFeature> placetype = query.getPlaceType();
		GenericGisDao dao = GlobalDirectory.daoMap.get(query.getPlaceType());
		if (placetype != null) {
			GlobalDirectory.daoMap.get(placetype);
		}
		if (dao == null) {
			throw new GeolocSearchException("No gisFeatureDao or no placetype can be found for " + placetype + " can be found.");
		}
		List<GisFeatureDistance> results = dao.getNearestAndDistanceFrom(query.getPoint(), query.getRadius(),
			query.getFirstPaginationIndex(), query.getMaxNumberOfResults(),query.hasDistanceField(), 
			query.getQuery(), query.getMinRadius());

		long end = System.currentTimeMillis();
		long qTime = end - start;
		logger.info(query + " took " + (qTime) + " ms and returns " + results.size() + " results");
		return new LocSearchResultDto(results, qTime);

	}

	public LocSearchResultDto searchInPolygon(GeolocQuery query) throws GeolocSearchException {
		statsUsageService.increaseUsage(StatsUsageType.GEOLOC);
		Assert.notNull(query, "Can not execute a null query");
		long start = System.currentTimeMillis();
		Class<? extends GisFeature> placetype = query.getPlaceType();
		GenericGisDao dao = GlobalDirectory.daoMap.get(query.getPlaceType());
		if (placetype != null) {
			GlobalDirectory.daoMap.get(placetype);
		}
		if (dao == null)
			throw new GeolocSearchException("No gisFeatureDao or no placetype can be found for " + placetype + " can be found.");

		List<GisFeatureDistance> results = dao.getInPolygon(query.getPolygon(),query.getExterior(), 
				query.getFirstPaginationIndex(), query.getMaxNumberOfResults(), query.getPlaceType(),
				query.getQuery(), query.getSortCriteria());

		return new LocSearchResultDto(results, System.currentTimeMillis() - start);
	}

	/**
	 * @param gisDao
	 *            the gisDao to set
	 */
	public void setGisDao(GenericGisDao gisDao) {
		this.gisDao = gisDao;
	}

	/**
	 * @return the gisDao
	 */
	public GenericGisDao getGisDao() {
		return gisDao;
	}
}
