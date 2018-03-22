
package services;

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

/**
 * The requirements don't specify any functional requirements
 * regarding the replies of Comments. We have treated the Replies
 * as a different entity from Comments, so they should have their
 * own services and tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ReplyServiceTest extends AbstractTest {

	@Autowired
	private ReplyService	replyService;
	@Autowired
	private CommentService	commentService;


	/**
	 * Tests the creation of Replies.
	 * This method tests the creation of an empty Reply before
	 * passing it to the corresponding view.
	 * 
	 * Case 1: An attendant to a Rendezvous tries to reply an existing
	 * Comment. No exception is expected.
	 * Case 2: A non-attendant to a Rendezvous tries to reply an
	 * existing Comment. An <code>IllegalArgumentException</code> is expected.
	 * 
	 */
	@Test
	public void driverCreateReply() {
		final Object testingData[][] = {
			{
				"User1", "Comment1", null
			}, {
				"User4", "Comment1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateReply((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the creation of Replies.
	 * This method searches for a Comment and creates a Reply for it.
	 * 
	 * @param username
	 *            The username of the user that is trying to create the Comment.
	 * @param commentBean
	 *            The name of the bean of the Comment that will
	 *            receive the new Reply.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	protected void templateCreateReply(final String username, final String commentBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Comment c = this.commentService.findOne(super.getEntityId(commentBean));
			final Reply res = this.replyService.create(c);
			Assert.notNull(res);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the saving of a Reply.
	 * This method checks the saving of Replies after the User has filled
	 * the text.
	 * 
	 * Case 1: An User tries to save his/her Reply for a Comment in
	 * a Rendezvous that he/she has RSVPd.
	 * Case 2: An User tries to save his/her Reply for a Comment in
	 * a Rendezvous that he/she has not RSVPd.
	 */
	@Test
	public void driverSaveReply() {
		final Object testingData[][] = {
			{
				"User1", "Comment1", null
			}, {
				"User4", "Comment1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSaveReply((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the saving of Comments.
	 * This template creates a Comment for a Rendezvous.
	 * 
	 * @param username
	 *            The name of the user trying to create the Reply.
	 * @param commentBean
	 *            The name of the bean of the Comment that will
	 *            receive the Reply.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	protected void templateSaveReply(final String username, final String commentBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Comment c = this.commentService.findOne(super.getEntityId(commentBean));
			final Reply res = this.replyService.create(c);
			res.setText("textito");
			final Reply saved = this.replyService.save(res, c);
			Assert.notNull(saved);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the finding of one Reply, given its ID.
	 * This method checks that a Reply stored in the database
	 * can be retrieved.
	 * 
	 * Case 1: A valid bean name is given. No exception expected.
	 * Case 2: An invalid bean name is given, which is the most similar case to
	 * giving an invalid ID. An <code>NullPointerException</code> is expected.
	 */
	@Test
	public void driverFindOneReply() {
		final Object testingData[][] = {
			{
				"Reply1", null
			}, {
				"non-valid", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindOneReply((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing the finding of one Reply.
	 * This method defines the template users for the tests that check
	 * the finding of one Reply.
	 * 
	 * @param replyBean
	 *            The name of the bean of the Reply that the user wants
	 *            to retrieve.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindOneReply(final String replyBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		Integer replyId;
		try {
			if (replyBean.equals("non-valid"))
				replyId = null;
			else
				replyId = this.getEntityId(replyBean);
			final Reply res = this.replyService.findOne(replyId);
			Assert.notNull(res);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the deletion of a Comment
	 * This method tests that a Comment can be deleted only by the Administrator.
	 * 
	 * Case 1: An Administrator tries to delete a Reply. No exception
	 * is expected.
	 * Case 2: An User tries to delete a Reply.
	 * An <code>NullPointerException</code> is expected.
	 */
	@Test
	public void driverDeleteReply() {
		final Object testingData[][] = {
			{
				"Admin", "Reply1", null
			}, {
				"User1", "Reply1", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteReply((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the deletion of Replies
	 * 
	 * @param username
	 *            The username of the user that tries to make the deletion.
	 * @param replyBean
	 *            The name of the Reply that is going to be deleted.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	public void templateDeleteReply(final String username, final String replyBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Reply res = this.replyService.findOne(super.getEntityId(replyBean));
			final Comment c = this.commentService.findCommentByReplyId(res.getId());
			this.replyService.delete(res);
			Assert.isTrue(!c.getReplies().contains(res));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
