
package services;

import java.util.Collection;
import java.util.Date;

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
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AnswerTest extends AbstractTest {

	@Autowired
	private AnswerService		answerService;
	@Autowired
	private QuestionService		questionService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserService			userService;


	@Test
	public void testCreate() {
		super.authenticate("USER2");
		final Answer a = this.answerService.create();
		Assert.notNull(a);
		super.unauthenticate();
	}

	@Test
	public void testSave() {
		super.authenticate("USER2");
		final Question q = this.createQuestion(this.createRendezvous());
		super.unauthenticate();

		super.authenticate("USER1");
		final Answer a = this.answerService.create();
		a.setText("Yes");
		final Answer saved = this.answerService.save(a, q);
		Assert.notNull(saved);
		Assert.isTrue(saved.getText().equals("Yes"));
		super.unauthenticate();
	}

	@Test
	public void testSaveAnswersByRendezvousCreator() {
		int i = 0;
		try {
			super.authenticate("USER2");
			final Question q = this.createQuestion(this.createRendezvous());
			final Answer a = this.answerService.create();
			a.setText("Yes");
			this.answerService.save(a, q);
			super.unauthenticate();
			i++;
		} catch (final Throwable oops) {
			super.unauthenticate();
		}
		Assert.isTrue(i == 0);
	}

	@Test
	public void testDeleteAnswer() {
		super.authenticate("USER2");
		final Question q = this.createQuestion(this.createRendezvous());
		super.unauthenticate();

		super.authenticate("USER1");
		final Answer a = this.answerService.create();
		a.setText("Yes");
		final Answer saved = this.answerService.save(a, q);
		Assert.notNull(saved);
		Assert.isTrue(saved.getText().equals("Yes"));
		this.answerService.delete(saved, q);
		super.unauthenticate();

		Assert.isTrue(q.getAnswers().isEmpty());
	}

	@Test
	public void testDeleteAnswerWrong() {
		int i = 0;
		try {
			super.authenticate("USER2");
			final Question q = this.createQuestion(this.createRendezvous());
			final Answer a = this.answerService.create();
			a.setText("Yes");
			final Answer saved = this.answerService.save(a, q);
			this.answerService.delete(saved, q);
			super.unauthenticate();
			i++;
		} catch (final Throwable oops) {
			super.unauthenticate();
		}
		Assert.isTrue(i == 0);
	}

	@Test
	public void testFindAnswersByUserAndRendezvous() {
		super.authenticate("USER2");
		final Question q = this.createQuestion(this.createRendezvous());
		User principal = this.userService.findByPrincipal();
		final Rendezvous r = q.getRendezvous();
		Collection<Answer> answers = this.answerService.findAnswersByUserAndRendezvous(principal.getId(), r.getId());
		Assert.isTrue(answers.isEmpty());
		super.unauthenticate();

		super.authenticate("USER1");
		final Answer a = this.answerService.create();
		a.setText("Yes");
		this.answerService.save(a, q);
		principal = this.userService.findByPrincipal();
		answers = this.answerService.findAnswersByUserAndRendezvous(principal.getId(), r.getId());
		Assert.isTrue(answers.size() == 1);

		super.unauthenticate();

	}
	private Question createQuestion(final Rendezvous r) {
		final Question q = this.questionService.create();
		q.setText("Question 1");
		final Question saved = this.questionService.save(q, r);
		r.setFinalMode(true);
		return saved;
	}
	@SuppressWarnings("deprecation")
	private Rendezvous createRendezvous() {
		Rendezvous r = this.rendezvousService.create();
		r.setFinalMode(false);
		r.setDeleted(false);
		r.setName("Title");
		r.setDescription("Description");
		final Date d = new Date(2019, 6, 1);
		r.setMoment(d);
		r = this.rendezvousService.save(r);
		return r;
	}
}
