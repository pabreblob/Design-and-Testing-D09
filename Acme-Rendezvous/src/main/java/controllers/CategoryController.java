
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.RendezvousService;
import services.ServiceService;
import domain.Category;
import domain.Service;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController {

	@Autowired
	private CategoryService		categoryService;
	@Autowired
	private ServiceService		serviceService;
	@Autowired
	private RendezvousService	rendezvousService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int categoryId) {
		ModelAndView res;

		final Category cat = this.categoryService.findOne(categoryId);
		final boolean father = cat.getParent() != null;

		final Collection<Category> children = cat.getChildren();
		final Collection<Service> services = this.serviceService.findServicesByCategory(categoryId);
		final boolean hasRendez = !this.rendezvousService.findRendezvousByCategoryId(categoryId).isEmpty();
		final boolean hasServ = !services.isEmpty();
		final boolean hasChild = !children.isEmpty();

		res = new ModelAndView("category/display");
		res.addObject("category", cat);
		res.addObject("children", children);
		res.addObject("father", father);
		res.addObject("services", services);
		res.addObject("hasRendez", hasRendez);
		res.addObject("hasServ", hasServ);
		res.addObject("hasChild", hasChild);
		res.addObject("requestURI", "category/display.do");
		if (father)
			res.addObject("fatherId", cat.getParent().getId());
		else
			res.addObject("fatherId", 0);

		return res;
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		final Collection<Category> top = this.categoryService.findTopCategories();

		res = new ModelAndView("category/list");
		res.addObject("categories", top);
		res.addObject("requestURI", "category/list.do");

		return res;
	}

}
