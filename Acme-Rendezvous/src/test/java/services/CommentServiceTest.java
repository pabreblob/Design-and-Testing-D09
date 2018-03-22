
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
import domain.Rendezvous;
import domain.Reply;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CommentServiceTest extends AbstractTest {

	@Autowired
	private CommentService		commentService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private ReplyService		replyService;


	/**
	 * Tests the creation of Comments.
	 * This method tests the creation of an empty Comment before
	 * passing it to the corresponding view.
	 * 
	 * Functional Requirement 5.6 (Acme-Rendezvous 1.0):
	 * An actor who is authenticated as a user must be able to:
	 * - Comment on a Rendezvous that he or she has RSVPd.
	 * 
	 * Case 1: An User is trying to create a Comment for a
	 * Rendezvous that he/she has RSVPd. No exception is expected
	 * Case 2: An User is trying to create a Comment for a
	 * Rendezvous that he/she has not RSPVd.
	 * An <code>IllegalArgumentException</code> is expected.
	 */
	@Test
	public void driverCreateComment() {
		final Object testingData[][] = {
			{
				"User1", "Rendezvous1", null
			}, {
				"User4", "Rendezvous1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateComment((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	/**
	 * Template for testing the creation of Comments
	 * This method defines the template used to test the creation of new
	 * Comments in Rendezvous that haven't ended yet and are in final mode.
	 * 
	 * @param username
	 *            The username of the user that is trying to create the Comment
	 * @param rendezvousBean
	 *            The name of the bean of the Rendezvous that will
	 *            receive the new Comment.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	protected void templateCreateComment(final String username, final String rendezvousBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Rendezvous r = this.rendezvousService.findOne(super.getEntityId(rendezvousBean));
			final Comment res = this.commentService.create(r);
			Assert.notNull(res);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the saving of a Comment.
	 * This method checks the saving of Comments after the User has written
	 * the text.
	 * 
	 * Functional requirement 5.6:
	 * An actor who is authenticated as a user must be able to:
	 * - Comment on a Rendezvous that he or she has RSVPd.
	 * 
	 * Case 1: An User tries to save his/her Comment in a Rendezvous that
	 * he/she has RSVPd.
	 * Case 2: An User tries to save his/her Comment in a Rendezvous that
	 * he/she has not RSVPd.
	 */
	@Test
	public void driverSaveComment() {
		final Object testingData[][] = {
			{
				"User1", "Rendezvous1", null
			}, {
				"User4", "Rendezvous1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSaveComment((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the saving of Comments.
	 * This template creates a Comment for a Rendezvous.
	 * 
	 * @param username
	 *            The name of the user trying to create the Comment.
	 * @param rendezBean
	 *            The name of the bean of the Rendezvous that will
	 *            receive the Comment.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	protected void templateSaveComment(final String username, final String rendezBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Rendezvous r = this.rendezvousService.findOne(super.getEntityId(rendezBean));
			final Comment res = this.commentService.create(r);
			res.setText("texto");
			final Comment saved = this.commentService.save(res, r);
			Assert.notNull(saved);
			Assert.isTrue(r.getComments().contains(saved));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the finding of one Comment, given its ID.
	 * This method checks that a Comment stored in the database
	 * can be retrieved.
	 * 
	 * Functional requirement 6.1(*), 5.6(*)(Acme-Rendezvous 1.0)
	 * 
	 * Case 1: A valid bean name is given. No exception expected.
	 * Case 2: An invalid bean name is given, which is the most similar case to
	 * giving an invalid ID. An <code>AssertionError</code> is expected.
	 * 
	 * (*) Even if the requirements don't explicitly say that an actor
	 * of any kind has to be able to retrieve a single Comment from the
	 * Database, it makes sense that this has to be possible, because it
	 * helps other functional requirements like deleting a Comment or
	 * writing a Reply on a Comment.
	 */
	@Test
	public void driverFindOneComment() {
		final Object testingData[][] = {
			{
				"Comment1", null
			}, {
				"non-valid", AssertionError.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindOneComment((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing the finding of one Comment.
	 * This method defines the template used for the tests that check the finding
	 * of one Comment.
	 * 
	 * @param commentBean
	 *            The name of the bean of the Comment that the user wants
	 *            to retrieve.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	public void templateFindOneComment(final String commentBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Comment res = this.commentService.findOne(super.getEntityId(commentBean));
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
	 * Functional requirement 6.1 (Acme-Rendezvous 1.0):
	 * An actor who is authenticated as an administrator must be able to:
	 * - Remove a comment that he or she thinks is inappropriate.
	 * 
	 * Case 1: An Administrator tries to delete a Comment. No exception is expected.
	 * Case 2: An User tries to delete a Comment.
	 * An <code>IllegalArgumentException</code> is expected.
	 */
	@Test
	public void driverDeleteComment() {
		final Object testingData[][] = {
			{
				"Admin", "Comment1", null
			}, {
				"User1", "Comment1", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteComment((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the deletion of Comments.
	 * 
	 * @param username
	 *            The username of the user that tries to make the deletion.
	 * @param commentBean
	 *            The name of the Comment that is going to be deleted.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	public void templateDeleteComment(final String username, final String commentBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Comment c = this.commentService.findOne(super.getEntityId(commentBean));
			final Rendezvous r = this.rendezvousService.findRendezvousByCommentId(c.getId());
			Assert.isTrue(r.getComments().contains(c));
			this.commentService.delete(c);
			Assert.isTrue(!r.getComments().contains(c));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the finding of a Comment given a Reply ID
	 * 
	 * This method tests that a Comment can be retrieved from the Database
	 * with the ID of one of its Replies.
	 * 
	 * Functional requirement 6.1(*) (Acme-Rendezvous 1.0)
	 * 
	 * (*) Even if the requirements don't explicitly say that any actor
	 * has to be able to retrieve a Comment by this means, this service
	 * serves as an auxiliary method for other services related
	 * to Reply and Comment.
	 */
	@Test
	public void testFindCommentByReplyId() {
		super.authenticate("user1");
		final Rendezvous r = this.rendezvousService.findOne(super.getEntityId("Rendezvous1"));
		Comment c = this.commentService.create(r);
		c.setText("test");
		c = this.commentService.save(c, r);
		Reply rep = this.replyService.create(c);
		rep.setText("text");
		rep = this.replyService.save(rep, c);
		final Comment found = this.commentService.findCommentByReplyId(rep.getId());
		Assert.isTrue(found.equals(c));

	}

}
