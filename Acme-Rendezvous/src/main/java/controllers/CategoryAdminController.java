
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

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
public class CategoryAdminController extends AbstractController {

	@Autowired
	private CategoryService		categoryService;
	@Autowired
	private CategoryController	categoryController;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final int categoryId) {
		ModelAndView res;

		final Category parent = this.categoryService.findOne(categoryId);
		final Category cat = this.categoryService.create(parent);

		res = this.createEditModelAndView(cat);

		return res;
	}

	@RequestMapping(value = "/create-top", method = RequestMethod.GET)
	public ModelAndView createTop() {
		ModelAndView res;

		final Category cat = this.categoryService.createTop();

		res = this.createEditModelAndView(cat);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int categoryId) {
		ModelAndView res;

		final Category cat = this.categoryService.findOne(categoryId);

		res = this.createEditModelAndView(cat);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "submit")
	public ModelAndView save(final Category cat, final BindingResult binding) {
		ModelAndView res;
		Category c = this.categoryService.reconstruct(cat, binding);

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
				if (cat.getParent() != null) {
					final Category parent = cat.getParent();
					res = this.createDisplayModelAndView(parent);
				} else
					res = this.categoryController.list();
			}
		} catch (final Throwable oops) {
			res = this.createDisplayModelAndView(cat, "category.commit.error");
		}

		return res;
	}
	@RequestMapping(value = "/move", method = RequestMethod.GET)
	public ModelAndView move(@RequestParam final int categoryId) {
		ModelAndView res;
		final Category child = this.categoryService.findOne(categoryId);
		final Category parent = child.getParent();
		final Collection<Category> categories = this.categoryService.findAll();
		Collection<Category> cataux = new ArrayList<Category>(categories);
		cataux = this.removeCategories(categories, child);
		cataux.remove(parent);
		cataux.remove(child);

		res = new ModelAndView("category/move");
		res.addObject("category", child);
		res.addObject("childId", categoryId);
		res.addObject("categories", cataux);

		return res;
	}
	@RequestMapping(value = "/move", method = RequestMethod.POST, params = "submit")
	public ModelAndView saveMove(@Valid final Category child, final BindingResult binding) {
		ModelAndView res;
		final int childId = child.getId();
		int parentId;
		if (child.getParent() != null) {
			final Category parent = child.getParent();
			parentId = parent.getId();
		} else
			parentId = 0;

		if (binding.hasErrors()) {
			res = new ModelAndView("category/move");
			res.addObject("categoryId", childId);
			res.addObject("message", "category.commit.error");
		} else
			try {
				final Category cat = this.categoryService.findOne(childId);
				Category parent = null;
				if (parentId > 0)
					parent = this.categoryService.findOne(parentId);

				this.categoryService.move(cat, parent);

				if (parent != null)
					res = this.createDisplayModelAndView(parent);
				else
					res = this.categoryController.list();
			} catch (final Throwable oops) {
				res = new ModelAndView("category/move");
				res.addObject("childId", childId);
				res.addObject("message", "category.commit.error");
			}

		return res;
	}
	protected ModelAndView createEditModelAndView(final Category cat) {
		return this.createEditModelAndView(cat, null);
	}

	protected ModelAndView createEditModelAndView(final Category cat, final String messageCode) {
		ModelAndView res;
		int parentId = 0;
		if (cat.getParent() != null)
			parentId = cat.getParent().getId();
		res = new ModelAndView("category/edit");
		res.addObject("category", cat);
		res.addObject("message", messageCode);
		res.addObject("parentId", parentId);
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

	//Auxiliar

	protected Collection<Category> removeCategories(Collection<Category> allCategories, final Category category) {
		if (category.getChildren().isEmpty())
			allCategories.remove(category);
		else {
			final Collection<Category> categories = category.getChildren();
			for (final Category c : categories)
				allCategories = this.removeCategories(allCategories, c);
		}
		return allCategories;
	}
}
