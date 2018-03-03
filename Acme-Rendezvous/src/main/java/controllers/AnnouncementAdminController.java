
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import domain.Announcement;

@Controller
@RequestMapping("/announcement/admin")
public class AnnouncementAdminController extends AbstractController {

	@Autowired
	private AnnouncementService	announcementService;


	@RequestMapping(value = "/delete")
	public ModelAndView delete(@RequestParam final int announcementId) {
		ModelAndView res;
		final Announcement announcement = this.announcementService.findOne(announcementId);
		final int rendezvousId = announcement.getRendezvous().getId();

		try {
			this.announcementService.delete(announcement);
			res = new ModelAndView("redirect:/announcement/list.do?rendezvousId=" + rendezvousId);
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:/announcement.list.do?rendezvousId=" + rendezvousId);
			res.addObject("message", "announcement.commit.error");
		}
		return res;
	}
}
