
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CategoryRepository;
import domain.Category;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository	categoryRepository;
	@Autowired
	private AdminService		adminService;


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

	public Category save(final Category cat) {
		Assert.notNull(cat);
		Assert.notNull(this.adminService.findByPrincipal());

		final Category res = this.categoryRepository.save(cat);
		Assert.notNull(res);

		final Category parent = cat.getParent();
		parent.getChildren().add(cat);
		return res;
	}

	public Category findOne(final int categoryId) {
		Assert.isTrue(categoryId > 0);
		return this.categoryRepository.findOne(categoryId);
	}

	public Collection<Category> findAll() {
		return this.categoryRepository.findAll();
	}

}
