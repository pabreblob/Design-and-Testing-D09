
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Question;

@Component
@Transactional
public class QuestionToStringConverter implements Converter<Question, String> {

	@Override
	public String convert(final Question question) {
		String res;
		if (question == null)
			res = null;
		else
			res = String.valueOf(question.getId());
		return res;
	}
}
