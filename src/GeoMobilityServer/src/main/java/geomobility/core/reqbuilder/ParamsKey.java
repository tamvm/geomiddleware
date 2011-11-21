package geomobility.core.reqbuilder;

import net.opengis.xls.v_1_1_0.NearestCriterionType;

/**
 * 
 * @author VoMinhTam
 *
 */
public class ParamsKey {

	//Request header
	public static final String VERSION_KEY = "version";
	public static final String USERNAME_KEY = "username";
	public static final String PASSWORD_KEY = "password";
	public static final String SESSION_KEY = "session";
	public static final String MSID = "msid";
	
	////Search Service
	public static final String GET_LOCATION_TYPE = "type";
	public static enum GetLocationType{
		NONE("none"), CURRENT_LOCATION("current"), SPECIFIC("specific"), ADDRESS("address");
		private String name;
		
		GetLocationType(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}
	public static final String LAT = "lat";
	public static final String LNG = "lng";
	
	//Gateway Service
	public static final String GW_MSIDTYPE = "msid_type";
	public static final String GW_MSIDVALUE = "msid_value";
	
	// Directory Service
	public static final String D_SEARCHTYPE_KEY = "search_type";
	public static final String D_DIRECTORYTYPE_KEY = "directory";
	public static final String D_POITYPE_KEY = "poi_type";
	public static final String D_POISUBTYPE_KEY = "poi_subtype";
	public static final String D_NEARESTTYPE_KEY = "nearest_type";
	public static final String D_MAXDISTANCE_KEY = "max"; 
	public static final String D_MINDISTANCE_KEY = "min"; 
	
	public static final String D_DEFAULT_DIRECTORYTYPE_KEY = "Yellow Pages";
	
	//Geocode
	public static final String G_COUNTRYCODE = "country_code";
	public static final String G_POSTALCODE = "postal";
	public static final String G_STREETNAME = "street";
	public static final String G_COUNTRY_SUBDIVISION = "subdivision";
	public static final String G_COUNTRY_SECONDARY_SUBDIVISION = "second_subdivision";
	
	//Presentation
	public static final String P_WIDTH = "width";
	public static final String P_HEIGHT = "height";
	public static final String P_START_LAT = "start_lat";
	public static final String P_START_LNG = "start_lng";
	public static final String P_END_LAT = "end_lat";
	public static final String P_END_LNG = "end_lng";
	
	public static enum SearchType {
		NEAREST("nearest"), WITHIN_DISTANCE("distance"), WITHIN_BOUNDARY("boundary");

		private String name;

		SearchType(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(NearestCriterionType.SHORTEST);
	}

}
