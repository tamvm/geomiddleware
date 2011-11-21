package geomobility.core.reqbuilder;

import geomobility.core.utils.Config;
import geomobility.core.utils.StaticFunc;
import geomobility.servlet.service.exception.InvalidParameterException;
import geomobility.servlet.service.exception.MissingParameterException;
import geomobility.servlet.service.exception.RestfulException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.taskdefs.XSLTProcess.Param;

import net.opengis.gml.v_3_1_1.DirectPositionType;
import net.opengis.gml.v_3_1_1.EnvelopeType;
import net.opengis.xls.v_1_1_0.DirectoryResponseType;
import net.opengis.xls.v_1_1_0.OutputType;
import net.opengis.xls.v_1_1_0.OverlayType;
import net.opengis.xls.v_1_1_0.POIWithDistanceType;
import net.opengis.xls.v_1_1_0.PointOfInterestType;
import net.opengis.xls.v_1_1_0.PortrayMapRequestType;

/**
 * 
 * @author VoMinhTam
 *
 */
public class PresentationBuilder extends RequestBuilder{
	// Singleton
	private static PresentationBuilder i = null;
	public static PresentationBuilder instance(){
		if (i==null)
			i = new PresentationBuilder();
		return i;
	}
	
	public PortrayMapRequestType build(DirectoryResponseType directoryResponseType, Map<String, String[]> params) throws RestfulException{
//		String width = getParameter(ParamsKey.P_WIDTH, params, String.valueOf(Config.DEFAULT_WIDTH));
//		String height = getParameter(ParamsKey.P_HEIGHT, params, String.valueOf(Config.DEFAULT_HEIGHT));
//		
//		Double startLat = StaticFunc.parseDouble(getParameter(ParamsKey.P_START_LAT, params, null));
//		Double startLng = StaticFunc.parseDouble(getParameter(ParamsKey.P_START_LNG, params, null));
//		Double endLat = StaticFunc.parseDouble(getParameter(ParamsKey.P_END_LAT, params, null));	
//		Double endLng =  StaticFunc.parseDouble(getParameter(ParamsKey.P_END_LNG, params, null));
		
		Double width = StaticFunc.parseDouble(getParameter(ParamsKey.P_WIDTH, params, String.valueOf(Config.DEFAULT_WIDTH)));
		Double height = StaticFunc.parseDouble(getParameter(ParamsKey.P_HEIGHT, params, String.valueOf(Config.DEFAULT_HEIGHT)));
		
		Double startLat = StaticFunc.parseDouble(getParameter(ParamsKey.P_START_LAT, params, null));
		Double startLng = StaticFunc.parseDouble(getParameter(ParamsKey.P_START_LNG, params, null));
		Double endLat = StaticFunc.parseDouble(getParameter(ParamsKey.P_END_LAT, params, null));	
		Double endLng =  StaticFunc.parseDouble(getParameter(ParamsKey.P_END_LNG, params, null));
		
		if (width==null || height==null || startLat==null || startLng==null || endLat==null || endLng==null)
			throw new InvalidParameterException(ParamsKey.P_WIDTH + "," + ParamsKey.P_HEIGHT + "," + ParamsKey.P_START_LAT + "," + ParamsKey.P_START_LNG + "," + ParamsKey.P_END_LAT + "," + ParamsKey.P_END_LNG, " may be incorect format");
		
		PortrayMapRequestType portrayMapRequestType = new PortrayMapRequestType();
		ArrayList<OverlayType> listOverlayTypes = new ArrayList<OverlayType>();
		portrayMapRequestType.setOverlay(listOverlayTypes);
		
		List<POIWithDistanceType> listPOIs = directoryResponseType.getPOIContext();
		Double ulLat = 0D;
		Double ulLng = Double.MAX_VALUE;
		Double lrLat = Double.MAX_VALUE;
		Double lrLng = 0D;
		
		for (POIWithDistanceType poiWithDistanceType : listPOIs){
			PointOfInterestType poi = poiWithDistanceType.getPOI();
			
			OverlayType overlayType = new OverlayType();
			overlayType.setPOI(poi);
			listOverlayTypes.add(overlayType);
			
			//Find upper left and lower right of BBox
			Double lat = poi.getPoint().getPos().getValue().get(0);
			Double lng = poi.getPoint().getPos().getValue().get(1);
			
			if (lat >ulLat)
				ulLat = lat;
			if (lng<ulLng)
				ulLng = lng;
			if (lat<lrLat)
				lrLat = lat;
			if (lng>lrLng)
				lrLng = lng;
			
			//TODO If there's just ONE POI returned
		}
//		
//		OutputType outputType = new OutputType();
//		List<OutputType> listOutputTypes = new ArrayList<OutputType>();
//		listOutputTypes.add(outputType);
//		
//		portrayMapRequestType.setOutput(listOutputTypes);
		
		//@Khiem help me to input some sample OutputType and LayerType
		//Add OutputType
		/*<Output width="400" height="400" format="png8">
			<BBoxContext>
				<gml:pos>-117.204 33.977</gml:pos>
				<gml:pos>-117.094 34.333</gml:pos>
			</BBoxContext>
		</Output>*/
		
		if (startLat==null || startLng==null || endLat==null || endLng==null)
			throw new MissingParameterException("");
		
		OutputType outputType = new OutputType();
		portrayMapRequestType.getOutput().add(outputType);

		outputType.setFormat(Config.DEFAULT_FORMAT_OUTPUT);
		//Calculate width and height
		double outputWidth = Math.abs(ulLat - lrLat)*width/Math.abs(startLat-endLat);
		double outputHeight = Math.abs(lrLng - ulLng)*height/Math.abs(endLng-startLng);
		
		outputType.setWidth(new BigInteger(String.valueOf(outputWidth)));
		outputType.setHeight(new BigInteger(String.valueOf(outputHeight)));
		
		EnvelopeType envelopeType = new EnvelopeType();
		outputType.setBBoxContext(envelopeType);
		
		DirectPositionType upperDirectPositionType =new DirectPositionType();
		upperDirectPositionType.getValue().add(ulLat);
		upperDirectPositionType.getValue().add(ulLng);
		
		DirectPositionType lowerDirectPositionType = new DirectPositionType();
		lowerDirectPositionType.getValue().add(lrLat);
		lowerDirectPositionType.getValue().add(lrLng);
		
		envelopeType.setUpperCorner(upperDirectPositionType);
		envelopeType.setLowerCorner(lowerDirectPositionType);
		
		return portrayMapRequestType;
	}
}
