
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Comment;
import domain.Rendezvous;
import domain.Reply;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class CommentTest extends AbstractTest {

	@Autowired
	private CommentService		commentService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserService			userService;
	@Autowired
	private ReplyService		replyService;


	@Test
	public void testCreate() {
		super.authenticate("User1");
		final Rendezvous r = new ArrayList<Rendezvous>(this.rendezvousService.findAll()).get(0);
		final Comment res = this.commentService.create(r);
		Assert.notNull(res);
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		final Comment r = new ArrayList<Comment>(this.commentService.findAll()).get(0);
		final Comment res = this.commentService.findOne(r.getId());
		Assert.notNull(res);
	}

	@Test
	public void testFindAll() {
		final Collection<Comment> res = this.commentService.findAll();
		Assert.notNull(res);
		Assert.notEmpty(res);
	}

	@Test
	public void testSave() {
		super.authenticate("User1");
		final Rendezvous r = new ArrayList<Rendezvous>(this.rendezvousService.findAll()).get(0);
		final Comment res = this.commentService.create(r);
		res.setText("texto");
		final Comment saved = this.commentService.save(res, r);
		Assert.notNull(saved);
		Assert.isTrue(r.getComments().contains(saved));
		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("Admin");
		final Rendezvous r = new ArrayList<Rendezvous>(this.rendezvousService.findAll()).get(0);
		final List<Comment> comments = new ArrayList<Comment>(r.getComments());
		final Comment c = comments.get(0);
		this.commentService.delete(c);
		Assert.isTrue(!r.getComments().contains(c));
		super.unauthenticate();
	}

	@Test
	public void testFindCommentsByUserId() {
		super.authenticate("User1");
		final User u = this.userService.findByPrincipal();
		final Collection<Comment> res = this.commentService.findCommentsByUserId(u.getId());
		for (final Comment c : res)
			Assert.isTrue(c.getAuthor().equals(u));
	}

	@Test
	public void testFindCommentByReplyId() {
		final Reply r = new ArrayList<Reply>(this.replyService.findAll()).get(0);
		final Comment c = this.commentService.findCommentByReplyId(r.getId());
		Assert.isTrue(c.getReplies().contains(r));

	}
	//Negative Tests

	@Test
	public void testDeleteUser() {
		boolean check = true;
		try {
			super.authenticate("User1");
			final Rendezvous r = new ArrayList<Rendezvous>(this.rendezvousService.findAll()).get(0);
			final List<Comment> comments = new ArrayList<Comment>(r.getComments());
			final Comment c = comments.get(0);
			this.commentService.delete(c);
			check = false;
		} catch (final Throwable oops) {
			Assert.isTrue(true);
		}
		Assert.isTrue(check);
	}

	@Test
	public void testDeleteUnauthenticated() {
		boolean check = true;
		try {
			final Rendezvous r = new ArrayList<Rendezvous>(this.rendezvousService.findAll()).get(0);
			final List<Comment> comments = new ArrayList<Comment>(r.getComments());
			final Comment c = comments.get(0);
			this.commentService.delete(c);
			check = false;
		} catch (final Throwable oops) {
			Assert.isTrue(true);
		}
		Assert.isTrue(check);
	}

}
