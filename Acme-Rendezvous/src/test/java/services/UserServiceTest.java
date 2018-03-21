
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

import security.UserAccount;
import utilities.AbstractTest;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UserServiceTest extends AbstractTest {

	@Autowired
	private UserService	userService;


	/**
	 * Tests the creation of users.
	 * <p>
	 * This method is used to test the creation of empty users before passing them to the corresponding views.
	 * <p>
	 * Functional requirement :
	 * <p>
	 * An actor who is not authenticated must be able to:
	 * <p>
	 * Register to the system as an user.
	 */
	@Test
	public void testCreateUser() {

		final User res = this.userService.create();
		Assert.notNull(res);

	}
	/**
	 * Tests the saving of user data when registering as an user.
	 * <p>
	 * This method tests the registration as an userr as it would be done by an anonymous actor in the corresponding views. Functional requirement:
	 * <p>
	 * An actor who is not authenticated must be able to:
	 * <p>
	 * Register to the system as an user.
	 * <p>
	 * Case 1: An actor registers as an user and inputs a valid username. The registration is done succesfully. <br>
	 * Case 2: An actor registers as an user and does not input any username. The registration is expected to fail.
	 */
	@Test
	public void driverSaveUser() {
		final Object testingData[][] = {
			{
				"testUser1", null
			}, {
				null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSave((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing registering as an user.
	 * <p>
	 * This method defines the template used for the tests that check the saving of a new user data.
	 * 
	 * @param username
	 *            The username given to the user. It must not be null. In the first test case, it is not null and therefore,
	 *            the actor must register successfully as an user. In the second scenario, the username is null and the registration
	 *            must fail.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateSave(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final User res = this.userService.create();
			res.getUserAccount().setUsername(username);
			res.getUserAccount().setPassword("password");
			res.setName("name");
			res.setSurname("surname");
			res.setEmail("test@test.com");
			res.setAddress(null);
			res.setPhone(null);
			res.setBirthdate(new Date(System.currentTimeMillis() - 100000000));
			final User u = this.userService.save(res);
			final User found = this.userService.findOne(u.getId());
			Assert.notNull(u);
			Assert.isTrue(found.equals(u));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the finding of one user.
	 * <p>
	 * This method checks that the profile of an user can be accessed to.
	 * <p>
	 * Case 1: The system looks for an existing user and retrieves his data succesfully. <br>
	 * Case 2: The system looks for a non existent user and is expected to fail when trying to retrieve his data.
	 */
	@Test
	public void driverFindOneUser() {
		final Object testingData[][] = {
			{
				"User1", null
			}, {
				"User2", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindOne(super.getEntityId((String) testingData[i][0]), (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing the finding of one user.
	 * <p>
	 * This method defines the template used for the tests that check the finding of one user.
	 * 
	 * @param userId
	 *            The id of the user that we want to find. In order to test what would happen
	 *            when the method receives an invalid id, we will set the userId value to null when the user received is user2
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindOne(Integer userId, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final User user2 = new ArrayList<User>(this.userService.findAll()).get(1);
			if (this.userService.findOne(userId).equals(user2))
				userId = null;
			final User u = this.userService.findOne(userId);
			Assert.notNull(u);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the listing of users.
	 * <p>
	 * This method tests the listing of the users stored in the database.
	 */
	@Test
	public void testFindAllUsers() {
		final Collection<User> res = this.userService.findAll();
		Assert.notNull(res);
		Assert.isTrue(res.size() > 0);
	}
	/**
	 * Tests finding the user who is logged in.
	 */
	@Test
	public void testFindByPrincipal() {
		super.authenticate("user1");
		final User res = this.userService.findByPrincipal();
		Assert.isTrue(res.getId() == new ArrayList<User>(this.userService.findAll()).get(0).getId());
		super.unauthenticate();

	}
	/**
	 * Tests finding users by user account.
	 */
	@Test
	public void testFindByUserAccount() {
		super.authenticate("user1");
		final UserAccount ua = this.userService.findByPrincipal().getUserAccount();
		final User found = this.userService.findByUserAccountId(ua.getId());
		Assert.notNull(found);
		super.authenticate(null);
	}

}
