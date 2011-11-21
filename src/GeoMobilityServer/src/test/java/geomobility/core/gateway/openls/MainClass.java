package geomobility.core.gateway.openls;
//package geomobility.core.gateway.openls.main;
//
//import geomobility.core.gateway.mlp.entity.svc_result.Alt;
//import geomobility.core.gateway.mlp.entity.svc_result.AltUnc;
//import geomobility.core.gateway.mlp.entity.svc_result.Box;
//import geomobility.core.gateway.mlp.entity.svc_result.CircularArcArea;
//import geomobility.core.gateway.mlp.entity.svc_result.CircularArea;
//import geomobility.core.gateway.mlp.entity.svc_result.Coord;
//import geomobility.core.gateway.mlp.entity.svc_result.Direction;
//import geomobility.core.gateway.mlp.entity.svc_result.EllipticalArea;
//import geomobility.core.gateway.mlp.entity.svc_result.GsmNetParam;
//import geomobility.core.gateway.mlp.entity.svc_result.LevConf;
//import geomobility.core.gateway.mlp.entity.svc_result.LineString;
//import geomobility.core.gateway.mlp.entity.svc_result.LinearRing;
//import geomobility.core.gateway.mlp.entity.svc_result.Msid;
//import geomobility.core.gateway.mlp.entity.svc_result.MultiLineString;
//import geomobility.core.gateway.mlp.entity.svc_result.MultiPoint;
//import geomobility.core.gateway.mlp.entity.svc_result.MultiPolygon;
//import geomobility.core.gateway.mlp.entity.svc_result.Pd;
//import geomobility.core.gateway.mlp.entity.svc_result.Point;
//import geomobility.core.gateway.mlp.entity.svc_result.Polygon;
//import geomobility.core.gateway.mlp.entity.svc_result.Pos;
//import geomobility.core.gateway.mlp.entity.svc_result.Poserr;
//import geomobility.core.gateway.mlp.entity.svc_result.QosNotMet;
//import geomobility.core.gateway.mlp.entity.svc_result.Shape;
//import geomobility.core.gateway.mlp.entity.svc_result.Slia;
//import geomobility.core.gateway.mlp.entity.svc_result.Speed;
//import geomobility.core.gateway.mlp.entity.svc_result.Time;
//import geomobility.core.gateway.mlp.entity.svc_result.TransId;
//import geomobility.core.gateway.openls.entity.AbstractBodyType;
//import geomobility.core.gateway.openls.entity.AbstractCurveType;
//import geomobility.core.gateway.openls.entity.AbstractRequestParametersType;
//import geomobility.core.gateway.openls.entity.AbstractResponseParametersType;
//import geomobility.core.gateway.openls.entity.AbstractRingPropertyType;
//import geomobility.core.gateway.openls.entity.AbstractRingType;
//import geomobility.core.gateway.openls.entity.AbstractSurfaceType;
//import geomobility.core.gateway.openls.entity.AngleType;
//import geomobility.core.gateway.openls.entity.AngleType1;
//import geomobility.core.gateway.openls.entity.CircleByCenterPointType;
//import geomobility.core.gateway.openls.entity.CircularArcType;
//import geomobility.core.gateway.openls.entity.CurveInterpolationType;
//import geomobility.core.gateway.openls.entity.DirectPositionType;
//import geomobility.core.gateway.openls.entity.EllipseType;
//import geomobility.core.gateway.openls.entity.HorAccType;
//import geomobility.core.gateway.openls.entity.InputGatewayParametersType;
//import geomobility.core.gateway.openls.entity.InputMSIDsType;
//import geomobility.core.gateway.openls.entity.InputMSInformationType;
//import geomobility.core.gateway.openls.entity.LengthType;
//import geomobility.core.gateway.openls.entity.LinearRingType;
//import geomobility.core.gateway.openls.entity.MultiPolygonType;
//import geomobility.core.gateway.openls.entity.OutputGatewayParametersType;
//import geomobility.core.gateway.openls.entity.OutputMSIDsType;
//import geomobility.core.gateway.openls.entity.OutputMSInformationType;
//import geomobility.core.gateway.openls.entity.PointType;
//import geomobility.core.gateway.openls.entity.PolygonPropertyType;
//import geomobility.core.gateway.openls.entity.PolygonType;
//import geomobility.core.gateway.openls.entity.PositionType;
//import geomobility.core.gateway.openls.entity.QualityOfPositionType;
//import geomobility.core.gateway.openls.entity.RequestType;
//import geomobility.core.gateway.openls.entity.ResponseType;
//import geomobility.core.gateway.openls.entity.SLIAType;
//import geomobility.core.gateway.openls.entity.SLIRType;
//import geomobility.core.gateway.openls.entity.SpeedType;
//import geomobility.core.gateway.openls.entity.SpeedUnitType;
//import geomobility.core.gateway.openls.entity.TimeType;
//import geomobility.core.gateway.openls.entity.VerAccType;
//import geomobility.core.gateway.openls.entity.XLSType;
//
//import java.io.File;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.Locale;
//import java.util.TimeZone;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBElement;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
//import javax.xml.datatype.Duration;
//import javax.xml.datatype.XMLGregorianCalendar;
//import javax.xml.datatype.DatatypeConstants.Field;
//import javax.xml.namespace.QName;
//
//public class MainClass {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		try {
//			JAXBContext jc = JAXBContext
//					.newInstance("geomobility.core.gateway.openls.entity");
//			Unmarshaller u = jc.createUnmarshaller();
//			File f = new File("data/slia-openls.xml");
//			JAXBElement element = (JAXBElement) u.unmarshal(f);
//
//			XLSType item = (XLSType) element.getValue();
//			List<JAXBElement<? extends AbstractBodyType>> listRT = item
//					.getBody();
//			int sizeBody = listRT.size();
//
//			// TODO check type of element
//			for (int i = 0; i < sizeBody; i++) {
//
//			}
//			// TODO if element i is RequestType
//			JAXBElement<RequestType> reqType = (JAXBElement<RequestType>) listRT
//					.get(0);
//			excuteRequestType(reqType);
//			/*
//			 * //TODO else .... neu kieu khac thi ep kieu lai
//			 * JAXBElement<ResponseType> respType = (JAXBElement<ResponseType>)
//			 * listRT .get(0);
//			 * 
//			 * Slia slia = new Slia(); excuteResponseType(respType,slia);
//			 */
//			System.out.println();
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void excuteResponseType(JAXBElement<ResponseType> respType,
//			Slia slia) {
//		Class<ResponseType> declaredType = respType.getDeclaredType();
//		// TODO cac thuoc tinh cua SLIAType
//		QName name = respType.getName();
//		boolean nil = respType.isNil();
//		Class scope = respType.getScope();
//		ResponseType value = respType.getValue();
//
//		JAXBElement<? extends AbstractResponseParametersType> responseParameters = value
//				.getResponseParameters();
//
//		SLIAType sliaType = (SLIAType) responseParameters.getValue();
//		OutputGatewayParametersType outputGatewayParameters = sliaType
//				.getOutputGatewayParameters();
//
//		String locationType = outputGatewayParameters.getLocationType();
//		OutputMSIDsType outputMSIDs = outputGatewayParameters.getOutputMSIDs();
//		String priority = outputGatewayParameters.getPriority();
//		QualityOfPositionType requestQoP = outputGatewayParameters
//				.getRequestedQoP();
//		String requestedsrsName = outputGatewayParameters.getRequestedsrsName();
//		OutputMSInformationType outputMSInformation = outputMSIDs
//				.getOutputMSInformation();
//		String session = outputMSIDs.getSession();
//
//		String encryption = outputMSInformation.getEncryption();
//		String msIDType = outputMSInformation.getMsIDType();
//		String msIDValue = outputMSInformation.getMsIDValue();
//		PositionType position = outputMSInformation.getPosition();
//
//		position.getCircleByCenterPoint();
//		position.getCircularArc();
//		position.getDirection();
//		position.getEllipse();
//		position.getLevelOfConf();
//		position.getMultiPolygon();
//		position.getPoint();
//		position.getPolygon();
//		position.getQoP();
//		position.getSpeed();
//		position.getTime();
//		//
//		// TODO phan nay la vi tri kieu SLIA lay duoc gia tri
//		Pos positionSlia = slia.getPos().get(0);
//
//		positionSlia.getGsmNetParam();
//		positionSlia.getMsid();
//		Pd pd = positionSlia.getPd();
//		positionSlia.getPoserr();
//		positionSlia.getTransId();
//
//		try {
//			String namespaceURI = "";
//			String localPart = "";
//			String prefix = "";
//			// TODO set QName
//			QName name1 = new QName(namespaceURI, localPart, prefix);
//
//			Class<ResponseType> declaredType1;
//			declaredType1 = (Class<ResponseType>) Class
//					.forName("geomobility.core.gateway.openls.entity.ResponseType");
//			ResponseType valueResponseType = new ResponseType();
//			setValueResponseType_ResponseType(valueResponseType, slia);
//
//			JAXBElement<ResponseType> response = new JAXBElement<ResponseType>(
//					name1, declaredType1, valueResponseType);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		// ...
//	}
//
//	private static void setValueResponseType_ResponseType(
//			ResponseType valueResponseType, Slia slia) {
//		try {
//			BigInteger numberOfRes = new BigInteger("1");
//			String requestID = "";
//			String version = "1.1";
//
//			Class<SLIAType> declaredType2 = (Class<SLIAType>) Class
//					.forName("geomobility.core.gateway.openls.entity.SLIAType");
//
//			OutputGatewayParametersType outputGatewayParameter1 = new OutputGatewayParametersType();
//			setOutputGatewayParameter1_valueSlia_responseParameters1(
//					outputGatewayParameter1, slia);
//			// TODO name2 QName ???
//			String namespaceURI = "";
//			String localPart = "";
//			String prefix = "";
//			QName name2 = new QName(namespaceURI, localPart, prefix);
//			// TODO valueSlia
//			SLIAType valueSlia = new SLIAType();
//			valueSlia.setOutputGatewayParameters(outputGatewayParameter1);
//			JAXBElement<SLIAType> responseParameters1 = new JAXBElement<SLIAType>(
//					name2, declaredType2, valueSlia);
//
//			valueResponseType.setNumberOfResponses(numberOfRes);
//			valueResponseType.setRequestID(requestID);
//			valueResponseType.setVersion(version);
//			valueResponseType.setResponseParameters(responseParameters1);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	private static void setOutputGatewayParameter1_valueSlia_responseParameters1(
//			OutputGatewayParametersType outputGatewayParameter1, Slia slia) {
//		// TODO locationType1????
//
//		OutputMSIDsType outputMSIDs1 = new OutputMSIDsType();
//		setOutputMSIDs1_outputGatewayParameter1(outputMSIDs1, slia);
//		// TODO set priority1 ???
//		String priority1 = "";
//		// TODO requestQoP1 ???
//		QualityOfPositionType requestQoP1 = new QualityOfPositionType();
//		HorAccType horizontalAcc = new HorAccType();
//		AngleType1 angle = new AngleType1();
//		horizontalAcc.setAngle(angle);
//		requestQoP1.setHorizontalAcc(horizontalAcc);
//		// TODO requestedsrsName1
//		String requestedsrsName1 = "";
//
//		String locationType1 = "";
//		outputGatewayParameter1.setLocationType(locationType1);
//		outputGatewayParameter1.setOutputMSIDs(outputMSIDs1);
//		outputGatewayParameter1.setPriority(priority1);
//		outputGatewayParameter1.setRequestedQoP(requestQoP1);
//		outputGatewayParameter1.setRequestedsrsName(requestedsrsName1);
//	}
//
//	private static void setOutputMSIDs1_outputGatewayParameter1(
//			OutputMSIDsType outputMSIDs1, Slia slia) {
//		Pos posZero = slia.getPos().get(0);
//
//		Msid msid = posZero.getMsid();
//
//		OutputMSInformationType outputMSInformation1 = new OutputMSInformationType();
//		// TODO encryption1
//		String encrypttion1 = posZero.getMsid().getEnc();
//		String msIDType1 = posZero.getMsid().getType();
//		String msIDValue1 = posZero.getMsid().getContent();
//
//		outputMSInformation1.setEncryption(encrypttion1);
//		outputMSInformation1.setMsIDType(msIDType1);
//		outputMSInformation1.setMsIDValue(msIDValue1);
//
//		PositionType position1 = new PositionType();
//		setPosition_OutputMSInformation1_OutputMSIDs1(position1, slia);
//		outputMSInformation1.setPosition(position1);
//		outputMSIDs1.setOutputMSInformation(outputMSInformation1);
//		// TODO set session
//		String session1 = "";
//		outputMSIDs1.setSession(session1);
//	}
//
//	private static void setPosition_OutputMSInformation1_OutputMSIDs1(
//			PositionType position1, Slia slia) {
//		// TODO
//
//		// MLP
//		Pos posZero = slia.getPos().get(0);
//
//		// Msid msid = posZero.getMsid(); // not do here
//		Pd pd = posZero.getPd();
//		GsmNetParam gsmNetParam = posZero.getGsmNetParam();
//		Poserr poserr = posZero.getPoserr();
//		String posMethod = posZero.getPosMethod();
//		TransId transId = posZero.getTransId();
//
//		gsmNetParam.getCgi().getCellid().getContent();
//		gsmNetParam.getCgi().getLac().getContent();
//		gsmNetParam.getCgi().getMcc().getContent();
//		gsmNetParam.getCgi().getMnc().getContent();
//		gsmNetParam.getImsi().getContent();
//		gsmNetParam.getLmsi().getContent();
//		gsmNetParam.getNeid().getVlrid().getCc().getContent();
//		gsmNetParam.getNeid().getVlrid().getNdc().getContent();
//		gsmNetParam.getNeid().getVlrid().getVlrno().getContent();
//		gsmNetParam.getNeid().getVmscid().getCc().getContent();
//		gsmNetParam.getNeid().getVmscid().getNdc().getContent();
//		gsmNetParam.getNeid().getVmscid().getVmscno().getContent();
//		gsmNetParam.getNmr().getContent();
//		gsmNetParam.getTa().getContent();
//
//		transId.getContent();
//		// TODO pd
//		Shape shape = pd.getShape();
//		Time time = pd.getTime();
//		Alt alt = pd.getAlt();
//		AltUnc altUnc = pd.getAltUnc();
//		Direction direction = pd.getDirection();
//		LevConf levConf = pd.getLevConf();
//		QosNotMet qosNotMet = pd.getQosNotMet();
//		Speed speed = pd.getSpeed();
//
//		Box box = shape.getBox();
//		List<Coord> listCoordBox = box.getCoord();
//		box.getCoord().get(0);
//		box.getGid();
//		box.getSrsName();
//		CircularArcArea circularArcArea = shape.getCircularArcArea();
//		circularArcArea.getAngularUnit();
//		circularArcArea.getCoord();// .getX() .getY() . getZ();
//		circularArcArea.getDistanceUnit(); // .getContent();
//		circularArcArea.getGid(); // String
//		circularArcArea.getInRadius();// .getContent()
//		circularArcArea.getOutRadius();// .getContent();
//		circularArcArea.getSrsName();// String
//		circularArcArea.getStartAngle();// .getContent();
//		circularArcArea.getStopAngle();// .getContent();
//		CircularArea circularArea = shape.getCircularArea();
//		circularArea.getCoord();// .getX() .getY() . getZ();
//		circularArea.getDistanceUnit();// .getContent();
//		circularArea.getGid(); // String
//		circularArea.getRadius();// .getContent();
//		circularArea.getSrsName();// String
//		EllipticalArea ellipticalArea = shape.getEllipticalArea();
//		ellipticalArea.getAngle();// .getContent()
//		ellipticalArea.getAngularUnit();// .getContent()
//		ellipticalArea.getCoord(); // get XYZ
//		ellipticalArea.getDistanceUnit(); // .getContent();
//		ellipticalArea.getGid(); // String
//		ellipticalArea.getSemiMajor(); // getContent();
//		ellipticalArea.getSemiMinor(); // getContent();
//		ellipticalArea.getSemiMajor(); // getContent();
//		LinearRing linearRing = shape.getLinearRing();
//		linearRing.getCoord(); // listCoord
//		linearRing.getGid(); // String
//		linearRing.getSrsName();// String
//		LineString lineString = shape.getLineString();
//		lineString.getCoord(); // listCoord
//		lineString.getGid(); // String
//		lineString.getSrsName();// String
//		MultiLineString multiLineString = shape.getMultiLineString();
//		multiLineString.getGid();// String
//		multiLineString.getLineString();// listLineString
//		multiLineString.getSrsName(); // String
//		MultiPoint multiPoint = shape.getMultiPoint();
//		multiPoint.getGid(); // String
//		multiPoint.getPoint();// listPoint
//		multiPoint.getSrsName();// String
//		MultiPolygon multiPolygon = shape.getMultiPolygon();
//		multiPolygon.getGid();// String
//		multiPolygon.getPolygonOrBoxOrCircularArea();
//		multiPolygon.getSrsName();
//		Point point = shape.getPoint();
//		point.getCoord();
//		point.getGid();
//		point.getSrsName();
//		Polygon polygon = shape.getPolygon();
//		polygon.getGid();
//		polygon.getInnerBoundaryIs();// .get(0).getLinearRing();
//		polygon.getOuterBoundaryIs();// .getLinearRing();
//		polygon.getSrsName();
//
//		alt.getContent();
//
//		altUnc.getContent();
//
//		direction.getContent();
//
//		levConf.getContent();
//
//		// OpenLS
//		CircleByCenterPointType circleByCenterPoint = new CircleByCenterPointType();
//		CircularArcType circularArcType = new CircularArcType();
//		AngleType1 angleType1 = new AngleType1();
//		EllipseType ellipse = new EllipseType();
//		String levelOfConf = "";
//		MultiPolygonType multiPolygonType = new MultiPolygonType();
//		PointType pointType = new PointType();
//		PolygonType polygonType = new PolygonType();
//		QualityOfPositionType QoP = new QualityOfPositionType();
//		SpeedType speedType = new SpeedType();
//		TimeType timeType = new TimeType();
//		// TODO circleByCenterPoint
//		AngleType endAngle_circleByCenterPoint = new AngleType();
//		endAngle_circleByCenterPoint.setUom("");
//		endAngle_circleByCenterPoint.setValue(0);
//		circleByCenterPoint.setEndAngle(endAngle_circleByCenterPoint); // TODO
//		CurveInterpolationType curveInterpolationType = CurveInterpolationType
//				.fromValue("");
//		circleByCenterPoint.setInterpolation(curveInterpolationType); // TODO
//																		// ????
//		circleByCenterPoint.setNumArc(new BigInteger(""));
//		DirectPositionType directPositionType = new DirectPositionType();
//		directPositionType.setDimension(new BigInteger(""));
//		directPositionType.setSrsName("");
//		List<Double> listDouble = new ArrayList<Double>();
//		directPositionType.setValue(listDouble);
//		circleByCenterPoint.setPos(directPositionType);
//		LengthType lengthType_circleByCenterPoint = new LengthType();
//		lengthType_circleByCenterPoint.setUom("");
//		lengthType_circleByCenterPoint.setValue(0);
//		circleByCenterPoint.setRadius(lengthType_circleByCenterPoint);
//		AngleType startAngle_circleByCenterPoint = new AngleType();
//		startAngle_circleByCenterPoint.setUom("");
//		startAngle_circleByCenterPoint.setValue(0);
//		circleByCenterPoint.setStartAngle(startAngle_circleByCenterPoint);
//		// TODO circularArcType
//		AngleType endAngle_circularArcType = new AngleType();
//		endAngle_circularArcType.setUom("");
//		endAngle_circularArcType.setValue(0);
//		circularArcType.setEndAngle(endAngle_circularArcType);
//		circularArcType.setGid("");
//		circularArcType.setId("id");
//		LengthType lengthType_innerRadius_circularArcType = new LengthType();
//		lengthType_innerRadius_circularArcType.setUom("");
//		lengthType_innerRadius_circularArcType.setValue(0);
//		circularArcType.setInnerRadius(lengthType_innerRadius_circularArcType);
//
//		circularArcType.setInterpolation(CurveInterpolationType
//				.fromValue("????")); // TODO ????
//		circularArcType.setNumArc(new BigInteger("0")); // TODO num
//		LengthType lengthType_outerRadius_circularArcType = new LengthType();
//		lengthType_outerRadius_circularArcType.setUom("");
//		lengthType_outerRadius_circularArcType.setValue(0);
//		circularArcType.setOuterRadius(lengthType_outerRadius_circularArcType);
//		circularArcType.setPos(new DirectPositionType());
//		circularArcType.setSrsName("srsname");
//		circularArcType.setStartAngle(new AngleType());
//		// TODO angleType1
//		angleType1.setAccuracy(new BigDecimal(""));// TODO
//		angleType1.setUom("");
//		angleType1.setValue(new BigDecimal(""));
//		// TODO ellipse
//		ellipse.setGid("");
//		ellipse.setId("");
//		ellipse.setMajorAxis(new LengthType());
//		ellipse.setMinorAxis(new LengthType());
//		ellipse.setPos(new DirectPositionType());
//		ellipse.setRotation(new AngleType());
//		ellipse.setSrsName("");
//		// TODO levelOfConf
//		levelOfConf = levConf.getContent();
//		// TODO multiPolygonType
//		PolygonPropertyType polygonPropertyType = new PolygonPropertyType();
//		multiPolygonType.getPolygonMember().add(polygonPropertyType);
//		polygonPropertyType.setPolygon(polygonType);
//
//		multiPolygonType.setId("");
//		multiPolygonType.setSrsName(multiPolygon.getSrsName());
//		multiPolygonType.setGid(multiPolygon.getGid());
//		// TODO pointType
//		DirectPositionType pos = new DirectPositionType();
//		List<Double> coordList = new ArrayList<Double>();
//		Double x = Double.parseDouble(circularArea.getCoord().getX()
//				.getContent());
//		Double y = Double.parseDouble(circularArea.getCoord().getY()
//				.getContent());
//		coordList.set(0, x);
//		coordList.set(0, y);
//		pos.setValue(coordList);
//		pos.setSrsName("");
//		pos.setDimension(new BigInteger("2"));
//		pointType.setPos(pos);
//
//		pointType.setGid("");
//		pointType.setId("");
//		pointType.setSrsName("");
//		// TODO polygonType
//		AbstractRingPropertyType abstractRingPropertyType = new AbstractRingPropertyType();
//		polygonType.setExterior(abstractRingPropertyType);// abstractRingPropertyType.setRing(value)
//
//		polygonType.setGid("");
//		polygonType.setId("");
//		polygonType.setSrsName("");
//		// TODO QoP
//		QoP.setHorizontalAcc(new HorAccType()); // new
//												// HorAccType().setAngle(value)
//												// new
//												// HorAccType().setDistance(value)
//		QoP.setResponseReq("");
//		QoP.setResponseTimer("");
//		QoP.setVerticalAcc(new VerAccType()); // new VerAccType().setDistance()
//		// TODO speedType
//		try {
//			speedType.setValue(new BigDecimal(speed.getContent()));
//
//			speedType.setAccuracy(new BigDecimal(""));
//			speedType.setUom(SpeedUnitType.fromValue(""));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		// TODO timeType
//		try {
//			timeType.setUtcOffset(new BigInteger(time.getUtcOff()));
//
//			// timeType.setBegin(new XMLGregorianCalendar)
//			// timeType.setDuration(new Duration)
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		position1.setCircleByCenterPoint(circleByCenterPoint);
//		position1.setCircularArc(circularArcType);
//		position1.setDirection(angleType1);
//		position1.setEllipse(ellipse);
//		position1.setLevelOfConf(levelOfConf);
//		position1.setMultiPolygon(multiPolygonType);
//		position1.setPoint(pointType);
//		position1.setPolygon(polygonType);
//		position1.setQoP(QoP);
//		position1.setSpeed(speedType);
//		position1.setTime(timeType);
//	}
//
//	private static void excuteRequestType(JAXBElement<RequestType> reqType) {
//		Class<RequestType> declaredType = reqType.getDeclaredType();
//		QName name = reqType.getName();
//		// QName nameQ = new QName(namespaceURI, localPart, prefix)
//		boolean nil = reqType.isNil();
//		Class scope = reqType.getScope();
//		RequestType value = reqType.getValue();
//		// declaredType
//		// name
//		// nil
//		// scope
//		// value
//		BigInteger maxinumResponses = value.getMaximumResponses();
//		String methodName = value.getMethodName();
//		String requestID = value.getRequestID();
//		JAXBElement<? extends AbstractRequestParametersType> requestParameters = value
//				.getRequestParameters();
//		String version = value.getVersion();
//
//		// TODO requestParameters
//		Class<? extends AbstractRequestParametersType> declaredTypeOfValueRequestParameters = requestParameters
//				.getDeclaredType();
//		// TODO if declaredTypeOfValueRequestParameters equal SLIRType
//		SLIRType slirType = (SLIRType) requestParameters.getValue();
//		excuteRequestParameters_SLIR_RequestType(slirType);
//		// TODO else ....
//
//	}
//
//	private static void excuteRequestParameters_SLIR_RequestType(
//			SLIRType slirType) {
//		// TODO
//		InputGatewayParametersType inputGatewayParameters = slirType
//				.getInputGatewayParameters();
//		InputMSIDsType inputMSIDs = inputGatewayParameters.getInputMSIDs();
//		String locationType = inputGatewayParameters.getLocationType();
//		String priority = inputGatewayParameters.getPriority();
//		QualityOfPositionType requestedQoP = inputGatewayParameters
//				.getRequestedQoP();
//		String requestedsrsName = inputGatewayParameters.getRequestedsrsName();
//
//		InputMSInformationType inputMSInfomation = inputMSIDs
//				.getInputMSInformation();
//		String session = inputMSIDs.getSession();
//
//		String encryption = inputMSInfomation.getEncryption();
//		String msIDType = inputMSInfomation.getMsIDType();
//		String msIDValue = inputMSInfomation.getMsIDValue();
//	}
//
//}
