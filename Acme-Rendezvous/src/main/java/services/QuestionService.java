
package services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.QuestionRepository;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;

@Service
@Transactional
public class QuestionService {

	@Autowired
	private QuestionRepository	questionRepository;
	@Autowired
	private UserService			userService;


	public QuestionService() {
		super();
	}

	public Question create() {
		final Question q = new Question();
		return q;
	}

	/*
	 * El rendezvous que recibe es aquel que contendrá la pregunta. Se
	 * necesita para realizar las comprobaciones
	 */
	public Question save(final Question q, final Rendezvous r) {
		Assert.isTrue(!r.isFinalMode());
		Assert.isTrue(!r.isDeleted());
		Assert.isTrue(r.getCreator().getId() == this.userService.findByPrincipal().getId());
		if (q.getId() != 0)
			Assert.isTrue(r.getQuestions().contains(q));
		if (q.getId() == 0)
			q.setAnswers(new ArrayList<Answer>());
		q.setRendezvous(r);
		final Question saved = this.questionRepository.save(q);
		if (q.getId() != 0) {
			r.getQuestions().remove(q);
			r.getQuestions().add(saved);
		} else
			r.getQuestions().add(saved);
		return saved;
	}

	public void delete(final Question q, final Rendezvous r) {
		Assert.isTrue(r.getQuestions().contains(q));
		Assert.isTrue(!r.isDeleted());
		Assert.isTrue(!r.isFinalMode());
		Assert.isTrue(r.getCreator().getId() == this.userService.findByPrincipal().getId());
		r.getQuestions().remove(q);

		this.questionRepository.delete(q.getId());
	}

	public Question findOne(final int questionId) {
		return this.questionRepository.findOne(questionId);
	}
}
