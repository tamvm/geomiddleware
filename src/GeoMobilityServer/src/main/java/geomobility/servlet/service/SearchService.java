/**
 * Copyright 2010 BkitMobile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geomobility.servlet.service;

import geomobility.core.GlobalCore;
import geomobility.core.directory.DController;
import geomobility.core.gateway.GatewayController;
import geomobility.core.locutility.geo.GeoController;
import geomobility.core.presentation.PresentationController;
import geomobility.core.reqbuilder.GatewayBuilder;
import geomobility.core.reqbuilder.GeocodeBuilder;
import geomobility.core.reqbuilder.POIBuilder;
import geomobility.core.reqbuilder.ParamsKey;
import geomobility.core.reqbuilder.ParamsKey.GetLocationType;
import geomobility.core.reqbuilder.PresentationBuilder;
import geomobility.core.utils.StaticFunc;
import geomobility.servlet.BaseRestfulService;
import geomobility.servlet.service.exception.InvalidParameterException;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.opengis.gml.v_3_1_1.PointType;
import net.opengis.xls.v_1_1_0.DirectoryRequestType;
import net.opengis.xls.v_1_1_0.DirectoryResponseType;
import net.opengis.xls.v_1_1_0.GeocodeRequestType;
import net.opengis.xls.v_1_1_0.GeocodeResponseType;
import net.opengis.xls.v_1_1_0.PortrayMapRequestType;
import net.opengis.xls.v_1_1_0.PortrayMapResponseType;
import net.opengis.xls.v_1_1_0.ResponseType;
import net.opengis.xls.v_1_1_0.SLIAType;
import net.opengis.xls.v_1_1_0.SLIRType;
import net.opengis.xls.v_1_1_0.XLSType;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class SearchService extends BaseRestfulService {

	public SearchService(String name) {
		super(new String[]{ParamsKey.GET_LOCATION_TYPE});
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception {
		checkParams(params);

		DirectoryRequestType directoryRequestType = null;
		
		String getLocationType = getParameter(ParamsKey.GET_LOCATION_TYPE, params);
		if (GetLocationType.NONE.toString().equals(getLocationType)){
			directoryRequestType = POIBuilder.instance().build(params);
		}else if (GetLocationType.CURRENT_LOCATION.toString().equals(getLocationType)){
			SLIRType slirType = GatewayBuilder.instance().build(params);
			List<SLIAType> listSlias = GatewayController.doController(slirType);
			
			directoryRequestType = POIBuilder.instance().build(listSlias.get(0), params);
		}else if (GetLocationType.SPECIFIC.toString().equals(getLocationType)){
			Double lat = null;
			Double lng = null; 
			
			try {
				lat = Double.parseDouble(getParameter("LAT", params));
				lng = Double.parseDouble(getParameter("LNG", params));
			} catch (Exception e) {
			}
			
			if (lat!=null && lng!=null){
				PointType pointType = new PointType();
				pointType.getPos().getValue().add(lat);
				pointType.getPos().getValue().add(lng);
				
				directoryRequestType = POIBuilder.instance().build(pointType, params);
			}else {
				throw new InvalidParameterException();
			}
		}else if (GetLocationType.ADDRESS.toString().equals(getLocationType)){
			GeocodeRequestType geocodeRequestType = GeocodeBuilder.instance().build(params);
			GeocodeResponseType geocodeResponseType = GeoController.doController(geocodeRequestType);
			directoryRequestType = POIBuilder.instance().build(geocodeResponseType, params);
		}else{
			throw new InvalidParameterException(ParamsKey.GET_LOCATION_TYPE, "Invalid");
		}
		
		DirectoryResponseType directoryResponseType = DController.doController(directoryRequestType);
//		List<POIWithDistanceType> listPOIWithDistance = directoryResponseType.getPOIContext();
//		listPOIWithDistance.get(0).getPOI();
		
		//Presentation Request Builder
		PortrayMapRequestType portrayMapRequestType = PresentationBuilder.instance().build(directoryResponseType, params);
		//TODO
		PortrayMapResponseType portrayMapResponseType = PresentationController.doController2(portrayMapRequestType);
		
		//Output PortrayMapResponseType to user
		XLSType xlsType = GlobalCore.objectFactoryXLS.createXLSType();
		ResponseType responseType = new ResponseType();
		responseType.setResponseParameters(GlobalCore.objectFactoryXLS.createPortrayMapResponse(portrayMapResponseType));
		xlsType.getBody().add(GlobalCore.objectFactoryXLS.createResponse(responseType));

		// Output to String
		OutputStream outputStream = StaticFunc.buildXLSResult(xlsType,
				GlobalCore.output);
		return outputStream.toString();
	}
}
