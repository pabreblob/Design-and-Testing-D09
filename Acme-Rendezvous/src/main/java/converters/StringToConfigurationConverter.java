
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.ConfigurationRepository;
import domain.Configuration;

public class StringToConfigurationConverter implements Converter<String, Configuration> {

	@Autowired
	private ConfigurationRepository	configurationRepository;


	@Override
	public Configuration convert(final String source) {
		final Configuration configuration;
		int id;
		try {
			id = Integer.valueOf(source);
			configuration = this.configurationRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return configuration;
	}

}
