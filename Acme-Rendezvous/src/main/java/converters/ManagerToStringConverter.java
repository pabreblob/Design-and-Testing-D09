
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Administrator;

@Component
@Transactional
public class ManagerToStringConverter implements Converter<Administrator, String> {

	@Override
	public String convert(final Administrator admin) {
		String result;
		if (admin == null)
			result = null;
		else
			result = String.valueOf(admin.getId());
		return result;
	}
}
