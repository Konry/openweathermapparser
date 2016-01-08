import java.io.IOException;

import org.json.JSONException;

public class OpenWeatherMain {

	public static void main(String[] args) {
		try {
			ConfigurationManager cm = new ConfigurationManager();
			new OpenWeatherParser(cm);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
