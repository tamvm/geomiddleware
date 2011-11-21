package geomobility.core.directory.dto;

import java.util.ArrayList;
import java.util.List;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class AttSearchResultDto extends DResultsDto {
	protected Long QTime = null;
	protected List<GisFeature> result;

	public AttSearchResultDto(List<GisFeature> r, Long time) {
		this.result = r;
		this.QTime = time;
	}

	/**
	 * Default Constructor
	 */
	public AttSearchResultDto() {
	}

	/**
	 * @return The list of {@link GisFeatureDistance}
	 */
	public List<GisFeature> getResult() {
		if (result == null)
			result = new ArrayList<GisFeature>();
		return result;
	}

	/**
	 * @return the numFound
	 */
	public int getNumFound() {
		return (result == null) ? 0 : result.size();
	}

	/**
	 * @return the qTime (aka : the execution Time) in ms
	 */
	public long getQTime() {
		return QTime;
	}

	public void addAttSearchResultDtd(AttSearchResultDto dto) {
		getResult().addAll(dto.getResult());
		QTime += dto.getQTime();
	}
}
