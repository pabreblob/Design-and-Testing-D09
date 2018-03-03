
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Announcement;
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AnnouncementTest extends AbstractTest {

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private UserService			userService;


	@Test
	public void testCreateAnnouncement() {
		super.authenticate("user1");
		final Announcement res = this.announcementService.create();
		Assert.notNull(res);
		super.authenticate(null);
	}

	@Test
	public void testSaveAnnouncement() {
		super.authenticate("user1");
		final Announcement res = this.announcementService.create();
		res.setTitle("titletest");
		res.setDescription("desctest");
		res.setRendezvous(new ArrayList<Rendezvous>(this.userService.findByPrincipal().getCreatedRendezvous()).get(0));
		final Announcement a = this.announcementService.save(res);
		final Announcement found = this.announcementService.findOne(a.getId());
		Assert.notNull(a);
		Assert.isTrue(found.equals(a));
		super.authenticate(null);
	}

	@Test
	public void testFindOneAnnouncement() {
		super.authenticate("user1");
		final Announcement res = this.announcementService.create();
		res.setTitle("titletest");
		res.setDescription("desctest");
		res.setRendezvous(new ArrayList<Rendezvous>(this.userService.findByPrincipal().getCreatedRendezvous()).get(0));
		final Announcement a = this.announcementService.save(res);
		final Announcement found = this.announcementService.findOne(a.getId());
		Assert.notNull(a);
		Assert.isTrue(found.equals(a));
		super.authenticate(null);
	}

	@Test
	public void testDeleteAnnouncement() {
		super.authenticate("user1");
		final Announcement res = this.announcementService.create();
		res.setTitle("titletest");
		res.setDescription("desctest");
		res.setRendezvous(new ArrayList<Rendezvous>(this.userService.findByPrincipal().getCreatedRendezvous()).get(0));
		final Announcement a = this.announcementService.save(res);
		this.announcementService.delete(a);
		final Announcement found = this.announcementService.findOne(a.getId());
		Assert.notNull(a);
		Assert.isTrue(found == null);
		super.authenticate(null);
	}

	@Test
	public void testFindByPrincipal() {
		super.authenticate("user1");
		final User principal = this.userService.findByPrincipal();
		final User aux = this.userService.findOne(principal.getId());
		Assert.isTrue(principal.equals(aux));
	}

	@Test
	public void testFindAnnouncementsByUserId() {
		super.authenticate("user1");
		final Collection<Announcement> res = this.announcementService.findAnnouncementsByPrincipal();
		Assert.notNull(res);
		Assert.isTrue(res.size() > 0);
		super.authenticate(null);
	}

}
