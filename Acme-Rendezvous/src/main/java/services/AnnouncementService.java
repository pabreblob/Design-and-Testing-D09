
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AnnouncementRepository;
import domain.Announcement;
import domain.User;

@Service
@Transactional
public class AnnouncementService {

	@Autowired
	private AnnouncementRepository	announcementRepository;

	@Autowired
	private UserService				userService;


	public Announcement create() {
		final Announcement res = new Announcement();
		res.setMoment(new Date(System.currentTimeMillis() - 1000));
		return res;
	}

	public Announcement save(final Announcement a) {
		final User u = this.userService.findByPrincipal();
		Assert.isTrue(a.getRendezvous().getCreator().equals(u));
		a.setMoment(new Date(System.currentTimeMillis() - 1000));
		final Announcement res = this.announcementRepository.save(a);
		a.getRendezvous().getAnnouncements().add(res);
		return res;
	}

	public void delete(final Announcement a) {
		a.getRendezvous().getAnnouncements().remove(a);
		this.announcementRepository.delete(a.getId());
	}

	public Collection<Announcement> findAnnouncementsByPrincipal() {
		return this.announcementRepository.findAnnouncementsByUserId(this.userService.findByPrincipal().getId());
	}

	public Announcement findOne(final int announcementId) {
		return this.announcementRepository.findOne(announcementId);
	}
}
