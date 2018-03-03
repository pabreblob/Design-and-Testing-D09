
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Rendezvous;

@Repository
public interface RendezvousRepository extends JpaRepository<Rendezvous, Integer> {

	@Query("select u.createdRendezvous from User u where u.userAccount.id = ?1")
	Collection<Rendezvous> findRendezvousCreatedByUserAccountId(int id);
	@Query("select r from User u join u.createdRendezvous r where u.userAccount.id = ?1 and r.deleted = false")
	Collection<Rendezvous> findRendezvousNotDeletedCreatedByUserId(int id);
	@Query("select r from User u join u.reservedRendezvous r where u.id = ?1 and r.finalMode = true and r.deleted = false")
	Collection<Rendezvous> findRendezvousJoinedAdultByUserId(int id);
	@Query("select r from User u join u.reservedRendezvous r where u.userAccount.id = ?1 and r.finalMode = true and r.deleted = false")
	Collection<Rendezvous> findRendezvousJoinedByUserAccountId(int id);
	@Query("select r from User u join u.reservedRendezvous r where u.id = ?1 and r.finalMode = true and r.deleted = false and r.adultContent = false")
	Collection<Rendezvous> findRendezvousJoinedByUserId(int id);
	@Query("select r from Rendezvous r where r.finalMode = true and r.deleted = false")
	Collection<Rendezvous> findFinalRendezvousAdult();
	@Query("select r from Rendezvous r where r.adultContent = false and r.finalMode = true and r.deleted = false")
	Collection<Rendezvous> findFinalRendezvous();
	@Query("select lr from Rendezvous r join r.linkedRendezvous lr where lr.deleted = false and r.id = ?1")
	Collection<Rendezvous> findRendezvousLinkedAdult(int rendezId);
	@Query("select lr from Rendezvous r join r.linkedRendezvous lr where lr.adultContent = false and lr.deleted = false and r.id = ?1")
	Collection<Rendezvous> findRendezvousLinked(int rendezId);
	@Query("select r.linkedRendezvous from Rendezvous r where r.id = ?1")
	Collection<Rendezvous> findAllRendezvousLinked(int rendezId);
	@Query("select r from Rendezvous r join r.comments c where c.id = ?1 ")
	Rendezvous findRendezvousByCommentId(int commentId);

	//Announcements
	@Query("select r.announcements from Rendezvous r where r.id = ?1")
	Collection<Rendezvous> findAnnouncementsByRendezId(int rendezId);

}
