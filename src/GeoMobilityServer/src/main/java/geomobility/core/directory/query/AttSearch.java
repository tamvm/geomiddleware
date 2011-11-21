package geomobility.core.directory.query;

import geomobility.core.GlobalCore;
import geomobility.core.directory.DirectorySearchEngine;
import geomobility.core.directory.GlobalDirectory;
import geomobility.core.directory.dto.AttSearchResultDto;
import geomobility.core.directory.dto.DResultsDto;
import geomobility.core.exception.DirectoryException;
import geomobility.core.exception.GeoException;
import geomobility.gisgraphy.builder.FulltextQueryBuilder;
import geomobility.localization.I18N;

import java.util.List;

import javax.xml.bind.JAXBElement;

import net.opengis.xls.v_1_1_0.DirectoryRequestType;
import net.opengis.xls.v_1_1_0.POIPropertiesType;
import net.opengis.xls.v_1_1_0.POIPropertyNameType;
import net.opengis.xls.v_1_1_0.POIPropertyType;
import net.opengis.xls.v_1_1_0.SortDirectionType;

import com.gisgraphy.domain.geoloc.service.ServiceException;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery;
import com.gisgraphy.helper.GeolocHelper;

/**
 * @author VoMinhTam
 */
public class AttSearch extends DirectorySearch {
	public AttSearch(DirectoryRequestType requestType) {
		this.directoryRequestType = requestType;
	}

	public DResultsDto searchPOI() throws GeoException, ServiceException {
		//List<GisFeature> listResults = new ArrayList<GisFeature>();

		JAXBElement<? extends POIPropertiesType> jaxbPOIPropertiesType = 
			(JAXBElement<? extends POIPropertiesType>) directoryRequestType.getPOISelectionCriteria();

		String sortCriteria = directoryRequestType.getSortCriteria();
		SortDirectionType sortDirectionType = directoryRequestType.getSortDirection();

		POIPropertiesType poiPropertiesType = jaxbPOIPropertiesType.getValue();
		// TODO It's yellow page or other
		// String directoryType = poiPropertiesType.getDirectoryType();

		List<JAXBElement<?>> jaxbPOIPropertyType = poiPropertiesType.getPOIProperty();

		// Build FulltextQuery
		POIPropertyType poiProperttyType = (POIPropertyType) jaxbPOIPropertyType.get(0).getValue();
		POIPropertyNameType name = poiProperttyType.getName();
		String value = poiProperttyType.getValue();
		FulltextQuery query = null;
		String type = "";
		if (name == POIPropertyNameType.POI_NAME || name == POIPropertyNameType.KEYWORD) {
			String[] type_value =GeolocHelper.getListPOIType(value); 
			type = type_value[0];
			query = FulltextQueryBuilder.getInstance().buildFromRequest(null, null, null, new String[] { type },// get from query
					type_value[1], GlobalDirectory.FROM, GlobalDirectory.MAX_RESULTS, "no", "", sortCriteria, (sortDirectionType == SortDirectionType.ASCENDING) ? true : false, "");
		} else if (name == POIPropertyNameType.NAICS_TYPE || name == POIPropertyNameType.SIC_TYPE) {
			POIPropertyType poiProperttyType2 = (POIPropertyType) jaxbPOIPropertyType.get(1).getValue();
			POIPropertyNameType name2 = poiProperttyType2.getName();

			type = value;
			if (name2 == POIPropertyNameType.NAICS_SUB_TYPE || name2 == POIPropertyNameType.SIC_SUB_TYPE) {
				String value2 = poiProperttyType2.getValue();
				query = FulltextQueryBuilder.getInstance().buildFromRequest(null, null, null, new String[] { type },// type
							value2, GlobalDirectory.FROM, GlobalDirectory.MAX_RESULTS, "no", "", sortCriteria,
							(sortDirectionType == SortDirectionType.ASCENDING) ? true : false, "");
			} else {
				throw new DirectoryException(I18N.m.getString("have_not_supported_yet"));
			}
		} else {
			throw new DirectoryException(I18N.m.getString("have_not_supported_yet"));
		}

		DirectorySearchEngine directorySearchEngine = new DirectorySearchEngine(
				GlobalDirectory.daoMap.get(GeolocHelper.getClassEntityFromString(type)),GlobalCore.statsUsageService);
		// For each query we do the search
		AttSearchResultDto dto = directorySearchEngine.executeAttributeSearch(query);

		return dto;
	}
}
