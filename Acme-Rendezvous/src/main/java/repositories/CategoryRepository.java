
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("select c from Category c where c.parent = null")
	Collection<Category> findTopCategories();

}
