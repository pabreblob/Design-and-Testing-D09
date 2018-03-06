
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select r from Service s join s.requests r where s.id = ?1 and r.rendezvous.deleted = false")
	Collection<Request> findRequestByServiceId(int serviceId);

}
