
package services;

import java.util.Date;

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
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class QuestionTest extends AbstractTest {

	@Autowired
	private QuestionService		questionService;
	@Autowired
	private RendezvousService	rendezvousService;


	@Test
	public void testCreate() {
		super.authenticate("USER2");
		final Question q = this.questionService.create();
		Assert.notNull(q);
		super.unauthenticate();
	}

	@Test
	public void testSaveQuestion() {
		super.authenticate("USER2");
		final Rendezvous r = this.createRendezvous();
		final int numberOfQuestion = r.getQuestions().size();
		final Question q = this.questionService.create();
		q.setText("Question 2");
		this.questionService.save(q, r);
		Assert.isTrue(numberOfQuestion != r.getQuestions().size());
		super.unauthenticate();
	}

	@Test
	public void testEditQuestion() {
		super.authenticate("USER2");

		//========================
		// Creating question
		//========================
		final Rendezvous r2 = this.createRendezvous();
		final Question q1 = this.questionService.create();
		q1.setText("Question 2");
		final Question q = this.questionService.save(q1, r2);
		//========================

		final String oldQuestion = q.getText();
		q.setText("LALALALALA");
		final Question q2 = this.questionService.save(q, r2);
		final String newQuestion = q2.getText();
		Assert.isTrue(!oldQuestion.equals(newQuestion));
		super.unauthenticate();
	}

	@Test
	public void testDeleteQuestion() {
		super.authenticate("USER2");

		//========================
		// Creating question
		//========================
		final Rendezvous r2 = this.createRendezvous();
		final Question q1 = this.questionService.create();
		q1.setText("Question 2");
		final Question q = this.questionService.save(q1, r2);
		//========================

		this.questionService.delete(q, r2);

		Assert.isTrue(r2.getQuestions().size() == 0);
		super.unauthenticate();
	}

	@Test
	public void testDeleteQuestionByOtherUser() {
		super.authenticate("USER2");
		//========================
		// Creating question
		//========================
		final Rendezvous r2 = this.createRendezvous();
		final Question q1 = this.questionService.create();
		q1.setText("Question 2");
		final Question q = this.questionService.save(q1, r2);
		//========================
		super.unauthenticate();
		int i = 0;
		try {
			super.authenticate("USER1");
			this.questionService.delete(q, r2);
			super.unauthenticate();
			i++;
		} catch (final Throwable oops) {
			super.unauthenticate();
		}
		Assert.isTrue(i == 0);
	}

	@Test
	public void testDeleteQuestionUnauthenticated() {
		super.authenticate("USER2");
		//========================
		// Creating question
		//========================
		final Rendezvous r2 = this.createRendezvous();
		final Question q1 = this.questionService.create();
		q1.setText("Question 2");
		final Question q = this.questionService.save(q1, r2);
		//========================
		super.unauthenticate();
		int i = 0;
		try {
			this.questionService.delete(q, r2);
			i++;
		} catch (final Throwable oops) {
		}
		Assert.isTrue(i == 0);
	}

	@Test
	public void testFindOne() {
		super.authenticate("USER2");

		//========================
		// Creating question
		//========================
		final Rendezvous r2 = this.createRendezvous();
		final Question q1 = this.questionService.create();
		q1.setText("Question 2");
		final Question q = this.questionService.save(q1, r2);
		//========================
		super.unauthenticate();

		Assert.isTrue(q.equals(this.questionService.findOne(q.getId())));
	}

	@SuppressWarnings("deprecation")
	private Rendezvous createRendezvous() {
		Rendezvous r = this.rendezvousService.create();
		r.setFinalMode(false);
		r.setDeleted(false);
		final Date d = new Date(2019, 6, 1);
		r.setMoment(d);
		r = this.rendezvousService.save(r);
		return r;
	}
}
