
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReplyRepository;
import domain.Administrator;
import domain.Comment;
import domain.Rendezvous;
import domain.Reply;
import domain.User;

@Service
@Transactional
public class ReplyService {

	@Autowired
	private ReplyRepository		replyRepository;
	@Autowired
	private UserService			userService;
	@Autowired
	private CommentService		commentService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private AdminService		adminService;
	@Autowired
	private Validator			validator;


	public Reply create(final Comment comment) {
		final Rendezvous rendezvous = this.rendezvousService.findRendezvousByCommentId(comment.getId());
		final User author = this.userService.findByPrincipal();
		Assert.isTrue(!rendezvous.isDeleted());
		Assert.isTrue(author.getReservedRendezvous().contains(rendezvous));

		final Reply res = new Reply();
		final Date moment = new Date(System.currentTimeMillis() - 1000);

		res.setMoment(moment);
		res.setAuthor(author);

		return res;
	}

	public Reply save(final Reply reply, final Comment comment) {
		Assert.notNull(reply);
		Assert.notNull(comment);
		Assert.isTrue(reply.getAuthor().equals(this.userService.findByPrincipal()));
		final Rendezvous rendezvous = this.rendezvousService.findRendezvousByCommentId(comment.getId());
		Assert.isTrue(!rendezvous.isDeleted());

		final Reply res = this.replyRepository.save(reply);
		comment.getReplies().add(res);

		return res;
	}

	public Reply findOne(final int replyId) {
		return this.replyRepository.findOne(replyId);
	}

	public Collection<Reply> findAll() {
		return this.replyRepository.findAll();
	}

	public void delete(final Reply reply) {
		Assert.notNull(reply);
		final Administrator principal = this.adminService.findByPrincipal();
		Assert.notNull(principal);
		final Comment comment = this.commentService.findCommentByReplyId(reply.getId());
		Assert.isTrue(comment.getReplies().contains(reply));
		comment.getReplies().remove(reply);
		this.replyRepository.delete(reply);

	}

	//Other

	public Collection<Reply> findRepliesByUserId(final int userId) {
		return this.replyRepository.findRepliesByUserId(userId);
	}

	public Reply reconstruct(final Reply reply, final BindingResult binding) {
		reply.setAuthor(this.userService.findByPrincipal());
		this.validator.validate(reply, binding);
		return reply;
	}

}
