package geomobility.core.locutility.geo;

import geomobility.core.locutility.GlobalLocUtility;
import geomobility.core.locutility.LocUtilEngine;
import geomobility.core.locutility.QueryResultDto;
import geomobility.core.utils.StaticFunc;
import geomobility.data.osm.entity.result.RoadsResult;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.opengis.xls.v_1_1_0.AddressType;
import net.opengis.xls.v_1_1_0.GeocodeRequestType;

import org.hibernate.HibernateException;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.StreetSearchMode;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class GeoExe {
	public static QueryResultDto<List<RoadsResult>> execute(GeocodeRequestType requestType) throws HibernateException, SQLException {
		QueryResultDto<List<RoadsResult>> result = new QueryResultDto<List<RoadsResult>>();
		long start = System.currentTimeMillis();
		
		Set<String> listStreet = new HashSet<String>();
		
		for (AddressType addressType : requestType.getAddress()){
			if (addressType.getStreetAddress().getStreet().size()==1){
				//geocode_nearest_full_adr
				String adr = AdrParser.parseAdr(addressType.getStreetAddress().getStreet().get(0).getValue()).get(AdrParser.STREET);
				result.getResult().add(LocUtilEngine.getInstance().searchRoads(adr, GlobalLocUtility.FROM, 
						GlobalLocUtility.MAX_RESULTS, StreetSearchMode.CONTAINS, addressType.getCountryCode()));
			}else if (addressType.getStreetAddress().getStreet().size()>1){
				//geocode_nearest_intersection_adr
				String adr1 = AdrParser.parseAdr(addressType.getStreetAddress().getStreet().get(0).getValue()).get(AdrParser.STREET);
				String adr2 = AdrParser.parseAdr(addressType.getStreetAddress().getStreet().get(1).getValue()).get(AdrParser.STREET);

				List<RoadsResult> listRoads1 = LocUtilEngine.getInstance().searchRoads(adr1, 0,1, StreetSearchMode.CONTAINS, addressType.getCountryCode());
				List<RoadsResult> listRoads2 = LocUtilEngine.getInstance().searchRoads(adr2, 0,1, StreetSearchMode.CONTAINS, addressType.getCountryCode());
				
				if (listRoads1.size()>0 && listRoads2.size()>0){
					String intersecPointStr = LocUtilEngine.getInstance().intersectionAdr(
							listRoads1.get(0).getWay().toString(), listRoads2.get(0).getWay().toString());
					
					if (!StaticFunc.isNOE(intersecPointStr)){
						for (RoadsResult rr : listRoads1){
							rr.setInterpolateStr(intersecPointStr);
						}
						
						for (RoadsResult rr : listRoads2){
							rr.setInterpolateStr(intersecPointStr);
						}
						result.getResult().add(listRoads1);
						result.getResult().add(listRoads2);
					}else{
						result.getResult().add(listRoads1);
					}
				}else if (listRoads1.size()>0){
					result.getResult().add(listRoads1);
				}else if (listRoads2.size()>0){
					result.getResult().add(listRoads2);
				}
			}
		}

		long end = System.currentTimeMillis();
		result.setTime(end - start);

		return result;
	}
	
//	public static
}
