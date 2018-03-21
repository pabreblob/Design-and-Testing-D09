
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
import domain.Rendezvous;
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
	 * <p>
	 * Afterwards, a manager will try to display the list of requests associated with the service and will check that the new request appears in that list. When using the Service 6, our test will treat it as an nonexistent service. Same will happen with
	 * Rendezvous 6
	 * <p>
	 * Case 1: An user requests a service for one of his rendezvous. The request is made successfully<br>
	 * Case 2: An user requests a service for one of his rendezvous which already had that service requested. The request is expected to fail.<br>
	 * Case 3: An user requests a service for a rendezvous he has not created.The request is expected to fail.<br>
	 * Case 4: An user requests a service for a rendezvous which has been deleted.The request is expected to fail.<br>
	 * Case 5: An user requests a service for a rendezvous which is not in final version.The request is expected to fail.<br>
	 * Case 6: An user requests a service which does not exist.The request is expected to fail.<br>
	 * Case 7: An user requests a service for a rendezvous which does not exist.The request is expected to fail.<br>
	 * Case 8: An unauthenticated actor requests a service.The request is expected to fail.<br>
	 * Case 9: An administrator requests a service.The request is expected to fail.<br>
	 * Case 10: An user requests a service for one of his rendezvous. The request is made successfully<br>
	 */
	@Test
	public void driverSaveRequest() {
		final Object testingData[][] = {
			{
				"Rendezvous1", "Service2", "user1", "manager1", null
			}, {
				"Rendezvous1", "Service1", "user1", "manager1", IllegalArgumentException.class
			}, {
				"Rendezvous1", "Service2", "user2", "manager1", IllegalArgumentException.class
			}, {
				"Rendezvous3", "Service1", "user2", "manager1", IllegalArgumentException.class
			}, {
				"Rendezvous5", "Service1", "user2", "manager1", IllegalArgumentException.class
			}, {
				"Rendezvous1", "Service6", "user1", "manager2", NullPointerException.class
			}, {
				"Rendezvous6", "Service3", "user1", "manager2", NullPointerException.class
			}, {
				"Rendezvous1", "Service1", null, "manager1", IllegalArgumentException.class
			}, {
				"Rendezvous1", "Service1", "admin", "manager1", IllegalArgumentException.class
			}, {
				"Rendezvous2", "Service2", "user1", "manager1", null
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSave(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
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
	 * @param manager
	 *            The manager whose service is being requested.
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateSave(Integer rendezvousId, Integer serviceId, final String username, final String manager, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Request res = this.requestService.create(this.serviceService.findOne(serviceId));
			final Service service6 = new ArrayList<Service>(this.serviceService.findAll()).get(5);
			final Rendezvous rendezvous6 = new ArrayList<Rendezvous>(this.rendezvousService.findAll()).get(5);
			if (this.serviceService.findOne(serviceId).equals(service6))
				serviceId = null;

			if (this.rendezvousService.findOne(rendezvousId).equals(rendezvous6))
				rendezvousId = null;
			res.setComment("comment");
			res.setService(this.serviceService.findOne(serviceId));
			res.setRendezvous(this.rendezvousService.findOne(rendezvousId));
			res.setCreditCard(new ArrayList<Request>(this.requestService.findAll()).get(0).getCreditCard());
			res.setRendezvous(this.rendezvousService.findOne(rendezvousId));
			final Request saved = this.requestService.save(res);
			Assert.notNull(saved);
			super.unauthenticate();
			super.authenticate(manager);
			Assert.isTrue(this.requestService.findRequestByServiceId(serviceId).contains(saved));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the finding of one request.
	 * <p>
	 * This method checks that a request's information can be accessed.
	 * <p>
	 * Case 1: The system looks for an existing request and retrieves its data succesfully. <br>
	 * Case 2: The system looks for a non existent request and is expected to fail when trying to retrieve its data.
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
