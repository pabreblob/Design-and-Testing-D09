
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RendezvousService;
import domain.Announcement;
import domain.Rendezvous;

@Controller
@RequestMapping("/announcement")
public class AnnouncementController extends AbstractController {

	@Autowired
	private RendezvousService	rendezvousService;


	@RequestMapping("/list")
	public ModelAndView list(@RequestParam final int rendezvousId) {
		final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
		Assert.isTrue(!r.isDeleted());
		final Collection<Announcement> announcements = r.getAnnouncements();
		ModelAndView res;
		res = new ModelAndView("announcement/list");
		res.addObject("requestURI", "announcement/list.do");
		res.addObject("announcements", announcements);
		return res;
	}
}
