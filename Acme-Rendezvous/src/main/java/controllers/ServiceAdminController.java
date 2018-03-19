
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.ServiceService;
import domain.Service;

@Controller
@RequestMapping("/service/admin")
public class ServiceAdminController extends AbstractController {

	@Autowired
	ServiceService			serviceService;
	@Autowired
	ConfigurationService	configurationService;


	//	Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Service> services;
		services = this.serviceService.findAll();
		final String currency = this.configurationService.find().getCurrency();
		res = new ModelAndView("service/list");

		res.addObject("services", services);
		res.addObject("currency", currency);
		res.addObject("requestURI", "service/admin/list.do");

		return res;
	}

	//	Delete
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int serviceId) {
		ModelAndView res;
		try {
			this.serviceService.cancel(serviceId);
			res = new ModelAndView("redirect:list.do");
		} catch (final Exception e) {
			res = new ModelAndView("service/list");
			res.addObject("message", "service.commit.error");
		}
		return res;
	}

}
