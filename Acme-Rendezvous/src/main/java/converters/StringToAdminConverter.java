
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.AdminRepository;
import domain.Administrator;

@Component
@Transactional
public class StringToAdminConverter implements Converter<String, Administrator> {

	@Autowired
	AdminRepository	adminRepository;


	@Override
	public Administrator convert(final String arg0) {
		Administrator res;
		int id;

		try {
			if (StringUtils.isEmpty(arg0))
				res = null;
			else {
				id = Integer.valueOf(arg0);
				res = this.adminRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}
}
