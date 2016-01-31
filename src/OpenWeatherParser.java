import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;

import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import net.aksingh.owmjapis.OpenWeatherMap.Language;
import net.aksingh.owmjapis.OpenWeatherMap.Units;

public class OpenWeatherParser {

	ConfigurationManager cm = null;

	/**
	 * Parses the weather and extracts the temperature, humidity, pressure,
	 * windspeed and winddegree into files
	 * 
	 * @param cm
	 * @throws IOException
	 * @throws JSONException
	 */
	public OpenWeatherParser(ConfigurationManager cm) throws IOException, JSONException {
		this.cm = cm;
		CurrentWeather cwd = getWeatherFromOWM(cm.configuration.getProperty("default_location"));

		String temperatureHtml = buildHTMLFile(Float.toString(parseTemperature(cwd)) + "°C");
		saveHtmlFile(cm.configuration.getProperty("storage_file_name_temperature"), temperatureHtml);
		String humidityHtml = buildHTMLFile(Float.toString(cwd.getMainInstance().getHumidity()) + " %");
		saveHtmlFile(cm.configuration.getProperty("storage_file_name_humidity"), humidityHtml);
		String pressureHtml = buildHTMLFile(Float.toString(cwd.getMainInstance().getPressure()) + " hPa");
		saveHtmlFile(cm.configuration.getProperty("storage_file_name_pressure"), pressureHtml);
		String windSpeedHtml = buildHTMLFile(Float.toString(cwd.getWindInstance().getWindSpeed()) + " m/sec"
				+ analyseWindSpeed(cwd.getWindInstance().getWindSpeed()));
		saveHtmlFile(cm.configuration.getProperty("storage_file_name_windspeed"), windSpeedHtml);
		String windDegreeHtml = buildHTMLFile(Float.toString(cwd.getWindInstance().getWindDegree()) + "°");
		saveHtmlFile(cm.configuration.getProperty("storage_file_name_winddegree"), windDegreeHtml);
		String windDirectionHtml = buildHTMLFile(analyseWindDirection(cwd.getWindInstance().getWindDegree()));
		saveHtmlFile(cm.configuration.getProperty("storage_file_name_winddirection"), windDirectionHtml);
	}

	/**
	 * Gets the weather from the openweathermap pages
	 * 
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public CurrentWeather getWeatherFromOWM(String cityName) throws IOException, JSONException {
		OpenWeatherMap owm = new OpenWeatherMap(Units.METRIC, Language.GERMAN,
				cm.configuration.getProperty("owm_api_code"));
		CurrentWeather cwd = owm.currentWeatherByCityName(cityName);

		if (cwd.getCityName() == null) {
			System.exit(-1);
		}
		System.out.println("City: " + cwd.getCityName());
		System.out.println("Temperature: " + parseTemperature(cwd) + "°C");
		System.out.println("Humidity: " + cwd.getMainInstance().getHumidity() + " %");
		System.out.println("Pressure: " + cwd.getMainInstance().getPressure() + " hPa");
		System.out.println("Windspeed: " + cwd.getWindInstance().getWindSpeed() + " meter/sec "
				+ analyseWindSpeed(cwd.getWindInstance().getWindSpeed()));
		System.out.println("Winddegree: " + cwd.getWindInstance().getWindDegree() + "°");
		System.out.println("Winddirection: " + analyseWindDirection(cwd.getWindInstance().getWindDegree()));
		return cwd;
	}

	private float parseTemperature(CurrentWeather cwd) {
		double temperature = cwd.getMainInstance().getTemperature();
		if (temperature > 100) {
			temperature = temperature - 273.5;
		}
		return (float) (Math.round(temperature * 10) / 10.0);
	}

	public static String analyseWindDirection(float windDegree) {
		if (!Float.isNaN(windDegree)) {
			if (((windDegree >= 348.75 && windDegree <= 360.0) || (windDegree < 11.25 && windDegree >= 0.0))) {
				return "Nord (N - " + windDegree + "°)";
			} else if (windDegree >= 11.25 && windDegree < 33.75) {
				return "Nord-Nordost (NNO - " + windDegree + "°)";
			} else if (windDegree >= 33.75 && windDegree < 56.25) {
				return "Nordost (NO - " + windDegree + "°)";
			} else if (windDegree >= 56.25 && windDegree < 78.75) {
				return "Ost-Nordost (ONO - " + windDegree + "°)";
			} else if (windDegree >= 78.75 && windDegree < 101.25) {
				return "Ost (O - " + windDegree + "°)";
			} else if (windDegree >= 101.25 && windDegree < 123.75) {
				return "Ost-Südost (OSO - " + windDegree + "°)";
			} else if (windDegree >= 123.75 && windDegree < 146.25) {
				return "Südost (SO - " + windDegree + "°)";
			} else if (windDegree >= 146.25 && windDegree < 168.75) {
				return "Süd-Südost (SSO - " + windDegree + "°)";
			} else if (windDegree >= 168.75 && windDegree < 191.25) {
				return "Süd (S - " + windDegree + "°)";
			} else if (windDegree >= 191.25 && windDegree < 213.75) {
				return "Süd-Südwest (SSW - " + windDegree + "°)";
			} else if (windDegree >= 213.75 && windDegree < 236.25) {
				return "Südwest (SW - " + windDegree + "°)";
			} else if (windDegree >= 236.25 && windDegree < 258.75) {
				return "West-Südwest (WSW - " + windDegree + "°)";
			} else if (windDegree >= 258.75 && windDegree < 281.25) {
				return "West (W - " + windDegree + "°)";
			} else if (windDegree >= 281.25 && windDegree < 303.75) {
				return "West-NordWest (WNW - " + windDegree + "°)";
			} else if (windDegree >= 303.75 && windDegree < 326.25) {
				return "Nordwest (NW - " + windDegree + "°)";
			} else if (windDegree >= 326.25 && windDegree < 348.75) {
				return "Nord-Nordwest (NNW - " + windDegree + "°)";
			} else {
				return "Windstill";
			}
		} else {
			return "Keine Winddaten";
		}
	}

	public static String analyseWindSpeed(float speedInMeterperSec) {
		if (!Float.isNaN(speedInMeterperSec)) {
			if (speedInMeterperSec < 0.3) {
				return "(0 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else if (speedInMeterperSec >= 0.3 && speedInMeterperSec < 1.6) {
				return "(1 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else if (speedInMeterperSec >= 1.6 && speedInMeterperSec < 3.4) {
				return "(2 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else if (speedInMeterperSec >= 3.4 && speedInMeterperSec < 5.5) {
				return "(3 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else if (speedInMeterperSec >= 5.5 && speedInMeterperSec < 8.0) {
				return "(4 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else if (speedInMeterperSec >= 8.0 && speedInMeterperSec < 10.8) {
				return "(5 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else if (speedInMeterperSec >= 10.8 && speedInMeterperSec < 13.9) {
				return "(6 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else if (speedInMeterperSec >= 13.9 && speedInMeterperSec < 17.2) {
				return "(7 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else if (speedInMeterperSec >= 17.2 && speedInMeterperSec < 20.8) {
				return "(8 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else if (speedInMeterperSec >= 20.8 && speedInMeterperSec < 24.5) {
				return "(9 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else if (speedInMeterperSec >= 24.5 && speedInMeterperSec < 28.5) {
				return "(10 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else if (speedInMeterperSec >= 28.5 && speedInMeterperSec < 32.7) {
				return "(11 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else if (speedInMeterperSec >= 32.7) {
				return "(12 Bft - " + calcSpeedInKMperHour(speedInMeterperSec) + " km/h)";
			} else {
				return "Windstill";
			}
		} else {
			return "Keine Winddaten";
		}
	}

	private static double calcSpeedInKMperHour(float speedInMeterperSec) {
		return Math.round(speedInMeterperSec * 3.6 * 10) / 10.0;
	}

	/**
	 * Generates a minimal html file of a string, which is inserted in the body
	 * 
	 * @param text
	 * @return
	 */
	private String buildHTMLFile(String bodyText) {
		StringBuilder htmlFileBuilder = new StringBuilder();
		htmlFileBuilder.append("<html><head><title></title></head><body><p>");
		htmlFileBuilder.append(bodyText);
		htmlFileBuilder.append("</p></body></html>");
		return htmlFileBuilder.toString();
	}

	/**
	 * Get a route and save their steps into a html file, without html format
	 * creates the new directory if it is not existing
	 * 
	 * @return
	 * @throws IOException
	 */
	private void saveHtmlFile(String fileName, String htmlText) throws IOException {
		String storagePath = new String(cm.configuration.getProperty("storage_path"));
		String storageFilename = fileName;
		if (fileName == null || fileName.equals("")) {
			return;
		}
		File file = null;
		if (storagePath.isEmpty()) {
			file = new File(storageFilename);
		} else {
			file = new File(storagePath + java.io.File.separator + storageFilename);
		}
		File path = new File(storagePath);
		if (!path.exists() || !path.isDirectory()) {
			path.mkdirs();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(htmlText);
		bw.close();
	}
}
