
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Category;
import domain.Service;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ServiceServiceTest extends AbstractTest {

	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private ManagerService	managerService;

	@Autowired
	private CategoryService	categoryService;


	/**
	 * Tests the creation of Services.
	 * <p>
	 * This method is used to test the creation of empty services before passing them to the corresponding views.
	 */
	@Test
	public void testCreateService() {
		super.authenticate("manager1");
		final Service res = this.serviceService.create();
		Assert.notNull(res);
		Assert.notNull(res.getRequests());
		super.unauthenticate();
	}

	/**
	 * Tests the saving of services.
	 * <p>
	 * This method tests the creation and later saving of services as it would be done by a manager in the corresponding views.
	 * 
	 * Case 1: Manager 1 create and save a service. Nothing is expected.
	 * 
	 * Case 2: Manager 3 create and save a service. Nothing is expected.
	 * 
	 * Case 1: User 1 create and save a service. NullPointerException is expected.
	 * 
	 * Case 1: Admin create and save a service. NullPointerException is expected.
	 */
	@Test
	public void driverSaveService() {
		final Object testingData[][] = {
			{
				"manager1", null
			}, {
				"manager2", null
			}, {
				"user1", NullPointerException.class
			}, {
				"admin", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSave((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing the saving of services.
	 * <p>
	 * This method defines the template used for the tests that check the saving of services.
	 * 
	 * @param username
	 *            The username of the user that logs in.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateSave(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Service res = this.serviceService.create();
			res.setName("name");
			res.setDescription("desctest");

			final Category c = this.categoryService.findOne(super.getEntityId("Category1"));
			res.setCategory(c);
			final Service saved = this.serviceService.save(res);
			Assert.isTrue(this.managerService.findByPrincipal().getServices().contains(saved));
			Assert.isTrue(this.categoryService.findOne(super.getEntityId("Category1")).getServices().contains(saved));
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the editing of services.
	 * <p>
	 * This method tests the edition of services as it would be done by a manager in the corresponding views.
	 * 
	 * Case 1: Service 1 is edited by manager 1 (creator). Nothing is expected.
	 * 
	 * Case 2: Service 2 is edited by manager 1 (creator). Nothing is expected.
	 * 
	 * Case 3: Service 1 is edited by manager 2. IllegalArgumentException is expected.
	 * 
	 * Case 4: Service 3 (deleted by admin) is edited by manager 2 (creator). IllegalArgumentException is expected.
	 * 
	 * Case 5: Service 1 is edited by admin. NullPointerException is expected.
	 */
	@Test
	public void driverEditService() {
		final Object testingData[][] = {
			{
				"manager1", "Service1", null
			}, {
				"manager1", "Service2", null
			}, {
				"manager2", "Service1", IllegalArgumentException.class
			}, {
				"manager2", "Service3", IllegalArgumentException.class
			}, {
				"admin", "Service1", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the editing of services.
	 * <p>
	 * This method defines the template used for the tests that check the editing of services.
	 * 
	 * @param username
	 *            The username of the user that logs in.
	 * @param serviceId
	 *            The id of the service that the user wants to edit
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateEdit(final String username, final int serviceId, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Service s = this.serviceService.findOne(serviceId);
			final String newName = "ServicioTestJunit";
			s.setName(newName);
			final Service saved = this.serviceService.save(s);
			Assert.isTrue(saved.getName().equals(newName));
			Assert.isTrue(this.categoryService.findOne(saved.getCategory().getId()).getServices().contains(saved));
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the finding of one service.
	 * <p>
	 * This method checks that a service stored in the database can be found.
	 * 
	 * Case 1: Find service 1. Nothing is expected.
	 * 
	 * Case 2: Find service 2. Nothing is expected.
	 */
	@Test
	public void driverFindOneService() {
		final Object testingData[][] = {
			{
				"Service1", null
			}, {
				"Service2", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindOne(super.getEntityId((String) testingData[i][0]), (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing the finding of one service.
	 * <p>
	 * This method defines the template used for the tests that check the finding of one service.
	 * 
	 * @param serviceId
	 *            The id of the service that user wants to find.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindOne(final Integer serviceId, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {

			final Service s = this.serviceService.findOne(serviceId);
			Assert.notNull(s);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the deletion of a service.
	 * <p>
	 * This method tests that a service can be saved and later deleted.
	 * 
	 * Case 1: Manager 1 delete a service created by him and not request. Nothing is expected
	 * 
	 * Case 2: Manager 2 delete a service created by manager 1. IllegalArgumentException is expected.
	 */
	@Test
	public void driverDeleteServices() {
		final Object testingData[][] = {
			{
				"manager1", "manager1", null
			}, {
				"manager1", "manager2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the deletion of a. service
	 * <p>
	 * This method defines the template used to test the deletion of a service.
	 * 
	 * @param username1
	 *            The username of the user that create the service .
	 * @param username2
	 *            The username of the user that delete the service .
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateDelete(final String username1, final String username2, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username1);
			final Service res = this.serviceService.create();
			res.setName("name");
			res.setDescription("desctest");
			final Category c = this.categoryService.findOne(super.getEntityId("Category1"));
			res.setCategory(c);
			final Service saved = this.serviceService.save(res);
			super.authenticate(null);

			super.authenticate(username2);
			this.serviceService.delete(saved);
			Assert.isTrue(!this.managerService.findByPrincipal().getServices().contains(saved));
			Assert.isTrue(!saved.getCategory().getServices().contains(saved));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the cancellation of a service.
	 * <p>
	 * This method tests that a service can be cancel if it has not been cancelled yet.
	 * 
	 * Case 1: Cancellation of a service 1. Nothing is expected.
	 * 
	 * Case 2: Cancellation of a service 2. Nothing is expected.
	 * 
	 * Case 3: Cancellation of a service 3 (it's cancelled). IllegalArgumentException is expected.
	 */
	@Test
	public void driverCancelServices() {
		final Object testingData[][] = {
			{
				"Service1", null
			}, {
				"Service2", null
			}, {
				"Service3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCancel(super.getEntityId((String) testingData[i][0]), (Class<?>) testingData[i][1]);
	}
	/**
	 * Template for testing the cancellation of a service.
	 * <p>
	 * This method defines the template used to test the cancellation of a service.
	 * 
	 * @param serviceId
	 *            The id of the service that we want to cancel
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateCancel(final int serviceId, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.serviceService.cancel(serviceId);
			Assert.isTrue(this.serviceService.findOne(serviceId).isCancelled());
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the listing of services.
	 * <p>
	 * This method tests the listing all of the services stored in the database.
	 */

	@Test
	public void testFindAllServices() {
		final Collection<Service> res = this.serviceService.findAll();
		Assert.notNull(res);
		Assert.isTrue(res.size() >= 3);
	}

	/**
	 * Tests the listing of services available.
	 * <p>
	 * This method tests the listing of the services stored in the database if they isn't cancelled.
	 */

	@Test
	public void testFindAllAvailableServices() {
		final Collection<Service> res = this.serviceService.findAvailableServices();
		Assert.notNull(res);
		Assert.isTrue(res.size() >= 2);
	}

	/**
	 * Tests the listing of services created by principal.
	 * <p>
	 * This method tests the listing of the services that a manager has created.
	 * 
	 * Case 1: List of services created by principal (Manager 1 is logged). Nothing is expected.
	 * 
	 * Case 2: List of services created by principal (Manager 2 is logged). Nothing is expected.
	 * 
	 * Case 3: List of services created by principal (User 1 is logged). NullPointerException is expected.
	 */
	@Test
	public void driverFindServicesCreatedByPrincipal() {
		final Object testingData[][] = {
			{
				"manager1", 2, null
			}, {
				"manager2", 1, null
			}, {
				"user1", 0, NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindByUserId((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the listing of services.
	 * <p>
	 * This method defines the template used to test the listing of services created by a manager. Note that the services are retrieved from the currently logged in user.
	 * 
	 * @param username
	 *            The username of the user that logs in.
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
			final Collection<Service> res = this.serviceService.findServicesCreatedByPrincipal();
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of services requested by a rendezvous.
	 * <p>
	 * This method tests the listing of services that a rendezvous has requested.
	 * 
	 * Case 1: List of services request by rendezvous 1. Nothing is expected.
	 * 
	 * Case 2: List of services request by rendezvous 2. Nothing is expected.
	 */

	@Test
	public void driverFindServicesByRendezvousId() {
		final Object testingData[][] = {
			{
				"Rendezvous1", 1, null
			}, {
				"Rendezvous2", 1, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindServicesByRendezvousId(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the listing of services requested by a rendezvous.
	 * <p>
	 * This method defines the template used to test the listing of services that a rendezvous has requested.
	 * 
	 * @param rendezId
	 *            The id of the rendezvous that we want to know its services.
	 * @param minimumLength
	 *            The minimum expected length of the list of services that the rendezvous has requested.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindServicesByRendezvousId(final int rendezId, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {

			final Collection<Service> res = this.serviceService.findServicesByRendezvousId(rendezId);
			Assert.notNull(res);
			Assert.isTrue(res.size() >= minimumLength);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of services associated with a category.
	 * <p>
	 * This method tests the listing of the services associated with a category.
	 * 
	 * Case 1: List of services associated with category 1. Nothing is expected.
	 * 
	 * Case 2: List of services associated with category 2. Nothing is expected.
	 * 
	 * Case 3: List of services associated with category 3. Nothing is expected.
	 */
	@Test
	public void driverFindServicesByCategoryId() {
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
			this.templateFindByCategoryId(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the listing of services associated with a category.
	 * <p>
	 * This method defines the template used to test the listing of services associates with a category.
	 * 
	 * @param categoryId
	 *            The id of the category that we want to know its services.
	 * @param minimumLength
	 *            The minimum expected length of the list of announcements that the user has created.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindByCategoryId(final int categoryId, final int minimumLength, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {

			final Collection<Service> res = this.serviceService.findServicesByCategory(categoryId);
			Assert.isTrue(res.size() >= minimumLength);
			super.authenticate(null);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
