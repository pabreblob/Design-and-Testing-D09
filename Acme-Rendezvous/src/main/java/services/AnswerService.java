
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AnswerRepository;
import domain.Answer;
import domain.Question;

@Service
@Transactional
public class AnswerService {

	@Autowired
	private AnswerRepository	answerRepository;
	@Autowired
	private UserService			userService;
	@Autowired
	private RendezvousService	rendezvousService;


	public Answer create() {
		final Answer a = new Answer();
		return a;
	}

	public Answer save(final Answer a, final Question q) {
		Assert.isTrue(!q.getAnswers().contains(a));
		Assert.isTrue(q.getRendezvous().isFinalMode());
		Assert.isTrue(!q.getRendezvous().getAttendants().contains(this.userService.findByPrincipal()));

		a.setAuthor(this.userService.findByPrincipal());
		a.setQuestion(q);
		final Answer saved = this.answerRepository.save(a);
		q.getAnswers().add(saved);
		return saved;
	}

	public void delete(final Answer a, final Question q) {
		Assert.isTrue(q.getAnswers().contains(a));
		Assert.isTrue(q.getRendezvous().isFinalMode());
		Assert.isTrue(a.getAuthor().getId() == this.userService.findByPrincipal().getId());
		q.getAnswers().remove(a);
		this.answerRepository.delete(a);
	}

	public Collection<Answer> findAnswersByUserAndRendezvous(final int userId, final int rendezvousId) {
		Assert.isTrue(this.userService.findOne(userId) != null);
		Assert.isTrue(this.rendezvousService.findOne(rendezvousId) != null);
		return this.answerRepository.findAnswersByUserAndRendezvous(userId, rendezvousId);
	}
}
