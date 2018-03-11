
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AnnouncementTest extends AbstractTest {

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private UserService			userService;


	/**
	 * Tests the creation of announcements.
	 * <p>
	 * This method is used to test the creation of empty announcements before passing them to the corresponding views.
	 */
	@Test
	public void testCreateAnnouncement() {
		super.authenticate("user1");
		final Announcement res = this.announcementService.create();
		Assert.notNull(res);
		super.authenticate(null);
	}

	/**
	 * Tests the saving of announcements.
	 * <p>
	 * This method tests the creation and later saving of announcements as it would be done by an user in the corresponding views.
	 */
	@Test
	public void driverSaveAnnouncement() {
		final Object testingData[][] = {
			{
				"user1", null
			}, {
				"user2", null
			}, {
				null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSave((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing the saving of announcements.
	 * <p>
	 * This method defines the template used for the tests that check the saving of announcements.
	 * 
	 * @param username
	 *            The username of the user that logs in. Use <code>null</code> if no user should be logged in.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateSave(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Announcement res = this.announcementService.create();
			res.setTitle("titletest");
			res.setDescription("desctest");
			res.setRendezvous(new ArrayList<Rendezvous>(this.userService.findByPrincipal().getCreatedRendezvous()).get(0));
			final Announcement a = this.announcementService.save(res);
			final Announcement found = this.announcementService.findOne(a.getId());
			Assert.notNull(a);
			Assert.isTrue(found.equals(a));
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the finding of one announcement.
	 * <p>
	 * This method checks that an announcement can be saved and later correctly found.
	 */
	@Test
	public void driverFindOneAnnouncement() {
		final Object testingData[][] = {
			{
				"user1", null
			}, {
				"user2", null
			}, {
				null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindOne((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing the finding of one announcement.
	 * <p>
	 * This method defines the template used for the tests that check the finding of one announcement.
	 * 
	 * @param username
	 *            The username of the user that logs in. Use <code>null</code> if no user should be logged in.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindOne(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Announcement res = this.announcementService.create();
			res.setTitle("titletest");
			res.setDescription("desctest");
			res.setRendezvous(new ArrayList<Rendezvous>(this.userService.findByPrincipal().getCreatedRendezvous()).get(0));
			final Announcement a = this.announcementService.save(res);
			final Announcement found = this.announcementService.findOne(a.getId());
			Assert.notNull(a);
			Assert.isTrue(found.equals(a));
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the deletion of an announcement.
	 * <p>
	 * This method tests that an announcement can be saved and later deleted, and checks that it cannot be found.
	 */
	@Test
	public void driverDeleteAnnouncement() {
		final Object testingData[][] = {
			{
				"user1", null
			}, {
				"user2", null
			}, {
				null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing the deletion of an announcement.
	 * <p>
	 * This method defines the template used to test the deletion of an announcement.
	 * 
	 * @param username
	 *            The username of the user that logs in. Use <code>null</code> if no user should be logged in.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateDelete(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
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
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of announcements.
	 * <p>
	 * This method tests the listing of the announcements that a user has created.
	 */
	@Test
	public void driverFindAnnouncementsByUserId() {
		final Object testingData[][] = {
			{
				"user1", 1, null
			}, {
				"user2", 0, null
			}, {
				null, 0, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindByUserId((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the listing of announcements.
	 * <p>
	 * This method defines the template used to test the listing of announcements. Note that the announcements are retrieved from the currently logged in user.
	 * 
	 * @param username
	 *            The username of the user that logs in. Use <code>null</code> if no user should be logged in.
	 * @param minimumLength
	 *            The minimum expected length of the list of announcements that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindByUserId(final String username, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Collection<Announcement> res = this.announcementService.findAnnouncementsByPrincipal();
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
