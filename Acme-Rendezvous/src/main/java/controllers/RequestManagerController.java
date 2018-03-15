
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ManagerService;
import services.RequestService;
import services.ServiceService;
import domain.Request;

@Controller
@RequestMapping("/request/manager")
public class RequestManagerController extends AbstractController {

	@Autowired
	RequestService	requestService;

	@Autowired
	ManagerService	managerService;

	@Autowired
	ServiceService	serviceService;


	//	Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int serviceId) {
		ModelAndView res;
		Collection<Request> r;
		Assert.isTrue(this.managerService.findByPrincipal().getServices().contains(this.serviceService.findOne(serviceId)));
		Assert.isTrue(this.serviceService.findOne(serviceId).isCancelled() == false);
		r = this.requestService.findRequestByServiceId(serviceId);
		res = new ModelAndView("request/list");
		res.addObject("requests", r);
		res.addObject("requestURI", "request/manager/list.do");

		return res;
	}
}
