package geomobility.core.directory.dto;

import java.util.ArrayList;
import java.util.List;

import com.gisgraphy.domain.valueobject.GeolocResultsDto;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 * @see {@link GeolocResultsDto}
 */
public class LocSearchResultDto extends DResultsDto {

	private List<GisFeatureDistance> result;

	/**
	 * @param result
	 *            The {@link GisFeatureDistance}'s Collection
	 * @param QTime
	 *            The execution time
	 */
	public LocSearchResultDto(List<GisFeatureDistance> result, Long QTime) {
		super();
		this.result = result;
		this.QTime = QTime;
	}

	/**
	 * Default Constructor
	 */
	public LocSearchResultDto() {
		super();
	}

	/**
	 * @return The list of {@link GisFeatureDistance}
	 */
	public List<GisFeatureDistance> getResult() {
		if (result == null)
			result = new ArrayList<GisFeatureDistance>();
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
	
	public void addResult(LocSearchResultDto loc){
		getResult().addAll(loc.getResult());
		QTime += loc.QTime;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof LocSearchResultDto) {
			LocSearchResultDto another = (LocSearchResultDto) obj;
			return result.equals(another.result);
		}
		return super.equals(obj);
	}
}
