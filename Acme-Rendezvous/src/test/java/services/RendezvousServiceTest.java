
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RendezvousServiceTest extends AbstractTest {

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	/**
	 * Tests the creation of Rendezvouses.
	 * <p>
	 * This method is used to test the creation of empty rendezvous before passing them to the corresponding views.
	 */
	@Test
	public void testCreate() {
		super.authenticate("user1");
		final Rendezvous res = this.rendezvousService.create();
		Assert.notNull(res);
		Assert.isTrue(res.getAttendants().size() == 1);
		Assert.notNull(res.getCreator());
		super.authenticate(null);
	}

	/**
	 * Tests the saving of rendezvouses.
	 * <p>
	 * This method tests the creation and later saving of rendezvouses as it would be done by an user in the corresponding views.
	 * 
	 * Case 1: Creation and saving of a rendezvous correctly. No exception is expected.
	 * 
	 * Case 2: Creation and saving of a rendezvous by any user. IllegalArgumentException is expected.
	 * 
	 * Case 3: Creation and saving of a rendezvous with date in the past. IllegalArgumentException is expected.
	 * 
	 * Case 4: Creation and saving of a rendezvous with adult content by a not adult user. IllegalArgumentException is expected.
	 * 
	 * Case 5: Creation and saving of a rendezvous by a not adult user. No exception is expected.
	 * 
	 */
	@Test
	public void testSave() {
		final Object testingData[][] = {
			{
				"user1", new Date(System.currentTimeMillis() + 100000000), true, null
			}, {
				null, new Date(System.currentTimeMillis() + 100000000), true, IllegalArgumentException.class
			}, {
				"user1", new Date(System.currentTimeMillis() - 100000000), true, IllegalArgumentException.class
			}, {
				"user2", new Date(System.currentTimeMillis() + 100000000), true, IllegalArgumentException.class
			}, {
				"user2", new Date(System.currentTimeMillis() + 100000000), false, null
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSave((String) testingData[i][0], (Date) testingData[i][1], (boolean) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	/**
	 * Template for testing the saving of rendezvouses.
	 * <p>
	 * This method defines the template used for the tests that check the saving of rendezvouses.
	 * 
	 * @param username
	 *            The username of the user that logs in. Use <code>null</code> if no user should be logged in.
	 * @param moment
	 *            The date of the rendezvous that you are creating.
	 * @param adultContent
	 *            It's to indicate if the rendezvous is for adult or not.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateSave(final String username, final Date moment, final boolean adultContent, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			final Rendezvous res = this.rendezvousService.create();
			res.setName("Encuentro 1");
			res.setDescription("El mejor encuentro de tu vida");
			res.setMoment(moment);
			res.setAdultContent(adultContent);
			final Rendezvous saved = this.rendezvousService.save(res);
			Assert.notNull(saved);
			this.authenticate(null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the editing of rendezvouses.
	 * <p>
	 * This method tests the edition of rendezvouses as it would be done by an user in the corresponding views.
	 * 
	 * Case 1: Edition and saved of a rendezvous. No exception is expected.
	 * 
	 * Case 2: Edition and saved of a rendezvous not created by principal. IllegalArgumentException is expected.
	 * 
	 * Case 3: Edition and saved of a rendezvous with moment in the past. IllegalArgumentException is expected.
	 */
	@Test
	public void testEdit() {
		final Object testingData[][] = {
			{
				"Rendezvous3", "user2", false, false, false, null

			}, {
				"Rendezvous2", "user2", false, false, false, IllegalArgumentException.class

			}, {
				"Rendezvous4", "user3", false, false, true, IllegalArgumentException.class

			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (boolean) testingData[i][2], (boolean) testingData[i][3], (boolean) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * Template for testing the editing of rendezvouses.
	 * <p>
	 * This method defines the template used for the tests that check the editing of services.
	 * 
	 * @param rendezId
	 *            The rendezvous that we want to edit
	 * @param username
	 *            The username of the user that logs in.
	 * @param deleted
	 *            It's to indicate if the rendezvous is deleted
	 * @param finalMode
	 *            It's to indicate if the rendezvous is in final version
	 * @param adultContent
	 *            It's to indicate if the rendezvous is for adult or not.
	 * 
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateEdit(final int rendezId, final String username, final boolean deleted, final boolean finalMode, final boolean adultContent, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			final Rendezvous res = this.rendezvousService.findOne(rendezId);
			res.setName("Encuentro 1");
			res.setDescription("El mejor encuentro de tu vida");
			res.setFinalMode(finalMode);
			res.setDeleted(deleted);
			res.setAdultContent(adultContent);
			final Rendezvous saved = this.rendezvousService.save(res);
			Assert.notNull(saved);
			this.authenticate(null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the finding of one rendezvous.
	 * <p>
	 * This method checks that a rendezvous stored in the database can be found.
	 */
	@Test
	public void testFindOne() {
		super.authenticate("user1");
		final Rendezvous res = this.rendezvousService.create();
		res.setName("name1");
		res.setDescription("description");
		res.setMoment(new Date(System.currentTimeMillis() + 100000000));
		final Rendezvous saved = this.rendezvousService.save(res);
		final Rendezvous found = this.rendezvousService.findOne(saved.getId());
		Assert.isTrue(found.equals(saved));
		super.authenticate(null);
	}

	/**
	 * Tests the finding of all rendezvouses.
	 * <p>
	 * This method checks that a rendezvous stored in the database is contained by all rendezvouses.
	 */
	@Test
	public void testFindAll() {
		super.authenticate("user1");
		final Rendezvous res = this.rendezvousService.create();
		res.setName("name1");
		res.setDescription("description");
		res.setMoment(new Date(System.currentTimeMillis() + 100000000));
		final Rendezvous saved = this.rendezvousService.save(res);
		Assert.isTrue(this.rendezvousService.findAll().contains(saved));
		super.authenticate(null);
	}

	/**
	 * Tests the linking of rendezvouses.
	 * <p>
	 * This method tests the linking of the rendezvouses that an user has created.
	 * 
	 * Case 1: An user link a rendezvous created in the test with another rendezvous stored in the database. No exception is expected.
	 * 
	 * Case 2: An user link a rendezvous created in the test with another rendezvous stored in the database not created by him. IllegalArgumentException is expected.
	 */
	@Test
	public void testLink() {
		final Object testingData[][] = {
			{
				"user1", "Rendezvous1", null

			}, {
				"user2", "Rendezvous1", IllegalArgumentException.class

			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateLink(super.getEntityId((String) testingData[i][1]), (String) testingData[i][0], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the linking of rendezvouses.
	 * <p>
	 * This method defines the template used to test the linking of rendezvouses created by an user. Note that the rendezvouses are retrieved from the currently logged in user.
	 * 
	 * @param rendezvousId
	 *            The rendezvous that we want to link.
	 * @param username
	 *            The username of the user that logs in.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	@SuppressWarnings("deprecation")
	protected void templateLink(final int rendezvousId, final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(username);
			final Rendezvous r1 = this.rendezvousService.findOne(rendezvousId);
			final Rendezvous res = this.rendezvousService.create();
			res.setName("name1");
			res.setDescription("description");
			res.setMoment(new Date(119, 11, 11, 15, 30));
			final Rendezvous r2 = this.rendezvousService.save(res);
			r1.getLinkedRendezvous().add(r2);
			this.rendezvousService.link(r1);
			Assert.isTrue(r2.getLinkedRendezvous().contains(r1));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);

	}

	/**
	 * Tests the unlinking of rendezvouses.
	 * <p>
	 * This method tests the unlinking of the rendezvouses that an user has created.
	 * 
	 * Case 1: An user unlink two rendezvous. No exception is expected.
	 * 
	 * Case 2: An user unlink two rendezvous not created by him. IllegalArgumentException is expected.
	 */
	@Test
	public void testUnLink() {
		final Object testingData[][] = {
			{
				"Rendezvous1", "Rendezvous2", "user1", null

			}, {
				"Rendezvous1", "Rendezvous2", "user2", IllegalArgumentException.class

			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUnlink(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	/**
	 * Template for testing the unlinking of rendezvouses.
	 * <p>
	 * This method defines the template used to test the unlinking of rendezvouses created by an user. Note that the rendezvouses are retrieved from the currently logged in user.
	 * 
	 * @param rendezvous1Id
	 *            The rendezvous 1 that we want to unlink.
	 * @param rendezvous2Id
	 *            The rendezvous 2 that we want to unlink.
	 * @param username
	 *            The username of the user that logs in.
	 * 
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateUnlink(final int rendezvous1Id, final int rendezvous2Id, final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(username);
			final Rendezvous r1 = this.rendezvousService.findOne(rendezvous1Id);
			final Rendezvous r2 = this.rendezvousService.findOne(rendezvous2Id);
			Assert.isTrue(r2.getLinkedRendezvous().contains(r1));
			r1.getLinkedRendezvous().remove(r2);
			final Collection<Rendezvous> oldRendezvous = new ArrayList<>();
			oldRendezvous.add(r2);
			this.rendezvousService.link(r1);
			this.rendezvousService.unlink(r1, oldRendezvous);
			Assert.isTrue(!r2.getLinkedRendezvous().contains(r1));
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);

	}

	/**
	 * Tests the joining of rendezvouses.
	 * <p>
	 * This method tests the joining of the rendezvouses by an user.
	 * 
	 * Case 1: An user join to a rendezvous. No exception is expected
	 * 
	 * Case 2: An user join to a rendezvous created by him. IllegalArgumentException is expected.
	 * 
	 * Case 3: A not adult user join to a rendezvous with adult content. IllegalArgumentException is expected.
	 * 
	 * Case 4: A nor adult user join to a rendezvous. No exception is expected.
	 */
	@Test
	public void testJoin() {
		final Object testingData[][] = {
			{
				"Rendezvous2", "user4", null

			}, {
				"Rendezvous2", "user1", IllegalArgumentException.class

			}, {
				"Rendezvous1", "user2", IllegalArgumentException.class

			}, {
				"Rendezvous2", "user2", null

			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateJoin(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	/**
	 * Template for testing the joining of rendezvouses.
	 * <p>
	 * This method defines the template used to test the joining of rendezvouses by an user.
	 * 
	 * @param rendezvousId
	 *            The rendezvous that user wants to join.
	 * @param username
	 *            The username of the user that logs in.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateJoin(final int rendezvousId, final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			super.authenticate(username);
			final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
			this.rendezvousService.join(r);
			Assert.isTrue(this.userService.findByPrincipal().getReservedRendezvous().contains(r));
			super.authenticate(null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the user cancelling his attendance to a Rendezvous.
	 * <p>
	 * This method tests cancelling the attendance to rendezvouses that an user has joined.
	 * 
	 * Case 1: An user no longer attends to a rendezvous. No exception is expected.
	 * 
	 * Case 2: An user no longer attends to a rendezvous created by him. IllegalArgumentException is expected.
	 * 
	 * Case 3: An user no longer attends to a rendezvous not joined before. IllegalArgumentException is expected.
	 */
	@Test
	public void testDisjoin() {
		final Object testingData[][] = {
			{
				"Rendezvous2", "user3", null

			}, {
				"Rendezvous2", "user1", IllegalArgumentException.class

			}, {
				"Rendezvous2", "user2", IllegalArgumentException.class

			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUnjoin(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	/**
	 * Template for testing cancelling the attendance to rendezvouses.
	 * <p>
	 * This method defines the template used to test cancelling the attendance to rendezvouses by an user.
	 * 
	 * @param rendezvousId
	 *            The rendezvous that user wants to no longer attend
	 * @param username
	 *            The username of the user that logs in.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateUnjoin(final int rendezvousId, final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			super.authenticate(username);
			final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
			Assert.isTrue(this.userService.findByPrincipal().getReservedRendezvous().contains(r));
			this.rendezvousService.unjoin(r);
			Assert.isTrue(!this.userService.findByPrincipal().getReservedRendezvous().contains(r));
			super.authenticate(null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the cancellation of a rendezvous.
	 * <p>
	 * This method tests that a rendezvous can be cancelled.
	 * 
	 * Case 1: A rendezvous is cancelled. No exception is expected.
	 * 
	 * Case 2: A rendezvous in final mode is cancelled. IllegalArgumentException is expected.
	 * 
	 * Case 3: A rendezvous not created by principal is cancelled. IllegalArgumentException is expected.
	 */
	@Test
	public void testCancel() {
		final Object testingData[][] = {
			{
				"Rendezvous5", "user2", null

			}, {
				"Rendezvous2", "user1", IllegalArgumentException.class

			}, {
				"Rendezvous6", "user2", IllegalArgumentException.class

			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCancel(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	/**
	 * Template for testing the cancellation of a rendezvous.
	 * <p>
	 * This method defines the template used to test the cancellation of a rendezvous.
	 * 
	 * @param rendezvousId
	 *            The id of the rendezvous that we want to cancel
	 * @param username
	 *            The username of the user that logs in.
	 * 
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateCancel(final int rendezvousId, final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			super.authenticate(username);
			final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
			Assert.isTrue(!r.isDeleted());
			this.rendezvousService.cancel(r);
			Assert.isTrue(r.isDeleted());
			super.authenticate(null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the deletion of a rendezvous.
	 * <p>
	 * This method tests that a rendezvous can be deleted in any situation by an admin.
	 * 
	 * Case 1: A rendezvous is deleted by an admin.
	 * 
	 * Case 2: Another rendezvous is deleted by an admin.
	 * 
	 * Case 1: Another rendezvous is deleted by an admin.
	 * 
	 * Note: Only administrators can delete rendezvous but it's controlled in the security.xml file.
	 */
	@Test
	public void testDelete() {
		final Object testingData[][] = {
			{
				"Rendezvous5", "admin", null

			}, {
				"Rendezvous1", "admin", null

			}, {
				"Rendezvous2", "admin", null
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDelete(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	/**
	 * Template for testing the deletion of a rendezvous.
	 * <p>
	 * This method defines the template used to test the cancellation of a service.
	 * 
	 * @param rendezvousId
	 *            The id of the rendezvous that we want to cancel
	 * @param username
	 *            The username of the admin that logs in.
	 * 
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateDelete(final int rendezvousId, final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			super.authenticate(username);
			this.rendezvousService.delete(rendezvousId);
			Assert.isTrue(this.rendezvousService.findOne(rendezvousId).isDeleted());
			super.authenticate(null);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of rendezvous created by principal.
	 * <p>
	 * This method tests the listing of the rendezvous that an user has created.
	 * 
	 * Case 1: User 1 lists all rendezvouses created by him. No exception is expected
	 * 
	 * Case 2: User 2 lists all rendezvouses created by him. No exception is expected
	 * 
	 * Case 3: User 3 lists all rendezvouses created by him. No exception is expected
	 * 
	 * Case 4: User 4 lists all rendezvouses created by him. No exception is expected
	 * 
	 * Case 5: User 5 lists all rendezvouses created by him. No exception is expected
	 * 
	 * Case 6: User 6 lists all rendezvouses created by him. No exception is expected
	 * 
	 * Case 7: Null lists all rendezvouses created by him. IllegalArgumentException is expected.
	 * 
	 * Case 8: Manager 1 lists all rendezvouses created by him. NullPointerException is expected.
	 * 
	 * Case 9: User 10 (it doesn't exist) lists all rendezvouses created by him. IllegalArgumentException.
	 * 
	 * Case 10: Manager 2 lists all rendezvouses created by him. NullPointerException is expected.
	 * 
	 * Case 11: Admin lists all rendezvouses created by him. IllegalArgumentException.
	 * 
	 * Case 12: Rendezvouses (not deleted) created by user 1. No exception is expected.
	 * 
	 * Case 13: Rendezvouses (not deleted) created by user 2. No exception is expected.
	 * 
	 * Case 14: Rendezvouses (not deleted) created by user 3. No exception is expected.
	 * 
	 * Case 15: Rendezvouses (not deleted) created by user 4. No exception is expected.
	 * 
	 * Case 16: Rendezvouses (not deleted) created by anything. IllegalArgumentException is expected.
	 * 
	 * Case 17: Rendezvouses (not deleted) created by user 8 (does not exist). IllegalArgumentException is expected.
	 * 
	 * Case 18: Rendezvouses (not deleted) created by manager 2. NullPointerException is expected.
	 * 
	 * Case 19: Rendezvouses (not deleted) created by manager 3 (does not exist). NullPointerException is expected.
	 * 
	 * Case 20: Rendezvouses (not deleted) created by admin. NullPointerException is expected.
	 * 
	 * Note: Case 1 to 11. It's the list that user see when they want to see rendezvouses created by them.
	 * 
	 * Note 2 : Case 12 to 20. It's the list that an user (or another actor) sees when they want to see rendezvouses created by other users.
	 */
	@Test
	public void driverRendezvousCreatedByPrincipal() {
		//All rendezvouses created by principal
		final Object testingData[][] = {
			{
				"user1", 2, null
			}, {
				"user2", 2, null
			}, {
				"user3", 1, null
			}, {
				"user4", 1, null
			}, {
				"user5", 0, null
			}, {
				"user6", 0, null
			}, {
				null, 0, IllegalArgumentException.class
			}, {
				"Manager1", 0, NullPointerException.class
			}, {
				"User10", 0, IllegalArgumentException.class
			}, {
				"Manager2", 0, NullPointerException.class
			}, {
				"Admin", 0, NullPointerException.class
			},
			//All rendezvouses not deleted created by principal
			{
				"user1", 2, null
			}, {
				"user2", 1, null
			}, {
				"user3", 1, null
			}, {
				"user4", 1, null
			}, {
				null, 0, IllegalArgumentException.class
			}, {
				"user8", 0, IllegalArgumentException.class
			}, {
				"Manager2", 0, NullPointerException.class
			}, {
				"Manager3", 0, IllegalArgumentException.class
			}, {
				"Admin", 0, NullPointerException.class
			}
		};
		for (int i = 0; i <= 10; i++)
			this.templateFindByUserId((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

		for (int i = 11; i < testingData.length; i++)
			this.templateFindNotDeletedByUserId((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	/**
	 * Template for testing the listing of rendezvouses.
	 * <p>
	 * This method defines the template used to test the listing of rendezvouses created by an user. Note that the rendezvouses are retrieved from the currently logged in user.
	 * 
	 * @param username
	 *            The username of the user that logs in.
	 * @param minimumLength
	 *            The minimum expected length of the list of rendezvouses that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindByUserId(final String username, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Collection<Rendezvous> res = this.rendezvousService.findRendezvousCreatedByPrincipal();
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Template for testing the listing of rendezvouses not deleted.
	 * <p>
	 * This method defines the template used to test the listing of rendezvouses created by an user. Note that the rendezvouses are retrieved from the currently logged in user.
	 * 
	 * @param username
	 *            The username of the user that logs in.
	 * @param minimumLength
	 *            The minimum expected length of the list of rendezvouses that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindNotDeletedByUserId(final String username, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Collection<Rendezvous> res = this.rendezvousService.findRendezvousNotDeletedCreatedByPrincipal();
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of rendezvous joined by principal.
	 * <p>
	 * This method tests the listing of the rendezvous that an user is joined.
	 * 
	 * Case 1: List rendezvouses joined by principal. No exception is expected.
	 * 
	 * Case 2: List rendezvouses joined by principal. No exception is expected.
	 * 
	 * Case 3: List rendezvouses joined by principal. IllegalArgumentException is expected.
	 */
	@Test
	public void driverRendezvousJoinedByPrincipal() {
		final Object testingData[][] = {
			{
				"user1", 2, null
			}, {
				"user2", 0, null
			}, {
				null, 0, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindJoinedByPrincipal((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the listing of rendezvouses.
	 * <p>
	 * This method defines the template used to test the listing of rendezvouses that an user is joined.
	 * 
	 * @param username
	 *            The username of the user that logs in.
	 * @param minimumLength
	 *            The minimum expected length of the list of rendezvouses that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindJoinedByPrincipal(final String username, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Collection<Rendezvous> res = this.rendezvousService.findRendezvousJoinedByPrincipal();
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of rendezvous joined by an user.
	 * <p>
	 * This method tests the listing of the rendezvous that an user has joined.
	 * 
	 * 
	 */
	@Test
	public void driverRendezvousJoinedByUserId() {
		final Object testingData[][] = {
			{
				"User1", 1, null
			}, {
				"User2", 0, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindJoinedByUserId(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the listing of rendezvouses.
	 * <p>
	 * This method defines the template used to test the listing of rendezvouses joined by an user.
	 * 
	 * @param username
	 *            The username of the user that logs in.
	 * @param minimumLength
	 *            The minimum expected length of the list of rendezvouses that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindJoinedByUserId(final int userId, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			Assert.notNull(this.userService.findOne(userId));
			final Collection<Rendezvous> res = this.rendezvousService.findRendezvousJoinedByUserId(userId);
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the listing of rendezvous joined by principal.
	 * <p>
	 * This method tests the listing of the rendezvous that an user is joined included with adult content.
	 * 
	 * Case 1: List rendezvouses joined by user 1. Nothing is expected.
	 * 
	 * Case 2: List rendezvouses joined by user 2. Nothing is expected.
	 */
	@Test
	public void driverRendezvousJoinedAdultByUserId() {
		final Object testingData[][] = {
			{
				"User1", 1, null
			}, {
				"User2", 0, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindJoinedAdultByUserId(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the listing of rendezvouses.
	 * <p>
	 * This method defines the template used to test the listing of rendezvouses joined by an user included with adult content.
	 * 
	 * @param username
	 *            The username of the user that logs in.
	 * @param minimumLength
	 *            The minimum expected length of the list of rendezvouses that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindJoinedAdultByUserId(final int userId, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			Assert.notNull(this.userService.findOne(userId));
			final Collection<Rendezvous> res = this.rendezvousService.findRendezvousJoinedByUserId(userId);
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of rendezvous in final mode.
	 * <p>
	 * This method tests the listing of the rendezvous in final mode.
	 */
	@Test
	public void testFindFinalRendezvous() {
		Assert.isTrue(this.rendezvousService.findFinalRendezvous().size() >= 1);
	}

	/**
	 * Tests the listing of rendezvous in final mode.
	 * <p>
	 * This method tests the listing of the rendezvous in final mode included with adult content.
	 */
	@Test
	public void testFindFinalAdultRendezvous() {
		Assert.isTrue(this.rendezvousService.findFinalRendezvousAdult().size() >= 3);
	}

	/**
	 * Tests the listing of rendezvous in final mode linked.
	 * <p>
	 * This method tests the listing of the rendezvous in final mode linked.
	 * 
	 * Case 1: List rendezvouses without adult content linked by rendezvous 1. Nothing is expected.
	 * 
	 * Case 2: List rendezvouses without adult content linked by rendezvous 2. Nothing is expected.
	 * 
	 * Case 3: List rendezvouses without adult content linked by rendezvous 5. Nothing is expected.
	 */
	@Test
	public void driverFinalRendezvousLinked() {
		final Object testingData[][] = {
			{
				"Rendezvous1", 1, null
			}, {
				"Rendezvous2", 0, null
			}, {
				"Rendezvous5", 0, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindFinalRendezvousLinked(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	/**
	 * Template for testing the listing of rendezvouses linked with another rendezvous.
	 * <p>
	 * This method defines the template used to test the listing of rendezvouses joined with another rendezvous.
	 * 
	 * @param username
	 *            The username of the user that logs in.
	 * @param minimumLength
	 *            The minimum expected length of the list of rendezvouses that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindFinalRendezvousLinked(final int rendezvousId, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Collection<Rendezvous> res = this.rendezvousService.findFinalRendezvousLinked(rendezvousId);
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of rendezvous linked.
	 * <p>
	 * This method tests the listing of the rendezvous linked with another rendezvous.
	 * 
	 * Case 1: List rendezvouses linked by rendezvous 1. Nothing is expected.
	 * 
	 * Case 2: List rendezvouses linked by rendezvous 2. Nothing is expected.
	 * 
	 * Case 3: List rendezvouses linked by rendezvous 5. Nothing is expected.
	 */
	@Test
	public void driverFinalRendezvousLinkedAdult() {
		final Object testingData[][] = {
			{
				"Rendezvous1", 1, null
			}, {
				"Rendezvous2", 1, null
			}, {
				"Rendezvous5", 0, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindFinalRendezvousAdultLinked(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	/**
	 * Template for testing the listing of rendezvouses linked with another rendezvous.
	 * <p>
	 * This method defines the template used to test the listing of rendezvouses joined with another rendezvous included with adult content.
	 * 
	 * @param username
	 *            The username of the user that logs in.
	 * @param minimumLength
	 *            The minimum expected length of the list of rendezvouses that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindFinalRendezvousAdultLinked(final int rendezvousId, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Collection<Rendezvous> res = this.rendezvousService.findFinalRendezvousLinkedAdult(rendezvousId);
			Assert.notNull(res);
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of rendezvous linked.
	 * <p>
	 * This method tests the listing of the rendezvous linked with another rendezvous. Not necessary that rendezvouses are in final mode to be shown.
	 * 
	 * Case 1: List rendezvouses linked by rendezvous 1. Nothing is expected.
	 * 
	 * Case 2: List rendezvouses linked by rendezvous 2. Nothing is expected.
	 * 
	 * Case 3: List rendezvouses linked by rendezvous 5. Nothing is expected.
	 * 
	 * Note: In this test, the method of the service used, return all rendezvouses linked included rendezvouses that not are in final mode.
	 */
	@Test
	public void driverAllRendezvousLinked() {
		final Object testingData[][] = {
			{
				"Rendezvous1", 1, null
			}, {
				"Rendezvous2", 1, null
			}, {
				"Rendezvous5", 1, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindAllRendezvousLinked(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	/**
	 * Template for testing the listing of rendezvouses linked with another rendezvous.
	 * <p>
	 * This method defines the template used to test the listing of rendezvouses linked with another rendezvous. Not necessary that rendezvouses are in final mode to be shown.
	 * 
	 * @param username
	 *            The username of the user that logs in.
	 * @param minimumLength
	 *            The minimum expected length of the list of rendezvouses that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindAllRendezvousLinked(final int rendezvousId, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Collection<Rendezvous> res = this.rendezvousService.findAllRendezvousLinked(rendezvousId);
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the displaying of rendezvous by comment.
	 * <p>
	 * This method finds a rendezvous that has the comment.
	 * 
	 * Case 1: Search of rendezvous 1 by comment 1. Nothing is expected.
	 * 
	 * Case 2: Search of rendezvous 2 by comment 2. Nothing is expected.
	 * 
	 * Case 3: Search of rendezvous 3 by comment 3. IllegalArgumentException is expected.
	 */
	@Test
	public void driverRendezvousByCommentId() {
		final Object testingData[][] = {
			{
				"Comment1", "Rendezvous1", null
			}, {
				"Comment2", "Rendezvous2", null
			}, {
				"Comment3", "Rendezvous3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindRendezvousByComment(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	/**
	 * Template for testing the displaying of rendezvouses by comment.
	 * <p>
	 * This method defines the template used to test the displaying of rendezvous with the comment selected.
	 * 
	 * @param username
	 *            The username of the user that logs in.
	 * @param minimumLength
	 *            The minimum expected length of the list of rendezvouses that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindRendezvousByComment(final int commentId, final int rendezvousId, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Rendezvous res = this.rendezvousService.findRendezvousByCommentId(commentId);
			Assert.isTrue(res.getId() == rendezvousId);
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of rendezvouses available for requests.
	 * <p>
	 * This method tests the listing of rendezvouses available for request a service.
	 * 
	 * Case 1: List of rendezvouses available for request the service 1 by user 1. Nothing is expected.
	 * 
	 * Case 2: List of rendezvouses available for request the service 2 by user 2. Nothing is expected.
	 * 
	 * Case 3: List of rendezvouses available for request the service 1 by user 2. Nothing is expected.
	 * 
	 * Case 4: List of available for request the service 1 by anything. IllegalArgumentException is expected.
	 */
	@Test
	public void driverRendezvousForRequestByPrincipal() {
		final Object testingData[][] = {
			{
				"user1", "Service1", 0, null
			}, {
				"user1", "Service2", 2, null
			}, {
				"user2", "Service1", 0, null
			}, {
				null, "Service1", 0, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindRendezvousForRequestByPrincipal((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (int) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	/**
	 * Template for testing the listing of rendezvouses availables for request a service.
	 * <p>
	 * This method defines the template used to test the listing of rendezvouses availables for request a service
	 * 
	 * @param username
	 *            The username of the user that logs in.
	 * @param serviceId
	 *            The id of the service that the user want to request for a rendezvous.
	 * @param minimumLength
	 *            The minimum expected length of the list of rendezvouses that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindRendezvousForRequestByPrincipal(final String username, final int serviceId, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Collection<Rendezvous> res = this.rendezvousService.findRendezvousforRequestByPrincipal(serviceId);
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of rendezvouses by categories.
	 * <p>
	 * This method tests de listing of rendezvouses that request services with a category.
	 * 
	 * Case 1: List the rendezvouses with Category 1. Nothing is expected.
	 * 
	 * Case 2: List the rendezvouses with Category 2. Nothing is expected.
	 * 
	 * Case 3: List the rendezvouses with Category 3. Nothing is expected.
	 */
	@Test
	public void driverRendezvousByCategory() {
		final Object testingData[][] = {
			{
				"Category1", 0, null
			}, {
				"Category2", 1, null
			}, {
				"Category3", 0, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindRendezvousByCategory(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	/**
	 * Template for testing the listing of rendezvouses.
	 * <p>
	 * This method defines the template used to test the listing of rendezvouses created by an user. Note that the rendezvouses are retrieved from the currently logged in user.
	 * 
	 * @param categoryId
	 *            The id of the category that user want to know its rendezvouses
	 * @param minimumLength
	 *            The minimum expected length of the list of rendezvouses that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindRendezvousByCategory(final int categoryId, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Collection<Rendezvous> res = this.rendezvousService.findRendezvousByCategoryId(categoryId);
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of rendezvouses by categories.
	 * <p>
	 * This method tests the listing of rendezvouses that request services with a category. Can contain adult content rendezvouses.
	 * 
	 * Case 1: List the rendezvouses with Category 1. Nothing is expected.
	 * 
	 * Case 2: List the rendezvouses with Category 2. Nothing is expected.
	 * 
	 * Case 3: List the rendezvouses with Category 3. Nothing is expected.
	 */
	@Test
	public void driverRendezvousAdultByCategory() {
		final Object testingData[][] = {
			{
				"Category1", 0, null
			}, {
				"Category2", 2, null
			}, {
				"Category3", 0, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindAdultRendezvousByCategory(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the listing of rendezvouses.
	 * <p>
	 * This method defines the template used to test the listing of rendezvouses created by an user. Can contains rendezvouses with adult content.
	 * 
	 * @param categoryId
	 *            The id of the category that user want to know its rendezvouses
	 * @param minimumLength
	 *            The minimum expected length of the list of rendezvouses that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindAdultRendezvousByCategory(final int categoryId, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Collection<Rendezvous> res = this.rendezvousService.findRendezvousWithAdultContentByCategoryId(categoryId);
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
