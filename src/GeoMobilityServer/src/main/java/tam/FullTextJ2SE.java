package tam;

import geomobility.core.directory.DirectorySearchEngine;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery;
import com.gisgraphy.domain.repository.BusStationDao;
import com.gisgraphy.service.impl.StatsUsageServiceImpl;

/**
 * @author VoMinhTam
 */
public class FullTextJ2SE {
	public static void main(String[] args) {
		FulltextQuery query = new FulltextQuery("parada parque saavedra");

		DirectorySearchEngine fullTextSearchEngine = new DirectorySearchEngine(
				new BusStationDao(), new StatsUsageServiceImpl());
//		System.out.println(fullTextSearchEngine
//				.executeQueryToDatabaseObjectsTam(query));
	}
}
