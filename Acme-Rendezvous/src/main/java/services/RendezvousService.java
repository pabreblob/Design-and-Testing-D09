
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RendezvousRepository;
import security.LoginService;
import domain.Announcement;
import domain.Comment;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class RendezvousService {

	@Autowired
	private RendezvousRepository	rendezvousRepository;

	@Autowired
	private UserService				userService;

	@Autowired
	private Validator				validator;


	//	@Autowired
	//	private AdminService			adminService;

	//	@Autowired
	//	private AnnouncementService		announcementService;
	//
	//	@Autowired
	//	private QuestionService			questionService;
	//
	//	@Autowired
	//	private CommentService			commentService;

	public Rendezvous create() {
		final Rendezvous res = new Rendezvous();
		final User creator = this.userService.findByPrincipal();
		res.setCreator(creator);
		final Collection<User> attendans = new ArrayList<User>();
		attendans.add(creator);
		res.setAttendants(attendans);
		res.setAnnouncements(new ArrayList<Announcement>());
		res.setComments(new ArrayList<Comment>());
		res.setLinkedRendezvous(new ArrayList<Rendezvous>());
		res.setQuestions(new ArrayList<Question>());

		return res;
	}
	public Collection<Rendezvous> findAll() {
		return this.rendezvousRepository.findAll();
	}

	public Rendezvous findOne(final int rendezvousId) {
		final Rendezvous res = this.rendezvousRepository.findOne(rendezvousId);
		return res;
	}

	public Rendezvous save(final Rendezvous rendezvous) {
		if (rendezvous.getId() != 0) {
			Assert.isTrue((rendezvous.getCreator().getUserAccount().getId() == LoginService.getPrincipal().getId()));
			//Assert.isTrue(this.findOne(rendezvous.getId()).isFinalMode() == false);
			Assert.isTrue(this.findOne(rendezvous.getId()).isDeleted() == false);
			Assert.isTrue(this.findOne(rendezvous.getId()).getMoment().after(new Date()));
		}
		Assert.isTrue(rendezvous.getMoment().after(new Date()));
		final Integer edad = this.calculateAge(this.userService.findByPrincipal().getBirthdate());
		if (edad < 18)
			Assert.isTrue(rendezvous.isAdultContent() == false);
		final Rendezvous saved = this.rendezvousRepository.save(rendezvous);
		if (rendezvous.getCreator().getReservedRendezvous().contains(rendezvous))
			rendezvous.getCreator().getCreatedRendezvous().remove(rendezvous);
		rendezvous.getCreator().getCreatedRendezvous().add(rendezvous);

		for (final User u : saved.getAttendants()) {
			if (u.getReservedRendezvous().contains(rendezvous))
				u.getReservedRendezvous().remove(rendezvous);
			u.getReservedRendezvous().add(rendezvous);

		}
		return saved;
	}

	public Rendezvous link(final Rendezvous rendezvous) {
		Assert.isTrue(rendezvous.getCreator().equals(this.userService.findByPrincipal()));
		final Rendezvous res = this.rendezvousRepository.save(rendezvous);
		for (final Rendezvous r : rendezvous.getLinkedRendezvous()) {
			if (r.getLinkedRendezvous().contains(rendezvous))
				r.getLinkedRendezvous().remove(rendezvous);
			r.getLinkedRendezvous().add(rendezvous);
		}
		return res;
	}

	public void unlink(final Rendezvous rendez, final Collection<Rendezvous> old) {
		Assert.isTrue(rendez.getCreator().equals(this.userService.findByPrincipal()));
		for (final Rendezvous r : old)
			r.getLinkedRendezvous().remove(rendez);
	}
	public Rendezvous join(final Rendezvous rendezvous) {
		Assert.isTrue(!rendezvous.getAttendants().contains(this.userService.findByPrincipal()));
		final User user = this.userService.findByPrincipal();
		final Integer edad = this.calculateAge(user.getBirthdate());
		if (rendezvous.isAdultContent())
			Assert.isTrue(edad >= 18);
		user.getReservedRendezvous().add(rendezvous);
		rendezvous.getAttendants().add(user);
		return rendezvous;
	}

	public Rendezvous unjoin(final Rendezvous rendezvous) {
		final User user = this.userService.findByPrincipal();
		Assert.isTrue(!rendezvous.getCreator().equals(user));
		Assert.isTrue(rendezvous.getAttendants().contains(this.userService.findByPrincipal()));
		user.getReservedRendezvous().remove(rendezvous);
		rendezvous.getAttendants().remove(user);
		return rendezvous;

	}
	public void cancel(final Rendezvous rendezvous) {
		assert rendezvous != null;
		assert rendezvous.getId() != 0;
		Assert.isTrue((rendezvous.getCreator().getUserAccount().getId() == LoginService.getPrincipal().getId()));
		Assert.isTrue(rendezvous.isFinalMode() == false);
		rendezvous.setDeleted(true);
		this.rendezvousRepository.save(rendezvous);
	}

	public void delete(final int rendezId) {
		assert rendezId != 0;
		final Rendezvous rendez = this.findOne(rendezId);

		//-------------------------
		rendez.setDeleted(true);
		this.rendezvousRepository.save(rendez);
		//-------------------------
		//		final Administrator admin = this.adminService.findByUserAccountId(LoginService.getPrincipal().getId());
		//		System.out.println(admin);
		//Assert.notNull(this.adminService.findByPrincipal());
		//		rendez.getCreator().getCreatedRendezvous().remove(rendez);
		//		for (final User u : rendez.getAttendants())
		//			u.getReservedRendezvous().remove(rendez);
		//
		//		for (final Rendezvous r : rendez.getLinkedRendezvous())
		//			r.getLinkedRendezvous().remove(rendez);
		//
		//		for (final Announcement a : rendez.getAnnouncements())
		//			this.announcementService.delete(a);
		//
		//		for (final Question q : rendez.getQuestions())
		//			this.questionService.delete(q, rendez);
		//
		//		for (final Comment c : rendez.getComments())
		//			this.commentService.delete(c);
		//
		//		this.rendezvousRepository.delete(rendezId);

	}

	public Collection<Rendezvous> findRendezvousCreatedByPrincipal() {
		final int id = LoginService.getPrincipal().getId();
		Assert.notNull(id);
		return this.rendezvousRepository.findRendezvousCreatedByUserAccountId(id);
	}

	public Collection<Rendezvous> findRendezvousNotDeletedCreatedByPrincipal() {
		final int id = LoginService.getPrincipal().getId();
		Assert.notNull(id);
		return this.rendezvousRepository.findRendezvousNotDeletedCreatedByUserId(id);
	}

	public Collection<Rendezvous> findRendezvousJoinedByPrincipal() {
		final int id = LoginService.getPrincipal().getId();
		Assert.notNull(id);
		return this.rendezvousRepository.findRendezvousJoinedByUserAccountId(id);
	}

	public Collection<Rendezvous> findRendezvousJoinedByUserId(final int id) {
		Assert.notNull(id);
		return this.rendezvousRepository.findRendezvousJoinedByUserId(id);
	}

	public Collection<Rendezvous> findRendezvousJoinedAdultByUserId(final int id) {
		Assert.notNull(id);
		return this.rendezvousRepository.findRendezvousJoinedAdultByUserId(id);
	}

	public Collection<Rendezvous> findFinalRendezvous() {
		return this.rendezvousRepository.findFinalRendezvous();
	}

	public Collection<Rendezvous> findFinalRendezvousAdult() {
		return this.rendezvousRepository.findFinalRendezvousAdult();
	}

	public Collection<Rendezvous> findFinalRendezvousLinked(final int rendezvousId) {
		return this.rendezvousRepository.findRendezvousLinked(rendezvousId);
	}
	public Collection<Rendezvous> findFinalRendezvousLinkedAdult(final int rendezvousId) {
		return this.rendezvousRepository.findRendezvousLinkedAdult(rendezvousId);
	}

	public Collection<Rendezvous> findAllRendezvousLinked(final int rendezId) {
		return this.rendezvousRepository.findAllRendezvousLinked(rendezId);
	}
	public Rendezvous findRendezvousByCommentId(final int commentId) {
		return this.rendezvousRepository.findRendezvousByCommentId(commentId);
	}

	public Collection<Rendezvous> findAnnouncementsByRendezId(final int rendezId) {
		return this.rendezvousRepository.findAnnouncementsByRendezId(rendezId);
	}

	public Rendezvous reconstruct(final Rendezvous rendez, final BindingResult binding) {
		Rendezvous result;

		result = this.rendezvousRepository.findOne(rendez.getId());
		result.setLinkedRendezvous(rendez.getLinkedRendezvous());

		this.validator.validate(result, binding);
		return result;
	}

	public Rendezvous reconstruct2(final Rendezvous rendez, final BindingResult binding) {
		Rendezvous result;
		if (rendez.getId() == 0) {
			result = rendez;
			final User creator = this.userService.findByPrincipal();
			result.setCreator(creator);
			final Collection<User> attendans = new ArrayList<User>();
			attendans.add(creator);
			result.setAttendants(attendans);
			result.setAnnouncements(new ArrayList<Announcement>());
			result.setComments(new ArrayList<Comment>());
			result.setLinkedRendezvous(new ArrayList<Rendezvous>());
			result.setQuestions(new ArrayList<Question>());
		} else {
			result = this.rendezvousRepository.findOne(rendez.getId());

			result.setName(rendez.getName());

			result.setDescription(rendez.getDescription());
			result.setMoment(rendez.getMoment());
			result.setPictureURL(rendez.getPictureURL());
			result.setGpsCoordinates(rendez.getGpsCoordinates());
			result.setFinalMode(rendez.isFinalMode());
			result.setAdultContent(rendez.isAdultContent());
			result.setDeleted(rendez.isDeleted());

		}
		this.validator.validate(result, binding);
		return result;
	}

	@SuppressWarnings("deprecation")
	private int calculateAge(final Date birthdate) {
		final LocalDate birth = new LocalDate(birthdate.getYear() + 1900, birthdate.getMonth() + 1, birthdate.getDate());
		final LocalDate now = new LocalDate();
		return Years.yearsBetween(birth, now).getYears();
	}

}
