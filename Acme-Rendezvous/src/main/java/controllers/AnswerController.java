
package controllers;

import java.util.Collection;
import java.util.Date;

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
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/answer")
public class AnswerController extends AbstractController {

	@Autowired
	private AnswerService		answerService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserService			userService;


	public AnswerController() {
		super();
	}

	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final int userId, @RequestParam final int rendezvousId) {
		try {
			final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
			Assert.isTrue(!r.isDeleted());
			User u;
			try {
				u = this.userService.findByPrincipal();
			} catch (final Throwable oops) {
				u = null;
			}
			if (u == null || this.calculateAge(u.getBirthdate()) < 18)
				Assert.isTrue(!r.isAdultContent());
			final ModelAndView res;
			final Collection<Answer> answers = this.answerService.findAnswersByUserAndRendezvous(userId, rendezvousId);
			res = new ModelAndView("answer/display");
			res.addObject("answers", answers);
			return res;
		} catch (final Throwable oops) {
			return new ModelAndView("redirect:/welcome/index.do");
		}

	}
	@SuppressWarnings("deprecation")
	private int calculateAge(final Date birthdate) {
		final LocalDate birth = new LocalDate(birthdate.getYear() + 1900, birthdate.getMonth() + 1, birthdate.getDate());
		final LocalDate now = new LocalDate();
		return Years.yearsBetween(birth, now).getYears();
	}
}
