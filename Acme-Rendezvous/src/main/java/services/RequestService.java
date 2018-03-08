
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import domain.Request;

@Service
@Transactional
public class RequestService {

	@Autowired
	RequestRepository	requestRepository;

	@Autowired
	ServiceService		serviceService;

	@Autowired
	UserService			userService;


	public Request create(final domain.Service s) {
		final Request request = new Request();
		request.setService(s);
		return request;
	}

	public Request save(final Request request) {
		for (final Request r : request.getRendezvous().getRequests())
			Assert.isTrue(!r.getService().equals(request.getService()));
		final Request saved = this.requestRepository.save(request);
		saved.getService().getRequests().add(saved);
		saved.getRendezvous().getRequests().add(saved);
		return saved;
	}

	public Request findOne(final int requestId) {
		Assert.isTrue(requestId != 0);
		return this.requestRepository.findOne(requestId);
	}

	public Collection<Request> findAll() {
		return this.requestRepository.findAll();
	}

	public Collection<Request> findRequestByServiceId(final int serviceId) {
		return this.requestRepository.findRequestByServiceId(serviceId);
	}

	public Collection<Request> findRequestByPrincipal() {
		return this.requestRepository.findRequestByUserId(this.userService.findByPrincipal().getId());
	}
}
