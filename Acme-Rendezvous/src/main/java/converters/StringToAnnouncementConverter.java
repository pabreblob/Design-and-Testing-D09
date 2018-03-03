
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.AnnouncementRepository;
import domain.Announcement;

@Component
@Transactional
public class StringToAnnouncementConverter implements Converter<String, Announcement> {

	@Autowired
	private AnnouncementRepository	announcementRepository;


	@Override
	public Announcement convert(final String source) {
		final Announcement announcement;
		int id;
		try {
			id = Integer.valueOf(source);
			announcement = this.announcementRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return announcement;
	}

}
