package geomobility.core.directory;

import geomobility.core.GlobalCore;
import geomobility.core.directory.dto.AttSearchResultDto;
import geomobility.core.directory.dto.DResultsDto;
import geomobility.core.directory.dto.LocSearchResultDto;
import geomobility.core.directory.query.AttSearch;
import geomobility.core.directory.query.DirectorySearch;
import geomobility.core.directory.query.LocSearch;
import geomobility.core.directory.query.RegSearch;
import geomobility.core.exception.DirectoryException;
import geomobility.core.exception.GeoException;
import geomobility.core.utils.StaticFunc;

import java.io.OutputStream;
import java.util.List;

import com.gisgraphy.domain.geoloc.service.ServiceException;

import net.opengis.xls.v_1_1_0.DirectoryRequestType;
import net.opengis.xls.v_1_1_0.DirectoryResponseType;
import net.opengis.xls.v_1_1_0.POILocationType;
import net.opengis.xls.v_1_1_0.ResponseType;
import net.opengis.xls.v_1_1_0.XLSType;

/**
 * @author VoMinhTam
 */
public class DController {
	public static String doController(String xml) throws GeoException, ServiceException {
		XLSType xlsType = StaticFunc.getXLSType(xml);
		List<DirectoryRequestType> listDirReqType = StaticFunc.parse(xlsType);

		xlsType.getBody().clear();

		for (DirectoryRequestType dirRequestType : listDirReqType) {
			DirectorySearch directorySearch = getTypeSearch(dirRequestType);

			DResultsDto resultsDto = directorySearch.searchPOI();
			DirectoryResponseType resType;
			if (resultsDto instanceof LocSearchResultDto) {
				resType = DBuilder.buildLoc((LocSearchResultDto) resultsDto);	
			} else {
				resType = DBuilder.buildAtt((AttSearchResultDto) resultsDto);
			}

			ResponseType responseType = new ResponseType();
			responseType.setResponseParameters(GlobalCore.objectFactoryXLS
					.createDirectoryResponse(resType));
			xlsType.getBody().add(GlobalCore.objectFactoryXLS.createResponse(responseType));
		}

		// Output to String
		OutputStream outputStream = StaticFunc.buildXLSResult(xlsType,GlobalCore.output);
		return outputStream.toString();
	}

	private static DirectorySearch getTypeSearch(DirectoryRequestType direct) {
		POILocationType poiLocationType = direct.getPOILocation();
		
		if (poiLocationType == null) {
			return new AttSearch(direct);
		} else {
			
			//NAM: diff reg search
			try {
				if(poiLocationType.getNearest() != null
						&& poiLocationType.getNearest().getLocation() != null 
						&& poiLocationType.getNearest().getLocation().size() > 1) {
					return new RegSearch(direct);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new LocSearch(direct);
		}
	}
	
	public static DirectoryResponseType doController(DirectoryRequestType directoryRequestType) throws GeoException, ServiceException{
		DirectorySearch directorySearch = getTypeSearch(directoryRequestType);

		DResultsDto resultsDto = directorySearch.searchPOI();
		DirectoryResponseType resType;
		if (resultsDto instanceof LocSearchResultDto) {
			resType = DBuilder.buildLoc((LocSearchResultDto) resultsDto);
		} else {
			resType = DBuilder.buildAtt((AttSearchResultDto) resultsDto);
		}

		return resType;
	}
}
