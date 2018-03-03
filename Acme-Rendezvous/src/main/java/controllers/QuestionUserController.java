
package controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.QuestionService;
import services.RendezvousService;
import services.UserService;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;

@Controller
@RequestMapping("/question/user")
public class QuestionUserController extends AbstractController {

	@Autowired
	private QuestionService		questionService;
	@Autowired
	private UserService			userService;
	@Autowired
	private RendezvousService	rendezvousService;


	public QuestionUserController() {
		super();
	}

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam final int rendezvousId) {
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.isTrue(!rendezvous.isDeleted());
		Assert.isTrue(this.userService.findByPrincipal().getId() == rendezvous.getCreator().getId());
		ModelAndView res;
		res = new ModelAndView("question/list");
		res.addObject("rendezvous", rendezvous);
		return res;
	}

	@RequestMapping("/create")
	public ModelAndView create(@RequestParam final int rendezvousId) {
		final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
		Assert.isTrue(!r.isDeleted());
		Assert.isTrue(!r.isFinalMode());
		Assert.isTrue(this.userService.findByPrincipal().getCreatedRendezvous().contains(r));
		final Question q = this.questionService.create();

		final ModelAndView res;
		res = new ModelAndView("question/edit");
		res.addObject("rendezvous", r);
		res.addObject("question", q);
		return res;
	}

	@RequestMapping(value = "/edit")
	public ModelAndView edit(@RequestParam final int questionId, @RequestParam final int rendezvousId) {
		final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
		final Question q = this.questionService.findOne(questionId);
		Assert.isTrue(!r.isDeleted());
		Assert.isTrue(!r.isFinalMode());
		Assert.isTrue(r.getCreator().getId() == this.userService.findByPrincipal().getId());
		Assert.isTrue(r.getQuestions().contains(q));
		final ModelAndView res = new ModelAndView("question/edit");
		res.addObject("question", q);
		res.addObject("rendezvous", r);
		return res;
	}

	@RequestMapping(value = "/edit", params = "submit")
	public ModelAndView save(final Question question, final BindingResult binding, @RequestParam final int rendezvousId) {
		final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
		if (binding.hasErrors()) {

			final ModelAndView res = new ModelAndView("question/edit");
			res.addObject("question", question);
			res.addObject("rendezvous", r);
			return res;
		} else
			try {
				Assert.isTrue(!r.isDeleted());
				Assert.isTrue(!r.isFinalMode());
				Assert.isTrue(r.getCreator().getId() == this.userService.findByPrincipal().getId());
				if (question.getId() != 0)
					Assert.isTrue(r.getQuestions().contains(question));
				question.setAnswers(new ArrayList<Answer>());
				this.questionService.save(question, r);
				final ModelAndView res = new ModelAndView("redirect:list.do?rendezvousId=" + r.getId());
				return res;
			} catch (final Throwable oops) {
				final ModelAndView res = new ModelAndView("question/edit");
				res.addObject("question", question);
				res.addObject("rendezvous", r);
				res.addObject("message", "question.error");
				return res;
			}
	}

	@RequestMapping("/delete")
	public ModelAndView delete(@RequestParam final int questionId, @RequestParam final int rendezvousId) {
		final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
		final Question q = this.questionService.findOne(questionId);
		try {
			Assert.isTrue(!r.isDeleted());
			Assert.isTrue(!r.isFinalMode());
			Assert.isTrue(r.getCreator().getId() == this.userService.findByPrincipal().getId());
			Assert.isTrue(r.getQuestions().contains(q));
			this.questionService.delete(q, r);
			final ModelAndView res = new ModelAndView("redirect:list.do?rendezvousId=" + r.getId());
			return res;
		} catch (final Throwable oops) {
			final ModelAndView res = new ModelAndView("redirect:list.do?rendezvousId=" + r.getId());
			return res;
		}
	}
}
