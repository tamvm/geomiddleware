package geomobility.data.osm.entity;

import geomobility.core.utils.StaticFunc;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.IntrospectionIgnoredField;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "planet_osm_roads")
public class Roads implements Serializable {
	private static final long serialVersionUID = 2333253016097685390L;
	public static final String NAME = "name";
	public static final String INTERPOLATION = "interpolation";
	public static final String WAY = "way";
	public static final String CENTROID = "centroid";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "osm_id")
	private int osm_id;

	private String access;
	// private String flats;
	// private String housenumber;
	// private String interpolation;
	private String admin_level;
	private String aerialway;
	private String aeroway;
	private String amenity;
	private String area;
	private String barrier;
	private String bicycle;
	private String bridge;
	private String boundary;
	private String building;
	private String construction;
	private String cutting;
	private String disused;
	private String embankment;
	private String foot;
	private String highway;
	private String historic;
	private String horse;
	private String junction;
	private String landuse;
	private String layer;
	private String learning;
	private String leisure;
	private String lock;
	private String man_made;
	private String military;
	private String motorcar;
	private String name;
	private String natural;
	private String oneway;
	private String operator;
	private String power;
	private String power_source;
	private String place;
	private String railway;
	private String ref;
	private String religion;
	private String residence;
	private String route;
	private String service;
	private String shop;
	private String sport;
	private String tourism;
	private String tracktype;
	private String tunnel;
	private String waterway;
	private String width;
	private String wood;
	private Integer z_order;
	private Double way_area;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	@Index(name = "planet_osm_roads_index")
	@Column(nullable = false, name = "way")
	private LineString way;

	private String countrycode;
	
	private String countryname;
	private String adm1name;
	private String adm2name;
	private String adm3name;
	private String adm4name;
	private String housenumber;
	private String street;
	private String interpolation;
	
//	@IntrospectionIgnoredField
//	@Transient
//	private String interpolateStr;

	// Not in table
	// private double distance;

	@Id
	@Column(name = "osm_id")
	public int getOsm_id() {
		return osm_id;
	}

	public void setOsm_id(int osmId) {
		osm_id = osmId;
	}

	@Column(name = "access")
	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	// @Column(name = "addr:flats")
	// public String getFlats() {
	// return flats;
	// }
	//
	// public void setFlats(String flats) {
	// this.flats = flats;
	// }
	//
	// @Column(name = "addr:housenumber")
	// public String getHousenumber() {
	// return housenumber;
	// }
	//
	// public void setHousenumber(String housenumber) {
	// this.housenumber = housenumber;
	// }

	@Column(name = "admin_level")
	public String getAdmin_level() {
		return admin_level;
	}

	public void setAdmin_level(String adminLevel) {
		admin_level = adminLevel;
	}

	@Column(name = "aerialway")
	public String getAerialway() {
		return aerialway;
	}

	public void setAerialway(String aerialway) {
		this.aerialway = aerialway;
	}

	@Column(name = "aeroway")
	public String getAeroway() {
		return aeroway;
	}

	public void setAeroway(String aeroway) {
		this.aeroway = aeroway;
	}

	@Column(name = "amenity")
	public String getAmenity() {
		return amenity;
	}

	public void setAmenity(String amenity) {
		this.amenity = amenity;
	}

	@Column(name = "area")
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Column(name = "barrier")
	public String getBarrier() {
		return barrier;
	}

	public void setBarrier(String barrier) {
		this.barrier = barrier;
	}

	@Column(name = "bicycle")
	public String getBicycle() {
		return bicycle;
	}

	public void setBicycle(String bicycle) {
		this.bicycle = bicycle;
	}

	@Column(name = "bridge")
	public String getBridge() {
		return bridge;
	}

	public void setBridge(String bridge) {
		this.bridge = bridge;
	}

	@Column(name = "boundary")
	public String getBoundary() {
		return boundary;
	}

	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}

	@Column(name = "building")
	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	@Column(name = "contruction")
	public String getConstruction() {
		return construction;
	}

	public void setConstruction(String construction) {
		this.construction = construction;
	}

	@Column(name = "cutting")
	public String getCutting() {
		return cutting;
	}

	public void setCutting(String cutting) {
		this.cutting = cutting;
	}

	@Column(name = "disused")
	public String getDisused() {
		return disused;
	}

	public void setDisused(String disused) {
		this.disused = disused;
	}

	@Column(name = "embankment")
	public String getEmbankment() {
		return embankment;
	}

	public void setEmbankment(String embankment) {
		this.embankment = embankment;
	}

	@Column(name = "foot")
	public String getFoot() {
		return foot;
	}

	public void setFoot(String foot) {
		this.foot = foot;
	}

	@Column(name = "highway")
	public String getHighway() {
		return highway;
	}

	public void setHighway(String highway) {
		this.highway = highway;
	}

	@Column(name = "historic")
	public String getHistoric() {
		return historic;
	}

	public void setHistoric(String historic) {
		this.historic = historic;
	}

	@Column(name = "horse")
	public String getHorse() {
		return horse;
	}

	public void setHorse(String horse) {
		this.horse = horse;
	}

	@Column(name = "junction")
	public String getJunction() {
		return junction;
	}

	public void setJunction(String junction) {
		this.junction = junction;
	}

	@Column(name = "landuse")
	public String getLanduse() {
		return landuse;
	}

	public void setLanduse(String landuse) {
		this.landuse = landuse;
	}

	@Column(name = "layer")
	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	@Column(name = "learning")
	public String getLearning() {
		return learning;
	}

	public void setLearning(String learning) {
		this.learning = learning;
	}

	@Column(name = "leisure")
	public String getLeisure() {
		return leisure;
	}

	public void setLeisure(String leisure) {
		this.leisure = leisure;
	}

	@Column(name = "lock")
	public String getLock() {
		return lock;
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	@Column(name = "man_made")
	public String getMan_made() {
		return man_made;
	}

	public void setMan_made(String manMade) {
		man_made = manMade;
	}

	@Column(name = "military")
	public String getMilitary() {
		return military;
	}

	public void setMilitary(String military) {
		this.military = military;
	}

	@Column(name = "motorcar")
	public String getMotorcar() {
		return motorcar;
	}

	public void setMotorcar(String motorcar) {
		this.motorcar = motorcar;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "natural")
	public String getNatural() {
		return natural;
	}

	public void setNatural(String natural) {
		this.natural = natural;
	}

	@Column(name = "oneway")
	public String getOneway() {
		return oneway;
	}

	public void setOneway(String oneway) {
		this.oneway = oneway;
	}

	@Column(name = "operator")
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "power")
	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	@Column(name = "power_source")
	public String getPower_source() {
		return power_source;
	}

	public void setPower_source(String powerSource) {
		power_source = powerSource;
	}

	@Column(name = "place")
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Column(name = "railway")
	public String getRailway() {
		return railway;
	}

	public void setRailway(String railway) {
		this.railway = railway;
	}

	@Column(name = "ref")
	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	@Column(name = "religion")
	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	@Column(name = "residence")
	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	@Column(name = "route")
	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	@Column(name = "service")
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	@Column(name = "shop")
	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	@Column(name = "sport")
	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	@Column(name = "tourism")
	public String getTourism() {
		return tourism;
	}

	public void setTourism(String tourism) {
		this.tourism = tourism;
	}

	@Column(name = "tracktype")
	public String getTracktype() {
		return tracktype;
	}

	public void setTracktype(String tracktype) {
		this.tracktype = tracktype;
	}

	@Column(name = "tunnel")
	public String getTunnel() {
		return tunnel;
	}

	public void setTunnel(String tunnel) {
		this.tunnel = tunnel;
	}

	@Column(name = "waterway")
	public String getWaterway() {
		return waterway;
	}

	public void setWaterway(String waterway) {
		this.waterway = waterway;
	}

	@Column(name = "width")
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@Column(name = "wood")
	public String getWood() {
		return wood;
	}

	public void setWood(String wood) {
		this.wood = wood;
	}

	@Column(name = "z_order")
	public Integer getZ_order() {
		return z_order;
	}

	public void setZ_order(Integer zOrder) {
		z_order = zOrder;
	}

	@Column(name = "way_area")
	public Double getWay_area() {
		return way_area;
	}

	public void setWay_area(Double wayArea) {
		way_area = wayArea;
	}

	@Type(type = "org.hibernatespatial.GeometryUserType")
	public LineString getWay() {
		return way;
	}

	public void setWay(LineString way) {
		this.way = way;
	}

	/**
	 * @param interpolation
	 *            the interpolation to set
	 */
	// public void setInterpolation(String interpolation) {
	// this.interpolation = interpolation;
	// }

	/**
	 * @return the interpolation
	 */
	// @Column(name = "addr:interpolation")
	// public String getInterpolation() {
	// return interpolation;
	// }

	/**
	 * @param countrycode
	 *            the countrycode to set
	 */
	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	/**
	 * @return the countrycode
	 */
	@Column(name = "countrycode")
	public String getCountrycode() {
		return countrycode;
	}
	
	public String getCountryname() {
		return countryname;
	}

	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}

	public String getAdm1name() {
		return adm1name;
	}

	public void setAdm1name(String adm1name) {
		this.adm1name = adm1name;
	}

	public String getAdm2name() {
		return adm2name;
	}

	public void setAdm2name(String adm2name) {
		this.adm2name = adm2name;
	}

	public String getAdm3name() {
		return adm3name;
	}

	public void setAdm3name(String adm3name) {
		this.adm3name = adm3name;
	}

	public String getAdm4name() {
		return adm4name;
	}

	public void setAdm4name(String adm4name) {
		this.adm4name = adm4name;
	}

	public String getHousenumber() {
		return housenumber;
	}

	public void setHousenumber(String housenumber) {
		this.housenumber = housenumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getInterpolation() {
		return interpolation;
	}

	public void setInterpolation(String interpolation) {
		this.interpolation = interpolation;
	}
}
