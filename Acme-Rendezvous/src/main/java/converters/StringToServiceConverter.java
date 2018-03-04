
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.ServiceRepository;
import domain.Service;

public class StringToServiceConverter implements Converter<String, Service> {

	@Autowired
	private ServiceRepository	serviceRepository;


	@Override
	public Service convert(final String source) {
		final Service service;
		int id;
		try {
			id = Integer.valueOf(source);
			service = this.serviceRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return service;
	}

}
