
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ServiceService;
import domain.Service;

@Controller
@RequestMapping("/service/user")
public class ServiceUserController extends AbstractController {

	@Autowired
	ServiceService	serviceService;


	//	Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Service> services;
		services = this.serviceService.findAvailableServices();
		res = new ModelAndView("service/list");

		res.addObject("services", services);
		res.addObject("requestURI", "service/user/list.do");

		return res;
	}

}
