
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Administrator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdminServiceTest extends AbstractTest {

	@Autowired
	private AdminService	adminService;


	/**
	 * Tests the finding of one administrator.
	 * <p>
	 * This method checks that the profile of a manager can be accessed to.
	 */
	@Test
	public void testFindOne() {
		final Administrator res = this.adminService.create();
		res.setName("name1");
		res.setSurname("surname1");
		res.setEmail("email1@ejemplo.com");
		res.setPhone("123456792");
		res.setAddress("test");
		final Date birthdate = new Date(System.currentTimeMillis() - 100000000);
		res.setBirthdate(birthdate);
		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		final Administrator saved = this.adminService.save(res);
		final Administrator found = this.adminService.findOne(saved.getId());
		Assert.isTrue(found.equals(saved));
	}
	/**
	 * Tests the listing of the system administrators.
	 * <p>
	 * This method checks that the administrators can be listed.
	 */
	@Test
	public void testFindAll() {
		final Administrator res = this.adminService.create();
		res.setName("name1");
		res.setSurname("surname1");
		res.setEmail("email1@ejemplo.com");
		res.setPhone("123456792");
		res.setAddress("test");
		final Date birthdate = new Date(System.currentTimeMillis() - 100000000);
		res.setBirthdate(birthdate);
		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		final Administrator saved = this.adminService.save(res);
		Assert.isTrue(this.adminService.findAll().contains(saved));
	}
	/**
	 * Tests finding the administrator who is logged in.
	 */
	@Test
	public void testFindByPrincipal() {
		super.authenticate("admin");
		final Administrator principal = this.adminService.findByPrincipal();
		final Administrator aux = this.adminService.findOne(principal.getId());
		Assert.isTrue(principal.equals(aux));
	}
	/**
	 * Tests finding administrators by user account.
	 */
	@Test
	public void testFindByUserAccount() {
		super.authenticate("admin");
		final UserAccount ua = this.adminService.findByPrincipal().getUserAccount();
		final Administrator found = this.adminService.findByUserAccountId(ua.getId());
		Assert.notNull(found);
		super.authenticate(null);
	}
	/**
	 * Tests the method related to the dashboard information an administrator must have access to.
	 * <p>
	 * This method tests that only an administrator can access to dashboard related services. An actor not authenticated at admin will not be able to use these services.
	 * <p>
	 * Case 1: An actor logged in as an administrator tries to display the dashboard information. The information is displayed succesfully <br>
	 * Case 2: An actor who is not an administrator tries to display the dashboard information. This is expected to fail.
	 */
	@Test
	public void driverDashboard() {
		final Object testingData[][] = {
			{
				"admin", null
			}, {
				"user1", IllegalArgumentException.class
			}, {
				null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDashboard((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}
	/**
	 * Template for testing dashboard methods.
	 * <p>
	 * This method defines the template used for the tests that check the dashboard functions.
	 * 
	 * @param username
	 *            The username of the user trying to access the dashboard. If the user is not an administrator, the test is expected to fail
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateDashboard(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		super.authenticate(username);

		try {
			this.adminService.getAverageRendezvousCreatedPerUser();
			this.adminService.getStandartDeviationRendezvousCreatedPerUser();
			this.adminService.getRatioUsersWithCreatedRendezvous();
			this.adminService.getAverageAttendantsPerRendezvous();
			this.adminService.getAverageRendezvousReservedPerUser();
			this.adminService.getStandartRendezvousReservedPerUser();
			this.adminService.getMostReservedRendezvous();
			this.adminService.getAverageAnnouncementsPerRendezvous();
			this.adminService.getStandartDeviationAnnouncementsPerRendezvous();
			this.adminService.getRendezvousesAnnouncementsOver75();
			this.adminService.getRendezvousesLinkedPlus10();
			this.adminService.getAverageQuestionsPerRendezvous();
			this.adminService.getStandartDeviationQuestionsPerRendezvous();
			this.adminService.getAverageAnswersPerRendezvous();
			this.adminService.getStandartDeviationAnswersPerRendezvous();
			this.adminService.getAverageRepliesPerComment();
			this.adminService.getStandartDeviationRepliesPerComment();
			this.adminService.getAverageRequestsPerRendezvous();
			this.adminService.getStandartRequestsPerRendezvous();
			this.adminService.getMinRequestsPerRendezvous();
			this.adminService.getMaxRequestsPerRendezvous();
			this.adminService.getAverageServicePerCategory();
			this.adminService.getBestSellingServices();
			this.adminService.getTopSellingServices();
			this.adminService.getManagersMoreThanAvgService();
			this.adminService.getAverageCategoryPerRendezvous();
			this.adminService.getManagersMostCancelled();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
