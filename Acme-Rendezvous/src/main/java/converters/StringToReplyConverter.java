
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.ReplyRepository;
import domain.Reply;

public class StringToReplyConverter implements Converter<String, Reply> {

	@Autowired
	private ReplyRepository	replyRepository;


	@Override
	public Reply convert(final String source) {
		final Reply reply;
		int id;
		try {
			id = Integer.valueOf(source);
			reply = this.replyRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return reply;
	}

}
