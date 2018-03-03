
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Rendezvous extends DomainEntity {

	private String						name;
	private String						description;
	private Date						moment;
	private String						pictureURL;
	private GPSCoordinates				gpsCoordinates;
	private boolean						finalMode;
	private boolean						adultContent;
	private boolean						deleted;

	private User						creator;
	private Collection<User>			attendants;
	private Collection<Rendezvous>		linkedRendezvous;
	private Collection<Announcement>	announcements;
	private Collection<Question>		questions;
	private Collection<Comment>			comments;

	private Collection<Request>			requests;


	public Rendezvous() {
		super();
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@Valid
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
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
	public GPSCoordinates getGpsCoordinates() {
		return this.gpsCoordinates;
	}

	public void setGpsCoordinates(final GPSCoordinates gpsCoordinates) {
		this.gpsCoordinates = gpsCoordinates;
	}

	public boolean isFinalMode() {
		return this.finalMode;
	}

	public void setFinalMode(final boolean finalMode) {
		this.finalMode = finalMode;
	}

	public boolean isAdultContent() {
		return this.adultContent;
	}

	public void setAdultContent(final boolean adultContent) {
		this.adultContent = adultContent;
	}

	public boolean isDeleted() {
		return this.deleted;
	}

	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public User getCreator() {
		return this.creator;
	}

	public void setCreator(final User creator) {
		this.creator = creator;
	}

	@NotEmpty
	@NotNull
	@ManyToMany
	public Collection<User> getAttendants() {
		return this.attendants;
	}

	public void setAttendants(final Collection<User> attendants) {
		this.attendants = attendants;
	}

	@NotNull
	@ManyToMany
	public Collection<Rendezvous> getLinkedRendezvous() {
		return this.linkedRendezvous;
	}

	public void setLinkedRendezvous(final Collection<Rendezvous> linkedRendezvous) {
		this.linkedRendezvous = linkedRendezvous;
	}

	@NotNull
	@OneToMany(mappedBy = "rendezvous")
	public Collection<Announcement> getAnnouncements() {
		return this.announcements;
	}

	public void setAnnouncements(final Collection<Announcement> announcements) {
		this.announcements = announcements;
	}

	@NotNull
	@OneToMany
	public Collection<Question> getQuestions() {
		return this.questions;
	}

	public void setQuestions(final Collection<Question> questions) {
		this.questions = questions;
	}
	@NotNull
	@OneToMany
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

	@NotNull
	@OneToMany(mappedBy = "rendezvous")
	public Collection<Request> getRequests() {
		return this.requests;
	}

	public void setRequests(final Collection<Request> requests) {
		this.requests = requests;
	}

}
