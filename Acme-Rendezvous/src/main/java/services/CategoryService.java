
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
import domain.Category;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository	categoryRepository;
	@Autowired
	private AdminService		adminService;
	@Autowired
	private Validator			validator;


	public Category create(final Category parent) {
		Assert.notNull(this.adminService.findByPrincipal());
		Assert.notNull(parent);

		final Category cat = new Category();
		final Collection<domain.Service> services = new ArrayList<domain.Service>();
		final Collection<Category> children = new ArrayList<Category>();
		cat.setParent(parent);
		cat.setChildren(children);
		cat.setServices(services);

		return cat;
	}

	public Category createTop() {
		Assert.notNull(this.adminService.findByPrincipal());

		final Category cat = new Category();
		final Collection<domain.Service> services = new ArrayList<domain.Service>();
		final Collection<Category> children = new ArrayList<Category>();
		cat.setParent(null);
		cat.setChildren(children);
		cat.setServices(services);

		return cat;
	}

	public Category save(final Category cat) {
		Assert.notNull(cat);
		Assert.notNull(this.adminService.findByPrincipal());

		final Category res = this.categoryRepository.save(cat);
		Assert.notNull(res);

		if (res.getParent() != null) {
			final Category parent = cat.getParent();
			parent.getChildren().add(cat);
		}

		return res;
	}

	public Category findOne(final int categoryId) {
		Assert.isTrue(categoryId > 0);
		return this.categoryRepository.findOne(categoryId);
	}

	public Collection<Category> findAll() {
		return this.categoryRepository.findAll();
	}

	public void delete(final Category cat) {
		Assert.notNull(cat);
		Assert.notNull(this.adminService.findByPrincipal());
		Assert.isTrue(cat.getChildren().isEmpty());
		Assert.isTrue(cat.getServices().isEmpty());

		if (cat.getParent() != null)
			cat.getParent().getChildren().remove(cat);
		this.categoryRepository.delete(cat);

	}

	//Other

	public void move(final Category child, final Category parent) {
		Assert.notNull(child);
		Assert.notNull(this.adminService.findByPrincipal());

		if (child.getParent() != null)
			child.getParent().getChildren().remove(child);

		child.setParent(parent);
		if (parent != null)
			parent.getChildren().add(child);
	}

	public Collection<Category> findTopCategories() {
		return this.categoryRepository.findTopCategories();
	}

	public Category reconstruct(final Category category, final BindingResult binding) {
		Category res;
		if (category.getId() == 0) {
			res = category;
			final Collection<domain.Service> services = new ArrayList<domain.Service>();
			final Collection<Category> children = new ArrayList<Category>();
			res.setChildren(children);
			res.setServices(services);
		} else {
			res = this.categoryRepository.findOne(category.getId());

			res.setName(category.getName());
			res.setDescription(category.getDescription());
		}
		this.validator.validate(res, binding);

		return res;
	}

}
