
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.RendezvousRepository;
import domain.Rendezvous;

@Component
@Transactional
public class StringToRendezvousConverter implements Converter<String, Rendezvous> {

	@Autowired
	RendezvousRepository	rendezvousRepository;


	@Override
	public Rendezvous convert(final String arg0) {
		Rendezvous res = null;
		int id;

		try {
			if (StringUtils.isEmpty(arg0))
				res = null;
			else {
				id = Integer.valueOf(arg0);
				res = this.rendezvousRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}
}
