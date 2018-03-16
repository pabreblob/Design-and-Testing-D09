
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
import domain.Manager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ManagerServiceTest extends AbstractTest {

	@Autowired
	private ManagerService	managerService;


	/**
	 * Tests the creation of managers.
	 * <p>
	 * This method is used to test the creation of empty managers before passing them to the corresponding views.
	 * <p>
	 * Functional requirement 3:
	 * <p>
	 * An actor who is not authenticated must be able to:
	 * <p>
	 * 1. Register to the system as a manager.
	 */
	@Test
	public void testCreateManager() {

		final Manager res = this.managerService.create();
		Assert.notNull(res);

	}
	/**
	 * Tests the saving of manager data when registering as a manager.
	 * <p>
	 * This method tests the registration as a manager as it would be done by an anonymous user in the corresponding views. Functional requirement 3:
	 * <p>
	 * An actor who is not authenticated must be able to:
	 * <p>
	 * 1. Register to the system as a manager.
	 */
	@Test
	public void driverSaveManager() {
		final Object testingData[][] = {
			{
				"testManager1", null
			}, {
				null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSave((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing registering as a manager.
	 * <p>
	 * This method defines the template used for the tests that check the saving of a new manager data.
	 * 
	 * @param username
	 *            The username given to the manager. It must not be null. In the first test case, it is not null and therefore,
	 *            the user must register successfully as a manager. In the second scenario, the username is null and the registration
	 *            must fail.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateSave(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Manager res = this.managerService.create();
			res.getUserAccount().setUsername(username);
			res.getUserAccount().setPassword("password");
			res.setName("name");
			res.setSurname("surname");
			res.setVat("ES12345678Z");
			res.setEmail("test@test.com");
			res.setAddress(null);
			res.setPhone(null);
			res.setBirthdate(new Date(System.currentTimeMillis() - 100000000));
			final Manager m = this.managerService.save(res);
			final Manager found = this.managerService.findOne(m.getId());
			Assert.notNull(m);
			Assert.isTrue(found.equals(m));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the finding of one manager.
	 * <p>
	 * This method checks that the profile of a manager can be accessed to.
	 */
	@Test
	public void driverFindOneManager() {
		final Object testingData[][] = {
			{
				"Manager1", null
			}, {
				"Manager2", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindOne(super.getEntityId((String) testingData[i][0]), (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing the finding of one manager.
	 * <p>
	 * This method defines the template used for the tests that check the finding of one manager.
	 * 
	 * @param managerId
	 *            The id of the manager that we want to find. In order to test what would happen
	 *            when the method receives an invalid id, we will set the managerId value to null when the manager received is Manager2
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindOne(Integer managerId, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Manager manager2 = new ArrayList<Manager>(this.managerService.findAll()).get(1);
			if (this.managerService.findOne(managerId).equals(manager2))
				managerId = null;
			final Manager m = this.managerService.findOne(managerId);
			Assert.notNull(m);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the listing of managers.
	 * <p>
	 * This method tests the listing of the managers stored in the database.
	 */
	@Test
	public void testFindAllManagers() {
		final Collection<Manager> res = this.managerService.findAll();
		Assert.notNull(res);
		Assert.isTrue(res.size() > 0);
	}
	/**
	 * Tests finding the manager who is logged in.
	 */
	@Test
	public void testFindByPrincipal() {
		super.authenticate("manager1");
		final Manager res = this.managerService.findByPrincipal();
		Assert.isTrue(res.getId() == new ArrayList<Manager>(this.managerService.findAll()).get(0).getId());
		super.unauthenticate();

	}
}
