
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Answer;

@Component
@Transactional
public class AnswerToStringConverter implements Converter<Answer, String> {

	@Override
	public String convert(final Answer answer) {
		String res;
		if (answer == null)
			res = null;
		else
			res = String.valueOf(answer.getId());
		return res;
	}
}
