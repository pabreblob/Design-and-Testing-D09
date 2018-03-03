
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import repositories.CommentRepository;
import domain.Comment;

public class StringToCommentConverter implements Converter<String, Comment> {

	@Autowired
	private CommentRepository	commentRepository;


	@Override
	public Comment convert(final String source) {
		final Comment comment;
		int id;
		try {
			id = Integer.valueOf(source);
			comment = this.commentRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return comment;
	}

}
