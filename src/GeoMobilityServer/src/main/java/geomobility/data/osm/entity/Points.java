package geomobility.data.osm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "planet_osm_point")
public class Points implements Serializable {
	public static final String WAY = "way";

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "osm_id")
	private Integer osm_id;

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
	private String capital;
	private String construction;
	private String cutting;
	private String disused;
	private String ele;
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
	private String text;
	private String poi;
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
	private String tunnel;
	private String waterway;
	private String width;
	private String wood;
	private Integer z_order;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	@Index(name = "planet_osm_point_index")
	@Column(nullable = false, name = "way")
	private Point way;

	@Column(name = "countrycode")
	private String countrycode;
	
	private String countryname;
	private String adm1name;
	private String adm2name;
	private String adm3name;
	private String adm4name;
	private String housenumber;
	private String street;
	private String interpolation;

	@Column(name = "osm_id")
	public Integer getOsm_id() {
		return osm_id;
	}

	@Column(name = "admin_level")
	public String getAdmin_level() {
		return admin_level;
	}

	@Column(name = "aerialway")
	public String getAerialway() {
		return aerialway;
	}

	@Column(name = "aeroway")
	public String getAeroway() {
		return aeroway;
	}

	@Column(name = "amenity")
	public String getAmenity() {
		return amenity;
	}

	@Column(name = "area")
	public String getArea() {
		return area;
	}

	@Column(name = "barrier")
	public String getBarrier() {
		return barrier;
	}

	@Column(name = "bicycle")
	public String getBicycle() {
		return bicycle;
	}

	@Column(name = "bridge")
	public String getBridge() {
		return bridge;
	}

	@Column(name = "boundary")
	public String getBoundary() {
		return boundary;
	}

	@Column(name = "building")
	public String getBuilding() {
		return building;
	}

	@Column(name = "capital")
	public String getCapital() {
		return capital;
	}

	@Column(name = "construction")
	public String getConstruction() {
		return construction;
	}

	@Column(name = "cutting")
	public String getCutting() {
		return cutting;
	}

	@Column(name = "disused")
	public String getDisused() {
		return disused;
	}

	@Column(name = "ele")
	public String getEle() {
		return ele;
	}

	@Column(name = "embankment")
	public String getEmbankment() {
		return embankment;
	}

	@Column(name = "foot")
	public String getFoot() {
		return foot;
	}

	@Column(name = "highway")
	public String getHighway() {
		return highway;
	}

	@Column(name = "historic")
	public String getHistoric() {
		return historic;
	}

	@Column(name = "horse")
	public String getHorse() {
		return horse;
	}

	@Column(name = "junction")
	public String getJunction() {
		return junction;
	}

	@Column(name = "landuse")
	public String getLanduse() {
		return landuse;
	}

	@Column(name = "layer")
	public String getLayer() {
		return layer;
	}

	@Column(name = "learning")
	public String getLearning() {
		return learning;
	}

	@Column(name = "leisure")
	public String getLeisure() {
		return leisure;
	}

	@Column(name = "lock")
	public String getLock() {
		return lock;
	}

	@Column(name = "man_made")
	public String getMan_made() {
		return man_made;
	}

	@Column(name = "military")
	public String getMilitary() {
		return military;
	}

	@Column(name = "motorcar")
	public String getMotorcar() {
		return motorcar;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	@Column(name = "natural")
	public String getNatural() {
		return natural;
	}

	@Column(name = "oneway")
	public String getOneway() {
		return oneway;
	}

	@Column(name = "text")
	public String getText() {
		return text;
	}

	@Column(name = "poi")
	public String getPoi() {
		return poi;
	}

	@Column(name = "power")
	public String getPower() {
		return power;
	}

	@Column(name = "power_source")
	public String getPower_source() {
		return power_source;
	}

	@Column(name = "place")
	public String getPlace() {
		return place;
	}

	@Column(name = "railway")
	public String getRailway() {
		return railway;
	}

	@Column(name = "ref")
	public String getRef() {
		return ref;
	}

	@Column(name = "religion")
	public String getReligion() {
		return religion;
	}

	@Column(name = "residence")
	public String getResidence() {
		return residence;
	}

	@Column(name = "route")
	public String getRoute() {
		return route;
	}

	@Column(name = "service")
	public String getService() {
		return service;
	}

	@Column(name = "shop")
	public String getShop() {
		return shop;
	}

	@Column(name = "sport")
	public String getSport() {
		return sport;
	}

	@Column(name = "tourism")
	public String getTourism() {
		return tourism;
	}

	@Column(name = "tunnel")
	public String getTunnel() {
		return tunnel;
	}

	@Column(name = "waterway")
	public String getWaterway() {
		return waterway;
	}

	@Column(name = "width")
	public String getWidth() {
		return width;
	}

	@Column(name = "wood")
	public String getWood() {
		return wood;
	}

	@Column(name = "z_order")
	public Integer getZ_order() {
		return z_order;
	}

	public Point getWay() {
		return way;
	}

	public void setOsm_id(Integer osmId) {
		osm_id = osmId;
	}

	public void setAdmin_level(String adminLevel) {
		admin_level = adminLevel;
	}

	public void setAerialway(String aerialway) {
		this.aerialway = aerialway;
	}

	public void setAeroway(String aeroway) {
		this.aeroway = aeroway;
	}

	public void setAmenity(String amenity) {
		this.amenity = amenity;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setBarrier(String barrier) {
		this.barrier = barrier;
	}

	public void setBicycle(String bicycle) {
		this.bicycle = bicycle;
	}

	public void setBridge(String bridge) {
		this.bridge = bridge;
	}

	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public void setConstruction(String construction) {
		this.construction = construction;
	}

	public void setCutting(String cutting) {
		this.cutting = cutting;
	}

	public void setDisused(String disused) {
		this.disused = disused;
	}

	public void setEle(String ele) {
		this.ele = ele;
	}

	public void setEmbankment(String embankment) {
		this.embankment = embankment;
	}

	public void setFoot(String foot) {
		this.foot = foot;
	}

	public void setHighway(String highway) {
		this.highway = highway;
	}

	public void setHistoric(String historic) {
		this.historic = historic;
	}

	public void setHorse(String horse) {
		this.horse = horse;
	}

	public void setJunction(String junction) {
		this.junction = junction;
	}

	public void setLanduse(String landuse) {
		this.landuse = landuse;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public void setLearning(String learning) {
		this.learning = learning;
	}

	public void setLeisure(String leisure) {
		this.leisure = leisure;
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	public void setMan_made(String manMade) {
		man_made = manMade;
	}

	public void setMilitary(String military) {
		this.military = military;
	}

	public void setMotorcar(String motorcar) {
		this.motorcar = motorcar;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNatural(String natural) {
		this.natural = natural;
	}

	public void setOneway(String oneway) {
		this.oneway = oneway;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setPoi(String poi) {
		this.poi = poi;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public void setPower_source(String powerSource) {
		power_source = powerSource;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public void setRailway(String railway) {
		this.railway = railway;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public void setTourism(String tourism) {
		this.tourism = tourism;
	}

	public void setTunnel(String tunnel) {
		this.tunnel = tunnel;
	}

	public void setWaterway(String waterway) {
		this.waterway = waterway;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setWood(String wood) {
		this.wood = wood;
	}

	public void setZ_order(Integer zOrder) {
		z_order = zOrder;
	}

	public void setWay(Point way) {
		this.way = way;
	}

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

	// /**
	// * @param distance
	// * the distance to set
	// */
	// public void setDistance(double distance) {
	// this.distance = distance;
	// }
	//
	// /**
	// * @return the distance
	// */
	// public double getDistance() {
	// return distance;
	// }
	//
	// /**
	// * @param roads
	// * the roads to set
	// */
	// public void setRoads(Roads roads) {
	// this.roads = roads;
	// }
	//
	// /**
	// * @return the roads
	// */
	// public Roads getRoads() {
	// return roads;
	// }
}
