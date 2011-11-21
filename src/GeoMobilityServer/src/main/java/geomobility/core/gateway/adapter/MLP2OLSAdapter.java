package geomobility.core.gateway.adapter;

import geomobility.core.GlobalCore;
import geomobility.core.exception.GeoException;
import geomobility.core.gateway.mlp.entity.svc_result.Box;
import geomobility.core.gateway.mlp.entity.svc_result.CircularArcArea;
import geomobility.core.gateway.mlp.entity.svc_result.CircularArea;
import geomobility.core.gateway.mlp.entity.svc_result.Coord;
import geomobility.core.gateway.mlp.entity.svc_result.Direction;
import geomobility.core.gateway.mlp.entity.svc_result.EllipticalArea;
import geomobility.core.gateway.mlp.entity.svc_result.InnerBoundaryIs;
import geomobility.core.gateway.mlp.entity.svc_result.LineString;
import geomobility.core.gateway.mlp.entity.svc_result.LinearRing;
import geomobility.core.gateway.mlp.entity.svc_result.Msid;
import geomobility.core.gateway.mlp.entity.svc_result.MultiLineString;
import geomobility.core.gateway.mlp.entity.svc_result.MultiPoint;
import geomobility.core.gateway.mlp.entity.svc_result.MultiPolygon;
import geomobility.core.gateway.mlp.entity.svc_result.OuterBoundaryIs;
import geomobility.core.gateway.mlp.entity.svc_result.Pd;
import geomobility.core.gateway.mlp.entity.svc_result.Point;
import geomobility.core.gateway.mlp.entity.svc_result.Polygon;
import geomobility.core.gateway.mlp.entity.svc_result.Pos;
import geomobility.core.gateway.mlp.entity.svc_result.Poserr;
import geomobility.core.gateway.mlp.entity.svc_result.QosNotMet;
import geomobility.core.gateway.mlp.entity.svc_result.Result;
import geomobility.core.gateway.mlp.entity.svc_result.Shape;
import geomobility.core.gateway.mlp.entity.svc_result.Slia;
import geomobility.core.gateway.mlp.entity.svc_result.Slirep;
import geomobility.core.gateway.mlp.entity.svc_result.Speed;
import geomobility.core.gateway.mlp.entity.svc_result.Time;
import geomobility.core.utils.Log;
import geomobility.core.utils.StaticFunc;
import geomobility.localization.I18N;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import net.opengis.gml.v_3_1_1.AbstractRingPropertyType;
import net.opengis.gml.v_3_1_1.CircleByCenterPointType;
import net.opengis.gml.v_3_1_1.DirectPositionListType;
import net.opengis.gml.v_3_1_1.DirectPositionType;
import net.opengis.gml.v_3_1_1.LengthType;
import net.opengis.gml.v_3_1_1.LinearRingType;
import net.opengis.gml.v_3_1_1.MultiPolygonType;
import net.opengis.gml.v_3_1_1.PointType;
import net.opengis.gml.v_3_1_1.PolygonPropertyType;
import net.opengis.gml.v_3_1_1.PolygonType;
import net.opengis.xls.v_1_1_0.AngleType;
import net.opengis.xls.v_1_1_0.CircularArcType;
import net.opengis.xls.v_1_1_0.EllipseType;
import net.opengis.xls.v_1_1_0.OutputGatewayParametersType;
import net.opengis.xls.v_1_1_0.OutputMSIDsType;
import net.opengis.xls.v_1_1_0.OutputMSInformationType;
import net.opengis.xls.v_1_1_0.PositionType;
import net.opengis.xls.v_1_1_0.QualityOfPositionType;
import net.opengis.xls.v_1_1_0.SLIAType;
import net.opengis.xls.v_1_1_0.SpeedType;
import net.opengis.xls.v_1_1_0.TimeType;

import com.gisgraphy.helper.GeolocHelper;
import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;

/**
 * @author VoMinhTam
 */
public class MLP2OLSAdapter {
	private static final String TAG = MLP2OLSAdapter.class.getSimpleName();

	public static List<SLIAType> convertSLIAMessage(Slia slia) throws GeoException {
		List<SLIAType> listSliaTypes = new ArrayList<SLIAType>();

		for (int i = 0; i < slia.getPos().size(); i++) {
			SLIAType sliaType = new SLIAType();
			sliaType.setRequestID(slia.getReqId() == null ? null : slia
					.getReqId().getContent());

			// Some ununsed content
			// resid, result (other content), addInfo
			// pd.alt, pd.alt_unc
			Result resultObj = slia.getResult();
			if (resultObj != null) {
				String resid = resultObj.getResid();
				String result = resultObj.getContent();

				Log.d(TAG, String.format(
						"SLIA Message: \n-ResID: %s\n-Result: %s\n", resid,
						result));
			}

			String addInfo = slia.getAddInfo() == null ? null : slia
					.getAddInfo().getContent();

			Pos pos = slia.getPos().get(i);
			Msid msid = pos.getMsid();

			Pd pd = pos.getPd();
			pd.getAlt();
			pd.getAltUnc();
			// pd.getDirection();
			// pd.getLevConf();
			pd.getQosNotMet();

			Poserr poserr = pos.getPoserr();

			if (pd != null) {
				Time time = pd.getTime();

				Speed speed = pd.getSpeed();

				Direction direction = pd.getDirection();

				Shape shape = pd.getShape();

				Point point = shape.getPoint();

				LineString lineString = shape.getLineString();
				// Some unused shape in OpenLS: lineString, multiLineString,
				// linearRing
				Polygon polygon = shape.getPolygon();
				Box box = shape.getBox();
				CircularArea circularArea = shape.getCircularArea();
				CircularArcArea circularArcArea = shape.getCircularArcArea();
				EllipticalArea ellipticalArea = shape.getEllipticalArea();
				MultiLineString multiLineString = shape.getMultiLineString();
				MultiPoint multiPoint = shape.getMultiPoint();
				MultiPolygon multiPolygon = shape.getMultiPolygon();
				LinearRing linearRing = shape.getLinearRing();

				// OpenLS
				OutputGatewayParametersType outputGatewayParametersType = new OutputGatewayParametersType();
				outputGatewayParametersType.setLocationType(msid.getType());
				sliaType
						.setOutputGatewayParameters(outputGatewayParametersType);

				// Some missing information in OpenLS
				// requestedsrsName, priority, qualityOfPositionType

				OutputMSIDsType outputMSIDsType = new OutputMSIDsType();
				outputGatewayParametersType.setOutputMSIDs(outputMSIDsType);
				// session

				OutputMSInformationType outputMSInformationType = new OutputMSInformationType();
				outputMSInformationType.setMsIDType(msid.getType());
				outputMSInformationType.setMsIDValue(msid.getContent());
				outputMSInformationType.setEncryption(msid.getEnc());
				outputMSIDsType.setOutputMSInformation(outputMSInformationType);

				PositionType positionType = new PositionType();
				outputMSInformationType.setPosition(positionType);

				TimeType timeType = new TimeType();
				try {
					timeType.setUtcOffset(new BigInteger(time.getUtcOff()));
				} catch (Exception e) {
				}
				// begin
				try {
					DatatypeFactoryImpl datatypeFactory = new DatatypeFactoryImpl();
					timeType.setDuration(datatypeFactory.newDuration(Long
							.parseLong(time.getContent())));
				} catch (Exception e) {
				}
				positionType.setTime(timeType);

				SpeedType speedType = new SpeedType();
				try {
					speedType.setValue(new BigDecimal(speed.getContent()));
					// accuracy, uom
				} catch (Exception e) {
				}
				positionType.setSpeed(speedType);

				AngleType angleType = new AngleType();
				try {
					angleType.setValue(new BigDecimal(direction.getContent()));
					// accuracy, uom
				} catch (Exception e) {
				}
				positionType.setDirection(angleType);

				positionType.setLevelOfConf(pd.getLevConf() == null ? null : pd
						.getLevConf().getContent());

				QualityOfPositionType qualityOfPositionType = new QualityOfPositionType();
				QosNotMet qosNotMet = pd.getQosNotMet();
				if (qosNotMet != null)
					positionType.setQoP(qualityOfPositionType);

				// PointType
				positionType.setPoint(getPointType(point));

				// EllipseType
				positionType.setEllipse(getEllipseType(ellipticalArea));

				// CircleByCenterPointType
				positionType
						.setCircleByCenterPoint(getCircleByCenterPointType(circularArea));

				// CircularArcType
				positionType
						.setCircularArc(getCircularArcType(circularArcArea));

				// Polygon
				positionType.setPolygon(getPolygonType(polygon));

				// MultiPolygonType
				positionType.setMultiPolygon(getMultiPolygonType(multiPolygon));

				listSliaTypes.add(sliaType);
			} else if (poserr != null) {
				// TODO MLP return ERROR Message
				String resid = "";
				String result = "";
				resultObj = poserr.getResult();
				if (resultObj != null) {
					resid = resultObj.getResid();
					result = resultObj.getContent();
				}

				addInfo = poserr.getAddInfo() == null ? null : poserr
						.getAddInfo().getContent();
				Time time = poserr.getTime();
				String contentTime = "";
				String utc = "";
				if (time != null) {
					contentTime = time.getContent();
					utc = time.getUtcOff();
				}
				Log.e(TAG, "SLIA Error Message:\n" + "-ResID: " + resid
						+ "\n-Result: " + result + "\nAddInfo: " + addInfo
						+ "\n-Time: " + contentTime + "(" + utc + ")\n");
			}
		}

		return listSliaTypes;
	}

	public static List<SLIAType> convertSLIRepMessage(Slirep slirep) throws GeoException {
		List<SLIAType> listSliaTypes = new ArrayList<SLIAType>();

		for (int i = 0; i < slirep.getPos().size(); i++) {
			SLIAType sliaType = new SLIAType();
			sliaType.setRequestID(slirep.getReqId() == null ? null : slirep
					.getReqId().getContent());

			Pos pos = slirep.getPos().get(i);
			Msid msid = pos.getMsid();

			Pd pd = pos.getPd();
			Poserr poserr = pos.getPoserr();

			if (pd != null) {
				Time time = pd.getTime();

				Speed speed = pd.getSpeed();

				Direction direction = pd.getDirection();

				Shape shape = pd.getShape();

				Point point = shape.getPoint();

				LineString lineString = shape.getLineString();
				// Some unused shape in OpenLS: lineString, multiLineString,
				// linearRing
				Polygon polygon = shape.getPolygon();
				Box box = shape.getBox();
				CircularArea circularArea = shape.getCircularArea();
				CircularArcArea circularArcArea = shape.getCircularArcArea();
				EllipticalArea ellipticalArea = shape.getEllipticalArea();
				MultiLineString multiLineString = shape.getMultiLineString();
				MultiPoint multiPoint = shape.getMultiPoint();
				MultiPolygon multiPolygon = shape.getMultiPolygon();
				LinearRing linearRing = shape.getLinearRing();

				// OpenLS
				OutputGatewayParametersType outputGatewayParametersType = new OutputGatewayParametersType();
				outputGatewayParametersType.setLocationType(msid.getType());
				sliaType
						.setOutputGatewayParameters(outputGatewayParametersType);

				// Some missing information in OpenLS
				// requestedsrsName, priority, qualityOfPositionType

				OutputMSIDsType outputMSIDsType = new OutputMSIDsType();
				outputGatewayParametersType.setOutputMSIDs(outputMSIDsType);
				// session

				OutputMSInformationType outputMSInformationType = new OutputMSInformationType();
				outputMSInformationType.setMsIDType(msid.getType());
				outputMSInformationType.setMsIDValue(msid.getContent());
				outputMSInformationType.setEncryption(msid.getEnc());
				outputMSIDsType.setOutputMSInformation(outputMSInformationType);

				PositionType positionType = new PositionType();
				outputMSInformationType.setPosition(positionType);

				TimeType timeType = new TimeType();
				try {
					timeType.setUtcOffset(new BigInteger(time.getUtcOff()));
				} catch (Exception e) {
				}
				// begin
				try {
					DatatypeFactoryImpl datatypeFactory = new DatatypeFactoryImpl();
					timeType.setDuration(datatypeFactory.newDuration(Long
							.parseLong(time.getContent())));
				} catch (Exception e) {
				}
				positionType.setTime(timeType);

				SpeedType speedType = new SpeedType();
				try {
					speedType.setValue(new BigDecimal(speed.getContent()));
					// accuracy, uom
				} catch (Exception e) {
				}
				positionType.setSpeed(speedType);

				AngleType angleType = new AngleType();
				try {
					angleType.setValue(new BigDecimal(direction.getContent()));
					// accuracy, uom
				} catch (Exception e) {
				}
				positionType.setDirection(angleType);

				positionType.setLevelOfConf(pd.getLevConf() == null ? null : pd
						.getLevConf().getContent());

				QualityOfPositionType qualityOfPositionType = new QualityOfPositionType();
				QosNotMet qosNotMet = pd.getQosNotMet();
				if (qosNotMet != null)
					positionType.setQoP(qualityOfPositionType);

				// PointType
				positionType.setPoint(getPointType(point));

				// EllipseType
				positionType.setEllipse(getEllipseType(ellipticalArea));

				// CircleByCenterPointType
				positionType
						.setCircleByCenterPoint(getCircleByCenterPointType(circularArea));

				// CircularArcType
				positionType
						.setCircularArc(getCircularArcType(circularArcArea));

				// Polygon
				positionType.setPolygon(getPolygonType(polygon));

				// MultiPolygonType
				positionType.setMultiPolygon(getMultiPolygonType(multiPolygon));

				listSliaTypes.add(sliaType);
			} else if (poserr != null) {
				// TODO MLP return ERROR Message
				String resid = "";
				String result = "";
				Result resultObj = poserr.getResult();
				if (resultObj != null) {
					resid = resultObj.getResid();
					result = resultObj.getContent();
				}

				String addInfo = poserr.getAddInfo() == null ? null : poserr
						.getAddInfo().getContent();
				Time time = poserr.getTime();
				String contentTime = "";
				String utc = "";
				if (time != null) {
					contentTime = time.getContent();
					utc = time.getUtcOff();
				}
				Log.e(TAG, "SLIA Error Message:\n" + "-ResID: " + resid
						+ "\n-Result: " + result + "\nAddInfo: " + addInfo
						+ "\n-Time: " + contentTime + "(" + utc + ")\n");
			}
		}

		return listSliaTypes;
	}

	public static PointType getPointType(Point point) {
		if (point == null)
			return null;
		Coord coord = point.getCoord();

		PointType pointType = new PointType();
		pointType.setGid(point.getGid());
		pointType.setId(point.getGid());
		pointType.setSrsName(point.getSrsName());

		DirectPositionType directPositionType = new DirectPositionType();
		directPositionType.setSrsName(point.getSrsName());

		int dimen = 1;
		try {
			directPositionType.getValue().add(
					Double.parseDouble(coord.getX().getContent()));
		} catch (Exception e) {
		}

		if (coord.getY() != null) {
			dimen++;
			try {
				directPositionType.getValue().add(
						Double.parseDouble(coord.getY().getContent()));
			} catch (Exception e) {
			}
		}
		if (coord.getZ() != null) {
			dimen++;
			try {
				directPositionType.getValue().add(
						Double.parseDouble(coord.getZ().getContent()));
			} catch (Exception e) {
			}
		}
		directPositionType.setSrsDimension(new BigInteger(dimen + ""));
		directPositionType.setSrsName(point.getSrsName());
		pointType.setPos(directPositionType);

		return pointType;
	}

	public static EllipseType getEllipseType(EllipticalArea ellipticalArea) {
		if (ellipticalArea == null)
			return null;
		Coord coord = ellipticalArea.getCoord();

		EllipseType ellipseType = new EllipseType();
		ellipseType.setGid(ellipticalArea.getGid());
		ellipseType.setId(ellipticalArea.getGid());
		ellipseType.setSrsName(ellipticalArea.getSrsName());

		DirectPositionType directPositionType = new DirectPositionType();
		directPositionType.setSrsName(ellipticalArea.getSrsName());

		int dimen = 1;
		try {
			directPositionType.getValue().add(
					Double.parseDouble(coord.getX().getContent()));
		} catch (Exception e) {
		}

		if (coord.getY() != null) {
			dimen++;
			try {
				directPositionType.getValue().add(
						Double.parseDouble(coord.getY().getContent()));
			} catch (Exception e) {
			}
		}
		if (coord.getZ() != null) {
			dimen++;
			try {
				directPositionType.getValue().add(
						Double.parseDouble(coord.getZ().getContent()));
			} catch (Exception e) {
			}
		}
		directPositionType.setSrsDimension(new BigInteger(dimen + ""));
		directPositionType.setSrsName(ellipticalArea.getSrsName());
		ellipseType.setPos(directPositionType);

		try {
			net.opengis.gml.v_3_1_1.AngleType angleType = new net.opengis.gml.v_3_1_1.AngleType();
			angleType.setValue(GeolocHelper.getAngleInDegree(ellipticalArea
					.getAngularUnit() == null ? null : ellipticalArea
					.getAngularUnit().getContent(), Double
					.parseDouble(ellipticalArea.getAngle().getContent())));
			ellipseType.setRotation(angleType);
		} catch (Exception e) {
		}

		try {
			LengthType lengthType = new LengthType();
			lengthType.setValue(GeolocHelper.getLengthInMeter(ellipticalArea
					.getDistanceUnit() == null ? null : ellipticalArea
					.getDistanceUnit().getContent(), Double
					.parseDouble(ellipticalArea.getSemiMajor().getContent())));
			ellipseType.setMajorAxis(lengthType);
			LengthType lengthType2 = new LengthType();
			lengthType2.setValue(GeolocHelper.getLengthInMeter(ellipticalArea
					.getDistanceUnit() == null ? null : ellipticalArea
					.getDistanceUnit().getContent(), Double
					.parseDouble(ellipticalArea.getSemiMinor().getContent())));
			ellipseType.setMajorAxis(lengthType2);
		} catch (Exception e) {
		}

		return ellipseType;
	}

	public static CircleByCenterPointType getCircleByCenterPointType(
			CircularArea circularArea) {
		if (circularArea == null)
			return null;
		Coord coord = circularArea.getCoord();

		// Some missed values in OpenLS: interpolation
		CircleByCenterPointType circlePointType = new CircleByCenterPointType();
		// circlePointType.setInterpolation();
		circlePointType.setNumArc(new BigInteger("1"));

		DirectPositionType directPositionType = new DirectPositionType();
		directPositionType.setSrsName(circularArea.getSrsName());

		int dimen = 1;
		try {
			directPositionType.getValue().add(
					Double.parseDouble(coord.getX().getContent()));
		} catch (Exception e) {
		}

		if (coord.getY() != null) {
			dimen++;
			try {
				directPositionType.getValue().add(
						Double.parseDouble(coord.getY().getContent()));
			} catch (Exception e) {
			}
		}
		if (coord.getZ() != null) {
			dimen++;
			try {
				directPositionType.getValue().add(
						Double.parseDouble(coord.getZ().getContent()));
			} catch (Exception e) {
			}
		}
		directPositionType.setSrsDimension(new BigInteger(dimen + ""));
		directPositionType.setSrsName(circularArea.getSrsName());
		circlePointType.setPos(directPositionType);

		try {
			LengthType lengthType = new LengthType();
			lengthType.setValue(GeolocHelper.getLengthInMeter(circularArea
					.getDistanceUnit() == null ? null : circularArea
					.getDistanceUnit().getContent(), Double
					.parseDouble(circularArea.getRadius().getContent())));
			circlePointType.setRadius(lengthType);
		} catch (Exception e) {
		}

		return circlePointType;
	}

	public static CircularArcType getCircularArcType(
			CircularArcArea circularArcArea) {
		if (circularArcArea == null)
			return null;
		Coord coord = circularArcArea.getCoord();

		CircularArcType circularArcType = new CircularArcType();
		// circlePointType.setInterpolation();
		circularArcType.setNumArc(new BigInteger("1"));
		circularArcType.setGid(circularArcArea.getGid());
		circularArcType.setId(circularArcArea.getGid());
		circularArcType.setSrsName(circularArcArea.getSrsName());

		DirectPositionType directPositionType = new DirectPositionType();
		directPositionType.setSrsName(circularArcArea.getSrsName());

		int dimen = 1;
		try {
			directPositionType.getValue().add(
					Double.parseDouble(coord.getX().getContent()));
		} catch (Exception e) {
		}

		if (coord.getY() != null) {
			dimen++;
			try {
				directPositionType.getValue().add(
						Double.parseDouble(coord.getY().getContent()));
			} catch (Exception e) {
			}
		}
		if (coord.getZ() != null) {
			dimen++;
			try {
				directPositionType.getValue().add(
						Double.parseDouble(coord.getZ().getContent()));
			} catch (Exception e) {
			}
		}
		directPositionType.setSrsDimension(new BigInteger(dimen + ""));
		directPositionType.setSrsName(circularArcArea.getSrsName());
		circularArcType.setPos(directPositionType);

		try {
			LengthType lengthType = new LengthType();
			lengthType.setValue(GeolocHelper.getLengthInMeter(circularArcArea
					.getDistanceUnit() == null ? null : circularArcArea
					.getDistanceUnit().getContent(), Double
					.parseDouble(circularArcArea.getInRadius().getContent())));
			circularArcType.setInnerRadius(lengthType);
			LengthType lengthType2 = new LengthType();
			lengthType2.setValue(GeolocHelper.getLengthInMeter(circularArcArea
					.getDistanceUnit() == null ? null : circularArcArea
					.getDistanceUnit().getContent(), Double
					.parseDouble(circularArcArea.getOutRadius().getContent())));
			circularArcType.setOuterRadius(lengthType2);
		} catch (Exception e) {
		}

		try {
			net.opengis.gml.v_3_1_1.AngleType angleType = new net.opengis.gml.v_3_1_1.AngleType();
			angleType
					.setValue(GeolocHelper.getAngleInDegree(circularArcArea
							.getAngularUnit() == null ? null : circularArcArea
							.getDistanceUnit().getContent(), Double
							.parseDouble(circularArcArea.getStartAngle()
									.getContent())));
			circularArcType.setStartAngle(angleType);
			net.opengis.gml.v_3_1_1.AngleType angleType2 = new net.opengis.gml.v_3_1_1.AngleType();
			angleType2.setValue(GeolocHelper.getAngleInDegree(circularArcArea
					.getAngularUnit() == null ? null : circularArcArea
					.getDistanceUnit().getContent(), Double
					.parseDouble(circularArcArea.getStopAngle().getContent())));
			circularArcType.setEndAngle(angleType2);
		} catch (Exception e) {
		}

		return circularArcType;
	}

	public static PolygonType getPolygonType(Polygon polygon) throws GeoException {
		if (polygon == null)
			return null;
		OuterBoundaryIs outerBoundaryIs = polygon.getOuterBoundaryIs();
		LinearRing linearRing = outerBoundaryIs.getLinearRing();
		List<Coord> listCoords = linearRing.getCoord();

		List<InnerBoundaryIs> listInnerBoundaryIs = polygon
				.getInnerBoundaryIs();

		PolygonType polygonType = new PolygonType();
		polygonType.setGid(polygon.getGid());
		polygonType.setId(polygon.getGid());
		polygonType.setSrsName(polygon.getSrsName());

		// Exterior
		LinearRingType linearRingType = new LinearRingType();
		linearRingType.setId(linearRing.getGid());
		linearRingType.setGid(linearRing.getGid());
		linearRingType.setSrsName(linearRing.getSrsName());

		DirectPositionListType listDirectPositionTypes = linearRingType
				.getPosList();
		linearRingType.setPosList(listDirectPositionTypes);
		for (Coord coord : listCoords) {
			DirectPositionType directPositionType = new DirectPositionType();
			directPositionType.setSrsName(linearRing.getSrsName());

			if (coord.getX()== null || StaticFunc.isNOE(coord.getX().getContent()))
				throw new GeoException(I18N.m.getString("invalid_parameter"));
			
			int dimen = 1;
			try {
				listDirectPositionTypes.getValue().add(Double.parseDouble(coord.getX().getContent()));
			} catch (Exception e) {
			}

			if (coord.getY() != null) {
				dimen++;
				try {
					listDirectPositionTypes.getValue().add(Double.parseDouble(coord.getY().getContent()));
				} catch (Exception e) {
				}
			}
			if (coord.getZ() != null) {
				dimen++;
				try {
					listDirectPositionTypes.getValue().add(Double.parseDouble(coord.getZ().getContent()));
				} catch (Exception e) {
				}
			}
			listDirectPositionTypes.setSrsDimension(new BigInteger(dimen + ""));
		}
		
//		for (Coord coord : listCoords) {
//			DirectPositionType directPositionType = new DirectPositionType();
//			directPositionType.setSrsName(linearRing.getSrsName());
//
//			if (coord.getX()== null || StaticFunc.isNOEString(coord.getX().getContent()))
//				throw new GeoException(I18N.m.getString("invalid_parameter"));
//			
//			int dimen = 1;
//			try {
//				directPositionType.getValue().add(
//						Double.parseDouble(coord.getX().getContent()));
//			} catch (Exception e) {
//			}
//
//			if (coord.getY() != null) {
//				dimen++;
//				try {
//					directPositionType.getValue().add(
//							Double.parseDouble(coord.getY().getContent()));
//				} catch (Exception e) {
//				}
//			}
//			if (coord.getZ() != null) {
//				dimen++;
//				try {
//					directPositionType.getValue().add(
//							Double.parseDouble(coord.getZ().getContent()));
//				} catch (Exception e) {
//				}
//			}
//			directPositionType.setSrsDimension(new BigInteger(dimen + ""));
//			listDirectPositionTypes.add(directPositionType);
//		}

		JAXBElement<LinearRingType> jaxbElement = new JAXBElement<LinearRingType>(
				new QName("http://www.opengis.net/gml", "LinearRingType"),
				LinearRingType.class, linearRingType);
		AbstractRingPropertyType abstractRingPropertyType = new AbstractRingPropertyType();
		abstractRingPropertyType.setRing(jaxbElement);
		
		polygonType.setExterior(new JAXBElement<AbstractRingPropertyType>(
				new QName("http://www.opengis.net/gml", "AbstractRingPropertyType"),
				AbstractRingPropertyType.class, abstractRingPropertyType));

		// Interior
		List<AbstractRingPropertyType> listInterior = new ArrayList<AbstractRingPropertyType>();
		for (int i=0;i<polygonType.getInterior().size(); i++){
			listInterior.add(polygonType.getInterior().get(i).getValue());
		}
		for (InnerBoundaryIs innerBoundaryIs : listInnerBoundaryIs) {
			linearRing = innerBoundaryIs.getLinearRing();

			linearRingType = new LinearRingType();
			listDirectPositionTypes = linearRingType.getPosList();
			linearRingType.setId(linearRing.getGid());
			linearRingType.setGid(linearRing.getGid());
			linearRingType.setSrsName(linearRing.getSrsName());

			linearRingType.setPosList(listDirectPositionTypes);
			listDirectPositionTypes.setSrsName(linearRing.getSrsName());
			for (Coord coord : listCoords) {
				if (coord.getX()== null || StaticFunc.isNOE(coord.getX().getContent()))
					throw new GeoException(I18N.m.getString("invalid_parameter"));

				int dimen = 1;
				try {
					listDirectPositionTypes.getValue().add(Double.parseDouble(coord.getX().getContent()));
				} catch (Exception e) {
				}

				if (coord.getY() != null) {
					dimen++;
					try {
						listDirectPositionTypes.getValue().add(Double.parseDouble(coord.getY().getContent()));
					} catch (Exception e) {
					}
				}
				if (coord.getZ() != null) {
					dimen++;
					try {
						listDirectPositionTypes.getValue().add(Double.parseDouble(coord.getZ().getContent()));
					} catch (Exception e) {
					}
				}
				listDirectPositionTypes.setSrsDimension(new BigInteger(dimen + ""));
			}
//			for (Coord coord : listCoords) {
//				DirectPositionType directPositionType = new DirectPositionType();
//				directPositionType.setSrsName(linearRing.getSrsName());
//				
//				if (coord.getX()== null || StaticFunc.isNOEString(coord.getX().getContent()))
//					throw new GeoException(I18N.m.getString("invalid_parameter"));
//
//				int dimen = 1;
//				try {
//					directPositionType.getValue().add(
//							Double.parseDouble(coord.getX().getContent()));
//				} catch (Exception e) {
//				}
//
//				if (coord.getY() != null) {
//					dimen++;
//					try {
//						directPositionType.getValue().add(
//								Double.parseDouble(coord.getY().getContent()));
//					} catch (Exception e) {
//					}
//				}
//				if (coord.getZ() != null) {
//					dimen++;
//					try {
//						directPositionType.getValue().add(
//								Double.parseDouble(coord.getZ().getContent()));
//					} catch (Exception e) {
//					}
//				}
//				directPositionType.setSrsDimension(new BigInteger(dimen + ""));
//				listDirectPositionTypes.add(directPositionType);
//			}

			abstractRingPropertyType = new AbstractRingPropertyType();
			jaxbElement = new JAXBElement<LinearRingType>(new QName(
					"http://www.opengis.net/gml", "LinearRingType"),
					LinearRingType.class, linearRingType);
			abstractRingPropertyType.setRing(jaxbElement);

			listInterior.add(abstractRingPropertyType);
		}

		return polygonType;
	}

	public static MultiPolygonType getMultiPolygonType(MultiPolygon mPolygon) {
		if (mPolygon == null)
			return null;
		List<Object> listPolygons = mPolygon.getPolygonOrBoxOrCircularArea();

		MultiPolygonType mPolygonType = new MultiPolygonType();
		mPolygonType.setId(mPolygon.getGid());
		mPolygonType.setGid(mPolygon.getGid());
		mPolygonType.setSrsName(mPolygon.getSrsName());

		List<PolygonPropertyType> listPolygonPropertyTypes = mPolygonType
				.getPolygonMember();
		PolygonPropertyType polygonPropertyType;
		try{
			for (Object o : listPolygons) {
				if (o instanceof Polygon) {
					Polygon polygon = (Polygon) o;
	
					polygonPropertyType = new PolygonPropertyType();
					polygonPropertyType.setPolygon(getPolygonType(polygon));
					listPolygonPropertyTypes.add(polygonPropertyType);
				} else if (o instanceof Box) {
					Box box = (Box) o;
	
					polygonPropertyType = new PolygonPropertyType();
					polygonPropertyType.setPolygon(getPolygonTypeFromBox(box));
				} else if (o instanceof CircularArea) {
					// TODO Cannot get Polygon from CircularArea
				} else if (o instanceof CircularArcArea) {
					// TODO Cannot get Polygon from CircularArcArea
				} else if (o instanceof EllipticalArea) {
					// TODO Cannot get Polygon from EllipticalArea
				}
			}
		}catch (GeoException ge){
			ge.printStackTrace();
		}
		return mPolygonType;
	}

	public static PolygonType getPolygonTypeFromBox(Box box) throws GeoException {
		if (box == null)
			return null;
		List<Coord> listCoords = box.getCoord();

		PolygonType polygonType = new PolygonType();
		polygonType.setGid(box.getGid());
		polygonType.setId(box.getGid());
		polygonType.setSrsName(box.getSrsName());

		// Exterior
		LinearRingType linearRingType = new LinearRingType();
		linearRingType.setId(box.getGid());
		linearRingType.setGid(box.getGid());
		linearRingType.setSrsName(box.getSrsName());

//		List<DirectPositionType> listDirectPositionTypes = linearRingType
//				.getPos();
//		 linearRingType.setPos(listDirectPositionTypes);
		 DirectPositionListType listDirectPositionTypes = linearRingType.getPosList();
		 linearRingType.setPosList(listDirectPositionTypes);
		 
		 listDirectPositionTypes.setSrsName(box.getSrsName());
		 for (Coord coord : listCoords) {
			if (coord.getX()== null || StaticFunc.isNOE(coord.getX().getContent()))
					throw new GeoException(I18N.m.getString("invalid_parameter"));
			 
			int dimen = 1;
			try {
				listDirectPositionTypes.getValue().add(Double.parseDouble(coord.getX().getContent()));
			} catch (Exception e) {
			}

			if (coord.getY() != null) {
				dimen++;
				try {
					listDirectPositionTypes.getValue().add(Double.parseDouble(coord.getY().getContent()));
				} catch (Exception e) {
				}
			}
			if (coord.getZ() != null) {
				dimen++;
				try {
					listDirectPositionTypes.getValue().add(Double.parseDouble(coord.getZ().getContent()));
				} catch (Exception e) {
				}
			}
			listDirectPositionTypes.setSrsDimension(new BigInteger(dimen + ""));
		}
		 
//		for (Coord coord : listCoords) {
//			DirectPositionType directPositionType = new DirectPositionType();
//			directPositionType.setSrsName(box.getSrsName());
//
//			int dimen = 1;
//			try {
//				directPositionType.getValue().add(
//						Double.parseDouble(coord.getX().getContent()));
//			} catch (Exception e) {
//			}
//
//			if (coord.getY() != null) {
//				dimen++;
//				try {
//					directPositionType.getValue().add(
//							Double.parseDouble(coord.getY().getContent()));
//				} catch (Exception e) {
//				}
//			}
//			if (coord.getZ() != null) {
//				dimen++;
//				try {
//					directPositionType.getValue().add(
//							Double.parseDouble(coord.getZ().getContent()));
//				} catch (Exception e) {
//				}
//			}
//			directPositionType.setSrsDimension(new BigIntger(dimen + ""));
//			listDirectPositionTypes.add(directPositionType);
//		}

		AbstractRingPropertyType abstractRingPropertyType = new AbstractRingPropertyType();
		JAXBElement<LinearRingType> jaxbElement = new JAXBElement<LinearRingType>(
				new QName("http://www.opengis.net/gml", "LinearRingType"),
				LinearRingType.class, linearRingType);
		abstractRingPropertyType.setRing(jaxbElement);
		
		polygonType.setExterior(GlobalCore.objectFactoryGML.createInnerBoundaryIs(abstractRingPropertyType));
//		polygonType.setExterior(new JAXBElement<AbstractRingPropertyType>(
//				new QName("http://www.opengis.net/gml", "AbstractRingPropertyType"),
//				AbstractRingPropertyType.class, abstractRingPropertyType).getValue());

		return polygonType;
	}
}
