
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.RendezvousService;
import domain.Comment;
import domain.Rendezvous;

@Controller
@RequestMapping("/comment/admin")
public class CommentAdminController extends AbstractController {

	@Autowired
	private CommentService		commentService;
	@Autowired
	private RendezvousService	rendezvousService;


	@RequestMapping(value = "/delete")
	public ModelAndView delete(@RequestParam final int commentId) {
		final Comment c = this.commentService.findOne(commentId);
		final Rendezvous r = this.rendezvousService.findRendezvousByCommentId(commentId);
		ModelAndView res;

		try {
			this.commentService.delete(c);
			res = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + r.getId());
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + r.getId());
			res.addObject("message", "comment.commit.error");
		}
		return res;
	}

}
