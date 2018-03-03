
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
import domain.Reply;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ReplyTest extends AbstractTest {

	@Autowired
	private ReplyService	replyService;
	@Autowired
	private CommentService	commentService;
	@Autowired
	private UserService		userService;


	@Test
	public void testCreate() {
		super.authenticate("User1");
		final Comment c = new ArrayList<Comment>(this.commentService.findAll()).get(0);
		final Reply res = this.replyService.create(c);
		Assert.notNull(res);
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		final Reply r = new ArrayList<Reply>(this.replyService.findAll()).get(0);
		final Reply res = this.replyService.findOne(r.getId());
		Assert.notNull(res);
	}

	@Test
	public void testFindAll() {
		final Collection<Reply> res = this.replyService.findAll();
		Assert.notNull(res);
		Assert.notEmpty(res);
	}

	@Test
	public void testSave() {
		super.authenticate("User1");
		final Comment c = new ArrayList<Comment>(this.commentService.findAll()).get(0);
		final Reply res = this.replyService.create(c);
		res.setText("textito");
		final Reply saved = this.replyService.save(res, c);
		Assert.notNull(saved);
		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("Admin");
		final Comment c = new ArrayList<Comment>(this.commentService.findAll()).get(0);
		final List<Reply> replies = new ArrayList<Reply>(c.getReplies());
		final Reply res = replies.get(0);
		this.replyService.delete(res);
		Assert.isTrue(!c.getReplies().contains(res));
		super.unauthenticate();

	}

	@Test
	public void testFindRepliesByUserId() {
		final User u = this.userService.findOne(36);
		final Collection<Reply> res = this.replyService.findRepliesByUserId(35);
		for (final Reply r : res)
			Assert.isTrue(r.getAuthor().equals(u));
	}

}
