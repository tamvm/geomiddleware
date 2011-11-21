package geomobility.core.presentation;

import geomobility.core.exception.GeoException;
import geomobility.core.exception.PresentationException;
import geomobility.core.utils.Config;
import geomobility.core.utils.Log;
import geomobility.core.utils.StaticFunc;
import geomobility.localization.I18N;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.TransformerException;

import junit.framework.Assert;
import net.opengis.gml.v_3_1_1.AbstractRingPropertyType;
import net.opengis.gml.v_3_1_1.BoundingBoxType1;
import net.opengis.gml.v_3_1_1.CircleByCenterPointType;
import net.opengis.gml.v_3_1_1.CoordType;
import net.opengis.gml.v_3_1_1.DirectPositionType;
import net.opengis.gml.v_3_1_1.EnvelopeType;
import net.opengis.gml.v_3_1_1.LineStringType;
import net.opengis.gml.v_3_1_1.LinearRingType;
import net.opengis.gml.v_3_1_1.MultiPolygonType;
import net.opengis.gml.v_3_1_1.PointType;
import net.opengis.gml.v_3_1_1.PolygonType;
import net.opengis.sld.v_1_1_0.GetMapType;
import net.opengis.sld.v_1_1_0.OutputType.Size;
import net.opengis.xls.v_1_1_0.CenterContextType;
import net.opengis.xls.v_1_1_0.CircularArcType;
import net.opengis.xls.v_1_1_0.EllipseType;
import net.opengis.xls.v_1_1_0.MapType;
import net.opengis.xls.v_1_1_0.OutputType;
import net.opengis.xls.v_1_1_0.OverlayType;
import net.opengis.xls.v_1_1_0.PointOfInterestType;
import net.opengis.xls.v_1_1_0.PortrayMapRequestType;
import net.opengis.xls.v_1_1_0.PositionType;
import net.opengis.xls.v_1_1_0.RadiusType;
import net.opengis.xls.v_1_1_0.RouteGeometryType;

import org.geotools.GML;
import org.geotools.GML.Version;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.UserLayer;
import org.geotools.styling.builder.AnchorPointBuilder;
import org.geotools.styling.builder.DisplacementBuilder;
import org.geotools.styling.builder.PointPlacementBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class GeoServerRequestBuilder {

	private static final String TAG = GeoServerRequestBuilder.class
			.getSimpleName();
	private static int j;
	
	private static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
	private static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
	
	

	/**********************************/
	/**
	 * @deprecated
	 */
	private static String buildURLGetMap(double x1, double y1, double x2,
			double y2, String format, String layer, int width, int height,
			int[] featureID, String srs) {
		StringBuilder str = new StringBuilder(Config.WMS_PATH).append(String
				.format("?bbox=%f,%f,%f,%f", x1, y1, x2, y2));
		str.append("&Format=").append(
				StaticFunc.isNOE(format) ? Config.DEFAULT_FORMAT_OUTPUT
						: format);
		str.append("&request=GetMap");
		str.append("&layers=").append(
				StaticFunc.isNOE(layer) ? Config.DEFAULT_LAYER : layer);
		str.append("&width=").append(width == 0 ? Config.DEFAULT_WIDTH : width);
		str.append("&height=").append(
				width == 0 ? Config.DEFAULT_HEIGHT : height);
		str.append("&srs=").append(
				StaticFunc.isNOE(srs) ? Config.DEFAULT_SRD : srs);
		str.append("&featureid=");
		if (featureID.length > 0) {
			for (int i = 0; i < featureID.length; i++) {
				str.append(Config.DEFAULT_LAYER_NAME + "." + featureID[i]);
				if (i != featureID.length - 1)
					str.append(",");
			}
		} else {
			str.append("0");
		}

		Log.d(TAG, str.toString());
		return str.toString();
	}

	public static List<GetMapType> buildRequestGetMap(
			PortrayMapRequestType portrayMap) throws GeoException, TransformerException {
		List<GetMapType> results = new ArrayList<GetMapType>();		

		for (int i = 0; i < portrayMap.getOutput().size(); i++) {
			GetMapType getMapType = new GetMapType();
			getMapType.VERSION = "1.2.0";
			getMapType.service = "WMS";
			getMapType.setCRS("EPSG:4326");
			Log.d("GeoServerRequestBuilder - GetMapType toString", getMapType.toString());
			results.add(getMapType);

			//BoundingBox
			OutputType output = portrayMap.getOutput().get(i);
			BoundingBoxType1 boundingBoxType1 = addBoundingBox(output);
			getMapType.setBoundingBox(boundingBoxType1);
			
			//OutputType
			net.opengis.sld.v_1_1_0.OutputType outputTypeSLD = addOutputTypeSLD(output);
			getMapType.setOutput(outputTypeSLD);
		}
		
		return results;
	}
	
	
	public static List<String> bulidRequestSLDString(PortrayMapRequestType portrayMap) throws TransformerException, PresentationException, IOException, SchemaException
	{
		List<String> results = new ArrayList<String>();
		
			
		
		for (int i = 0; i < portrayMap.getOutput().size(); i++) {
			String xml = "";
			
			//StyledLayerDescriptor
			SLDTransformer transformer = new SLDTransformer();
			StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();		
			
			//UserStyle					
			
			
			String[] fcXML;
			//Convert Overlays to UserLayers
			if(portrayMap.getOverlay().size() != 0)
			{
				//NamedLayer
//				NamedLayer namedLayer = styleFactory.createNamedLayer();
//				namedLayer.setName("airport");
//				sld.addStyledLayer(namedLayer);
				
				fcXML = new String[portrayMap.getOverlay().size()];
			for(int j = 0; j < portrayMap.getOverlay().size();j++)
			{
				 
				//UserLayer
				//UserLayer userlayer = styleFactory.createUserLayer();
				UserLayer userlayer = convertOverlayType2UserStyle(portrayMap.getOverlay().get(j));
				sld.addStyledLayer(userlayer);	
				
			
				
				//InlineFeature			
				Log.d("GeoServerRequestBuilder", portrayMap.getOverlay().toString());
				fcXML[j] = convertOverlayType2FeatureCollections(portrayMap.getOverlay().get(j));				
				Log.d("GeoServerRequestBuilder - XML" , fcXML[j]);
				
				//NamedLayer
//				NamedLayer namedLayer = styleFactory.createNamedLayer();
//				namedLayer.setName("airport");
//				sld.addStyledLayer(namedLayer);
			}
		
			}
			else
			{
				fcXML = new String[1];
				//UserLayer
				UserLayer userlayer = createDefaultUserLayer();
				sld.addStyledLayer(userlayer);
				
				//InlineFeature
				fcXML[0] = createDefaultFeatureCollection();
				
				//NamedLayer
//				NamedLayer namedLayer = styleFactory.createNamedLayer();
//				namedLayer.setName("airport");
//				sld.addStyledLayer(namedLayer);
			}
			
			
			
			
			String sldXML = transformer.transform(sld);			
			Log.d("GeoServerRequestBuilder - XML SLD", sldXML);
			
			
			//Merge fcXML to sldXML
			xml = sldXML;
			int indexOfUserLayer = 0;
			String patternUserLayer = "<sld:UserLayer>";
			int insertingIndex = 0;
			for(int j = 0; j < fcXML.length;j++)
			{
				insertingIndex = xml.indexOf(patternUserLayer, indexOfUserLayer) + patternUserLayer.length();
				xml = xml.substring(0,insertingIndex) + fcXML[j] + xml.substring(insertingIndex);
				Log.d("GeoServerRequestBuilder - Updating XML", xml);
				indexOfUserLayer = insertingIndex;
			}
			
			
			xml = xml.substring(xml.indexOf('<', 1));
			
			Log.d("GeoServerRequestBuilder - XML SLD + FC", xml);
			
			
			results.add(xml);
		
		}
		
		

		
		return results;
	}	
	
	
	private static FeatureTypeStyle createPOIPointStyle(String typeOfLabel)
	{
		Graphic gr = styleFactory.createDefaultGraphic();

		Mark mark = styleFactory.getCircleMark();		

		mark.setStroke(styleFactory.createStroke(filterFactory.literal(Color.BLUE), filterFactory.literal(1)));

		mark.setFill(styleFactory.createFill(filterFactory.literal(Color.CYAN)));

		gr.graphicalSymbols().clear();
		gr.graphicalSymbols().add(mark);


		
		PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);
		//text.setLabelPlacement(new l)
		TextSymbolizer text  = styleFactory.createTextSymbolizer();
		PropertyName proText = filterFactory.property(typeOfLabel);
		
		PointPlacementBuilder<String> pointPlacementBuilder = new PointPlacementBuilder<String>();
		//LabelPlacementBuilder<String> labelPlacementBuilder = new LabelPlacementBuilder<String>();
		AnchorPointBuilder<String> anchorPointBuilder = new AnchorPointBuilder<String>();
		DisplacementBuilder<String> displacementBuilder = new DisplacementBuilder<String>();
		
		
		PointPlacement pointPlacement = pointPlacementBuilder.build();
		AnchorPoint anchorPoint = anchorPointBuilder.build();
		anchorPoint.setAnchorPointX(filterFactory.literal(0.0));
		anchorPoint.setAnchorPointY(filterFactory.literal(0.5));
		
		Displacement displacement = displacementBuilder.build();
		displacement.setDisplacementX(filterFactory.literal(5));
		displacement.setDisplacementY(filterFactory.literal(5));
		Literal rotationExp = filterFactory.literal(0);
		pointPlacement.setRotation(rotationExp);
		pointPlacement.setAnchorPoint(anchorPoint);
		pointPlacement.setDisplacement(displacement);	
		
		List<Expression> family = new ArrayList<Expression>();
		family.add(filterFactory.literal("Liberation Mono"));
		Font font; 
		text.setLabel(proText);
		if(typeOfLabel == "Address")
		{
			font = styleFactory.font(family,filterFactory.literal("normal"),filterFactory.literal("Bold"),filterFactory.literal(20));
			text.setPriority(proText);
			gr.setSize(filterFactory.literal(10));
		}
		else
		{
			font = styleFactory.font(family,filterFactory.literal("normal"),filterFactory.literal("Bold"),filterFactory.literal(12));
			gr.setSize(filterFactory.literal(5));
		}
			
		
		text.setLabelPlacement(pointPlacement);
		text.setFont(font);		
		text.addToOptions(text.SPACE_AROUND_KEY, "-1");
		//Log.d(GeoServerRequestBuilder.class.toString() + "- spaceAround" , text.SPACE_AROUND_KEY + " " + text.DEFAULT_SPACE_AROUND);
		
				
		

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(text);
		rule.symbolizers().add(sym);
		FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
		
		PropertyName propertyName = ff.property( "Type" );
		Literal literal = ff.literal("POIPoint");
		PropertyIsEqualTo filter = ff.equals( propertyName, literal );				
		rule.setFilter(filter);
		
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
		
		return fts;

	}
	
	private static FeatureTypeStyle createPOSPointStyle()
	{		
		
		Graphic gr = styleFactory.createDefaultGraphic();

		Mark mark = styleFactory.getCircleMark();

		mark.setStroke(styleFactory.createStroke(filterFactory.literal(Color.BLUE), filterFactory.literal(1)));

		mark.setFill(styleFactory.createFill(filterFactory.literal(Color.CYAN)));

		gr.graphicalSymbols().clear();
		gr.graphicalSymbols().add(mark);
		gr.setSize(filterFactory.literal(5));

		/*
		 * Setting the geometryPropertyName arg to null signals that we want to
		 * draw the default geomettry of features
		 */
		PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);
		

		
		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(sym);


		FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
		
		PropertyName propertyName = ff.property( "Type" );
		Literal literal = ff.literal("POSPoint");
		PropertyIsEqualTo filter = ff.equals( propertyName, literal );				
		rule.setFilter(filter);
		
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
		
		return fts;
	}	
		
	private static FeatureTypeStyle createPOSMultiPolygonStyle()
	{
		Stroke stroke = styleFactory.createStroke(
				filterFactory.literal(Color.BLUE), filterFactory.literal(1),
				filterFactory.literal(0.5));

		// create a partial opaque fill
		Fill fill = styleFactory.createFill(filterFactory.literal(Color.CYAN),
				filterFactory.literal(0.5));

		/*
		 * Setting the geometryPropertyName arg to null signals that we want to
		 * draw the default geometry of features
		 */
		PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke,
				fill, null);		
		
		

		Rule rule = styleFactory.createRule();
		
		
		rule.symbolizers().add(sym);
		
		FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
		PropertyName propertyName = ff.property( "Type" );
		Literal literal = ff.literal("POSMultiPolygon");
		PropertyIsEqualTo filter = ff.equals( propertyName, literal );		
				
		rule.setFilter(filter);
		
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });	
		
		return fts;
	}
	
	private static FeatureTypeStyle createPOSPolygonStyle()
	{
		Stroke stroke = styleFactory.createStroke(
				filterFactory.literal(Color.BLUE), filterFactory.literal(1),
				filterFactory.literal(0.5));

		// create a partial opaque fill
		Fill fill = styleFactory.createFill(filterFactory.literal(Color.CYAN),
				filterFactory.literal(0.5));
		

		/*
		 * Setting the geometryPropertyName arg to null signals that we want to
		 * draw the default geometry of features
		 */
		PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke,
				fill, null);

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(sym);
		
		FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
		PropertyName propertyName = ff.property( "Type" );
		Literal literal = ff.literal("POSPolygon");
		PropertyIsEqualTo filter = ff.equals( propertyName, literal );		
				
		rule.setFilter(filter);
		
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });	
		
		return fts;
	}
	

	
	private static FeatureTypeStyle createPOSCircleStyle()
	{
		Stroke stroke = styleFactory.createStroke(
				filterFactory.literal(Color.BLUE), filterFactory.literal(1),
				filterFactory.literal(0.5));

		// create a partial opaque fill
		Fill fill = styleFactory.createFill(filterFactory.literal(Color.CYAN),
				filterFactory.literal(0.5));

		/*
		 * Setting the geometryPropertyName arg to null signals that we want to
		 * draw the default geometry of features
		 */
		PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke,
				fill, null);

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(sym);
		
		FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
		PropertyName propertyName = ff.property( "Type" );
		Literal literal = ff.literal("POSCirclePolygon");
		PropertyIsEqualTo filter = ff.equals( propertyName, literal );		
				
		rule.setFilter(filter);
		
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });	
		
		return fts;

	}
	
	private static FeatureTypeStyle createRGLineStringStyle()
	{
		Stroke stroke = styleFactory.createStroke(
				filterFactory.literal(Color.BLUE), filterFactory.literal(1));

		/*
		 * Setting the geometryPropertyName arg to null signals that we want to
		 * draw the default geomettry of features
		 */
		LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(sym);
		FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
		PropertyName propertyName = ff.property( "Type" );
		Literal literal = ff.literal("RGLineString");
		PropertyIsEqualTo filter = ff.equals( propertyName, literal );		
				
		rule.setFilter(filter);
		
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
		return fts;
	}
	
	private static String createDefaultFeatureCollection() throws SchemaException, IOException
	{
		//Using Point for DefaultFeature
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		SimpleFeatureCollection collection = FeatureCollections.newCollection();
		GML encode = new GML(Version.WFS1_0);						
		encode.setNamespace("wms", "http://www.opengis.net/wms");
		
		SimpleFeatureType POINTTYPE = DataUtilities.createType("Position","pointProperties:Point,Type:\"\"");		
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(POINTTYPE);
	

		Point POSPoint = geometryFactory.createPoint(new Coordinate(0,0));
		
		featureBuilder.add(POSPoint);
		featureBuilder.add("POSPoint");		
		
		SimpleFeature feature = featureBuilder.buildFeature(null);		
		collection.add(feature);
		
		encode.encode(bout, collection);
		
		
		
		String xml = bout.toString().replaceAll("\n", "").replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "<InlineFeature>").concat("</InlineFeature>");
		
		//Remove <gml:boundedBy>
		String startPattern = "<gml:boundedBy>";
		String endPattern = "</gml:boundedBy>";
		while(xml.indexOf("<gml:boundedBy>") != -1)
		{
			xml = xml.substring(0, xml.indexOf(startPattern)) + xml.substring(xml.indexOf(endPattern) + endPattern.length());  
		}
		Log.d("GeoServerRequestBuilder - XML FeatureCollection", xml);
		
	return xml;
	}
	
	private static UserLayer createDefaultUserLayer()
	{
		//Using size 0 for Point
		Graphic gr = styleFactory.createDefaultGraphic();		

		gr.graphicalSymbols().clear();		
		gr.setSize(filterFactory.literal(0.000001));

		/*
		 * Setting the geometryPropertyName arg to null signals that we want to
		 * draw the default geomettry of features
		 */
		PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);
		

		
		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(sym);


		FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
		
		PropertyName propertyName = ff.property( "Type" );
		Literal literal = ff.literal("POSPoint");
		PropertyIsEqualTo filter = ff.equals( propertyName, literal );				
		rule.setFilter(filter);
		
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });

		UserLayer userlayer = styleFactory.createUserLayer();
		userlayer.setName("defaultLayer");
		Style style = styleFactory.createStyle();
		style.addFeatureTypeStyle(fts);
		userlayer.addUserStyle(style);
		
		
		return userlayer;
	}
	
		
	private static String convertOverlayType2FeatureCollections(OverlayType overlayType) throws IOException, SchemaException, PresentationException {
		//TODO Add later
		
		
		
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			SimpleFeatureCollection collection = FeatureCollections.newCollection();
			GML encode = new GML(Version.WFS1_0);						
			encode.setNamespace("wms", "http://www.opengis.net/wms");
			
			//StyleType styleType = overlayType.getStyle();			
			MapType mapType = overlayType.getMap();
			PointOfInterestType poi = overlayType.getPOI();
			RouteGeometryType routeGeometry = overlayType.getRouteGeometry();
			//BigInteger zorder = overlayType.getZorder();
			PositionType position = overlayType.getPosition();
			
			if (position!=null){
				PointType point = position.getPoint();
				PolygonType polygon = position.getPolygon();
				MultiPolygonType multiPolygon = position.getMultiPolygon();
				//Not use
				EllipseType ellipse = position.getEllipse();
				CircularArcType circularArc = position.getCircularArc();
				CircleByCenterPointType circleByCenterPoint = position.getCircleByCenterPoint();
				
				if (point !=null){
					
					if(point.getSrsName() == null || point.getSrsName().indexOf("4326") != -1)
					{
					
					SimpleFeatureType POINTTYPE = DataUtilities.createType("Position","pointProperties:Point,Type:\"\",Address:string");		
					SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(POINTTYPE);
				

					Point POSPoint = geometryFactory.createPoint(new Coordinate(Double.valueOf(point.getPos().getValue().get(0).toString()), Double.valueOf( point.getPos().getValue().get(1).toString())));
					
					featureBuilder.add(POSPoint);
					featureBuilder.add("POSPoint");					
					
					SimpleFeature feature = featureBuilder.buildFeature(null);		
					collection.add(feature);					
					
					}
					else
						throw new PresentationException("have_not_supported_yet");
					
				}
				if (polygon != null){
					if(polygon.getSrsName() == null || polygon.getSrsName().indexOf("4326") != -1)
					{
					SimpleFeatureType POLYGONTYPE = DataUtilities.createType("Position", "polygonProperties:Polygon,Type:\"\"");
					SimpleFeatureBuilder polygonFeatureBuilder = new SimpleFeatureBuilder(POLYGONTYPE);
					
//					FileOutputStream out = new FileOutputStream("POSPolygon.xsd"); 
//					GML localEncode = new GML(Version.GML2);
//					localEncode.setBaseURL(new URL("http://localhost/"));
//					localEncode.encode(out, POLYGONTYPE);
							
					//Exterior
					AbstractRingPropertyType abstractRingPropertyTypeExterior = polygon.getExterior().getValue();				
					
					LinearRingType linearRingTypeExterior = (LinearRingType) abstractRingPropertyTypeExterior.getRing().getValue();
					
					int numOfExteriorPos = linearRingTypeExterior.getPosOrPointPropertyOrPointRep().size();
					//Coordinate[] exteriorCoord = new Coordinate[linearRingTypeExterior.getPosList().getValue().size()];						
					Coordinate[] exteriorCoord = new Coordinate[numOfExteriorPos + 1];					
					
					for (int i=0;i< numOfExteriorPos;i++){
						//exteriorCoord[i] = new Coordinate(Double.valueOf(linearRingTypeExterior.getPosList().getValue().get(0).toString()), Double.valueOf(linearRingTypeExterior.getPosList().getValue().get(1).toString()));
						DirectPositionType exteriorPos = (DirectPositionType) linearRingTypeExterior.getPosOrPointPropertyOrPointRep().get(i).getValue();
						exteriorCoord[i] = new Coordinate(exteriorPos.getValue().get(0), exteriorPos.getValue().get(1));
					}
					
					//Start pos and end pos must be the same
					DirectPositionType exteriorPos = (DirectPositionType) linearRingTypeExterior.getPosOrPointPropertyOrPointRep().get(0).getValue();
					exteriorCoord[numOfExteriorPos] = new Coordinate(exteriorPos.getValue().get(0), exteriorPos.getValue().get(1));
					
					//Set shell
					LinearRing shell = geometryFactory.createLinearRing(exteriorCoord);
					
					//Interior
					LinearRing[] holes = new LinearRing[polygon.getInterior().size()];	
					
					for (int i=0;i<polygon.getInterior().size();i++){
						AbstractRingPropertyType abstractRingPropertyTypeInterior = polygon.getInterior().get(i).getValue();						
						LinearRingType linearRingTypeInterior = (LinearRingType) abstractRingPropertyTypeInterior.getRing().getValue();
						
						int numOfInteriorPos = linearRingTypeInterior.getPosOrPointPropertyOrPointRep().size();
						Coordinate[] interiorCoord = new Coordinate[numOfExteriorPos + 1];
						
						for (int j=0;j<numOfInteriorPos;j++){
							DirectPositionType interiorPos = (DirectPositionType) linearRingTypeInterior.getPosOrPointPropertyOrPointRep().get(j).getValue(); 
							interiorCoord[j] = new Coordinate(interiorPos.getValue().get(0), interiorPos.getValue().get(1));
						}
						//Start pos and end pos must be the same
						DirectPositionType interiorPos = (DirectPositionType) linearRingTypeInterior.getPosOrPointPropertyOrPointRep().get(0).getValue(); 
						interiorCoord[numOfInteriorPos] = new Coordinate(interiorPos.getValue().get(0), interiorPos.getValue().get(1));
						
						//Set holes
						holes[i] = geometryFactory.createLinearRing(interiorCoord);
					}			
					
					
					Polygon POSPolygon = geometryFactory.createPolygon(shell, holes);					
										
					polygonFeatureBuilder.add(POSPolygon);
					polygonFeatureBuilder.add("POSPolygon");
					SimpleFeature polygonFeature = polygonFeatureBuilder.buildFeature(null);
					collection.add(polygonFeature);
					
					//encode.setNamespace("Position", "http://localhost/POSPolygon.xsd");
					//encode.setLegacy(true);
					
					}
					else
						throw new PresentationException("have_not_supported_yet");
					
					
				} else if (multiPolygon != null){
					
					if(multiPolygon.getSrsName() == null || multiPolygon.getSrsName().indexOf("4326") != -1)
					{
					
					SimpleFeatureType MULTIPOLYGONTYPE = DataUtilities.createType("Multipolygon", "multipolygonProperties:MultiPolygon,Type:\"\",id:0");
					SimpleFeatureBuilder multiPolygonFeatureBuilder = new SimpleFeatureBuilder(MULTIPOLYGONTYPE);
					
//					FileOutputStream out = new FileOutputStream("POSMultiPolygon.xsd"); 
//					GML localEncode = new GML(Version.GML2);
//					localEncode.setBaseURL(new URL("http://localhost/"));
//					localEncode.encode(out, MULTIPOLYGONTYPE);
					
					int numOfPolygons = multiPolygon.getPolygonMember().size();
					Polygon[] POSPolygons = new Polygon[numOfPolygons]; 
					
					for(int index = 0;index<numOfPolygons;index++)
					{
						
						PolygonType polygonMember = multiPolygon.getPolygonMember().get(index).getPolygon();
						
						//Exterior
						AbstractRingPropertyType abstractRingPropertyTypeExterior = polygonMember.getExterior().getValue();				
						
						LinearRingType linearRingTypeExterior = (LinearRingType) abstractRingPropertyTypeExterior.getRing().getValue();
						
						int numOfExteriorPos = linearRingTypeExterior.getPosOrPointPropertyOrPointRep().size();
						//Coordinate[] exteriorCoord = new Coordinate[linearRingTypeExterior.getPosList().getValue().size()];						
						Coordinate[] exteriorCoord = new Coordinate[numOfExteriorPos + 1];					
						
						for (int i=0;i< numOfExteriorPos;i++){
							//exteriorCoord[i] = new Coordinate(Double.valueOf(linearRingTypeExterior.getPosList().getValue().get(0).toString()), Double.valueOf(linearRingTypeExterior.getPosList().getValue().get(1).toString()));
							DirectPositionType exteriorPos = (DirectPositionType) linearRingTypeExterior.getPosOrPointPropertyOrPointRep().get(i).getValue();
							exteriorCoord[i] = new Coordinate(exteriorPos.getValue().get(0), exteriorPos.getValue().get(1));
						}
						
						//Start pos and end pos must be the same
						DirectPositionType exteriorPos = (DirectPositionType) linearRingTypeExterior.getPosOrPointPropertyOrPointRep().get(0).getValue();
						exteriorCoord[numOfExteriorPos] = new Coordinate(exteriorPos.getValue().get(0), exteriorPos.getValue().get(1));
						
						//Set shell
						LinearRing shell = geometryFactory.createLinearRing(exteriorCoord);
						
						//Interior
						LinearRing[] holes = new LinearRing[polygonMember.getInterior().size()];	
						
						for (int i=0;i<polygonMember.getInterior().size();i++){
							AbstractRingPropertyType abstractRingPropertyTypeInterior = polygonMember.getInterior().get(i).getValue();						
							LinearRingType linearRingTypeInterior = (LinearRingType) abstractRingPropertyTypeInterior.getRing().getValue();
							
							int numOfInteriorPos = linearRingTypeInterior.getPosOrPointPropertyOrPointRep().size();
							Coordinate[] interiorCoord = new Coordinate[numOfExteriorPos + 1];
							
							for (int j=0;j<numOfInteriorPos;j++){
								DirectPositionType interiorPos = (DirectPositionType) linearRingTypeInterior.getPosOrPointPropertyOrPointRep().get(j).getValue(); 
								interiorCoord[j] = new Coordinate(interiorPos.getValue().get(0), interiorPos.getValue().get(1));
							}
							//Start pos and end pos must be the same
							DirectPositionType interiorPos = (DirectPositionType) linearRingTypeInterior.getPosOrPointPropertyOrPointRep().get(0).getValue(); 
							interiorCoord[numOfInteriorPos] = new Coordinate(interiorPos.getValue().get(0), interiorPos.getValue().get(1));
							
							//Set holes
							holes[i] = geometryFactory.createLinearRing(interiorCoord);
						}

						POSPolygons[index] = geometryFactory.createPolygon(shell, holes);
					}
							
										
					
					MultiPolygon POSMultiPolygon = new MultiPolygon(POSPolygons, geometryFactory);
					multiPolygonFeatureBuilder.add(POSMultiPolygon);
					multiPolygonFeatureBuilder.add("POSMultiPolygon");
					SimpleFeature multiPolygonFeature = multiPolygonFeatureBuilder.buildFeature(null);
					collection.add(multiPolygonFeature);
					
				}
				else
					throw new PresentationException("have_not_supported_yet");
					
				} else if (circleByCenterPoint != null){
					if(circleByCenterPoint.getNumArc().toString().equals("1")&& (circleByCenterPoint.getRadius().getUom().equalsIgnoreCase("M") || circleByCenterPoint.getRadius().getUom() == null))
					{
						if(circleByCenterPoint.getPos().getSrsName()== null || circleByCenterPoint.getPos().getSrsName().indexOf("4326") != -1)						
						{
						Log.d("GeoServerRequestBuilder - CircleByCenterPoint Content", circleByCenterPoint.toString());
						SimpleFeatureType CIRCLEPOLYGONTYPE = DataUtilities.createType("Position", "circlepolygonProperties:Polygon,Type:\"\"");
						SimpleFeatureBuilder polygonFeatureBuilder = new SimpleFeatureBuilder(CIRCLEPOLYGONTYPE);
						
//						FileOutputStream out = new FileOutputStream("POSCirclePolygon.xsd"); 
//						GML localEncode = new GML(Version.GML2);
//						localEncode.setBaseURL(new URL("http://localhost/"));
//						localEncode.encode(out, CIRCLEPOLYGONTYPE);
												
						//GeolocHelper
						//Convert to default Coordinated Reference System
						//GeolocHelper.transform(verlayType.get, srsTo, fromCoors, toCoors)
						Polygon POSCirclePolygon = GeolocHelper.createCirclePolygonBox(circleByCenterPoint.getPos().getValue().get(0), circleByCenterPoint.getPos().getValue().get(1), circleByCenterPoint.getRadius().getValue(), 20);
													
						
						polygonFeatureBuilder.add(POSCirclePolygon);
						polygonFeatureBuilder.add("POSCirclePolygon");
						SimpleFeature polygonFeature = polygonFeatureBuilder.buildFeature(null);
						collection.add(polygonFeature);
						}
						else
							throw new PresentationException("have_not_supported_yet");
					} else						
						throw new PresentationException("have_not_supported_yet");
						
				}
				else if (circularArc != null){
					
					throw new PresentationException("have_not_supported_yet");
					
				}
			} //end Position
			else if (poi!=null){
				if(poi.getPoint().getSrsName() == null || poi.getPoint().getSrsName().indexOf("4326") != -1)
				{
				
				SimpleFeatureType POIPOINTTYPE = DataUtilities.createType("POI","pointProperties:Point,Type:\"\",POIName:string,Address:string");		
				SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(POIPOINTTYPE);
			
				Log.d("GeoServerRequestBuilder - a POI", poi.toString());
				
				Point POIPoint = geometryFactory.createPoint(new Coordinate(Double.valueOf(poi.getPoint().getPos().getValue().get(0).toString()), Double.valueOf( poi.getPoint().getPos().getValue().get(1).toString())));
							
				
				featureBuilder.add(POIPoint);
				featureBuilder.add("POIPoint");
				
				if(poi.getPOIName() != null)
					featureBuilder.add(poi.getPOIName());
				else if(poi.getAddress().getFreeFormAddress() != null)
				{
					featureBuilder.add(null);
					featureBuilder.add(poi.getAddress().getFreeFormAddress());
				}
					
				
				SimpleFeature feature = featureBuilder.buildFeature(null);		
				collection.add(feature);					

				
				//featureBuilder.add("26/37 Do Quang Dau, Quan 1, TP HCM");
				}
			
			else
				throw new PresentationException("have_not_supported_yet");
			}
			else if(routeGeometry != null){
				if(routeGeometry.getLineString().getSrsName() == null || routeGeometry.getLineString().getSrsName().indexOf("4326")!= -1)
				{
				
				SimpleFeatureType LINETYPE = DataUtilities.createType("RouteGeometry","lineStringProperties:LineString,Type:\"\"");
				SimpleFeatureBuilder lineStringFeatureBuilder = new SimpleFeatureBuilder(LINETYPE);
				
//				FileOutputStream out = new FileOutputStream("RGLineString.xsd"); 
//				GML localEncode = new GML(Version.GML2);
//				localEncode.setBaseURL(new URL("http://localhost/"));
//				localEncode.encode(out, LINETYPE);
				
				LineStringType lineString = routeGeometry.getLineString();
				Log.d("GeoServerRequestBuilder - LineString Content", lineString.toString());
				
				int numOfLineStringPos = lineString.getPosOrPointPropertyOrPointRep().size();
				Coordinate[] lineStringCoord = new Coordinate[numOfLineStringPos];					
				
				for (int i=0;i< numOfLineStringPos;i++){
					//exteriorCoord[i] = new Coordinate(Double.valueOf(linearRingTypeExterior.getPosList().getValue().get(0).toString()), Double.valueOf(linearRingTypeExterior.getPosList().getValue().get(1).toString()));
					DirectPositionType lineStringPos = (DirectPositionType) lineString.getPosOrPointPropertyOrPointRep().get(i).getValue();
					lineStringCoord[i] = new Coordinate(lineStringPos.getValue().get(0), lineStringPos.getValue().get(1));
				}				
				LineString RGLineString = geometryFactory.createLineString(lineStringCoord);
				
				lineStringFeatureBuilder.add(RGLineString);
				lineStringFeatureBuilder.add("RGLineString");
				SimpleFeature lineStringFeature = lineStringFeatureBuilder.buildFeature(null);
				collection.add(lineStringFeature);			
				}
				else
					throw new PresentationException("have_not_supported_yet");
			}
			else if(mapType != null){
				throw new PresentationException("have_not_supported_yet");
			}
			
			
			encode.encode(bout, collection);
			
			
			
			String xml = bout.toString().replaceAll("\n", "").replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "<InlineFeature>").concat("</InlineFeature>");
			
			//Remove <gml:boundedBy>
			String startPattern = "<gml:boundedBy>";
			String endPattern = "</gml:boundedBy>";
			while(xml.indexOf("<gml:boundedBy>") != -1)
			{
				xml = xml.substring(0, xml.indexOf(startPattern)) + xml.substring(xml.indexOf(endPattern) + endPattern.length());  
			}
			Log.d("GeoServerRequestBuilder - XML FeatureCollection", xml);
			
		return xml;
	}
	

	private static UserLayer convertOverlayType2UserStyle(OverlayType overlayType) throws IOException, SchemaException, PresentationException {
		//TODO Add later	
			
		UserLayer userlayer = styleFactory.createUserLayer();
		userlayer.setName("Inline");
		Style style = styleFactory.createStyle();
		userlayer.addUserStyle(style);
		
		
		
		
			//StyleType styleType = overlayType.getStyle();			
			MapType mapType = overlayType.getMap();
			PointOfInterestType poi = overlayType.getPOI();
			RouteGeometryType routeGeometry = overlayType.getRouteGeometry();
			//BigInteger zorder = overlayType.getZorder();
			PositionType position = overlayType.getPosition();
			
			if (position!=null){
				PointType point = position.getPoint();
				PolygonType polygon = position.getPolygon();
				MultiPolygonType multiPolygon = position.getMultiPolygon();
				//Not use
				EllipseType ellipse = position.getEllipse();
				CircularArcType circularArc = position.getCircularArc();
				CircleByCenterPointType circleByCenterPoint = position.getCircleByCenterPoint();
				
				if (point !=null){					
					
					style.addFeatureTypeStyle(createPOSPointStyle());
				}
				if (polygon != null ){
					
					style.addFeatureTypeStyle(createPOSPolygonStyle());					
					
				} else if(multiPolygon != null){
					style.addFeatureTypeStyle(createPOSMultiPolygonStyle());
				} else if (circleByCenterPoint != null){
					if(circleByCenterPoint.getNumArc().toString().equals("1")&& (circleByCenterPoint.getRadius().getUom().equalsIgnoreCase("M") || circleByCenterPoint.getRadius().getUom() == null))
					{
						style.addFeatureTypeStyle(createPOSCircleStyle());
						
					} else						
						throw new PresentationException("have_not_supported_yet");
						
				}
				else if (circularArc != null){
					
					throw new PresentationException("have_not_supported_yet");
					
				}
			} //end Position
			else if (poi!=null){
				if(poi.getPOIName() != null)
					style.addFeatureTypeStyle(createPOIPointStyle("POIName"));
				else if(poi.getAddress().getFreeFormAddress() != null)
					style.addFeatureTypeStyle(createPOIPointStyle("Address"));
			}
			else if(routeGeometry != null){
				style.addFeatureTypeStyle(createRGLineStringStyle());
				
			}
			else if(mapType != null){
				throw new PresentationException("have_not_supported_yet");
			}
			
			
		
		return userlayer;
	}
	


	
	private static void convertPos2Coord(PointType point){
		if (point.getPos().getValue().size()>0){
			CoordType coord = new CoordType();
			coord.setX(new BigDecimal(point.getPos().getValue().get(0)));
			coord.setY(new BigDecimal(point.getPos().getValue().get(1)));
			point.setPos(null);
			point.setCoord(coord);
		}
	}
	
	private static void convertListPos2ListCoord(JAXBElement<AbstractRingPropertyType> jaxbElements){
		AbstractRingPropertyType abstractRingPropertyType = jaxbElements.getValue();
		LinearRingType linearRingType = (LinearRingType) abstractRingPropertyType.getRing().getValue();
		
		for (int k=0;k<linearRingType.getPosOrPointPropertyOrPointRep().size();k++){
			JAXBElement<DirectPositionType> jaxbPos = (JAXBElement<DirectPositionType>) linearRingType.getPosOrPointPropertyOrPointRep().get(k);
			DirectPositionType pos = jaxbPos.getValue();
			
			CoordType coord = new CoordType();
			coord.setX(new BigDecimal(pos.getValue().get(0)));
			coord.setY(new BigDecimal(pos.getValue().get(1)));
			linearRingType.getCoord().add(coord);
		}
		//Clear
		linearRingType.getPosOrPointPropertyOrPointRep().clear();
//		linearRingType.setPosOrPointPropertyOrPointRep(null);
//		linearRingType.setPosOrPointPropertyOrPointRep(new ArrayList<JAXBElement<?>>());
	}

	private static net.opengis.sld.v_1_1_0.OutputType addOutputTypeSLD(OutputType output) {
		net.opengis.sld.v_1_1_0.OutputType outputTypeSLD = new net.opengis.sld.v_1_1_0.OutputType();
		outputTypeSLD.setTransparent(true);
		
		Size size = new Size();
		size.setWidth(output.getWidth());
		size.setHeight(output.getHeight());
		outputTypeSLD.setSize(size);	
		
		
//		if(StaticFunc.isNOEString(output.getFormat()))
			outputTypeSLD.setFormat(Config.DEFAULT_IMG_FORMAT);
//		else if(output.getFormat().indexOf("image") != -1)
//			outputTypeSLD.setFormat(output.getFormat());
//		else
//			outputTypeSLD.setFormat("image/" + output.getFormat());
		
		outputTypeSLD.setBGcolor(Config.DEFAULT_BGCOLOR);
		return outputTypeSLD;
	}

	private static BoundingBoxType1 addBoundingBox(OutputType output)
			throws PresentationException, GeoException {
		EnvelopeType envelopeType = output.getBBoxContext();
		CenterContextType centerContextType = output.getCenterContext();
		
		BoundingBoxType1 boundingBoxType1 = new BoundingBoxType1();

		if (envelopeType != null) {
			String srs = envelopeType.getSrsName();
			if(srs == null || srs.indexOf("4326") != -1)
			{
			
			Log.d("GeoServerRequestBuilder - SRS of EnvelopType", srs);

			// BBox
			Assert.assertTrue("BBContext must have at least 2 Pos", envelopeType.getPos()!= null && envelopeType.getPos().size()>1);
			
			DirectPositionType pos1 = envelopeType.getPos().get(0);
			DirectPositionType pos2 = envelopeType.getPos().get(1);
			
			Assert.assertTrue("Pos1 must have at least 2 values", pos1.getValue().size()>1);
			Assert.assertTrue("Pos2 must have at least 2 values", pos2.getValue().size()>1);
			
			CoordType coordType1 = new CoordType();
			boundingBoxType1.setCoord1(coordType1);
			coordType1.setX(new BigDecimal(pos1.getValue().get(0)));
			coordType1.setY(new BigDecimal(pos1.getValue().get(1)));
			if (pos1.getValue().size()>2)
				coordType1.setY(new BigDecimal(pos1.getValue().get(2)));
			
			CoordType coordType2 = new CoordType();
			boundingBoxType1.setCoord2(coordType2);
			coordType2.setX(new BigDecimal(pos2.getValue().get(0)));
			coordType2.setY(new BigDecimal(pos2.getValue().get(1)));
			if (pos2.getValue().size()>2)
				coordType2.setY(new BigDecimal(pos2.getValue().get(2)));
			}
			else
			{
				throw new PresentationException("have_not_yet_supported");
			}

		} else if (centerContextType != null) {
			String srs = centerContextType.getSRS();
			if (StaticFunc.isNOE(srs))
				srs = centerContextType.getCenterPoint().getSrsName();
			if (StaticFunc.isNOE(srs))
				srs = centerContextType.getCenterPoint().getPos()
						.getSrsName();

			// Transform
			double[] toCoors = new double[2];
			DirectPositionType pos = centerContextType.getCenterPoint()
					.getPos();
			if (!StaticFunc.isNOE(srs)
					&& !srs.equals(Config.DEFAULT_SRD)) {
				double[] fromCoors = new double[2];
				if (pos.getValue().size() != 2)
					throw new PresentationException(I18N.m
							.getString("invalid_request"));
				fromCoors[0] = pos.getValue().get(0);
				fromCoors[1] = pos.getValue().get(1);
				try {
					
					//Log.d("GeoServerRequestBuilder - srs", srs);
					//srs = "#26911";
					GeolocHelper.transform(srs, Config.DEFAULT_SRD,fromCoors, toCoors);
				} catch (Exception e) {
					//e.printStackTrace();
					throw new PresentationException("cannot_transform_srs");
				}
			} else {
				toCoors[0] = pos.getValue().get(0);
				toCoors[1] = pos.getValue().get(1);
			}

			BigInteger displayScale = centerContextType.getDisplayScale();
			BigInteger dpi = centerContextType.getDPI();
			RadiusType radiusType = centerContextType.getRadius();

			if (displayScale != null && displayScale.intValue() != 0) {
				double meters = GeolocHelper
						.convertPixel2Meter(displayScale.intValue());
				Envelope envelope = GeolocHelper.getBounding(toCoors[0],
						toCoors[1], meters);

				CoordType coordType1 = new CoordType();
				boundingBoxType1.setCoord1(coordType1);
				coordType1.setX(new BigDecimal(envelope.getMinX()));
				coordType1.setY(new BigDecimal(envelope.getMinY()));
				
				CoordType coordType2 = new CoordType();
				boundingBoxType1.setCoord2(coordType2);
				coordType2.setX(new BigDecimal(envelope.getMaxX()));
				coordType2.setY(new BigDecimal(envelope.getMaxY()));
			} else if (dpi != null && dpi.intValue() != 0) {
				// TODO
				throw new GeoException(I18N.m
						.getString("have_not_supported_yet"));
			} else if (radiusType != null
					&& radiusType.getValue().intValue() != 0) {
				double radius = GeolocHelper.getLengthInMeter(
						centerContextType.getRadius().getUnit(), radiusType
								.getValue().intValue());
				Envelope envelope = GeolocHelper.getBounding(toCoors[0],
						toCoors[1], radius);

				CoordType coordType1 = new CoordType();
				boundingBoxType1.setCoord1(coordType1);
				coordType1.setX(new BigDecimal(envelope.getMinX()));
				coordType1.setY(new BigDecimal(envelope.getMinY()));
				
				CoordType coordType2 = new CoordType();
				boundingBoxType1.setCoord2(coordType2);
				coordType2.setX(new BigDecimal(envelope.getMaxX()));
				coordType2.setY(new BigDecimal(envelope.getMaxY()));
			} else
				throw new PresentationException(I18N.m
						.getString("not_supplied_radius"));
			
		}
		return boundingBoxType1;
	}

}
