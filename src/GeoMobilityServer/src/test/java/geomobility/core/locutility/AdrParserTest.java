package geomobility.core.locutility;

import geomobility.core.locutility.geo.AdrParser;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class AdrParserTest {
	public static void main(String[] args) {

	}

	@Test
	public void test() {
		String result = AdrParser.parseAdr(
				"1600, Pennsylvania Ave NW, Washington DC 20502").toString();
		Assert.assertEquals("{street=Pennsylvania NW, number=1600}", result);
		System.out.println("1:" + result);

		result = AdrParser.parseAdr(
				"1600 Pennsylvania Ave NW, Washington DC 20502").toString();
		Assert.assertEquals("{street=Pennsylvania NW, number=1600}", result);
		System.out.println("2:" + result);

		result = AdrParser.parseAdr("Pennsylvania NW, Washington DC 20502")
				.toString();
		Assert.assertEquals("{street=Pennsylvania NW, number=}", result);
		System.out.println("3:" + result);

		result = AdrParser.parseAdr("1 Freedom Rd").toString();
		Assert.assertEquals("{street=Freedom, number=1}", result);
		System.out.println("4:" + result);
	}
}
