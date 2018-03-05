
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.CategoryRepository;
import domain.Category;

public class StringToCategoryConverter implements Converter<String, Category> {

	@Autowired
	private CategoryRepository	categoryRepository;


	@Override
	public Category convert(final String source) {
		Category category;
		int id;
		try {
			id = Integer.valueOf(source);
			category = this.categoryRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return category;
	}

}
