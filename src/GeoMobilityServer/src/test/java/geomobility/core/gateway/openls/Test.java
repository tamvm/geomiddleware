//package geomobility.core.gateway.openls;
//
//import geomobility.core.gateway.mlp.entity.svc_result.Box;
//import geomobility.core.gateway.mlp.entity.svc_result.CircularArcArea;
//import geomobility.core.gateway.mlp.entity.svc_result.CircularArea;
//import geomobility.core.gateway.mlp.entity.svc_result.Coord;
//import geomobility.core.gateway.mlp.entity.svc_result.Direction;
//import geomobility.core.gateway.mlp.entity.svc_result.EllipticalArea;
//import geomobility.core.gateway.mlp.entity.svc_result.InnerBoundaryIs;
//import geomobility.core.gateway.mlp.entity.svc_result.LineString;
//import geomobility.core.gateway.mlp.entity.svc_result.LinearRing;
//import geomobility.core.gateway.mlp.entity.svc_result.Msid;
//import geomobility.core.gateway.mlp.entity.svc_result.MultiLineString;
//import geomobility.core.gateway.mlp.entity.svc_result.MultiPoint;
//import geomobility.core.gateway.mlp.entity.svc_result.MultiPolygon;
//import geomobility.core.gateway.mlp.entity.svc_result.OuterBoundaryIs;
//import geomobility.core.gateway.mlp.entity.svc_result.Pd;
//import geomobility.core.gateway.mlp.entity.svc_result.Point;
//import geomobility.core.gateway.mlp.entity.svc_result.Polygon;
//import geomobility.core.gateway.mlp.entity.svc_result.Pos;
//import geomobility.core.gateway.mlp.entity.svc_result.Poserr;
//import geomobility.core.gateway.mlp.entity.svc_result.QosNotMet;
//import geomobility.core.gateway.mlp.entity.svc_result.Result;
//import geomobility.core.gateway.mlp.entity.svc_result.Shape;
//import geomobility.core.gateway.mlp.entity.svc_result.Slia;
//import geomobility.core.gateway.mlp.entity.svc_result.Speed;
//import geomobility.core.gateway.mlp.entity.svc_result.Time;
//import geomobility.core.utils.Log;
//import geomobility.core.utils.StaticFunc;
//
//import java.io.File;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBElement;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
//import javax.xml.namespace.QName;
//
//import net.opengis.gml.v_3_1_1.AbstractRingPropertyType;
//import net.opengis.gml.v_3_1_1.CircleByCenterPointType;
//import net.opengis.gml.v_3_1_1.DirectPositionListType;
//import net.opengis.gml.v_3_1_1.DirectPositionType;
//import net.opengis.gml.v_3_1_1.LengthType;
//import net.opengis.gml.v_3_1_1.LinearRingType;
//import net.opengis.gml.v_3_1_1.MultiPolygonType;
//import net.opengis.gml.v_3_1_1.PointType;
//import net.opengis.gml.v_3_1_1.PolygonPropertyType;
//import net.opengis.gml.v_3_1_1.PolygonType;
//import net.opengis.xls.v_1_1_0.AngleType;
//import net.opengis.xls.v_1_1_0.CircularArcType;
//import net.opengis.xls.v_1_1_0.EllipseType;
//import net.opengis.xls.v_1_1_0.OutputGatewayParametersType;
//import net.opengis.xls.v_1_1_0.OutputMSIDsType;
//import net.opengis.xls.v_1_1_0.OutputMSInformationType;
//import net.opengis.xls.v_1_1_0.PositionType;
//import net.opengis.xls.v_1_1_0.QualityOfPositionType;
//import net.opengis.xls.v_1_1_0.SLIAType;
//import net.opengis.xls.v_1_1_0.SpeedType;
//import net.opengis.xls.v_1_1_0.TimeType;
//
//import com.gisgraphy.helper.GeolocHelper;
//import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
//
//public class Test {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		try {
//			JAXBContext jc = JAXBContext
//					.newInstance("geomobility.core.gateway.mlp.entity.svc_result");
//			Unmarshaller u = jc.createUnmarshaller();
//			File f = new File("data/gateway/slia.xml");
//			System.out.println(StaticFunc.getContent("data/gateway/slia.xml"));
//			JAXBElement element = (JAXBElement) u.unmarshal(f);
//
//			List<SLIAType> listSliaTypes = new ArrayList<SLIAType>();
//
//			Slia slia = (Slia) element.getValue();
//
//			for (int i = 0; i < slia.getPos().size(); i++) {
//				SLIAType sliaType = new SLIAType();
//				sliaType.setRequestID(slia.getReqId() == null ? null : slia
//						.getReqId().getContent());
//
//				// Some ununsed content
//				// resid, result (other content), addInfo
//				// pd.alt, pd.alt_unc
//				Result resultObj = slia.getResult();
//				if (resultObj != null) {
//					String resid = resultObj.getResid();
//					String result = resultObj.getContent();
//
//					Log.d(Test.class.getSimpleName(), String.format(
//							"SLIA Message: \n-ResID: %s\n-Result: %s\n", resid,
//							result));
//				}
//
//				String addInfo = slia.getAddInfo() == null ? null : slia
//						.getAddInfo().getContent();
//
//				Pos pos = slia.getPos().get(i);
//				Msid msid = pos.getMsid();
//
//				Pd pd = pos.getPd();
//				Poserr poserr = pos.getPoserr();
//
//				if (pd != null) {
//					Time time = pd.getTime();
//
//					Speed speed = pd.getSpeed();
//
//					Direction direction = pd.getDirection();
//
//					Shape shape = pd.getShape();
//
//					Point point = shape.getPoint();
//
//					LineString lineString = shape.getLineString();
//					// Some unused shape in OpenLS: lineString, multiLineString,
//					// linearRing
//					Polygon polygon = shape.getPolygon();
//					Box box = shape.getBox();
//					CircularArea circularArea = shape.getCircularArea();
//					CircularArcArea circularArcArea = shape
//							.getCircularArcArea();
//					EllipticalArea ellipticalArea = shape.getEllipticalArea();
//					MultiLineString multiLineString = shape
//							.getMultiLineString();
//					MultiPoint multiPoint = shape.getMultiPoint();
//					MultiPolygon multiPolygon = shape.getMultiPolygon();
//					LinearRing linearRing = shape.getLinearRing();
//
//					// OpenLS
//					OutputGatewayParametersType outputGatewayParametersType = new OutputGatewayParametersType();
//					outputGatewayParametersType.setLocationType(msid.getType());
//					sliaType
//							.setOutputGatewayParameters(outputGatewayParametersType);
//
//					// Some missing information in OpenLS
//					// requestedsrsName, priority, qualityOfPositionType
//
//					OutputMSIDsType outputMSIDsType = new OutputMSIDsType();
//					outputGatewayParametersType.setOutputMSIDs(outputMSIDsType);
//					// session
//
//					OutputMSInformationType outputMSInformationType = new OutputMSInformationType();
//					outputMSInformationType.setMsIDType(msid.getType());
//					outputMSInformationType.setMsIDValue(msid.getContent());
//					outputMSInformationType.setEncryption(msid.getEnc());
//					outputMSIDsType
//							.setOutputMSInformation(outputMSInformationType);
//
//					PositionType positionType = new PositionType();
//					outputMSInformationType.setPosition(positionType);
//
//					TimeType timeType = new TimeType();
//					try {
//						timeType.setUtcOffset(new BigInteger(time.getUtcOff()));
//					} catch (Exception e) {
//					}
//					// begin
//					try {
//						DatatypeFactoryImpl datatypeFactory = new DatatypeFactoryImpl();
//						timeType.setDuration(datatypeFactory.newDuration(Long
//								.parseLong(time.getContent())));
//					} catch (Exception e) {
//					}
//					positionType.setTime(timeType);
//
//					SpeedType speedType = new SpeedType();
//					try {
//						speedType.setValue(new BigDecimal(speed.getContent()));
//						// accuracy, uom
//					} catch (Exception e) {
//					}
//					positionType.setSpeed(speedType);
//
//					AngleType angleType = new AngleType();
//					try {
//						angleType.setValue(new BigDecimal(direction
//								.getContent()));
//						// accuracy, uom
//					} catch (Exception e) {
//					}
//					positionType.setDirection(angleType);
//
//					positionType.setLevelOfConf(pd.getLevConf() == null ? null
//							: pd.getLevConf().getContent());
//
//					QualityOfPositionType qualityOfPositionType = new QualityOfPositionType();
//					QosNotMet qosNotMet = pd.getQosNotMet();
//					if (qosNotMet != null)
//						positionType.setQoP(qualityOfPositionType);
//
//					// PointType
//					positionType.setPoint(getPointType(point));
//
//					// EllipseType
//					positionType.setEllipse(getEllipseType(ellipticalArea));
//
//					// CircleByCenterPointType
//					positionType
//							.setCircleByCenterPoint(getCircleByCenterPointType(circularArea));
//
//					// CircularArcType
//					positionType
//							.setCircularArc(getCircularArcType(circularArcArea));
//
//					// Polygon
//					positionType.setPolygon(getPolygonType(polygon));
//
//					// MultiPolygonType
//					positionType
//							.setMultiPolygon(getMultiPolygonType(multiPolygon));
//
//					listSliaTypes.add(sliaType);
//				} else if (poserr != null) {
//					// TODO MLP return ERROR Message
//					String resid = "";
//					String result = "";
//					resultObj = poserr.getResult();
//					if (resultObj != null) {
//						resid = resultObj.getResid();
//						result = resultObj.getContent();
//					}
//
//					addInfo = poserr.getAddInfo() == null ? null : poserr
//							.getAddInfo().getContent();
//					Time time = poserr.getTime();
//					String contentTime = "";
//					String utc = "";
//					if (time != null) {
//						contentTime = time.getContent();
//						utc = time.getUtcOff();
//					}
//					Log.e(Test.class.getSimpleName(), "SLIA Error Message:\n"
//							+ "-ResID: " + resid + "\n-Result: " + result
//							+ "\nAddInfo: " + addInfo + "\n-Time: "
//							+ contentTime + "(" + utc + ")\n");
//				}
//			}
//
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static PointType getPointType(Point point) {
//		if (point == null)
//			return null;
//		Coord coord = point.getCoord();
//
//		PointType pointType = new PointType();
//		pointType.setGid(point.getGid());
//		pointType.setId(point.getGid());
//		pointType.setSrsName(point.getSrsName());
//
//		DirectPositionType directPositionType = new DirectPositionType();
//		directPositionType.setSrsName(point.getSrsName());
//
//		int dimen = 1;
//		try {
//			directPositionType.getValue().add(
//					Double.parseDouble(coord.getX().getContent()));
//		} catch (Exception e) {
//		}
//
//		if (coord.getY() != null) {
//			dimen++;
//			try {
//				directPositionType.getValue().add(
//						Double.parseDouble(coord.getY().getContent()));
//			} catch (Exception e) {
//			}
//		}
//		if (coord.getZ() != null) {
//			dimen++;
//			try {
//				directPositionType.getValue().add(
//						Double.parseDouble(coord.getZ().getContent()));
//			} catch (Exception e) {
//			}
//		}
//		directPositionType.setSrsDimension(new BigInteger(dimen + ""));
//		directPositionType.setSrsName(point.getSrsName());
//		pointType.setPos(directPositionType);
//
//		return pointType;
//	}
//
//	public static EllipseType getEllipseType(EllipticalArea ellipticalArea) {
//		if (ellipticalArea == null)
//			return null;
//		Coord coord = ellipticalArea.getCoord();
//
//		EllipseType ellipseType = new EllipseType();
//		ellipseType.setGid(ellipticalArea.getGid());
//		ellipseType.setId(ellipticalArea.getGid());
//		ellipseType.setSrsName(ellipticalArea.getSrsName());
//
//		DirectPositionType directPositionType = new DirectPositionType();
//		directPositionType.setSrsName(ellipticalArea.getSrsName());
//
//		int dimen = 1;
//		try {
//			directPositionType.getValue().add(
//					Double.parseDouble(coord.getX().getContent()));
//		} catch (Exception e) {
//		}
//
//		if (coord.getY() != null) {
//			dimen++;
//			try {
//				directPositionType.getValue().add(
//						Double.parseDouble(coord.getY().getContent()));
//			} catch (Exception e) {
//			}
//		}
//		if (coord.getZ() != null) {
//			dimen++;
//			try {
//				directPositionType.getValue().add(
//						Double.parseDouble(coord.getZ().getContent()));
//			} catch (Exception e) {
//			}
//		}
//		directPositionType.setSrsDimension(new BigInteger(dimen + ""));
//		directPositionType.setSrsName(ellipticalArea.getSrsName());
//		ellipseType.setPos(directPositionType);
//
//		try {
//			ellipseType.setRotation(new AngleType(GeolocHelper
//					.getAngleInDegree(
//							ellipticalArea.getAngularUnit() == null ? null
//									: ellipticalArea.getAngularUnit()
//											.getContent(), Double
//									.parseDouble(ellipticalArea.getAngle()
//											.getContent()))));
//		} catch (Exception e) {
//		}
//
//		try {
//			ellipseType.setMajorAxis(new LengthType(GeolocHelper
//					.getLengthInMeter(
//							ellipticalArea.getDistanceUnit() == null ? null
//									: ellipticalArea.getDistanceUnit()
//											.getContent(), Double
//									.parseDouble(ellipticalArea.getSemiMajor()
//											.getContent()))));
//			ellipseType.setMajorAxis(new LengthType(GeolocHelper
//					.getLengthInMeter(
//							ellipticalArea.getDistanceUnit() == null ? null
//									: ellipticalArea.getDistanceUnit()
//											.getContent(), Double
//									.parseDouble(ellipticalArea.getSemiMinor()
//											.getContent()))));
//		} catch (Exception e) {
//		}
//
//		return ellipseType;
//	}
//
//	public static CircleByCenterPointType getCircleByCenterPointType(
//			CircularArea circularArea) {
//		if (circularArea == null)
//			return null;
//		Coord coord = circularArea.getCoord();
//
//		// Some missed values in OpenLS: interpolation
//		CircleByCenterPointType circlePointType = new CircleByCenterPointType();
//		// circlePointType.setInterpolation();
//		circlePointType.setNumArc(new BigInteger("1"));
//
//		DirectPositionType directPositionType = new DirectPositionType();
//		directPositionType.setSrsName(circularArea.getSrsName());
//
//		int dimen = 1;
//		try {
//			directPositionType.getValue().add(
//					Double.parseDouble(coord.getX().getContent()));
//		} catch (Exception e) {
//		}
//
//		if (coord.getY() != null) {
//			dimen++;
//			try {
//				directPositionType.getValue().add(
//						Double.parseDouble(coord.getY().getContent()));
//			} catch (Exception e) {
//			}
//		}
//		if (coord.getZ() != null) {
//			dimen++;
//			try {
//				directPositionType.getValue().add(
//						Double.parseDouble(coord.getZ().getContent()));
//			} catch (Exception e) {
//			}
//		}
//		directPositionType.setSrsDimension(new BigInteger(dimen + ""));
//		directPositionType.setSrsName(circularArea.getSrsName());
//		circlePointType.setPos(directPositionType);
//
//		try {
//			circlePointType.setRadius(new LengthType(GeolocHelper
//					.getLengthInMeter(
//							circularArea.getDistanceUnit() == null ? null
//									: circularArea.getDistanceUnit()
//											.getContent(), Double
//									.parseDouble(circularArea.getRadius()
//											.getContent()))));
//		} catch (Exception e) {
//		}
//
//		return circlePointType;
//	}
//
//	public static CircularArcType getCircularArcType(
//			CircularArcArea circularArcArea) {
//		if (circularArcArea == null)
//			return null;
//		Coord coord = circularArcArea.getCoord();
//
//		CircularArcType circularArcType = new CircularArcType();
//		// circlePointType.setInterpolation();
//		circularArcType.setNumArc(new BigInteger("1"));
//		circularArcType.setGid(circularArcArea.getGid());
//		circularArcType.setId(circularArcArea.getGid());
//		circularArcType.setSrsName(circularArcArea.getSrsName());
//
//		DirectPositionType directPositionType = new DirectPositionType();
//		directPositionType.setSrsName(circularArcArea.getSrsName());
//
//		int dimen = 1;
//		try {
//			directPositionType.getValue().add(
//					Double.parseDouble(coord.getX().getContent()));
//		} catch (Exception e) {
//		}
//
//		if (coord.getY() != null) {
//			dimen++;
//			try {
//				directPositionType.getValue().add(
//						Double.parseDouble(coord.getY().getContent()));
//			} catch (Exception e) {
//			}
//		}
//		if (coord.getZ() != null) {
//			dimen++;
//			try {
//				directPositionType.getValue().add(
//						Double.parseDouble(coord.getZ().getContent()));
//			} catch (Exception e) {
//			}
//		}
//		directPositionType.setSrsDimension(new BigInteger(dimen + ""));
//		directPositionType.setSrsName(circularArcArea.getSrsName());
//		circularArcType.setPos(directPositionType);
//
//		try {
//			circularArcType.setInnerRadius(new LengthType(GeolocHelper
//					.getLengthInMeter(
//							circularArcArea.getDistanceUnit() == null ? null
//									: circularArcArea.getDistanceUnit()
//											.getContent(), Double
//									.parseDouble(circularArcArea.getInRadius()
//											.getContent()))));
//			circularArcType.setOuterRadius(new LengthType(GeolocHelper
//					.getLengthInMeter(
//							circularArcArea.getDistanceUnit() == null ? null
//									: circularArcArea.getDistanceUnit()
//											.getContent(), Double
//									.parseDouble(circularArcArea.getOutRadius()
//											.getContent()))));
//		} catch (Exception e) {
//		}
//
//		try {
//			circularArcType.setStartAngle(new AngleType(GeolocHelper
//					.getAngleInDegree(
//							circularArcArea.getAngularUnit() == null ? null
//									: circularArcArea.getDistanceUnit()
//											.getContent(), Double
//									.parseDouble(circularArcArea
//											.getStartAngle().getContent()))));
//			circularArcType.setEndAngle(new AngleType(GeolocHelper
//					.getAngleInDegree(
//							circularArcArea.getAngularUnit() == null ? null
//									: circularArcArea.getDistanceUnit()
//											.getContent(), Double
//									.parseDouble(circularArcArea.getStopAngle()
//											.getContent()))));
//		} catch (Exception e) {
//		}
//
//		return circularArcType;
//	}
//
//	public static PolygonType getPolygonType(Polygon polygon) {
//		if (polygon == null)
//			return null;
//		OuterBoundaryIs outerBoundaryIs = polygon.getOuterBoundaryIs();
//		LinearRing linearRing = outerBoundaryIs.getLinearRing();
//		List<Coord> listCoords = linearRing.getCoord();
//
//		List<InnerBoundaryIs> listInnerBoundaryIs = polygon
//				.getInnerBoundaryIs();
//
//		PolygonType polygonType = new PolygonType();
//		polygonType.setGid(polygon.getGid());
//		polygonType.setId(polygon.getGid());
//		polygonType.setSrsName(polygon.getSrsName());
//
//		// Exterior
//		LinearRingType linearRingType = new LinearRingType();
//		linearRingType.setId(linearRing.getGid());
//		linearRingType.setGid(linearRing.getGid());
//		linearRingType.setSrsName(linearRing.getSrsName());
//
//		List<DirectPositionType> listDirectPositionTypes = linearRingType
//				.getPos();
//		linearRingType.setPos(listDirectPositionTypes);
//		for (Coord coord : listCoords) {
//			DirectPositionType directPositionType = new DirectPositionType();
//			directPositionType.setSrsName(linearRing.getSrsName());
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
//
//		AbstractRingPropertyType abstractRingPropertyType = new AbstractRingPropertyType();
//		JAXBElement<LinearRingType> jaxbElement = new JAXBElement<LinearRingType>(
//				new QName("http://www.opengis.net/gml", "LinearRingType"),
//				LinearRingType.class, linearRingType);
//		abstractRingPropertyType.setRing(jaxbElement);
//		polygonType.setExterior(abstractRingPropertyType);
//
//		// Interior
//		List<AbstractRingPropertyType> listInterior = polygonType.getInterior();
//		for (InnerBoundaryIs innerBoundaryIs : listInnerBoundaryIs) {
//			linearRing = innerBoundaryIs.getLinearRing();
//
//			linearRingType = new LinearRingType();
//			listDirectPositionTypes = linearRingType.getPos();
//			linearRingType.setId(linearRing.getGid());
//			linearRingType.setGid(linearRing.getGid());
//			linearRingType.setSrsName(linearRing.getSrsName());
//
//			linearRingType.setPos(listDirectPositionTypes);
//			for (Coord coord : listCoords) {
//				DirectPositionType directPositionType = new DirectPositionType();
//				directPositionType.setSrsName(linearRing.getSrsName());
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
//
//			abstractRingPropertyType = new AbstractRingPropertyType();
//			jaxbElement = new JAXBElement<LinearRingType>(new QName(
//					"http://www.opengis.net/gml", "LinearRingType"),
//					LinearRingType.class, linearRingType);
//			abstractRingPropertyType.setRing(jaxbElement);
//
//			listInterior.add(abstractRingPropertyType);
//		}
//
//		return polygonType;
//	}
//
//	public static MultiPolygonType getMultiPolygonType(MultiPolygon mPolygon) {
//		if (mPolygon == null)
//			return null;
//		List<Object> listPolygons = mPolygon.getPolygonOrBoxOrCircularArea();
//
//		MultiPolygonType mPolygonType = new MultiPolygonType();
//		mPolygonType.setId(mPolygon.getGid());
//		mPolygonType.setGid(mPolygon.getGid());
//		mPolygonType.setSrsName(mPolygon.getSrsName());
//
//		List<PolygonPropertyType> listPolygonPropertyTypes = mPolygonType
//				.getPolygonMember();
//		PolygonPropertyType polygonPropertyType;
//		for (Object o : listPolygons) {
//			if (o instanceof Polygon) {
//				Polygon polygon = (Polygon) o;
//
//				polygonPropertyType = new PolygonPropertyType();
//				polygonPropertyType.setPolygon(getPolygonType(polygon));
//				listPolygonPropertyTypes.add(polygonPropertyType);
//			} else if (o instanceof Box) {
//				Box box = (Box) o;
//
//				polygonPropertyType = new PolygonPropertyType();
//				polygonPropertyType.setPolygon(getPolygonTypeFromBox(box));
//			} else if (o instanceof CircularArea) {
//				// TODO Cannot get Polygon from CircularArea
//			} else if (o instanceof CircularArcArea) {
//				// TODO Cannot get Polygon from CircularArcArea
//			} else if (o instanceof EllipticalArea) {
//				// TODO Cannot get Polygon from EllipticalArea
//			}
//		}
//
//		return mPolygonType;
//	}
//
//	public static PolygonType getPolygonTypeFromBox(Box box) {
//		if (box == null)
//			return null;
//		List<Coord> listCoords = box.getCoord();
//
//		PolygonType polygonType = new PolygonType();
//		polygonType.setGid(box.getGid());
//		polygonType.setId(box.getGid());
//		polygonType.setSrsName(box.getSrsName());
//
//		// Exterior
//		LinearRingType linearRingType = new LinearRingType();
//		linearRingType.setId(box.getGid());
//		linearRingType.setGid(box.getGid());
//		linearRingType.setSrsName(box.getSrsName());
//
//		DirectPositionListType listDirectPositionTypes = linearRingType
//				.getPosList();
//		linearRingType.setPosList(listDirectPositionTypes);
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
//			directPositionType.setSrsDimension(new BigInteger(dimen + ""));
//			listDirectPositionTypes.add(directPositionType);
//		}
//
//		AbstractRingPropertyType abstractRingPropertyType = new AbstractRingPropertyType();
//		JAXBElement<LinearRingType> jaxbElement = new JAXBElement<LinearRingType>(
//				new QName("http://www.opengis.net/gml", "LinearRingType"),
//				LinearRingType.class, linearRingType);
//		abstractRingPropertyType.setRing(jaxbElement);
//		polygonType.setExterior(abstractRingPropertyType);
//
//		return polygonType;
//	}
//}
