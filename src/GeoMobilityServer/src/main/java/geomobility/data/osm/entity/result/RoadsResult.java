package geomobility.data.osm.entity.result;

import geomobility.core.utils.StaticFunc;
import geomobility.data.osm.entity.Roads;

import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class RoadsResult extends Roads {
	private static final long serialVersionUID = 2333253016097685390L;

	private String interpolateStr;

	/**
	 * @param interpolateStr
	 *            the interpolateStr to set
	 */
	public void setInterpolateStr(String interpolateStr) {
		this.interpolateStr = interpolateStr;
	}

	/**
	 * @return the interpolateStr
	 */
	public String getInterpolateStr() {
		return interpolateStr;
	}

	public Point getInterpolatePoint() {
		if (!StaticFunc.isNOE(interpolateStr))
			return (Point) GeolocHelper.convertFromHEXEWKBToGeometry(interpolateStr);
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(GeolocHelper.convertFromHEXEWKBToGeometry("POINT(103.841109187497 22.3354264894544)"));
	}

}
