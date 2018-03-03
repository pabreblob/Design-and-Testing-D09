
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = "moment")
})
public class Comment extends DomainEntity {

	private Date				moment;
	private String				text;
	private String				pictureURL;

	private User				author;
	private Collection<Reply>	replies;


	public Comment() {
		super();
	}

	@Valid
	@NotNull
	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPictureURL() {
		return this.pictureURL;
	}

	public void setPictureURL(final String pictureURL) {
		this.pictureURL = pictureURL;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public User getAuthor() {
		return this.author;
	}

	public void setAuthor(final User author) {
		this.author = author;
	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<Reply> getReplies() {
		return this.replies;
	}

	public void setReplies(final Collection<Reply> replies) {
		this.replies = replies;
	}

}
