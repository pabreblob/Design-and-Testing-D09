
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import domain.Administrator;
import domain.Comment;
import domain.Rendezvous;
import domain.Reply;
import domain.User;

@Service
@Transactional
public class CommentService {

	@Autowired
	private CommentRepository	commentRepository;
	@Autowired
	private UserService			userService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private AdminService		adminService;
	@Autowired
	private Validator			validator;


	public CommentService() {
		super();
	}

	public Comment create(final Rendezvous rendezvous) {
		final User author = this.userService.findByPrincipal();
		Assert.isTrue(author.getReservedRendezvous().contains(rendezvous));
		Assert.isTrue(!rendezvous.isDeleted());

		final Comment res = new Comment();
		final Date moment = new Date(System.currentTimeMillis() - 1000);
		final Collection<Reply> replies = new ArrayList<Reply>();

		res.setMoment(moment);
		res.setAuthor(author);
		res.setReplies(replies);

		return res;
	}

	public Comment save(final Comment comment, final Rendezvous rendezvous) {
		Assert.notNull(comment);
		Assert.notNull(rendezvous);
		Assert.isTrue(comment.getAuthor().equals(this.userService.findByPrincipal()));
		Assert.isTrue(!rendezvous.isDeleted());

		comment.setMoment(new Date(System.currentTimeMillis() - 1000));
		final Comment res = this.commentRepository.save(comment);

		rendezvous.getComments().add(res);

		return res;

	}

	public Comment findOne(final int commentId) {
		Assert.isTrue(commentId != 0);
		return this.commentRepository.findOne(commentId);
	}

	public Collection<Comment> findAll() {
		return this.commentRepository.findAll();
	}

	public void delete(final Comment comment) {
		Assert.notNull(comment);
		final Rendezvous rendezvous = this.rendezvousService.findRendezvousByCommentId(comment.getId());
		final Administrator principal = this.adminService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(rendezvous.getComments().contains(comment));
		rendezvous.getComments().remove(comment);
		comment.getReplies().removeAll(comment.getReplies());
		this.commentRepository.delete(comment);
	}
	//Other

	public Comment findCommentByReplyId(final int replyId) {
		return this.commentRepository.findCommentByReplyId(replyId);
	}

	public Collection<Comment> findCommentsByUserId(final int userId) {
		return this.commentRepository.findCommentsByUserId(userId);
	}

	public Collection<Comment> findCommentsOrdered(final int rendezvousId) {
		return this.commentRepository.findCommentsOrdered(rendezvousId);
	}

	public Comment reconstruct(final Comment comment, final BindingResult binding) {
		final Collection<Reply> replies = new ArrayList<Reply>();
		comment.setReplies(replies);
		comment.setAuthor(this.userService.findByPrincipal());
		this.validator.validate(comment, binding);
		return comment;
	}

}
