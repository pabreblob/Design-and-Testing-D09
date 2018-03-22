
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CategoryServiceTest extends AbstractTest {

	@Autowired
	private CategoryService	categoryService;


	/**
	 * Tests the creation of Categories.
	 * This method tests the creation of a Top Category, which means it has no parent,
	 * and a Sub-Category for that top category.
	 * 
	 * Functional requirement 11.1 (Acme-Rendezvous 2.0):
	 * An actor who is authenticated as an administrator must be able to:
	 * manage the catalogue of categories, which includes: [...] creating [...].
	 * 
	 * Case 1: An Administrator tries to create both a new Top-Category
	 * and a Sub-Category. No exception is expected.
	 * Case 2: An User tries to create Categories.
	 * An <code>IllegalArgumentException</code> is expected
	 */
	@Test
	public void driverCreateCategory() {
		final Object testingData[][] = {
			{
				"admin", null
			}, {
				"user1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateCategory((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}
	/**
	 * Template for testing the creation of Categories
	 * This template creates a Top-Category and a Sub-Category for the first.
	 * 
	 * @param username
	 *            The name of the user trying to create the Categories.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	protected void templateCreateCategory(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Category top = this.categoryService.createTop();
			Assert.notNull(top);
			final Category cat = this.categoryService.create(top);
			Assert.notNull(cat);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	/**
	 * Tests the saving of a Category.
	 * This method check the saving of categories after the Administrator
	 * writes the data.
	 * 
	 * Functional requirement 11.1 (Acme-Rendezvous 2.0):
	 * An actor who is authenticated as an administrator must be able to:
	 * manage the catalogue of categories, which includes: [...] creating [...]
	 * 
	 * Case 1: The Administrator creates a new Sub-Category. No exception is expected.
	 * 
	 * Case 2: The Administrator creates a new Top-Category. No exception is expected.
	 * 
	 * Case 3: An User tries to create a new category and save it.
	 * An <code>IllegalArgumentException</code> is expected.
	 * 
	 * Case 4: An unidentified user tries to create a new category and save it.
	 * An <code>IllegalArgumentException</code> is expected.
	 */
	@Test
	public void driverSaveCategory() {
		final Object testingData[][] = {
			{
				"admin", "Category1", null
			}, {
				"admin", null, null
			}, {
				"user1", "Category1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateSave((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the saving of Categories.
	 * 
	 * @param username
	 *            The name of the user trying to save a category.
	 * @param parentBean
	 *            The name of the bean of a Category. If null,
	 *            a top-category is created. If different from null and valid,
	 *            a new sub-category is created for that category
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	protected void templateSave(final String username, final String parentBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(username);
			Category cat;
			Category parent = null;
			if (parentBean != null) {
				parent = this.categoryService.findOne(super.getEntityId(parentBean));
				cat = this.categoryService.create(parent);
			} else
				cat = this.categoryService.createTop();
			cat.setName("name");
			cat.setDescription("description");

			final Category saved = this.categoryService.save(cat);
			Assert.notNull(saved);
			final Category found = this.categoryService.findOne(saved.getId());
			Assert.isTrue(found.equals(saved));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the edition of a Category.
	 * This method tests the edition of categories as it would be done
	 * by an administrator in the corresponding view.
	 * 
	 * Functional requirement 11.1 (Acme-Rendezvous 2.0):
	 * An actor who is authenticated as an administrator must be able to:
	 * manage the catalogue of categories, which includes: [...] updating [...]
	 * 
	 * Case 1: Category 1 is edited by an administrator. No exception is expected.
	 * Case 2: Category 1 is edited by an user.
	 * An <code>IllegalArgumentException</code> is expected.
	 */
	@Test
	public void driverEditCategory() {
		final Object testingData[][] = {
			{
				"admin", "Category1", null
			}, {
				"user", "Category1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the editing of Categories.
	 * This method defines the template used for the tests that check
	 * the editing of Categories.
	 * 
	 * @param username
	 *            The username of the user that is logged in.
	 * @param categoryBean
	 *            The name of the bean of the Category that the user wants to edit.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	protected void templateEdit(final String username, final String categoryBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Category cat = this.categoryService.findOne(super.getEntityId(categoryBean));
			final String newName = "newNameTest";
			cat.setName(newName);
			final String newDesc = "newDescTest";
			cat.setDescription(newDesc);
			final Category saved = this.categoryService.save(cat);
			Assert.notNull(saved);
			Assert.isTrue(saved.getName().equals(newName));
			Assert.isTrue(saved.getDescription().equals(newDesc));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the finding of one Category, given its ID.
	 * This method checks that a Category stored in the database
	 * can be retrieved.
	 * 
	 * Functional requirement 11.1 (Acme-Rendezvous 2.0):
	 * An actor who is authenticated as an administrator must be able to:
	 * manage the catalogue of categories, which includes:
	 * [...] updating*, deleting* [...]
	 * 
	 * Case 1: A valid bean name is given. No exception expected.
	 * Case 2: An invalid bean name is given, which is the most similar case to
	 * giving an invalid ID. An <code>AssertionError</code> is expected.
	 * 
	 * (*) Even if the requirements don't explicitly say that an actor of any kind has
	 * to be able to retrieve one Category from the Database, it makes
	 * sense that this has to be possible, not only for every actor
	 * so they can see the information regarding the Categories,
	 * but for the administrator so he/she can be able to update
	 * and delete them.
	 */
	@Test
	public void driverFindOneCategory() {
		final Object testingData[][] = {
			{
				"Category1", null
			}, {
				"non-valid", AssertionError.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateFindOne((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * Template for testing the finding of one Category.
	 * This method defines the template used for the tests that check the finding
	 * of one Category.
	 * 
	 * @param categoryBean
	 *            The name of the bean of the Category that the user
	 *            wants to retrieve.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	protected void templateFindOne(final String categoryBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final Category cat = this.categoryService.findOne(super.getEntityId(categoryBean));
			Assert.notNull(cat);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the finding of Top-Categories.
	 * This method tests that the subset of only Categories without parent is retrieved
	 * correctly.
	 * 
	 * Functional requirement 11.1 (Acme-Rendezvous 2.0):
	 * An actor who is authenticated as an administrator must be able to:
	 * manage the catalogue of categories, which includes: [...] listing* [...]
	 * 
	 * (*) Even if the requirements don't explicitly say that any actor has to be
	 * able to see the list of "Top-Categories" only, this method is necessary for
	 * the organisation of the lists in the views that include listing Categories.
	 */
	@Test
	public void testFindTopCategories() {
		super.authenticate("admin");
		Category test1 = this.categoryService.createTop();
		test1.setName("test1");
		test1.setDescription("test1");
		test1 = this.categoryService.save(test1);
		Category test2 = this.categoryService.createTop();
		test2.setName("test2");
		test2.setDescription("test2");
		test2 = this.categoryService.save(test2);
		super.unauthenticate();
		final Collection<Category> tops = this.categoryService.findTopCategories();
		Assert.notNull(tops);
		Assert.isTrue(tops.contains(test1) && tops.contains(test2));
	}

	/**
	 * Tests the finding of All Categories.
	 * 
	 * This method tests that All Categories can be retrieved at once from the
	 * Database.
	 * 
	 * Functional requirement 11.1 (Acme-Rendezvous 2.0):
	 * An actor who is authenticated as an administrator must be able to:
	 * manage the catalogue of categories, which includes: [...] re-organising* [...]
	 * 
	 * (*) Even if the requirements don't explicitly say that any actor has to be
	 * able to list all Categories at once, this method is needed when re-organising
	 * the hierarchy of Categories, because the options have to be filtered.
	 * See the method <code>move</code> and the
	 * auxiliary method <code>removeCategories</code> in
	 * the <code>CategoryAdminController</code> for further details.
	 */
	@Test
	public void testFindAllCategories() {
		super.authenticate("admin");
		Category test1 = this.categoryService.createTop();
		test1.setName("test1");
		test1.setDescription("test1");
		test1 = this.categoryService.save(test1);
		Category test2 = this.categoryService.create(test1);
		test2.setName("test2");
		test2.setDescription("test2");
		test2 = this.categoryService.save(test2);
		super.unauthenticate();
		final Collection<Category> all = this.categoryService.findAll();
		Assert.notNull(all);
		Assert.isTrue(all.contains(test1) && all.contains(test2));
	}

	/**
	 * Tests the deletion of a Category
	 * This method tests that a Category can be deleted, only if it's
	 * empty at the moment of deletion (it has no Sub-Categories and
	 * no Services associated).
	 * 
	 * Functional requirement 11.1 (Acme-Rendezvous 2.0):
	 * An actor who is authenticated as an administrator must be able to:
	 * manage the catalogue of categories, which includes: [...] deleting [...]
	 * 
	 * Case 1: The Administrator deletes an empty Category. No exception is expected.
	 * Case 2: The Administrator tries to delete a non-empty Category. <code>IllegalArgumentException</code> expected.
	 * Case 3: An user tries to delete a Category. <code>IllegalArgumentException</code> expected.
	 */
	@Test
	public void driverDeleteCategory() {
		final Object testingData[][] = {
			{
				"admin", "Category4", null
			}, {
				"admin", "Category1", IllegalArgumentException.class
			}, {
				"user1", "Category4", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * Template for testing the deletion of a Category.
	 * This method defines the template used to test the deletion of a Category.
	 * 
	 * @param username
	 *            The username of the user that tries to make the deletion.
	 * @param categoryBean
	 *            The name of the Category that is going to be deleted.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	protected void templateDelete(final String username, final String categoryBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Category cat = this.categoryService.findOne(super.getEntityId(categoryBean));
			this.categoryService.delete(cat);
			super.unauthenticate();
			Assert.isTrue(!this.categoryService.findAll().contains(cat));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * Tests the moving of Categories.
	 * This method tests the moving of Categories between the hierarchy.
	 * 
	 * Functional requirement 11.1 (Acme-Rendezvous 2.0):
	 * An actor who is authenticated as an administrator must be able to:
	 * manage the catalogue of categories, which includes: [...] re-organising [...].
	 * 
	 * Case 1: The Administrator tries to move a Category to other Over-Category.
	 * No exception is expected.
	 * Case2: The Administrator tries to move a Category to one of its Sub-Categories.
	 * An <code>IllegalArgumentException</code> is expected.
	 * Case3: An user tries to move a Category.
	 * An <code>IllegalArgumentException</code> is expected.
	 */
	@Test
	public void driverMoveCategory() {
		final Object testingData[][] = {
			{
				"admin", "Category3", "Category2", null
			}, {
				"admin", "Category1", "Category2", IllegalArgumentException.class
			}, {
				"user1", "Category5", "Category1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateMove((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * Template for testing the moving of Categories.
	 * This method defines the template used to test the moving of Categories
	 * between the hierarchy.
	 * 
	 * @param username
	 *            The username of the user that is trying to move the category.
	 * @param childBean
	 *            The name of the bean of the Category that is going to be moved.
	 * @param parentBean
	 *            The name of the bean of the Category that will become the new Over-Category for
	 *            the target Category.
	 * @param expected
	 *            The expected exception to be thrown.
	 *            Use <code>null</code> if no exception is expected.
	 */
	protected void templateMove(final String username, final String childBean, final String parentBean, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			final Category child = this.categoryService.findOne(super.getEntityId(childBean));
			final Category parent = this.categoryService.findOne(super.getEntityId(parentBean));
			this.categoryService.move(child, parent);
			Assert.isTrue(parent.getChildren().contains(child));
			Assert.isTrue(child.getParent().equals(parent));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
