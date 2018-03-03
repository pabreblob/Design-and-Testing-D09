
package converters;

import java.net.URLDecoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.GPSCoordinates;

@Component
@Transactional
public class StringToGPSCoordinatesConverter implements Converter<String, GPSCoordinates> {

	@Override
	public GPSCoordinates convert(final String arg0) {
		final GPSCoordinates res;
		final String parts[];

		if (arg0 == null)
			res = null;
		else
			try {
				parts = arg0.split("\\|");
				res = new GPSCoordinates();
				res.setLatitude(Double.valueOf(URLDecoder.decode(parts[0], "UTF-8")));
				res.setLongitude(Double.valueOf(URLDecoder.decode(parts[1], "UTF-8")));
			} catch (final Throwable oops) {
				throw new IllegalArgumentException(oops);
			}
		return res;
	}
}
