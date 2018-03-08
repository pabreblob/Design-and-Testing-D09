
package controllers;

import java.util.ArrayList;
import java.util.List;

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
	public ModelAndView create(@RequestParam final int serviceId) {
		final Service s = this.serviceService.findOne(serviceId);
		final List<Request> reqs = new ArrayList<>(this.requestService.findRequestByPrincipal());
		final Request r = this.requestService.create(s);
		if (reqs.size() != 0)
			r.setCreditCard(reqs.get(0).getCreditCard());

		final ModelAndView res = new ModelAndView("request/edit");

		res.addObject("request", r);
		res.addObject("rendezvousSize", this.rendezvousService.findRendezvousforRequestByPrincipal(serviceId).size());
		res.addObject("requestURI", "request/user/create.do");
		res.addObject("rendezvous", this.rendezvousService.findRendezvousforRequestByPrincipal(serviceId));
		return res;
	}

	@RequestMapping(value = "/create", params = "save", method = RequestMethod.POST)
	public ModelAndView save(@Valid final Request r, final BindingResult binding) {

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
