
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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

	@Autowired
	Validator			validator;


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
			final domain.Service old = this.serviceRepository.findOne(service.getId());
			old.getCategory().getServices().remove(old);
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
		Assert.isTrue(!service.isCancelled());
		Assert.isTrue(this.managerService.findByPrincipal().getServices().contains(service));
		this.managerService.findByPrincipal().getServices().remove(service);
		service.getCategory().getServices().remove(service);
		service.getCategory().getServices().remove(service);
		this.serviceRepository.delete(service.getId());

	}
	public void cancel(final int serviceId) {
		Assert.isTrue(this.findOne(serviceId).isCancelled() == false);
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
	public Collection<domain.Service> findServicesByCategory(final int categoryId) {
		return this.serviceRepository.findServicesByCategory(categoryId);
	}

	public domain.Service reconstruct(final domain.Service service, final BindingResult binding) {
		domain.Service result;
		if (service.getId() == 0) {
			result = service;
			result.setRequests(new ArrayList<Request>());
		} else {
			result = this.serviceRepository.findOne(service.getId());
			result.setName(service.getName());
			result.setDescription(service.getDescription());
			result.setPictureUrl(service.getPictureUrl());
			result.setPrice(service.getPrice());
			result.setCategory(service.getCategory());

		}

		this.validator.validate(result, binding);
		return result;
	}
}
