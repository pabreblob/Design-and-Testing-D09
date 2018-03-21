
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
import domain.Answer;
import domain.Question;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AnswerServiceTest extends AbstractTest {

	@Autowired
	private AnswerService	answerService;
	@Autowired
	private QuestionService	questionService;


	/**
	 * Tests the creation of an answer
	 * <p>
	 * This method is used to test the creation of empty answer
	 * <p>
	 * Functional Requirement:
	 * <p>
	 * 21. An actor who is authenticated as a user must be able to:
	 * <p>
	 * 2. Answer the questions that are associated with a rendezvous that he or she’s RSVPing now
	 */
	@Test
	public void testCreate() {
		final Answer a = this.answerService.create();
		Assert.notNull(a);
	}

	/**
	 * Tests the saving of an answer
	 * <p>
	 * This method tests the saving of a answer after the user writes the corresponding data
	 * <p>
	 * Functional Requirement:
	 * <p>
	 * 21. An actor who is authenticated as a user must be able to:
	 * <p>
	 * 2. Answer the questions that are associated with a rendezvous that he or she’s RSVPing now
	 * <p>
	 * Case 1-5: All tests are ok
	 * <p>
	 * Case 6: No user is logged
	 * <p>
	 * Case 7: Question is null
	 * <p>
	 * Case 8: The user is the owner of the rendezvous (he can't answer his own question)
	 * <p>
	 * Case 9: The rendezvous is not in final mode
	 * <p>
	 * Case 10: The user has already answered the question
	 */
	@Test
	public void testSave() {
		final Object testingData[][] = {
			{
				"Question1", "User2", null
			}, {
				"Question1", "User3", null
			}, {
				"Question2", "User2", null
			}, {
				"Question2", "User3", null
			}, {
				"Question3", "User2", null
			}, {
				"Question1", null, IllegalArgumentException.class
			}, {
				"non-existent", "User1", NullPointerException.class
			}, {
				"Question1", "User1", IllegalArgumentException.class
			}, {
				"Question5", "User1", IllegalArgumentException.class
			}, {
				"Question3", "User3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSave((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the saving of an answer
	 * <p>
	 * This template create an answer and set the data according to these parameters, in order to see if it can be saved or not:
	 * 
	 * @param questionId
	 *            The id of a valid question we want to answer.
	 * @param username
	 *            The user who is logged
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateSave(final String questionId, final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		Integer questionIdN = null;
		if (!questionId.equals("non-existent"))
			questionIdN = this.getEntityId(questionId);
		try {
			final Question q = this.questionService.findOne(questionIdN);
			final Answer a = this.answerService.create();
			a.setText("Hello");
			final Answer saved = this.answerService.save(a, q);
			Assert.notNull(saved);
			Assert.isTrue(q.getAnswers().contains(saved));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}

	/**
	 * Tests the deleting of an answer
	 * <p>
	 * Case 1: User1 is the owner of the Question3, so he didn't answer the question
	 * <p>
	 * Case 2-3: All tests are ok
	 */
	@Test
	public void testDelete() {
		final Object testingData[][] = {
			{
				"Question3", "User1", IllegalArgumentException.class
			}, {
				"Question3", "User2", null
			}, {
				"Question3", "User3", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the deleting of an answer
	 * <p>
	 * This template searches the answers of a given question and try to delete one of them
	 * 
	 * @param questionId
	 *            The question whose answers we want to delete
	 * @param username
	 *            The user who is logged. The template is prepared to take a wrong answer
	 *            if you are "User1", in order to see what would happen if you are a different user
	 *            who actually answers the question
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateDelete(final String questionId, final String username, final Class<?> expected) {
		super.authenticate(username);
		Class<?> caught = null;
		final Question q = this.questionService.findOne(this.getEntityId(questionId));
		Answer a = null;
		if (!username.equals("User1")) {
			final List<Answer> answers = new ArrayList<>(this.answerService.findAnswersByUserAndRendezvous(this.getEntityId(username), q.getRendezvous().getId()));
			a = answers.get(0);
		} else {
			final List<Answer> answers = new ArrayList<>(this.answerService.findAnswersByUserAndRendezvous(this.getEntityId("User2"), q.getRendezvous().getId()));
			a = answers.get(0);
		}
		try {
			this.answerService.delete(a, a.getQuestion());
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}

	/**
	 * Testing the finding of an user and a rendezvous
	 * <p>
	 * This method give us the answers which the user writes to assist a rendezvous. This method is useful in different situation because the relationship doesn't let us know it easily.
	 * <p>
	 * Functional Requirement:
	 * <p>
	 * 20. An actor who is not authenticated must be able to
	 * <p>
	 * 1. Display information about the users who have RSVPd a rendezvous, which, in turn, must show their answers to the questions that the creator has registered.
	 */
	@Test
	public void testFindAnswersByUserAndRendezvous() {
		final int userId = this.getEntityId("User3");
		final int rendezvousId = this.getEntityId("Rendezvous2");
		final Collection<Answer> answers = this.answerService.findAnswersByUserAndRendezvous(userId, rendezvousId);
		Assert.notEmpty(answers);
	}

	/**
	 * If the user hasn't answered the question, this method returns an empty collection. The assert throws the IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFindAnswersByUserAndRendezvousWithWrongUser() {
		final int userId = this.getEntityId("User1");
		final int rendezvousId = this.getEntityId("Rendezvous2");
		final Collection<Answer> answers = this.answerService.findAnswersByUserAndRendezvous(userId, rendezvousId);
		Assert.notEmpty(answers);
	}

	/**
	 * If the user id is wrong, the method throws the IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFindAnswersByUserAndRendezvousWithWrongUser2() {
		final int userId = 0;
		final int rendezvousId = this.getEntityId("Rendezvous2");
		this.answerService.findAnswersByUserAndRendezvous(userId, rendezvousId);
	}
}
