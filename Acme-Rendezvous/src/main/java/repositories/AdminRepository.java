
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.Manager;
import domain.Rendezvous;
import domain.Service;

@Repository
public interface AdminRepository extends JpaRepository<Administrator, Integer> {

	@Query("select a from Administrator a where a.userAccount.id = ?1")
	Administrator findAdminByUserAccountId(int UserAccountId);
	//Dashboard
	@Query("select avg(u.createdRendezvous.size) from User u")
	Double averageRendezvousCreatedPerUser();
	@Query("select sqrt(sum(u.createdRendezvous.size*u.createdRendezvous.size)/count(u.createdRendezvous.size)-(avg(u.createdRendezvous.size)*avg(u.createdRendezvous.size))) from User u")
	Double standartDeviationRendezvousCreatedPerUser();
	@Query("select (select count(uc) from User uc where uc.createdRendezvous is not empty)*1.0/count(u) from User u")
	Double ratioUsersWithCreatedRendezvous();
	@Query("select avg(r.attendants.size) from Rendezvous r")
	Double averageAttendantsPerRendezvous();
	@Query("select sqrt(sum(r.attendants.size * r.attendants.size)/count(r.attendants.size)-(avg(r.attendants.size)*avg(r.attendants.size))) from Rendezvous r")
	Double standartDeviationAttendantsPerRendezvous();
	@Query("select avg(u.reservedRendezvous.size) from User u")
	Double averageRendezvousReservedPerUser();
	@Query("select sqrt(sum(u.reservedRendezvous.size*u.reservedRendezvous.size)/count(u.reservedRendezvous.size)-(avg(u.reservedRendezvous.size)*avg(u.reservedRendezvous.size))) from User u ")
	Double standartDeviationRendezvousReservedPerUser();
	@Query("select r from Rendezvous r order by r.attendants.size DESC")
	List<Rendezvous> mostReservedRendezvous();
	@Query("select avg(r.announcements.size) from Rendezvous r")
	Double averageAnnouncementsPerRendezvous();
	@Query("select sqrt(sum(r.announcements.size*r.announcements.size)/count(r.announcements.size)-(avg(r.announcements.size)*avg(r.announcements.size))) from Rendezvous r")
	Double standartDeviationAnnouncementsPerRendezvous();
	@Query("select r from Rendezvous r where r.announcements.size > (select 0.75*avg(re.announcements.size) from Rendezvous re)")
	List<Rendezvous> rendezvousesAnnouncementsOver75();
	@Query("Select r from Rendezvous r where r.linkedRendezvous.size > (Select 1.1*avg(re.linkedRendezvous.size) from Rendezvous re)")
	List<Rendezvous> rendezvousesLinkedPlus10();
	@Query("select avg(r.questions.size) from Rendezvous r")
	Double averageQuestionsPerRendezvous();
	@Query("select sqrt(sum(r.questions.size*r.questions.size)/count(r.questions.size)-(avg(r.questions.size)*avg(r.questions.size))) from Rendezvous r")
	Double standartDeviationQuestionsPerRendezvous();
	@Query("select avg(q.answers.size) from Rendezvous r join r.questions q")
	Double averageAnswersPerRendezvous();
	@Query("select sqrt(sum(q.answers.size*q.answers.size)/count(q.answers.size)-(avg(q.answers.size)*avg(q.answers.size))) from Rendezvous r join r.questions q")
	Double standartDeviationAnswersPerRendezvous();
	@Query("Select avg(c.replies.size) from Comment c")
	Double averageRepliesPerComment();
	@Query("select sqrt(sum(c.replies.size*c.replies.size)/count(c.replies.size)-(avg(c.replies.size)*avg(c.replies.size))) from Comment c")
	Double standartDeviationRepliesPerComment();
	//nuevas queries
	@Query("select m from Manager m where m.services.size > (select avg(mag.services.size) from Manager mag)")
	List<Manager> managersMoreThanAvg();
	@Query("select s from Service s where s.manager.id=?")
	List<Service> cancelledbyManager(int managerId);
	//falta managers con m�s servicios cancelados
	@Query("select s.manager from Service s where cancelled=true group by s.manager order by count(s) DESC")
	List<Manager> managersMostCancelled();
	@Query("select s from Service s order by s.requests.size DESC")
	List<Service> bestSellingServices();
	//average categories per rendezvous
	@Query("select count(distinct req.service.category) from Rendezvous r join r.requests req group by r")
	List<Long> averageCategoriesPerRendezvous();
	@Query("select avg(c.services.size) from Category c")
	Double averageServicePerCategory();
	@Query("select avg(r.requests.size) from Rendezvous r")
	Double averageRequestsPerRendezvous();
	@Query("select sqrt(sum(r.requests.size*r.requests.size)/count(r.requests.size)-(avg(r.requests.size)*avg(r.requests.size))) from Rendezvous r")
	Double standartDeviationRequestsPerRendezvous();
	@Query("select min(r.requests.size) from Rendezvous r")
	Double minRequestsPerRendezvous();
	@Query("select max(r.requests.size) from Rendezvous r")
	Double maxRequestsPerRendezvous();
}
