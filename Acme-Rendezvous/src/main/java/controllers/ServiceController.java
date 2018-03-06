
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ServiceService;
import domain.Service;

@Controller
@RequestMapping("/service")
public class ServiceController extends AbstractController {

	@Autowired
	ServiceService	serviceService;


	//Displaying
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int serviceId) {
		ModelAndView res;

		final Service s = this.serviceService.findOne(serviceId);
		Assert.isTrue(!s.isCancelled());

		res = new ModelAndView("service/display");
		res.addObject("service", s);
		int pictureSize;
		if (s.getPictureUrl() == null || s.getPictureUrl().length() == 0)
			pictureSize = 0;
		else
			pictureSize = s.getPictureUrl().length();
		res.addObject("pictureSize", pictureSize);

		return res;
	}

	//	Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezId) {
		ModelAndView res;
		Collection<Service> services;
		services = this.serviceService.findServicesByRendezvousId(rendezId);
		res = new ModelAndView("service/list");

		res.addObject("services", services);
		res.addObject("requestURI", "service/list.do");

		return res;
	}

}
