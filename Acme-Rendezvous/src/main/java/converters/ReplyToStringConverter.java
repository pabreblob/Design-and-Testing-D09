
package converters;

import org.springframework.core.convert.converter.Converter;

import domain.Reply;

public class ReplyToStringConverter implements Converter<Reply, String> {

	@Override
	public String convert(final Reply reply) {
		String res;
		if (reply == null)
			res = null;
		else
			res = String.valueOf(reply.getId());
		return res;
	}

}
