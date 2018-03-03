
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.RendezvousService;
import services.ReplyService;
import domain.Comment;
import domain.Rendezvous;
import domain.Reply;

@Controller
@RequestMapping("/reply/user")
public class ReplyUserController extends AbstractController {

	@Autowired
	private ReplyService		replyService;
	@Autowired
	private CommentService		commentService;
	@Autowired
	private RendezvousService	rendezvousService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int commentId) {
		final Comment c = this.commentService.findOne(commentId);

		final Reply r = this.replyService.create(c);
		final ModelAndView res = new ModelAndView("reply/edit");
		res.addObject("commentId", commentId);
		res.addObject("reply", r);
		return res;
	}

	@RequestMapping(value = "/create", params = "submit", method = RequestMethod.POST)
	public ModelAndView save(final Reply reply, final BindingResult binding, @RequestParam final int commentId) {
		final Comment c = this.commentService.findOne(commentId);
		final Rendezvous r = this.rendezvousService.findRendezvousByCommentId(commentId);
		final Reply rep = this.replyService.reconstruct(reply, binding);
		ModelAndView res;

		if (binding.hasErrors()) {
			res = new ModelAndView("reply/edit");
			res.addObject("reply", rep);
			res.addObject("commentId", commentId);
		} else
			try {
				this.replyService.save(rep, c);
				res = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + r.getId());
			} catch (final Throwable oops) {
				res = new ModelAndView("reply/edit");
				res.addObject("reply", rep);
				res.addObject("commentId", commentId);
				res.addObject("message", "reply.error");
			}

		return res;
	}
}
