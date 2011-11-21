package geomobility.core.directory.query;

import com.gisgraphy.domain.geoloc.service.ServiceException;

import geomobility.core.directory.dto.DResultsDto;
import geomobility.core.exception.GeoException;
import net.opengis.xls.v_1_1_0.DirectoryRequestType;

/**
 * The base class of {@link AttSearch} and {@link LocSearch}
 * 
 * @author VoMinhTam
 */
public abstract class DirectorySearch {
	protected DirectoryRequestType directoryRequestType;

	public abstract DResultsDto searchPOI() throws GeoException, ServiceException;
}
