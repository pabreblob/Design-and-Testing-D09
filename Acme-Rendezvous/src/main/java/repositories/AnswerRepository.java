
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

	@Query("Select a from Answer a where a.author.id = ?1 and a.question.rendezvous.id = ?2")
	Collection<Answer> findAnswersByUserAndRendezvous(int userId, int rendezvousId);

}
