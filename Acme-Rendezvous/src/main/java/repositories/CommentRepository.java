
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("select c from Comment c join c.replies r where r.id = ?1")
	Comment findCommentByReplyId(int replyId);

	@Query("select c from Comment c where c.author.id = ?1")
	Collection<Comment> findCommentsByUserId(int userId);

	@Query("select c from Rendezvous r join r.comments c where r.id=?1 order by c.moment DESC")
	Collection<Comment> findCommentsOrdered(int rendezvousId);

}
