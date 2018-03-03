
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.AnswerRepository;
import domain.Answer;

@Component
@Transactional
public class StringToAnswerConverter implements Converter<String, Answer> {

	@Autowired
	private AnswerRepository	answerRepository;


	@Override
	public Answer convert(final String source) {
		final Answer answer;
		int id;
		try {
			id = Integer.valueOf(source);
			answer = this.answerRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return answer;
	}
}
