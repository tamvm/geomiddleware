package geomobility.core.locutility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class QueryResultDto<T> {
	private long time;
	private List<T> result;

	public long getTime() {
		return time;
	}

	public List<T> getResult() {
		if (result == null)
			result = new ArrayList<T>();
		return result;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

}
