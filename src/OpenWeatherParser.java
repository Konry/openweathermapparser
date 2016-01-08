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
	 * Parses the weather and extracts the temperature, humidity, pressure, windspeed and winddegree into files
	 * @param cm
	 * @throws IOException
	 * @throws JSONException
	 */
	public OpenWeatherParser(ConfigurationManager cm) throws IOException, JSONException {
		this.cm = cm;
		CurrentWeather cwd = getWeatherFromOWM();
		
		String temperatureHtml = buildHTMLFile(Float.toString(cwd.getMainInstance().getTemperature())+ "°C");
		saveHtmlFile(cm.configuration.getProperty("storage_file_name_temperature"), temperatureHtml);
		String humidityHtml = buildHTMLFile(Float.toString(cwd.getMainInstance().getHumidity())+ " %");
		saveHtmlFile(cm.configuration.getProperty("storage_file_name_humidity"), humidityHtml);
		String pressureHtml = buildHTMLFile(Float.toString(cwd.getMainInstance().getPressure())+ " hPa");
		saveHtmlFile(cm.configuration.getProperty("storage_file_name_pressure"), pressureHtml);
		String windSpeedHtml = buildHTMLFile(Float.toString(cwd.getWindInstance().getWindSpeed())+ " m/sec");
		saveHtmlFile(cm.configuration.getProperty("storage_file_name_winddegree"), windSpeedHtml);
		String windDegreeHtml = buildHTMLFile(Float.toString(cwd.getWindInstance().getWindDegree())+ "°");
		saveHtmlFile(cm.configuration.getProperty("storage_file_name_windspeed"), windDegreeHtml);
	}

	/**
	 * Gets the weather from the openweathermap pages
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	private CurrentWeather getWeatherFromOWM() throws IOException, JSONException {
		OpenWeatherMap owm = new OpenWeatherMap(Units.METRIC, Language.GERMAN, cm.configuration.getProperty("owm_api_code"));
		CurrentWeather cwd = owm.currentWeatherByCityName(cm.configuration.getProperty("default_location"));
		
		if(cwd.getCityName() == null){
			System.exit(-1);
		}
		System.out.println("City: " + cwd.getCityName());
		System.out.println("Temperature: " + cwd.getMainInstance().getTemperature()+ "°C");
		System.out.println("Humidity: " + cwd.getMainInstance().getHumidity()+ " %");
		System.out.println("Pressure: " + cwd.getMainInstance().getPressure()+ " hPa");
		System.out.println("Windspeed: " + cwd.getWindInstance().getWindSpeed()+ " meter/sec");
		System.out.println("Winddegree: " + cwd.getWindInstance().getWindDegree()+ "°");
		return cwd;		
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
		File file = null;
		if (storagePath.isEmpty()) {
			file = new File(storageFilename);
		} else {
			file = new File(storagePath + java.io.File.separator + storageFilename);
		}
		File path = new File(storagePath);
		if(!path.exists() || !path.isDirectory()){
			path.mkdirs();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(htmlText);
		bw.close();
	}
}
