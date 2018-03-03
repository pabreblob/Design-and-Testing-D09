
package controllers;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RendezvousService;
import domain.Rendezvous;

@Controller
@RequestMapping("/rendezvous/administrator")
public class RendezvousAdminController extends AbstractController {

	@Autowired
	RendezvousService	rendezvousService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Rendezvous> rendezvous;
		rendezvous = this.rendezvousService.findAll();
		final Date currentTime = new Date(System.currentTimeMillis());
		final Timestamp timestamp = new Timestamp(currentTime.getTime());
		res = new ModelAndView("rendezvous/list");
		res.addObject("rendezvous", rendezvous);
		res.addObject("timestamp", timestamp);
		res.addObject("requestURI", "rendezvous/administrator/list.do");

		return res;
	}
	//	Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int rendezvousId) {
		ModelAndView res;
		try {
			this.rendezvousService.delete(rendezvousId);
			res = new ModelAndView("redirect:list.do");
		} catch (final Exception e) {
			res = new ModelAndView("rendezvous/list");
			res.addObject("message", "rendez.commit.error");
		}
		return res;
	}
}
