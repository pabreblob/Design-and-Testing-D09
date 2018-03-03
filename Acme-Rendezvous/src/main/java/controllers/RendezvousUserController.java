
package controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnswerService;
import services.RendezvousService;
import services.UserService;
import domain.Answer;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/rendezvous/user")
public class RendezvousUserController extends AbstractController {

	@Autowired
	RendezvousService	rendezvousService;

	@Autowired
	UserService			userService;

	@Autowired
	AnswerService		answerService;


	//	Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Collection<Rendezvous> rendezvous;
		rendezvous = this.rendezvousService.findRendezvousCreatedByPrincipal();
		final int rendezSizeLinked = this.rendezvousService.findRendezvousNotDeletedCreatedByPrincipal().size() - 1;
		System.out.println(rendezSizeLinked);
		res = new ModelAndView("rendezvous/list");
		final Date currentTime = new Date(System.currentTimeMillis());
		final Timestamp timestamp = new Timestamp(currentTime.getTime());
		res.addObject("rendezvous", rendezvous);
		res.addObject("timestamp", timestamp);
		res.addObject("rendezSizeLink", rendezSizeLinked);
		res.addObject("requestURI", "rendezvous/user/list.do");

		return res;
	}

	//Listing
	@RequestMapping(value = "/list-joined", method = RequestMethod.GET)
	public ModelAndView listCreated() {
		ModelAndView res;
		Collection<Rendezvous> rendezvous;
		rendezvous = this.rendezvousService.findRendezvousJoinedByPrincipal();

		res = new ModelAndView("rendezvous/list");
		final Date currentTime = new Date(System.currentTimeMillis());
		final Timestamp timestamp = new Timestamp(currentTime.getTime());
		res.addObject("rendezvous", rendezvous);
		res.addObject("timestamp", timestamp);
		res.addObject("userLogged", this.userService.findByPrincipal());
		res.addObject("requestURI", "rendezvous/user/list-joined.do");

		return res;
	}

	@RequestMapping(value = "/list-linked", method = RequestMethod.GET)
	public ModelAndView listLinked(@RequestParam final int rendezvousId) {
		ModelAndView res;
		Collection<Rendezvous> rendezvous;
		rendezvous = this.rendezvousService.findAllRendezvousLinked(rendezvousId);
		res = new ModelAndView("rendezvous/list");
		final User user = this.userService.findByPrincipal();
		Assert.isTrue(this.rendezvousService.findOne(rendezvousId).getCreator().equals(user));
		final Date currentTime = new Date(System.currentTimeMillis());
		final Timestamp timestamp = new Timestamp(currentTime.getTime());
		res.addObject("rendezvous", rendezvous);
		res.addObject("timestamp", timestamp);
		res.addObject("userLogged", user);
		res.addObject("requestURI", "rendezvous/list.do");

		return res;
	}

	//
	//	//Displaying
	//	@RequestMapping(value = "/display", method = RequestMethod.GET)
	//	public ModelAndView display(@RequestParam final int auditId) {
	//		ModelAndView res;
	//		Audit audit;
	//		audit = this.auditService.findOne(auditId);
	//		res = new ModelAndView("audit/display");
	//		res.addObject("audit", audit);
	//
	//		return res;
	//	}
	//
	//Creating
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView res;
		final Rendezvous r = this.rendezvousService.create();
		res = this.createEditModelAndView(r);

		return res;
	}
	//	//Editing
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int rendezvousId) {
		ModelAndView res;
		Rendezvous a;

		a = this.rendezvousService.findOne(rendezvousId);
		Assert.isTrue(this.userService.findByPrincipal().equals(a.getCreator()));
		Assert.isTrue(a.isFinalMode() == false);
		res = this.createEditModelAndView(a);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Rendezvous a, final BindingResult binding) {
		ModelAndView res;
		a = this.rendezvousService.reconstruct2(a, binding);
		if (binding.hasErrors())
			res = this.createEditModelAndView(a);
		else if ((a.getGpsCoordinates().getLatitude() != null && a.getGpsCoordinates().getLongitude() == null) || (a.getGpsCoordinates().getLatitude() == null && a.getGpsCoordinates().getLongitude() != null))
			res = this.createEditModelAndView(a, "rendez.coordinate.error");
		else
			try {
				this.rendezvousService.save(a);
				res = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(a, "rendez.commit.error");
			}
		return res;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "cancel")
	public ModelAndView cancel(Rendezvous r) {
		ModelAndView res;
		r = this.rendezvousService.reconstruct2(r, null);
		try {
			this.rendezvousService.cancel(r);
			res = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(r, "rendez.commit.error");
		}
		return res;
	}

	@RequestMapping(value = "/joinQuestion", method = RequestMethod.GET)
	public ModelAndView joinQuestion(@RequestParam final int rendezvousId) {
		ModelAndView res;
		final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
		if (r.getQuestions().size() == 0)
			res = new ModelAndView("redirect:/rendezvous/user/join.do?rendezvousId=" + rendezvousId);
		else
			res = new ModelAndView("redirect:/answer/user/fill.do?rendezvousId=" + rendezvousId);

		return res;
	}

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public ModelAndView join(@RequestParam final int rendezvousId) {
		ModelAndView res;
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		try {
			this.rendezvousService.join(rendezvous);
			res = new ModelAndView("redirect:list-joined.do");
		} catch (final Exception e) {
			res = new ModelAndView("redirect:list-joined.do");
			res.addObject("message", "rendez.commit.error");
		}
		return res;
	}
	@RequestMapping(value = "/unjoin", method = RequestMethod.GET)
	public ModelAndView unjoin(@RequestParam final int rendezvousId) {
		ModelAndView res;
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		try {
			for (final Answer a : this.answerService.findAnswersByUserAndRendezvous(this.userService.findByPrincipal().getId(), rendezvousId))
				this.answerService.delete(a, a.getQuestion());
			this.rendezvousService.unjoin(rendezvous);
			res = new ModelAndView("redirect:list-joined.do");
		} catch (final Exception e) {
			res = new ModelAndView("redirect:list-joined.do");
			res.addObject("message", "rendez.commit.error");
		}
		return res;
	}
	@RequestMapping(value = "/link", method = RequestMethod.GET)
	public ModelAndView link(@RequestParam final int rendezvousId) {
		ModelAndView res;
		final Rendezvous rendez = this.rendezvousService.findOne(rendezvousId);
		final Collection<Rendezvous> rendezs = this.rendezvousService.findRendezvousNotDeletedCreatedByPrincipal();
		rendezs.remove(rendez);
		res = new ModelAndView("rendezvous/link");
		res.addObject("rendezvous", rendez);
		res.addObject("rendezvouses", rendezs);
		return res;
	}

	@RequestMapping(value = "/link", method = RequestMethod.POST, params = "save")
	public ModelAndView linkSave(Rendezvous rendez, final BindingResult binding) {
		ModelAndView res;
		if (rendez.getLinkedRendezvous() == null || new ArrayList<>(rendez.getLinkedRendezvous()).get(0) == null)
			rendez.setLinkedRendezvous(new ArrayList<Rendezvous>());
		final Collection<Rendezvous> oldRendezvous = new ArrayList<Rendezvous>(this.rendezvousService.findOne(rendez.getId()).getLinkedRendezvous());
		oldRendezvous.removeAll(rendez.getLinkedRendezvous());

		rendez = this.rendezvousService.reconstruct(rendez, binding);
		if (binding.hasErrors()) {
			res = new ModelAndView("rendezvous/link");
			res.addObject("rendezvous", rendez);
		} else
			try {
				this.rendezvousService.link(rendez);
				this.rendezvousService.unlink(rendez, oldRendezvous);
				res = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				res = new ModelAndView("rendezvous/link");
				res.addObject("rendezvous", rendez);
				res.addObject("message", "rendez.commit.error");
			}
		return res;
	}
	//Ancillary methods
	protected ModelAndView createEditModelAndView(final Rendezvous r) {
		return this.createEditModelAndView(r, null);
	}
	protected ModelAndView createEditModelAndView(final Rendezvous r, final String messageCode) {
		ModelAndView res;
		final Integer edad = this.calculateAge(this.userService.findByPrincipal().getBirthdate());
		Boolean adult = false;
		if (edad >= 18)
			adult = true;
		res = new ModelAndView("rendezvous/edit");
		res.addObject("rendezvous", r);
		res.addObject("adult", adult);
		res.addObject("message", messageCode);
		return res;
	}

	@SuppressWarnings("deprecation")
	private int calculateAge(final Date birthdate) {
		final LocalDate birth = new LocalDate(birthdate.getYear() + 1900, birthdate.getMonth() + 1, birthdate.getDate());
		final LocalDate now = new LocalDate();
		return Years.yearsBetween(birth, now).getYears();
	}
}
