
package controllers;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import org.hibernate.Hibernate;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;
import services.CommentService;
import services.RendezvousService;
import services.UserService;
import domain.Administrator;
import domain.Comment;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/rendezvous")
public class RendezvousController extends AbstractController {

	@Autowired
	RendezvousService	rendezvousService;

	@Autowired
	UserService			userService;

	@Autowired
	AdminService		adminService;

	@Autowired
	CommentService		commentService;


	//	Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Rendezvous> rendezvous;
		Boolean adult = false;
		rendezvous = this.rendezvousService.findFinalRendezvous();
		res = new ModelAndView("rendezvous/list");
		try {
			final User user = this.userService.findByPrincipal();
			//			final DateTime fechaNacimiento = new DateTime(user.getBirthdate());
			//			final DateTime fechaActual = new DateTime();

			final Integer edad = this.calculateAge(user.getBirthdate());
			if (edad >= 18) {
				adult = true;
				rendezvous = this.rendezvousService.findFinalRendezvousAdult();
			}
			if (user != null)
				res.addObject("userLogged", user);
		} catch (final Exception e) {

		}
		final Date currentTime = new Date(System.currentTimeMillis());
		final Timestamp timestamp = new Timestamp(currentTime.getTime());
		res.addObject("rendezvous", rendezvous);
		res.addObject("timestamp", timestamp);
		res.addObject("adult", adult);
		res.addObject("requestURI", "rendezvous/list.do");

		return res;
	}
	//Linked list
	@RequestMapping(value = "/list-linked", method = RequestMethod.GET)
	public ModelAndView listLinked(@RequestParam final int rendezvousId) {
		ModelAndView res;
		Collection<Rendezvous> rendezvous;
		rendezvous = this.rendezvousService.findFinalRendezvousLinked(rendezvousId);
		res = new ModelAndView("rendezvous/list");
		try {
			final User user = this.userService.findByPrincipal();
			final Integer edad = this.calculateAge(user.getBirthdate());
			if (edad >= 18)
				rendezvous = this.rendezvousService.findFinalRendezvousLinkedAdult(rendezvousId);
			if (user != null)
				res.addObject("userLogged", user);
		} catch (final Exception e) {

		}
		final Date currentTime = new Date(System.currentTimeMillis());
		final Timestamp timestamp = new Timestamp(currentTime.getTime());
		res.addObject("rendezvous", rendezvous);
		res.addObject("timestamp", timestamp);
		res.addObject("requestURI", "rendezvous/list.do");

		return res;
	}
	//	Displaying
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int rendezvousId) {
		ModelAndView res;
		boolean mayor = false;
		final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
		Assert.isTrue(!r.isDeleted());
		final int linkedSize = r.getLinkedRendezvous().size();
		int linked2Size = this.rendezvousService.findFinalRendezvousLinked(rendezvousId).size();
		System.out.println(linked2Size);
		try {
			final User user = this.userService.findByPrincipal();
			final Integer edad = this.calculateAge(user.getBirthdate());
			if (edad >= 18)
				mayor = true;
			linked2Size = this.rendezvousService.findFinalRendezvousLinkedAdult(rendezvousId).size();
		} catch (final Exception e) {

		}

		try {
			final Administrator admin = this.adminService.findByPrincipal();
			if (admin != null)
				mayor = true;
		} catch (final Exception e) {

		}
		if (r.isAdultContent() == true)
			Assert.isTrue(mayor);

		final Collection<Comment> commentList = this.commentService.findCommentsOrdered(rendezvousId);
		for (final Comment c : commentList)
			Hibernate.initialize(c.getReplies());
		final boolean isFinal = r.isFinalMode();
		boolean rsvpd = false;
		try {
			final User u = this.userService.findByPrincipal();
			if (u != null)
				rsvpd = r.getAttendants().contains(u);
		} catch (final Exception e) {

		}

		res = new ModelAndView("rendezvous/display");
		System.out.println();
		res.addObject("rendezvous", r);
		res.addObject("rendezvousId", rendezvousId);
		res.addObject("linkedSize", linkedSize);
		res.addObject("linked2Size", linked2Size);
		res.addObject("comments", commentList);
		res.addObject("isFinal", isFinal);
		res.addObject("rsvpd", rsvpd);
		res.addObject("announcementSize", this.rendezvousService.findAnnouncementsByRendezId(r.getId()).size());
		int pictureSize;
		if (r.getPictureURL() == null || r.getPictureURL().length() == 0)
			pictureSize = 0;
		else
			pictureSize = r.getPictureURL().length();
		res.addObject("pictureSize", pictureSize);

		return res;
	}
	@SuppressWarnings("deprecation")
	private int calculateAge(final Date birthdate) {
		final LocalDate birth = new LocalDate(birthdate.getYear() + 1900, birthdate.getMonth() + 1, birthdate.getDate());
		final LocalDate now = new LocalDate();
		return Years.yearsBetween(birth, now).getYears();
	}
}
