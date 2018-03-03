
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnswerService;
import services.RendezvousService;
import services.UserService;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/answer/user")
public class AnswerUserController extends AbstractController {

	@Autowired
	private AnswerService		answerService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserService			userService;


	public AnswerUserController() {
		super();
	}

	@RequestMapping("/fill")
	public ModelAndView seeForm(@RequestParam final int rendezvousId, @RequestParam(required = false) final String error) {
		final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
		final User principal = this.userService.findByPrincipal();
		if (this.calculateAge(principal.getBirthdate()) < 18)
			Assert.isTrue(!r.isAdultContent());
		Assert.isTrue(r.isFinalMode());
		Assert.isTrue(!r.isDeleted());
		Assert.isTrue(!r.getAttendants().contains(this.userService.findByPrincipal()));
		final DateTime now = new DateTime();
		final DateTime moment = new DateTime(r.getMoment());
		Assert.isTrue(now.isBefore(moment));

		final ModelAndView res = new ModelAndView("answer/form");
		res.addObject("rendezId", r.getId());
		res.addObject("questions", r.getQuestions());
		if (error != null)
			res.addObject("messageError", true);
		return res;
	}

	@RequestMapping("/answers")
	public ModelAndView fillingForm(@RequestParam final int rendezvousId, final HttpServletRequest request) {
		try {

			final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
			final User principal = this.userService.findByPrincipal();
			if (this.calculateAge(principal.getBirthdate()) < 18)
				Assert.isTrue(!r.isAdultContent());
			Assert.isTrue(r.isFinalMode());
			Assert.isTrue(!r.isDeleted());
			Assert.isTrue(!r.getAttendants().contains(principal));
			final DateTime now = new DateTime();
			final DateTime moment = new DateTime(r.getMoment());
			Assert.isTrue(now.isBefore(moment));

			final Collection<Question> questions = new ArrayList<>(r.getQuestions());
			final Collection<Answer> answers = new ArrayList<>();
			String textTemporal;
			Answer temporal;
			for (final Question q : questions) {
				temporal = this.answerService.create();
				temporal.setAuthor(principal);
				temporal.setQuestion(q);
				textTemporal = request.getParameter(String.valueOf(q.getId()));
				if (textTemporal.equals(""))
					break;
				temporal.setText(request.getParameter(String.valueOf(q.getId())));
				answers.add(temporal);
			}
			Assert.isTrue(answers.size() == questions.size());
			for (final Answer answer : answers)
				this.answerService.save(answer, answer.getQuestion());
			return new ModelAndView("redirect:/rendezvous/user/join.do?rendezvousId=" + rendezvousId);
		} catch (final Throwable oops) {
			return new ModelAndView("redirect:fill.do?rendezvousId=" + rendezvousId + "&error=ERROR");
		}
	}

	@SuppressWarnings("deprecation")
	private int calculateAge(final Date birthdate) {
		final LocalDate birth = new LocalDate(birthdate.getYear() + 1900, birthdate.getMonth() + 1, birthdate.getDate());
		final LocalDate now = new LocalDate();
		return Years.yearsBetween(birth, now).getYears();
	}
}
