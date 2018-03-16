
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
import domain.Manager;
import domain.Rendezvous;
import domain.Service;

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
			final Double averageRendezvousCreatedPerUser = this.adminService.getAverageRendezvousCreatedPerUser();
			final Double standartRendezvousCreatedPerUser = this.adminService.getStandartDeviationRendezvousCreatedPerUser();
			final Double ratioUsersWithCreatedRendezvous = this.adminService.getRatioUsersWithCreatedRendezvous();
			final Double averageAttendantsPerRendezvous = this.adminService.getAverageAttendantsPerRendezvous();
			final Double averageRendezvousReservedPerUser = this.adminService.getAverageRendezvousReservedPerUser();
			final Double standartRendezvousReservedPerUser = this.adminService.getStandartRendezvousReservedPerUser();
			final List<Rendezvous> mostReservedRendezvous = this.adminService.getMostReservedRendezvous();
			final Double averageAnnouncementsPerRendezvous = this.adminService.getAverageAnnouncementsPerRendezvous();
			final Double standartAnnouncementsPerRendezvous = this.adminService.getStandartDeviationAnnouncementsPerRendezvous();
			final List<Rendezvous> rendezvousesAnnouncementsOver75 = this.adminService.getRendezvousesAnnouncementsOver75();
			final List<Rendezvous> rendezvousesLinkedPlus10 = this.adminService.getRendezvousesLinkedPlus10();
			final Double averageQuestionsPerRendezvous = this.adminService.getAverageQuestionsPerRendezvous();
			final Double standartQuestionsPerRendezvous = this.adminService.getStandartDeviationQuestionsPerRendezvous();
			final Double averageAnswersPerRendezvous = this.adminService.getAverageAnswersPerRendezvous();
			final Double standartAnswersPerRendezvous = this.adminService.getStandartDeviationAnswersPerRendezvous();
			final Double averageRepliesPerComment = this.adminService.getAverageRepliesPerComment();
			final Double standartRepliesPerComment = this.adminService.getStandartDeviationRepliesPerComment();
			final Double averageRequestsPerRendezvous = this.adminService.getAverageRequestsPerRendezvous();
			final Double standartRequestsPerRendezvous = this.adminService.getStandartRequestsPerRendezvous();
			final Double minRequestsPerRendezvous = this.adminService.getMinRequestsPerRendezvous();
			final Double maxRequestsPerRendezvous = this.adminService.getMaxRequestsPerRendezvous();
			final Double averageServicePerCategory = this.adminService.getAverageServicePerCategory();
			final List<Service> bestSellingServices = this.adminService.getBestSellingServices();
			final List<Service> topSellingServices = this.adminService.getTopSellingServices();
			final List<Manager> managersMoreThanAvg = this.adminService.getManagersMoreThanAvgService();
			final Double averageCategoryPerRendezvous = this.adminService.getAverageCategoryPerRendezvous();
			final List<Manager> managersMostCancelled = this.adminService.getManagersMostCancelled();
			Assert.notNull(averageRendezvousCreatedPerUser);
			Assert.notNull(standartRendezvousCreatedPerUser);
			Assert.notNull(ratioUsersWithCreatedRendezvous);
			Assert.notNull(averageAttendantsPerRendezvous);
			Assert.notNull(averageRendezvousReservedPerUser);
			Assert.notNull(standartRendezvousReservedPerUser);
			Assert.notNull(mostReservedRendezvous);
			Assert.notNull(averageAnnouncementsPerRendezvous);
			Assert.notNull(standartAnnouncementsPerRendezvous);
			Assert.notNull(rendezvousesAnnouncementsOver75);
			Assert.notNull(rendezvousesLinkedPlus10);
			Assert.notNull(averageQuestionsPerRendezvous);
			Assert.notNull(standartQuestionsPerRendezvous);
			Assert.notNull(averageAnswersPerRendezvous);
			Assert.notNull(standartAnswersPerRendezvous);
			Assert.notNull(averageRepliesPerComment);
			Assert.notNull(standartRepliesPerComment);
			Assert.notNull(standartRequestsPerRendezvous);
			Assert.notNull(minRequestsPerRendezvous);
			Assert.notNull(maxRequestsPerRendezvous);
			Assert.notNull(averageServicePerCategory);
			Assert.notNull(averageRequestsPerRendezvous);
			Assert.notNull(bestSellingServices);
			Assert.notNull(topSellingServices);
			Assert.notNull(managersMoreThanAvg);
			Assert.notNull(averageCategoryPerRendezvous);
			Assert.notNull(managersMostCancelled);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
