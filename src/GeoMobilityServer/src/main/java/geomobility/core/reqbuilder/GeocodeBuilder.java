package geomobility.core.reqbuilder;

import java.util.Map;

import net.opengis.xls.v_1_1_0.AddressType;
import net.opengis.xls.v_1_1_0.GeocodeRequestType;
import net.opengis.xls.v_1_1_0.NamedPlaceClassification;
import net.opengis.xls.v_1_1_0.NamedPlaceType;
import net.opengis.xls.v_1_1_0.StreetAddressType;
import net.opengis.xls.v_1_1_0.StreetNameType;

public class GeocodeBuilder extends RequestBuilder{
	// Singleton
	private static GeocodeBuilder i = null;
	public static GeocodeBuilder instance(){
		if (i==null)
			i = new GeocodeBuilder();
		return i;
	}
	
	public GeocodeRequestType build(Map<String, String[]> params){
		GeocodeRequestType geocodeRequestType = new GeocodeRequestType();
		
		AddressType addressType = new AddressType();
		geocodeRequestType.getAddress().add(addressType);
		
		addressType.setCountryCode(getParameter(ParamsKey.G_COUNTRYCODE, params, "VN"));
		addressType.setPostalCode(getParameter(ParamsKey.G_POSTALCODE, params, null));
		
		StreetAddressType streetAddressType = new StreetAddressType();
		addressType.setStreetAddress(streetAddressType);

		String[] streets = params.get(ParamsKey.G_STREETNAME);
		for (String street: streets){
			StreetNameType streetNameType = new StreetNameType();
			streetNameType.setValue(street);
			streetAddressType.getStreet().add(streetNameType);
		}
		
		String subdivision = getParameter(ParamsKey.G_COUNTRY_SUBDIVISION, params, null);
		NamedPlaceType namedPlaceType = new NamedPlaceType();
		namedPlaceType.setType(NamedPlaceClassification.COUNTRY_SUBDIVISION);
		namedPlaceType.setValue(subdivision);
		addressType.getPlace().add(namedPlaceType);
		
		String secondarySubdivision = getParameter(ParamsKey.G_COUNTRY_SECONDARY_SUBDIVISION, params, null);
		namedPlaceType = new NamedPlaceType();
		namedPlaceType.setType(NamedPlaceClassification.COUNTRY_SECONDARY_SUBDIVISION);
		namedPlaceType.setValue(secondarySubdivision);
		addressType.getPlace().add(namedPlaceType);
		
		return geocodeRequestType;
	}
}
