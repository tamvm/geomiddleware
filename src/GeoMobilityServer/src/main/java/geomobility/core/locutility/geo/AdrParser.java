package geomobility.core.locutility.geo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class AdrParser {
	public static final String NUMBER = "number";
	public static final String STREET = "street";
	public static final String STATE = "state";
	public static final String ZIP = "zip";

	public static Map<String, String> parseAdr(String input) {
		String[] arr = input.split(",");
		Map<String, String> result = new HashMap<String, String>();

		if (arr.length >= 1) {
			int firstIdx = arr[0].indexOf(" ");

			String number = "";
			String address = "";
			if (firstIdx > 0) {
				number = arr[0].substring(0, firstIdx).trim();
				if (!Character.isDigit(number.charAt(0))) {
					number = "";
					firstIdx = 0;
				}
				address = arr[0].substring(firstIdx);
			} else {
				number = arr[0];
				if (!Character.isDigit(number.charAt(0))) {
					number = "";
					address = arr[0];
				} else {
					if (arr.length > 1)
						address = arr[1];
				}
			}

			result.put(NUMBER, number.trim());
			result.put(STREET, removeMaker(address));
		}

		return result;
	}

	private static String removeMaker(String adr) {
		String[] streetMarker = new String[] { "street", "st", "st.", "avenue",
				"ave", "ave.", "blvd", "blvd.", "highway", "hwy", "hwy.",
				"box", "road", "rd", "rd.", "lane", "ln", "ln.", "circle",
				"circ", "circ.", "court", "ct", "ct.", "đường", "duong" };

		String[] arr = adr.split(" ");

		for (int i = 0; i < arr.length; i++) {
			for (String s : streetMarker) {
				if (s.equals(arr[i].toLowerCase())) {
					arr[i] = null;
					break;
				}
			}
		}

		// Build
		String result = "";
		for (String a : arr) {
			if (a != null)
				result += a + " ";
		}
		return result.trim();
	}

	/**
	 * Refer from the Internet
	 * 
	 * @param input
	 * @return
	 * @deprecated
	 */
	public static HashMap<String, String> parseAddress(String input) {
		input = input.replaceAll(",", "");
		input = input.replaceAll("  ", " ");
		String[] splitString = input.split(" ");
		String[] streetMarker = new String[] { "street", "st", "st.", "avenue",
				"ave", "ave.", "blvd", "blvd.", "highway", "hwy", "hwy.",
				"box", "road", "rd", "rd.", "lane", "ln", "ln.", "circle",
				"circ", "circ.", "court", "ct", "ct.", "đường", "duong" };
		String address1 = null;
		String address2 = "";
		String city = null;
		String state = null;
		String zip = null;
		int streetMarkerIndex = 0;

		zip = splitString[splitString.length - 1].toString();
		state = splitString[splitString.length - 2].toString();
		streetMarkerIndex = getLastIndexOf(splitString, streetMarker) + 1;
		StringBuilder sb = new StringBuilder();

		for (int counter = streetMarkerIndex; counter <= splitString.length - 3; counter++) {
			sb.append(splitString[counter] + " ");
		}
		city = sb.toString().trim();
		int addressIndex = 0;

		for (int counter = 0; counter <= streetMarkerIndex; counter++) {
			if (isIntNumber(splitString[counter])
					| splitString[counter].toString().toLowerCase() == "po"
					| splitString[counter].toString().toLowerCase().replaceAll(
							".", "") == "po") {
				addressIndex = counter;
				break; // TODO: might not be correct. Was : Exit For
			}
		}

		sb = new StringBuilder();
		for (int counter = addressIndex; counter <= streetMarkerIndex - 1; counter++) {
			sb.append(splitString[counter] + " ");
		}

		address1 = sb.toString().trim();

		sb = new StringBuilder();

		if (addressIndex == 0) {
			if (splitString[splitString.length - 2].toString() != splitString[streetMarkerIndex + 1]) {
				for (int counter = streetMarkerIndex; counter <= splitString.length - 2; counter++) {
					sb.append(splitString[counter] + " ");
				}
			}
		} else {
			for (int counter = 0; counter <= addressIndex - 1; counter++) {
				sb.append(splitString[counter] + " ");
			}
		}
		address2 = sb.toString().trim();

		HashMap<String, String> output = new HashMap<String, String>();
		output.put("Address1", address1);
		output.put(address2, "Address2");
		output.put(city, "City");
		output.put(state, "State");
		output.put(zip, "Zip");
		return output;
	}

	private static int getLastIndexOf(String[] sArray, String[] checkArray) {
		int sourceIndex = 0;
		int outputIndex = 0;
		for (String item : checkArray) {
			for (String source : sArray) {
				if (source.toLowerCase() == item.toLowerCase()) {
					outputIndex = sourceIndex;
					if (item.toLowerCase() == "box") {
						outputIndex = outputIndex + 1;
					}
				}
				sourceIndex = sourceIndex + 1;
			}
			sourceIndex = 0;
		}
		return outputIndex;
	}

	public static boolean isIntNumber(String num) {
		try {
			Integer.parseInt(num);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
