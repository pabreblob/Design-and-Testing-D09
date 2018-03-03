
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import services.RendezvousService;
import services.UserService;
import domain.Announcement;
import domain.Rendezvous;

@Controller
@RequestMapping("/announcement/user")
public class AnnouncementUserController extends AbstractController {

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private UserService			userService;

	@Autowired
	private RendezvousService	rendezvousService;


	@RequestMapping("/list")
	public ModelAndView list() {
		final Collection<Announcement> announcements = this.announcementService.findAnnouncementsByPrincipal();
		ModelAndView res;
		res = new ModelAndView("announcement/list");
		res.addObject("requestURI", "announcement/user/list.do");
		res.addObject("announcements", announcements);
		return res;
	}

	@RequestMapping("/create")
	public ModelAndView create(@RequestParam final int rendezvousId) {
		final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
		Assert.isTrue(this.userService.findByPrincipal().getCreatedRendezvous().contains(r));
		final Announcement a = this.announcementService.create();
		a.setRendezvous(r);
		final ModelAndView res;
		res = new ModelAndView("announcement/edit");
		res.addObject("announcement", a);
		return res;
	}

	@RequestMapping(value = "/edit", params = "submit")
	public ModelAndView save(@Valid final Announcement announcement, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(announcement);
		else
			try {
				this.announcementService.save(announcement);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(announcement, "announcement.commit.error");
			}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Announcement announcement) {
		ModelAndView result;

		result = this.createEditModelAndView(announcement, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Announcement announcement, final String message) {
		ModelAndView result;

		result = new ModelAndView("announcement/edit");
		result.addObject("announcement", announcement);
		result.addObject("message", message);

		return result;
	}
}
