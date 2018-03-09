
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.ManagerService;
import services.ServiceService;
import domain.Service;

@Controller
@RequestMapping("/service/manager")
public class ServiceManagerController extends AbstractController {

	@Autowired
	ServiceService	serviceService;

	@Autowired
	CategoryService	categoryService;
	@Autowired
	ManagerService	managerService;


	//	Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Service> services;
		services = this.serviceService.findAvailableServices();
		res = new ModelAndView("service/list");

		res.addObject("services", services);
		res.addObject("requestURI", "service/manager/list.do");

		return res;
	}

	//	Listing created
	@RequestMapping(value = "/list-created", method = RequestMethod.GET)
	public ModelAndView listCreated() {
		ModelAndView res;
		Collection<Service> services;
		services = this.serviceService.findServicesCreatedByPrincipal();
		res = new ModelAndView("service/list");

		res.addObject("services", services);
		res.addObject("requestURI", "service/manager/list-created.do");

		return res;
	}

	//Creating
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView res;
		final Service s = this.serviceService.create();
		res = this.createEditModelAndView(s);

		return res;
	}
	//	//Editing
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int serviceId) {
		ModelAndView res;
		Service s;
		s = this.serviceService.findOne(serviceId);
		Assert.isTrue(this.managerService.findByPrincipal().getServices().contains(s));
		res = this.createEditModelAndView(s);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Service s, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(s);
		else
			try {
				this.serviceService.save(s);
				res = new ModelAndView("redirect:list-created.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(s, "service.commit.error");
			}
		return res;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "cancel")
	public ModelAndView delete(final Service c) {
		ModelAndView res;

		try {
			this.serviceService.delete(c);
			res = new ModelAndView("redirect:list-created.do");
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(c, "service.commit.error");
		}
		return res;
	}

	//Ancillary methods
	protected ModelAndView createEditModelAndView(final Service s) {
		return this.createEditModelAndView(s, null);
	}
	protected ModelAndView createEditModelAndView(final Service s, final String messageCode) {
		ModelAndView res;
		res = new ModelAndView("service/edit");
		res.addObject("service", s);
		res.addObject("requestSize", s.getRequests().size());
		res.addObject("categories", this.categoryService.findAll());
		res.addObject("message", messageCode);
		return res;
	}
}
