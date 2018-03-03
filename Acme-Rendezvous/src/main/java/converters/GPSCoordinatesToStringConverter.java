
package converters;

import java.net.URLEncoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.GPSCoordinates;

@Component
@Transactional
public class GPSCoordinatesToStringConverter implements Converter<GPSCoordinates, String> {

	@Override
	public String convert(final GPSCoordinates gps) {
		String result;
		StringBuilder builder;

		if (gps == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append(URLEncoder.encode(Double.toString(gps.getLatitude()), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Double.toString(gps.getLongitude()), "UTF-8"));
				result = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}

		return result;
	}
}
