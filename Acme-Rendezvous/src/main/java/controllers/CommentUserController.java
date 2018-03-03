
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.RendezvousService;
import services.UserService;
import domain.Comment;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/comment/user")
public class CommentUserController extends AbstractController {

	@Autowired
	private CommentService		commentService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserService			userService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.isTrue(rendezvous.isFinalMode());
		final User principal = this.userService.findByPrincipal();
		Assert.isTrue(rendezvous.getAttendants().contains(principal));

		final Comment comment = this.commentService.create(rendezvous);
		final ModelAndView res = new ModelAndView("comment/edit");
		res.addObject("rendezvousId", rendezvousId);
		res.addObject("comment", comment);
		return res;
	}

	@RequestMapping(value = "/create", params = "submit", method = RequestMethod.POST)
	public ModelAndView save(final Comment comment, final BindingResult binding, @RequestParam final int rendezvousId) {
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		final Comment c = this.commentService.reconstruct(comment, binding);

		if (binding.hasErrors()) {
			final ModelAndView res = new ModelAndView("comment/edit");
			res.addObject("comment", c);
			res.addObject("rendezvousId", rendezvousId);
			return res;
		} else
			try {
				this.commentService.save(c, rendezvous);
				final ModelAndView res = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + rendezvousId);
				return res;
			} catch (final Throwable oops) {
				final ModelAndView res = new ModelAndView("comment/edit");
				res.addObject("comment", c);
				res.addObject("rendezvousId", rendezvousId);
				res.addObject("message", "comment.error");
				return res;
			}
	}

}
