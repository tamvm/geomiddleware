package geomobility.core.directory.dto;

/**
 * @author VoMinhTam (vo.mita.ov@gmail.com)
 */
public abstract class DResultsDto {
	protected long QTime;
	
	/**
	 * @return the qTime (aka : the execution Time) in ms
	 */
	public long getQTime() {
		return QTime;
	}
	
	public abstract int getNumFound();
}
