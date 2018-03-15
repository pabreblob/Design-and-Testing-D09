
package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RendezvousService;
import services.RequestService;
import services.ServiceService;
import domain.Request;
import domain.Service;

@Controller
@RequestMapping("/request/user")
public class RequestUserController extends AbstractController {

	@Autowired
	RequestService		requestService;

	@Autowired
	ServiceService		serviceService;

	@Autowired
	RendezvousService	rendezvousService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int serviceId, final HttpServletResponse response) {
		final Service s = this.serviceService.findOne(serviceId);
		final Request r = this.requestService.create(s);
		final List<Request> reqs = new ArrayList<>(this.requestService.findRequestByPrincipal());
		if (reqs.size() != 0) {
			final Request last = reqs.get(0);
			final Cookie cookie = new Cookie("CCHolderName", last.getCreditCard().getHolderName());
			final Cookie cookie2 = new Cookie("CCBrandName", last.getCreditCard().getBrandName());
			final Cookie cookie3 = new Cookie("CCNumber", last.getCreditCard().getNumber());
			final Cookie cookie4 = new Cookie("CCExpMonth", String.valueOf(last.getCreditCard().getExpMonth()));
			final Cookie cookie5 = new Cookie("CCExpYear", String.valueOf(last.getCreditCard().getExpYear()));
			final Cookie cookie6 = new Cookie("CCCVV", String.valueOf(last.getCreditCard().getCvv()));
			cookie.setPath("/");
			cookie2.setPath("/");
			cookie3.setPath("/");
			cookie4.setPath("/");
			cookie5.setPath("/");
			cookie6.setPath("/");
			response.addCookie(cookie);
			response.addCookie(cookie2);
			response.addCookie(cookie3);
			response.addCookie(cookie4);
			response.addCookie(cookie5);
			response.addCookie(cookie6);
		}
		final ModelAndView res = new ModelAndView("request/edit");

		res.addObject("request", r);
		res.addObject("rendezvousSize", this.rendezvousService.findRendezvousforRequestByPrincipal(serviceId).size());
		res.addObject("requestURI", "request/user/create.do");
		res.addObject("rendezvous", this.rendezvousService.findRendezvousforRequestByPrincipal(serviceId));
		return res;
	}

	@RequestMapping(value = "/create", params = "save", method = RequestMethod.POST)
	public ModelAndView save(@Valid final Request r, final BindingResult binding, final HttpServletResponse response) {

		ModelAndView res;

		if (binding.hasErrors()) {
			res = this.createEditModelAndView(r);
			res.addObject("rendezvousSize", this.rendezvousService.findRendezvousforRequestByPrincipal(r.getService().getId()).size());
		} else
			try {
				this.requestService.save(r);
				res = new ModelAndView("redirect:/service/user/list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(r, "request.commit.error");
				res.addObject("rendezvousSize", this.rendezvousService.findRendezvousforRequestByPrincipal(r.getService().getId()).size());
			}

		return res;
	}

	//Ancillary methods
	protected ModelAndView createEditModelAndView(final Request r) {
		return this.createEditModelAndView(r, null);
	}
	protected ModelAndView createEditModelAndView(final Request r, final String messageCode) {
		ModelAndView res;
		res = new ModelAndView("request/edit");
		res.addObject("request", r);
		res.addObject("requestURI", "request/user/create.do");
		res.addObject("rendezvous", this.rendezvousService.findRendezvousforRequestByPrincipal(r.getService().getId()));
		res.addObject("message", messageCode);
		return res;
	}
}
