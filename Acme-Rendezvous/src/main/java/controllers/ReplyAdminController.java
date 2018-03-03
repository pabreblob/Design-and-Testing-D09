
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.RendezvousService;
import services.ReplyService;
import domain.Comment;
import domain.Rendezvous;
import domain.Reply;

@Controller
@RequestMapping("/reply/admin")
public class ReplyAdminController extends AbstractController {

	@Autowired
	private ReplyService		replyService;
	@Autowired
	private CommentService		commentService;
	@Autowired
	private RendezvousService	rendezvousService;


	@RequestMapping(value = "/delete")
	public ModelAndView delete(@RequestParam final int replyId) {
		final Reply reply = this.replyService.findOne(replyId);
		final Comment c = this.commentService.findCommentByReplyId(replyId);
		final Rendezvous r = this.rendezvousService.findRendezvousByCommentId(c.getId());
		ModelAndView res;

		try {
			this.replyService.delete(reply);
			res = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + r.getId());
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:/rendezvous/display.do?rendezvousId=" + r.getId());
		}
		return res;
	}

}
