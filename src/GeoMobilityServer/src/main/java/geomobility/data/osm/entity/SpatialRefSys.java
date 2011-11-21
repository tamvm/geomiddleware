package geomobility.data.osm.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "spatial_ref_sys")
public class SpatialRefSys implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String SRID = "srid";
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer srid;
	private String auth_name;
	private Integer auth_srid;
	private String srtext;
	private String proj4text;

	public Integer getSrid() {
		return srid;
	}

	public String getAuth_name() {
		return auth_name;
	}

	public Integer getAuth_srid() {
		return auth_srid;
	}

	public String getSrtext() {
		return srtext;
	}

	public String getProj4text() {
		return proj4text;
	}

	public void setSrid(Integer srid) {
		this.srid = srid;
	}

	public void setAuth_name(String authName) {
		auth_name = authName;
	}

	public void setAuth_srid(Integer authSrid) {
		auth_srid = authSrid;
	}

	public void setSrtext(String srtext) {
		this.srtext = srtext;
	}

	public void setProj4text(String proj4text) {
		this.proj4text = proj4text;
	}
}
