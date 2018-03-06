
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ServiceRepository;
import domain.Manager;
import domain.Request;

@Service
@Transactional
public class ServiceService {

	@Autowired
	ServiceRepository	serviceRepository;

	@Autowired
	ManagerService		managerService;


	public domain.Service create() {
		final domain.Service res = new domain.Service();
		res.setRequests(new ArrayList<Request>());
		return res;
	}

	public domain.Service save(final domain.Service service) {
		final Manager manager = this.managerService.findByPrincipal();
		if (service.getId() != 0) {
			//Comprobar que el manager lo tiene en el servicio
			Assert.isTrue(manager.getServices().contains(service));
			Assert.isTrue(this.findOne(service.getId()).isCancelled() == false);
		}
		final domain.Service saved = this.serviceRepository.save(service);
		//Guardar en el manager
		if (manager.getServices().contains(saved))
			manager.getServices().remove(saved);
		manager.getServices().add(saved);
		saved.getCategory().getServices().add(saved);

		return saved;
	}

	public domain.Service findOne(final int serviceId) {
		Assert.isTrue(serviceId != 0);
		return this.serviceRepository.findOne(serviceId);
	}

	public Collection<domain.Service> findAll() {
		return this.serviceRepository.findAll();
	}

	public void delete(final domain.Service service) {
		//Comprobar que nadie usa el servicio
		Assert.isTrue(service.getRequests().size() == 0);
		this.managerService.findByPrincipal().getServices().remove(service);
		service.getCategory().getServices().remove(service);
		service.getCategory().getServices().remove(service);
		this.serviceRepository.delete(service.getId());

	}

	public void cancel(final int serviceId) {
		final domain.Service s = this.findOne(serviceId);
		s.setCancelled(true);
		this.serviceRepository.save(s);

	}

	public Collection<domain.Service> findAvailableServices() {
		return this.serviceRepository.findAvailableServices();
	}

	public Collection<domain.Service> findServicesCreatedByPrincipal() {
		return this.serviceRepository.findServicesCreatedByManagerId(this.managerService.findByPrincipal().getId());
	}

	public Collection<domain.Service> findServicesByRendezvousId(final int id) {
		return this.serviceRepository.findServicesByRendezvousId(id);
	}
}
