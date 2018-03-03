
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.QuestionRepository;
import domain.Question;

@Component
@Transactional
public class StringToQuestionConverter implements Converter<String, Question> {

	@Autowired
	private QuestionRepository	questionRepository;


	@Override
	public Question convert(final String source) {
		final Question question;
		int id;
		try {
			id = Integer.valueOf(source);
			question = this.questionRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return question;
	}

}
