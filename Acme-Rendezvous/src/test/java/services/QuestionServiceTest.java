
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Question;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class QuestionServiceTest extends AbstractTest {

	@Autowired
	private QuestionService		questionService;
	@Autowired
	private RendezvousService	rendezvousService;


	/**
	 * Tests the finding of one question
	 * <p>
	 * This test checks if you use it with a valid id and a <code>null</code>
	 */
	@Test
	public void driverFindOne() {
		final Object testingData[][] = {
			{
				"Question1", null
			}, {
				"non-existent", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindOne((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing the finding of a question
	 * <p>
	 * 
	 * @param questionId
	 *            The id of the question we want to search according to populateDatabase.xml file. In order to test what would happen
	 *            if it receive <code>null</code>, we must write "non-existent"
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindOne(final String questionId, final Class<?> expected) {
		Class<?> caught = null;
		Integer questionIdNumber;
		try {
			if (questionId.equals("non-existent"))
				questionIdNumber = null;
			else
				questionIdNumber = this.getEntityId(questionId);

			final Question q = this.questionService.findOne(questionIdNumber);
			Assert.notNull(q);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the creation of a question
	 * <p>
	 * This method is used to test the creation of empty question before passing them to the corresponding views.
	 */
	@Test
	public void testCreate() {
		final Question q = this.questionService.create();
		Assert.notNull(q);
	}

	/**
	 * Tests the saving of a question
	 * <p>
	 * This method tests the saving of a question after the user writes the corresponding data.
	 * <p>
	 * - Case 1-5: All tests are ok
	 * <p>
	 * - Case 6: There is no user logged
	 * <p>
	 * - Case 7: The user who is logged is not the same user who creates the question
	 * <p>
	 * - Case 8: The rendezvous is in final mode
	 * <p>
	 * - Case 9: The rendezvous is deleted
	 * <p>
	 * - Case 10: The rendezvous is <code>null</code>
	 * 
	 */
	@Test
	public void testSave() {
		final Object testingData[][] = {
			{
				"Question1", "Rendezvous5", "User2", null
			}, {
				"Question2", "Rendezvous6", "User4", null
			}, {
				"Question3", "Rendezvous5", "User2", null
			}, {
				"Question4", "Rendezvous6", "User4", null
			}, {
				"Question5", "Rendezvous5", "User2", null
			}, {
				"Question1", "Rendezvous5", null, IllegalArgumentException.class
			}, {
				"Question1", "Rendezvous5", "User1", IllegalArgumentException.class
			}, {
				"Question1", "Rendezvous1", "User1", IllegalArgumentException.class
			}, {
				"Question1", "Rendezvous2", "User1", IllegalArgumentException.class
			}, {
				"Question1", "non-existent", "User1", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * Template for testing the saving of a question
	 * <p>
	 * This template create a question and set the data according to these parameters, in order to see if it can be saved or not:
	 * 
	 * @param questionText
	 *            The text of the question we want to save
	 * @param rendezvousId
	 *            The id of a valid rendezvous where we want to create the question
	 * @param username
	 *            The user who is logged.
	 * @param expected
	 *            This method is used to test the creation of empty question before passing them to the corresponding views.
	 */
	protected void templateSave(final String questionText, final String rendezvousId, final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		try {
			Integer rendezvousIdN = null;
			if (!rendezvousId.equals("non-existent"))
				rendezvousIdN = this.getEntityId(rendezvousId);
			final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousIdN);

			final Question q = this.questionService.create();
			q.setRendezvous(rendezvous);
			q.setText(questionText);
			final Question saved = this.questionService.save(q, rendezvous);
			Assert.notNull(saved);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}

	/**
	 * Tests the deleting of a question
	 * <p>
	 * This method tests the deleting of a question.
	 * <p>
	 * Case 1-2: All tests are ok
	 * <p>
	 * Case 3: Trying to delete an already deleted question
	 * <p>
	 * Case 4: The rendezvous is in final mode
	 * <p>
	 * Case 5: There is no user logged
	 * <p>
	 * Case 6: The user who is logged is not the same user who creates the question
	 * <p>
	 * Case 7: The rendezvous is <code>null</code>
	 * <p>
	 * Case 8: The question is <code>null</code>
	 */
	@Test
	public void testDelete() {
		final Object testingData[][] = {
			{
				"Question5", "Rendezvous7", "User4", null
			}, {
				"Question6", "Rendezvous7", "User4", null
			}, {
				"Question5", "Rendezvous7", "User4", IllegalArgumentException.class
			}, {
				"Question1", "Rendezvous1", "User1", IllegalArgumentException.class
			}, {
				"Question7", "Rendezvous7", null, IllegalArgumentException.class
			}, {
				"Question7", "Rendezvous7", "User1", IllegalArgumentException.class
			}, {
				"Question7", "non-existent", "User4", NullPointerException.class
			}, {
				"non-existent", "Rendezvous7", "User4", NullPointerException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	/**
	 * Template for testing the deleting of a question
	 * <p>
	 * This template searches a question and tries to delete it, in order to see if it can be deleted or not:
	 * 
	 * @param questionId
	 *            The id of a question we want to delete, according to populateDatabase.xml file. In order to test what would happen
	 *            if it receive <code>null</code>, we must write "non-existent"
	 * @param rendezvousId
	 *            The id of a valid rendezvous where we want to delete the question, according to populateDatabase.xml file. In order
	 *            to test what would happen if it receive <code>null</code>, we must write "non-existent"
	 * @param username
	 *            The user who is logged.
	 * @param expected
	 *            This method is used to test the creation of empty question before passing them to the corresponding views.
	 */
	protected void templateDelete(final String questionId, final String rendezvousId, final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		Integer questionIdN = null;
		Integer rendezvousIdN = null;
		if (!questionId.equals("non-existent"))
			questionIdN = this.getEntityId(questionId);
		if (!rendezvousId.equals("non-existent"))
			rendezvousIdN = this.getEntityId(rendezvousId);
		try {
			this.questionService.delete(this.questionService.findOne(questionIdN), this.rendezvousService.findOne(rendezvousIdN));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}

}
