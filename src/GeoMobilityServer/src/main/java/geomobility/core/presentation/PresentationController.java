package geomobility.core.presentation;

import geomobility.core.GlobalCore;
import geomobility.core.exception.GeoException;
import geomobility.core.exception.PresentationException;
import geomobility.core.net.HttpData;
import geomobility.core.net.HttpRequest;
import geomobility.core.utils.Config;
import geomobility.core.utils.Log;
import geomobility.core.utils.StaticFunc;
import geomobility.localization.I18N;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import net.opengis.sld.v_1_1_0.GetMapType;
import net.opengis.xls.v_1_1_0.MapType;
import net.opengis.xls.v_1_1_0.OutputType;
import net.opengis.xls.v_1_1_0.PortrayMapRequestType;
import net.opengis.xls.v_1_1_0.PortrayMapResponseType;
import net.opengis.xls.v_1_1_0.RequestHeaderType;
import net.opengis.xls.v_1_1_0.XLSType;

import org.geotools.feature.SchemaException;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class PresentationController {
	private static final String TAG = PresentationController.class
			.getSimpleName();

	public static String doController(String xml) throws GeoException,
			TransformerException, IOException, SchemaException {
		XLSType xlsType = StaticFunc.getXLSType(xml);

		RequestHeaderType requestHeader = StaticFunc
				.parseRequestHeader(xlsType);
		Log.d("PresentationControler - RequestHeader", requestHeader.toString());

		if (requestHeader != null
				&& (requestHeader.getSrsName() == null || requestHeader
						.getSrsName().indexOf("4326") != -1)) {

			List<PortrayMapRequestType> listPortrayMapReqType = StaticFunc
					.parse(xlsType);

			xlsType.getBody().clear();

			for (PortrayMapRequestType portrayMap : listPortrayMapReqType) {
				List<GetMapType> listGetMapTypes = GeoServerRequestBuilder
						.buildRequestGetMap(portrayMap);

				List<String> listSLDString = GeoServerRequestBuilder
						.bulidRequestSLDString(portrayMap);

				// Testing
				for (int i = 0; i < listGetMapTypes.size(); i++) {
					GetMapType getMapType = listGetMapTypes.get(i);
					String getMapXML;

					GlobalCore.MyOutStream myOutStream = new GlobalCore.MyOutStream();
					myOutStream.clear();

					JAXBElement<GetMapType> jaxbElement = GlobalCore.objectFactorySLD
							.createGetMap(getMapType);
					try {
						GlobalCore.marshallerWMS.marshal(jaxbElement,
								myOutStream);
						Log.d("PresentationController - GetMapXML OutputType",
								myOutStream.toString());

						getMapXML = myOutStream.toString()
								.replaceAll("ns5", "ows")
								.replaceAll("sld", "ows")
								.replaceAll("ns1", "gml")
								.replaceAll("ns2", "xlink")
								.replaceAll("ns4", "se")
								.replaceAll("ns6", "ogc");
						if (getMapXML
								.indexOf("xmlns:ns8=\"http://www.opengis.net/wms\"") != -1) {
							Log.d(TAG, "ns8 = true");
							getMapXML = getMapXML.replaceAll(":?ns8:?", "");
						} else if (getMapXML
								.indexOf("xmlns:ns7=\"http://www.opengis.net/wms\"") != -1) {
							Log.d(TAG, "ns7 = true");
							getMapXML = getMapXML.replaceAll(":?ns7:?", "");
						}

						// Merge SLD XML to GetMap XML
						String pattern = "<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>";
						int insertingIndex = getMapXML.indexOf(">",
								pattern.length() + 1) + 1;
						getMapXML = getMapXML.substring(0, insertingIndex)
								+ listSLDString.get(i)
								+ getMapXML.substring(insertingIndex);
						Log.d("PresentationController - GetMapXML", getMapXML);

						// Send request to GeoServer and Get the imageResponse
						try {

							HttpData gotData = HttpRequest.postXML(
									Config.GEOSERVER_PATH, getMapXML);
							if (gotData.headers.get("Status").toString()
									.indexOf("OK") != -1) {

								FileWriter outStream = new FileWriter(
										"imageResponse.svg");
								BufferedWriter writer = new BufferedWriter(
										outStream);
								writer.write(gotData.content.toString());

								writer.close();
								outStream.close();

							}

							Log.d(PresentationController.class.toString()
									+ " - imageSVG", gotData.content);
						} catch (ConnectException e) {
							throw new GeoException(
									I18N.m.getString("cannot_connect_geoserver"));
						}

					} catch (JAXBException e) {
						e.printStackTrace();
						throw new GeoException(
								I18N.m.getString("cannot_build_result"));
					}

				}
			}
		} else
			throw new PresentationException("have_not_supportted_yet");

		// Output to String
		OutputStream outputStream = StaticFunc.buildXLSResult(xlsType,
				GlobalCore.output);
		return outputStream.toString();
	}

	public static boolean isValidImageLink(String result) {
		if (result.startsWith("<?xml")) {
			Log.e(TAG, result);
			return false;
		}

		return true;
	}

	// TODO Testing only
	public static void main(String[] args) throws IOException, SchemaException {
		try {
			// String result = doController(StaticFunc
			// .getContent("data/presentation/bounding_box_envelope_req.txt"));
			String result;
			try {
				result = doController(StaticFunc
						.getContent("data/presentation/"
								+ "current_location_and_pois.txt"));

			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// bounding_box_envelope_req.txt
			// center_point_scale_req.txt
			// hollow_polygon_req.txt
			// styles_circle_geometry_req.txt
			// styles_line_geometry_req.txt
			// styles_marker_req.txt
			// styles_multipolygon_geometry_req.txt
			// styles_polygon_geometry_req.txt

			// TestPOI
			// poi_overlays.txt
			// poi_areas_overlays.txt
			// current_location.txt
			// current_location_and_pois.txt

		} catch (GeoException e) {
			e.printStackTrace();
		}
	}

	public static String doController(
			PortrayMapRequestType portrayMapRequestType) throws Exception {
		List<GetMapType> listGetMapTypes = GeoServerRequestBuilder
				.buildRequestGetMap(portrayMapRequestType);

		List<String> listSLDString = GeoServerRequestBuilder
				.bulidRequestSLDString(portrayMapRequestType);

		// Testing
		GetMapType getMapType = listGetMapTypes.get(0);
		String getMapXML;

		GlobalCore.MyOutStream myOutStream = new GlobalCore.MyOutStream();
		myOutStream.clear();

		JAXBElement<GetMapType> jaxbElement = GlobalCore.objectFactorySLD
				.createGetMap(getMapType);
		try {
			GlobalCore.marshallerWMS.marshal(jaxbElement, myOutStream);
			Log.d("PresentationController - GetMapXML OutputType",
					myOutStream.toString());

			getMapXML = myOutStream.toString().replaceAll("ns5", "ows")
					.replaceAll("sld", "ows").replaceAll("ns1", "gml")
					.replaceAll("ns2", "xlink").replaceAll("ns4", "se")
					.replaceAll("ns6", "ogc");
			if (getMapXML.indexOf("xmlns:ns8=\"http://www.opengis.net/wms\"") != -1) {
				Log.d(TAG, "ns8 = true");
				getMapXML = getMapXML.replaceAll(":?ns8:?", "");
			} else if (getMapXML
					.indexOf("xmlns:ns7=\"http://www.opengis.net/wms\"") != -1) {
				Log.d(TAG, "ns7 = true");
				getMapXML = getMapXML.replaceAll(":?ns7:?", "");
			}

			// Merge SLD XML to GetMap XML
			String pattern = "<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>";
			int insertingIndex = getMapXML.indexOf(">", pattern.length() + 1) + 1;
			getMapXML = getMapXML.substring(0, insertingIndex)
					+ listSLDString.get(0)
					+ getMapXML.substring(insertingIndex);
			Log.d("PresentationController - GetMapXML", getMapXML);

			// Send request to GeoServer and Get the imageResponse
			try {

				HttpData gotData = HttpRequest.postXML(Config.GEOSERVER_PATH,
						getMapXML);
				if (gotData.headers.get("Status").toString().indexOf("OK") != -1) {

					FileWriter outStream = new FileWriter("imageResponse.svg");
					BufferedWriter writer = new BufferedWriter(outStream);
					writer.write(gotData.content.toString());

					writer.close();
					outStream.close();

				}

				return gotData.content;
			} catch (ConnectException e) {
				throw new GeoException(
						I18N.m.getString("cannot_connect_geoserver"));
			}

		} catch (JAXBException e) {
			e.printStackTrace();
			throw new GeoException(I18N.m.getString("cannot_build_result"));
		}
	}

	// TODO Khiem add later
	public static PortrayMapResponseType doController2(PortrayMapRequestType portrayMapRequestType) throws GeoException, Exception {
		List<GetMapType> listGetMapTypes = GeoServerRequestBuilder
				.buildRequestGetMap(portrayMapRequestType);

		List<String> listSLDString = GeoServerRequestBuilder
				.bulidRequestSLDString(portrayMapRequestType);

		// Testing
		GetMapType getMapType = listGetMapTypes.get(0);
		String getMapXML;

		GlobalCore.MyOutStream myOutStream = new GlobalCore.MyOutStream();
		myOutStream.clear();

		JAXBElement<GetMapType> jaxbElement = GlobalCore.objectFactorySLD
				.createGetMap(getMapType);
		try {
			GlobalCore.marshallerWMS.marshal(jaxbElement, myOutStream);
			Log.d("PresentationController - GetMapXML OutputType",
					myOutStream.toString());

			getMapXML = myOutStream.toString().replaceAll("ns5", "ows")
					.replaceAll("sld", "ows").replaceAll("ns1", "gml")
					.replaceAll("ns2", "xlink").replaceAll("ns4", "se")
					.replaceAll("ns6", "ogc");
			if (getMapXML.indexOf("xmlns:ns8=\"http://www.opengis.net/wms\"") != -1) {
				Log.d(TAG, "ns8 = true");
				getMapXML = getMapXML.replaceAll(":?ns8:?", "");
			} else if (getMapXML
					.indexOf("xmlns:ns7=\"http://www.opengis.net/wms\"") != -1) {
				Log.d(TAG, "ns7 = true");
				getMapXML = getMapXML.replaceAll(":?ns7:?", "");
			}

			// Merge SLD XML to GetMap XML
			String pattern = "<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>";
			int insertingIndex = getMapXML.indexOf(">", pattern.length() + 1) + 1;
			getMapXML = getMapXML.substring(0, insertingIndex)
					+ listSLDString.get(0)
					+ getMapXML.substring(insertingIndex);
			Log.d("PresentationController - GetMapXML", getMapXML);

			// Send request to GeoServer and Get the imageResponse
			try {

				HttpData gotData = HttpRequest.postXML(Config.GEOSERVER_PATH,
						getMapXML);
				if (gotData.headers.get("Status").toString().indexOf("OK") != -1) {

					FileWriter outStream = new FileWriter("imageResponse.svg");
					BufferedWriter writer = new BufferedWriter(outStream);
					writer.write(gotData.content.toString());

					writer.close();
					outStream.close();
				}

				//Build response
				PortrayMapResponseType portrayMapResponseType = new PortrayMapResponseType();
				
				MapType mapType = new MapType();
				List<MapType> listMapTypes = new ArrayList<MapType>();
				listMapTypes.add(mapType);
				portrayMapResponseType.setMap(listMapTypes);

				OutputType outputType = portrayMapRequestType.getOutput().get(0);
				mapType.setBBoxContext(outputType.getBBoxContext());
				mapType.setCenterContext(outputType.getCenterContext());
				
				return portrayMapResponseType;
			} catch (ConnectException e) {
				throw new GeoException(
						I18N.m.getString("cannot_connect_geoserver"));
			}

		} catch (JAXBException e) {
			e.printStackTrace();
			throw new GeoException(I18N.m.getString("cannot_build_result"));
		}
	}

}
