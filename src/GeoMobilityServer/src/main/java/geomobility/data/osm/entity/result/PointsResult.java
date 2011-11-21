package geomobility.data.osm.entity.result;

import geomobility.core.utils.StaticFunc;
import geomobility.data.osm.entity.Points;
import geomobility.data.osm.entity.Roads;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class PointsResult extends Points{

	public static final String DISTANCE = "distance";
	public static final String WAY84 = "way84";

	private static final long serialVersionUID = 1L;
	@Transient
	private List<Roads> roads;
	private double distance;
	private String way84;

	public List<Roads> getRoads() {
		if (roads == null)
			roads = new ArrayList<Roads>();
		return roads;
	}

	public void setRoads(List<Roads> roads) {
		this.roads = roads;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(Double distance) {
		this.distance = distance;
	}

	/**
	 * @return the distance
	 */
	public Double getDistance() {
		return distance;
	}

	/**
	 * @return the point84
	 */
	public Point getPoint84() {
		if (!StaticFunc.isNOE(way84))
			return (Point) GeolocHelper.convertFromHEXEWKBToGeometry(way84);
		return null;
	}
}
