//package tam;
//
//import geomobility.core.GlobalCore;
//import geomobility.core.directory.util.GlobalDirectory;
//import geomobility.gisgraphy.builder.GeolocQueryBuilder;
//
//import java.util.List;
//
//import org.hibernate.Session;
//
//import com.gisgraphy.domain.geoloc.entity.Adm;
//import com.gisgraphy.domain.geoloc.service.geoloc.GeolocQuery;
//import com.gisgraphy.domain.geoloc.service.geoloc.GeolocSearchEngine;
//import com.gisgraphy.domain.valueobject.GeolocResultsDto;
//
///**
// * @author VoMinhTam
// */
//public class Sample {
//	public static void main(String[] args) {
//		// Sample 1: Get specific feature by query
//		// System.out.println(getAllAdm());
//
//		// Sample 2: Get specific Gis feature
//		// AdmDao admDao = new AdmDao();
//		// System.out.println(admDao.get(7982L));
//
//		// !Sample 3: Search
//		// GisFeatureDao gisFeatureDao = new GisFeatureDao();
//		// System.out.println(gisFeatureDao.listFromText("Paris", true));
//
//		// Sample 4:
//		// GisFeatureDao gisFeatureDao = new GisFeatureDao();
//		// BusStationDao busStationDao = new BusStationDao();
//		// System.out.println(busStationDao.getNearestAndDistanceFromGisFeature(
//		// busStationDao.get(280247L), 1000, true));
//
//		// Sample 5:Street Search (Geocoder) in OpenStreetMapData
//		// StreetSearchQueryBuilder streetSearchQueryBuilder =
//		// StreetSearchQueryBuilder
//		// .getInstance();
//		// StreetSearchEngine streetSearchEngine = new StreetSearchEngine(
//		// new OpenStreetMapDao(), new StreetSearchResultsDtoSerializer(),
//		// new StatsUsageServiceImpl());
//		// streetSearchEngine.executeAndSerialize(streetSearchQueryBuilder
//		// .buildStreetSearchQuery(10, 106, 10000, 0, 3, "xml",
//		// "busstation", "no", "no", null, "BYWAY", "no",
//		// "parada parque saavedra", "CONTAINS"), System.out);
//
//		// Sample 6
//		GeolocQueryBuilder geolocQueryBuilder = GeolocQueryBuilder
//				.getInstance();
//		GeolocQuery geolocQuery = geolocQueryBuilder.buildGeolocQuery(-58, -34,
//				100000, 0, 3, "", "busstation", "no", "true", null, null);
//		GeolocSearchEngine geolocSearchEngine = new GeolocSearchEngine(
//				GlobalCore.statsUsageService);
//		GeolocResultsDto geolocResultsDto = geolocSearchEngine
//				.executeQuery(geolocQuery);
//		System.out.println(geolocResultsDto.getResult().get(0).getLat() + " " + geolocResultsDto.getResult().get(0).getLng());
//	}
//
//	private static List<Adm> getAllAdm() {
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		session.beginTransaction();
//		List result = session.createQuery("from Adm").setMaxResults(10).list();
//		// for (Event1 event : (List<Event1>) result) {
//		// System.out.println("Event (" + event.getDate() + ") : "
//		// + event.getTitle());
//		// }
//		session.getTransaction().commit();
//		session.close();
//
//		return (List<Adm>) result;
//	}
//
//}
