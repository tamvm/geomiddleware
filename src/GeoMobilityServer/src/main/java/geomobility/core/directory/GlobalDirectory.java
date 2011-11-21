package geomobility.core.directory;

import java.util.HashMap;
import java.util.Map;

import com.gisgraphy.domain.geoloc.entity.*;
import com.gisgraphy.domain.repository.*;
import com.gisgraphy.service.IStatsUsageService;
import com.gisgraphy.service.impl.StatsUsageServiceImpl;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class GlobalDirectory {
	
	//Set from request
	public static int FROM = 0;
	public static int MAX_RESULTS = 10;

	/**
	 * A Map with GisFeature call as keys and Dao as Values. A (Gis)dao is
	 * associated to a Class
	 */
	public final static Map<Class<? extends GisFeature>, GenericGisDao> daoMap = new HashMap<Class<? extends GisFeature>, GenericGisDao>();
	static {
		daoMap.put(Airport.class, new AirportDao());
		daoMap.put(AmusePark.class, new AmuseParkDao());
		daoMap.put(Aqueduc.class, new AqueducDao());
		daoMap.put(ATM.class, new ATMDao());
		daoMap.put(Bank.class, new BankDao());
		daoMap.put(Bar.class, new BarDao());
		daoMap.put(Bay.class, new BayDao());
		daoMap.put(Beach.class, new BeachDao());
		daoMap.put(Bridge.class, new BridgeDao());
		daoMap.put(Building.class, new BuildingDao());
		daoMap.put(BusStation.class, new BusStationDao());
		daoMap.put(Camp.class, new CampDao());
		daoMap.put(Canyon.class, new CanyonDao());
		daoMap.put(Casino.class, new CasinoDao());
		daoMap.put(Castle.class, new CastleDao());
		daoMap.put(Cemetery.class, new CemeteryDao());
		daoMap.put(Cirque.class, new CirqueDao());
		daoMap.put(City.class, new CityDao());
		daoMap.put(CitySubdivision.class, new CitySubdivisionDao());
		daoMap.put(Cliff.class, new CliffDao());
		daoMap.put(Coast.class, new CoastDao());
		daoMap.put(Continent.class, new ContinentDao());
		daoMap.put(Country.class, new CountryDao());
		daoMap.put(CourtHouse.class, new CourtHouseDao());
		daoMap.put(CustomsPost.class, new CustomsPostDao());
		daoMap.put(Dam.class, new DamDao());
		daoMap.put(Desert.class, new DesertDao());
		daoMap.put(Factory.class, new FactoryDao());
		daoMap.put(Falls.class, new FallsDao());
		daoMap.put(Farm.class, new FarmDao());
		daoMap.put(Field.class, new FieldDao());
		daoMap.put(FishingArea.class, new FishingAreaDao());
		daoMap.put(Fjord.class, new FjordDao());
		daoMap.put(Forest.class, new ForestDao());
		daoMap.put(Garden.class, new GardenDao());
		daoMap.put(GisFeature.class, new GisFeatureDao());
		daoMap.put(Golf.class, new GolfDao());
		daoMap.put(Gorge.class, new GorgeDao());
		daoMap.put(GrassLand.class, new GrassLandDao());
		daoMap.put(Gulf.class, new GulfDao());
		daoMap.put(Hill.class, new HillDao());
		daoMap.put(Hospital.class, new HospitalDao());
		daoMap.put(Hotel.class, new HotelDao());
		daoMap.put(House.class, new HouseDao());
		daoMap.put(Ice.class, new IceDao());
		daoMap.put(Island.class, new IslandDao());
		daoMap.put(Lake.class, new LakeDao());
		daoMap.put(Library.class, new LibraryDao());
		daoMap.put(LightHouse.class, new LightHouseDao());
		daoMap.put(Mall.class, new MallDao());
		daoMap.put(Marsh.class, new MarshDao());
		daoMap.put(MetroStation.class, new MetroStationDao());
		daoMap.put(Military.class, new MilitaryDao());
		daoMap.put(Mill.class, new MillDao());
		daoMap.put(Mine.class, new MineDao());
		daoMap.put(Mole.class, new MoleDao());
		daoMap.put(Monument.class, new MonumentDao());
		daoMap.put(Mound.class, new MoundDao());
		daoMap.put(Mountain.class, new MountainDao());
		daoMap.put(Museum.class, new MuseumDao());
		daoMap.put(Oasis.class, new OasisDao());
		daoMap.put(ObservatoryPoint.class, new ObservatoryPointDao());
		daoMap.put(Ocean.class, new OceanDao());
		daoMap.put(OperaHouse.class, new OperaHouseDao());
		daoMap.put(Park.class, new ParkDao());
		daoMap.put(Parking.class, new ParkingDao());
		daoMap.put(Plantation.class, new PlantationDao());
		daoMap.put(PolicePost.class, new PolicePostDao());
		daoMap.put(PoliticalEntity.class, new PoliticalEntityDao());
		daoMap.put(Pond.class, new PondDao());
		daoMap.put(Port.class, new PortDao());
		daoMap.put(PostOffice.class, new PostOfficeDao());
		daoMap.put(Prison.class, new PrisonDao());
		daoMap.put(Pyramid.class, new PyramidDao());
		daoMap.put(Quay.class, new QuayDao());
		daoMap.put(Rail.class, new RailDao());
		daoMap.put(RailRoadStation.class, new RailRoadStationDao());
		daoMap.put(Ranch.class, new RanchDao());
		daoMap.put(Ravin.class, new RavinDao());
		daoMap.put(Reef.class, new ReefDao());
		daoMap.put(Religious.class, new ReligiousDao());
		daoMap.put(Reserve.class, new ReserveDao());
		daoMap.put(Restaurant.class, new RestaurantDao());
		daoMap.put(Road.class, new RoadDao());
		daoMap.put(School.class, new SchoolDao());
		daoMap.put(Sea.class, new SeaDao());
		daoMap.put(Spring.class, new SpringDao());
		daoMap.put(Stadium.class, new StadiumDao());
		daoMap.put(Strait.class, new StraitDao());
		daoMap.put(Stream.class, new StreamDao());
		daoMap.put(Street.class, new StreetDao());
		daoMap.put(Theater.class, new TheaterDao());
		daoMap.put(Tower.class, new TowerDao());
		daoMap.put(Tree.class, new TreeDao());
		daoMap.put(Tunnel.class, new TunnelDao());
		daoMap.put(UnderSea.class, new UnderSeaDao());
		daoMap.put(Vineyard.class, new VineyardDao());
		daoMap.put(Volcano.class, new VolcanoDao());
		daoMap.put(WaterBody.class, new WaterBodyDao());
		daoMap.put(Zoo.class, new ZooDao());
		daoMap.put(Adm.class, new AdmDao());
	}
}
