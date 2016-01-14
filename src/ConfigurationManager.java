import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

/**
 * The configuration manager handles the configuration, reads it at program
 * start and write it if it is not existing
 * 
 * @author Konry
 *
 */
public class ConfigurationManager {

	private final String configComment = "This is the configuration file for the Application FFOpenWeatherMap.\n"
			+ "- The default location can be set to a city worldwide, please check before if they is existing in openweathermap\n"
			+ "- The language is also for the return language of the owm api\n"
			+ "- At the storage path the files are saved.\n"
			+ "- The storage_file_name_temperature has the temperature inside.\n"
			+ "- The storage_file_name_humidity has the relative humidity inside.\n"
			+ "- The storage_file_name_pressure has the pressure inside.\n"
			+ "- The storage_file_name_winddegree has the winddegree inside.\n"
			+ "- The storage_file_name_windspeed has the windspeed inside.\n\n";

	private final String versionString = "0.1.3";

	public Properties configuration = null;

	public ConfigurationManager() throws IOException {
		initiate();
	}

	/**
	 * The initialization process, checks if a configuration exists,
	 * if not create a new configuration file
	 * and read it out
	 * 
	 * @throws IOException
	 */
	private void initiate() throws IOException {
		if (!checkPropertiesFileExisting()) {
			generateDefaultConfigurationFile();
		}
		configuration = readDefaultConfiguarionFile();
		if (configuration.getProperty("version") == null || configuration.getProperty("version") != versionString) {
			replaceConfigurationFile(configuration);
		}
	}

	/**
	 * Check if the config file is already existing
	 * 
	 * @return
	 */
	private boolean checkPropertiesFileExisting() {
		File file = new File("config.cfg");
		return file.exists();
	}

	/**
	 * Reads the configuration file and write it into the properties file.
	 * 
	 * @return
	 * @throws IOException
	 */
	private Properties readDefaultConfiguarionFile() throws IOException {
		Properties prop = new Properties();
		FileInputStream in = new FileInputStream("config.cfg");
		prop.load(in);
		in.close();

		return prop;
	}

	/**
	 * Writes the configuration file with default values.
	 * 
	 * @throws IOException
	 */
	private void generateDefaultConfigurationFile() throws IOException {
		Properties defaultProperties = generateDefaultPropertiesObject();

		FileOutputStream out = new FileOutputStream("config.cfg");
		defaultProperties.store(out, configComment);
		out.close();
	}

	/**
	 * Writes the configuration file with default values.
	 * 
	 * @throws IOException
	 */
	private void writeConfigurationFile(Properties config) throws IOException {
		FileOutputStream out = new FileOutputStream("config.cfg");
		config.store(out, configComment);
		out.close();
	}

	private Properties generateDefaultPropertiesObject() {
		Properties defaultProperties = new Properties();

		defaultProperties.setProperty("default_location", "Luebeck");
		defaultProperties.setProperty("language", "de");
		defaultProperties.setProperty("version", "0.1.3");
		defaultProperties.setProperty("storage_path", "");
		defaultProperties.setProperty("storage_file_name_temperature", "owm_temperature.html");
		defaultProperties.setProperty("storage_file_name_humidity", "owm_humidity.html");
		defaultProperties.setProperty("storage_file_name_pressure", "owm_pressure.html");
		defaultProperties.setProperty("storage_file_name_winddegree", "owm_winddegree.html");
		defaultProperties.setProperty("storage_file_name_windspeed", "owm_windspeed.html");
		defaultProperties.setProperty("storage_file_name_winddirection", "owm_winddirection.html");
		defaultProperties.setProperty("owm_api_code", "TODO insert");
		return defaultProperties;
	}

	private void replaceConfigurationFile(Properties config) throws IOException {
		Properties newPropertiesFile = generateDefaultPropertiesObject();
		Iterator<Entry<Object, Object>> it = config.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, Object> pair = it.next();
			// System.out.println(pair.getKey() + " = " + pair.getValue());

			/* special rename handling... */
			if (!pair.getKey().toString().equals("version")) {
				newPropertiesFile.setProperty(pair.getKey().toString(), pair.getValue().toString());
			}
			it.remove(); // avoids a ConcurrentModificationException
		}
		configuration = newPropertiesFile;
		writeConfigurationFile(newPropertiesFile);
	}
}
