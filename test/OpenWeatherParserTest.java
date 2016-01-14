import org.junit.Assert;
import org.junit.Test;


public class OpenWeatherParserTest {

	@Test
	public void analyseCorrectWindDirection(){
		Assert.assertEquals("Nord (N - 355.45°)", OpenWeatherParser.analyseWindDirection(355.45f));
		Assert.assertEquals("Nord-Nordost (NNO - 15.45°)", OpenWeatherParser.analyseWindDirection(15.45f));
		Assert.assertEquals("Nord-Nordost (NNO - 32.45°)", OpenWeatherParser.analyseWindDirection(32.45f));
		Assert.assertEquals("Nordost (NO - 52.45°)", OpenWeatherParser.analyseWindDirection(52.45f));
		Assert.assertEquals("Ost-Nordost (ONO - 76.45°)", OpenWeatherParser.analyseWindDirection(76.45f));
		Assert.assertEquals("Ost (O - 100.0°)", OpenWeatherParser.analyseWindDirection(100.0f));
		Assert.assertEquals("Ost-Südost (OSO - 112.0°)", OpenWeatherParser.analyseWindDirection(112.0f));
		Assert.assertEquals("Südost (SO - 133.1°)", OpenWeatherParser.analyseWindDirection(133.1f));
		Assert.assertEquals("Süd-Südost (SSO - 147.31°)", OpenWeatherParser.analyseWindDirection(147.31f));
		Assert.assertEquals("Süd (S - 187.31°)", OpenWeatherParser.analyseWindDirection(187.31f));
		Assert.assertEquals("Süd-Südwest (SSW - 199.67°)", OpenWeatherParser.analyseWindDirection(199.67f));
		Assert.assertEquals("Südwest (SW - 229.43°)", OpenWeatherParser.analyseWindDirection(229.43f));
		Assert.assertEquals("West-Südwest (WSW - 248.14°)", OpenWeatherParser.analyseWindDirection(248.14f));
		Assert.assertEquals("West (W - 276.76°)", OpenWeatherParser.analyseWindDirection(276.76f));
		Assert.assertEquals("West-NordWest (WNW - 299.11°)", OpenWeatherParser.analyseWindDirection(299.11f));
		Assert.assertEquals("Nordwest (NW - 314.89°)", OpenWeatherParser.analyseWindDirection(314.89f));
		Assert.assertEquals("Nord-Nordwest (NNW - 332.6676°)", OpenWeatherParser.analyseWindDirection(332.6676f));
		Assert.assertEquals("Windstill", OpenWeatherParser.analyseWindDirection(365.45f));
		Assert.assertEquals("Windstill", OpenWeatherParser.analyseWindDirection(-5f));
		Assert.assertEquals("Keine Winddaten", OpenWeatherParser.analyseWindDirection(Float.NaN));
	}
}
