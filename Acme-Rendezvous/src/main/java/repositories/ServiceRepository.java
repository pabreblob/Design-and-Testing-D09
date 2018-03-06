
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

	@Query("select s from Service s where s.cancelled = false")
	Collection<Service> findAvailableServices();

	@Query("select m.services from Manager m where m.id = ?1")
	Collection<Service> findServicesCreatedByManagerId(int id);

	@Query("select req.service from Rendezvous r join r.requests req  where r.id = ?1")
	Collection<Service> findServicesByRendezvousId(int id);

}
