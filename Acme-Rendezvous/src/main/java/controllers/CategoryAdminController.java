
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import domain.Category;

@Controller
@RequestMapping("/category/admin")
public class CategoryAdminController {

	@Autowired
	private CategoryService		categoryService;
	@Autowired
	private CategoryController	categoryController;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final int categoryId) {
		ModelAndView res;

		final Category parent = this.categoryService.findOne(categoryId);
		final Category cat = this.categoryService.create(parent);

		res = new ModelAndView("category/edit");
		res.addObject("category", cat);

		return res;
	}

	@RequestMapping(value = "/create-top", method = RequestMethod.GET)
	public ModelAndView createTop() {
		ModelAndView res;

		final Category cat = this.categoryService.createTop();

		res = new ModelAndView("category/edit");
		res.addObject("category", cat);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int categoryId) {
		ModelAndView res;

		final Category cat = this.categoryService.findOne(categoryId);

		res = new ModelAndView("category/edit");
		res.addObject("category", cat);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "submit")
	public ModelAndView save(final Category cat, final BindingResult binding) {
		ModelAndView res;
		Category c = this.categoryService.reconstruct(cat, binding);

		System.out.println(binding);
		System.out.println(cat.getParent());
		if (binding.hasErrors())
			res = this.createEditModelAndView(c);
		else
			try {
				c = this.categoryService.save(c);
				res = this.createDisplayModelAndView(c);
				return res;
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(c, "category.commit.error");
			}

		return res;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete(@RequestParam final int categoryId) {
		final Category cat = this.categoryService.findOne(categoryId);
		ModelAndView res;

		try {
			if (!cat.getChildren().isEmpty())
				res = this.createDisplayModelAndView(cat, "category.childrenExist.error");
			else if (!cat.getServices().isEmpty())
				res = this.createDisplayModelAndView(cat, "category.servicesExist.error");
			else {
				this.categoryService.delete(cat);
				if (cat.getParent() != null)
					res = new ModelAndView("redirect:display.do?categoryId=" + cat.getParent().getId());
				else
					res = new ModelAndView("redirect:list.do");
			}
		} catch (final Throwable oops) {
			res = this.createDisplayModelAndView(cat, "category.commit.error");
		}

		return res;
	}
	protected ModelAndView createEditModelAndView(final Category cat) {
		return this.createEditModelAndView(cat, null);
	}

	protected ModelAndView createEditModelAndView(final Category cat, final String messageCode) {
		ModelAndView res;
		res = new ModelAndView("category/edit");
		res.addObject("category", cat);
		res.addObject("message", messageCode);
		res.addObject("parentId", cat.getParent().getId());
		return res;
	}

	protected ModelAndView createDisplayModelAndView(final Category cat) {
		return this.createDisplayModelAndView(cat, null);
	}

	protected ModelAndView createDisplayModelAndView(final Category cat, final String messageCode) {
		final ModelAndView res = this.categoryController.display(cat.getId());
		res.addObject("message", messageCode);
		return res;
	}
}
