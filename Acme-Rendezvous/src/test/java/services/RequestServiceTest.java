
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
import domain.Request;
import domain.Service;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RequestServiceTest extends AbstractTest {

	@Autowired
	private RequestService		requestService;
	@Autowired
	private ServiceService		serviceService;
	@Autowired
	private RendezvousService	rendezvousService;


	/**
	 * Tests requesting a service for a rendezvous.
	 * <p>
	 * This method tests requesting a service for a rendezvous a user has created. Functional requirement 4.An actor who is authenticated as a user must be able to:
	 * <p>
	 * Request a service for one of the rendezvouses that he or she's created. He or she must specify a valid credit card in every request for a service. Optionally, he or she can provide some comments in the request.
	 */
	@Test
	public void driverSaveRequest() {
		final Object testingData[][] = {
			{
				"Rendezvous1", "Service2", "user1", null
			}, {
				"Rendezvous1", "Service1", "user1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSave(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * Template for testing the request for a service.
	 * <p>
	 * This method defines the template used for the tests that check the saving of a new request data.
	 * 
	 * @param rendezvousId
	 *            The id of the rendezvous for which the user is requesting the service
	 * @param serviceId
	 *            The id of the service associated with the request. A rendezvous can only have one request per service.
	 * @param username
	 *            The username of the user who is making the request.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateSave(final int rendezvousId, final int serviceId, final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Request res = this.requestService.create(this.serviceService.findOne(serviceId));
			res.setComment("comment");
			res.setCreditCard(new ArrayList<Request>(this.requestService.findAll()).get(0).getCreditCard());
			res.setRendezvous(this.rendezvousService.findOne(rendezvousId));
			final Request saved = this.requestService.save(res);
			Assert.notNull(saved);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the finding of one request.
	 * <p>
	 * This method checks that a request's information can be accessed.
	 */
	@Test
	public void driverFindOneRequest() {
		final Object testingData[][] = {
			{
				"Request1", null
			}, {
				"Request2", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindOne(super.getEntityId((String) testingData[i][0]), (Class<?>) testingData[i][1]);
	}
	/**
	 * Template for testing the finding of one request.
	 * <p>
	 * This method defines the template used for the tests that check the finding of one request.
	 * 
	 * @param requestId
	 *            The id of the request that we want to find. In order to test what would happen
	 *            when the method receives an invalid id, we will set the requestId value to null when the manager received is Request2
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindOne(Integer requestId, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Request request2 = new ArrayList<Request>(this.requestService.findAll()).get(1);
			if (this.requestService.findOne(requestId).equals(request2))
				requestId = null;
			final Request req = this.requestService.findOne(requestId);
			Assert.notNull(req);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the listing of requests.
	 * <p>
	 * This method tests the listing of the requests stored in the database.
	 */
	@Test
	public void testFindAllRequests() {
		final Collection<Request> res = this.requestService.findAll();
		Assert.notNull(res);
		Assert.isTrue(res.size() > 0);
	}
	/**
	 * Tests the listing of requests related to a service.
	 * <p>
	 * This method tests the listing of the requests related to a service stored in the database.
	 * 
	 */
	@Test
	public void driverFindRequestByService() {
		final Object testingData[][] = {
			{
				"Service1", null
			}, {
				"Service2", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindRequestByService(super.getEntityId((String) testingData[i][0]), (Class<?>) testingData[i][1]);
	}
	/**
	 * Template for testing the finding of the requests related to a service.
	 * <p>
	 * This method defines the template used for the tests that check the finding requests by serviceId.
	 * 
	 * @param serviceId
	 *            The id of the service whose request we want to find. In order to test what would happen
	 *            when the method receives an invalid id, we will set the serviceId value to null when the manager received is Service2
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindRequestByService(Integer serviceId, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Service service2 = new ArrayList<Service>(this.serviceService.findAll()).get(1);
			if (this.serviceService.findOne(serviceId).equals(service2))
				serviceId = null;
			final Collection<Request> req = this.requestService.findRequestByServiceId(serviceId);
			Assert.notNull(req);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the listing of requests one user has made.
	 * <p>
	 * This method tests the listing of the requests related to a user stored in the database.
	 * 
	 */
	@Test
	public void testFindRequestByPrincipal() {
		super.authenticate("user1");
		final Collection<Request> res = this.requestService.findRequestByPrincipal();
		Assert.notNull(res);
		super.unauthenticate();

	}

}
