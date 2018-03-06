
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.RequestRepository;
import domain.Request;

public class StringToRequestConverter implements Converter<String, Request> {

	@Autowired
	private RequestRepository	requestRepository;


	@Override
	public Request convert(final String source) {
		final Request request;
		int id;
		try {
			id = Integer.valueOf(source);
			request = this.requestRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return request;
	}

}
