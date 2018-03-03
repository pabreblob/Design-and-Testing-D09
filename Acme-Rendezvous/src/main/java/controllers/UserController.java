
package controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.UserAccount;
import security.UserAccountService;
import services.AdminService;
import services.RendezvousService;
import services.UserService;
import domain.Administrator;
import domain.Rendezvous;
import domain.User;
import forms.UserForm;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	@Autowired
	private UserService			userService;
	@Autowired
	private AdminService		adminService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserAccountService	userAccountService;


	public UserController() {
		super();
	}
	@RequestMapping(value = "/list-all", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<User> users;
		final Collection<Administrator> administrators = this.adminService.findAll();
		final Boolean hasQuestions = false;
		final Boolean adminList = true;
		users = this.userService.findAll();

		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("adminList", adminList);
		result.addObject("administrators", administrators);
		result.addObject("hasQuestions", hasQuestions);
		result.addObject("requestURI", "user/list-all.do");

		return result;
	}
	@RequestMapping(value = "/list-attendants", method = RequestMethod.GET)
	public ModelAndView listAttendants(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Collection<User> users;
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		users = rendezvous.getAttendants();
		Boolean hasQuestions = false;
		final Boolean adminList = false;
		final Integer creator = rendezvous.getCreator().getId();
		if (rendezvous.getQuestions().size() > 0)
			hasQuestions = true;
		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("hasQuestions", hasQuestions);
		result.addObject("adminList", adminList);
		result.addObject("creator", creator);
		result.addObject("rendezvousId", rendezvousId);
		result.addObject("requestURI", "user/list-attendants.do");

		return result;
	}
	//Displaying
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {
		ModelAndView res;
		User r;

		r = this.userService.findOne(userId);
		Collection<Rendezvous> rendezvous = this.rendezvousService.findRendezvousJoinedByUserId(userId);
		try {
			final User user = this.userService.findByPrincipal();
			final Integer edad = this.calculateAge(user.getBirthdate());
			if (edad >= 18)
				rendezvous = this.rendezvousService.findRendezvousJoinedAdultByUserId(userId);
		} catch (final Exception e) {

		}
		final boolean rendezEmpty = rendezvous.isEmpty();
		final Date currentTime = new Date(System.currentTimeMillis());
		final Timestamp timestamp = new Timestamp(currentTime.getTime());
		res = new ModelAndView("user/display");
		res.addObject("user", r);
		res.addObject("rendezvous", rendezvous);
		res.addObject("rendezEmpty", rendezEmpty);
		res.addObject("timestamp", timestamp);
		res.addObject("requestURI", "user/display.do");
		res.addObject("userId", userId);

		return res;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final UserForm userForm = new UserForm();
		final UserAccount ua = this.userAccountService.create();
		userForm.setUserAccount(ua);

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.USER);
		authorities.add(auth);
		userForm.getUserAccount().setAuthorities(authorities);
		//		user = this.userService.create();
		result = this.createEditModelAndView(userForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final UserForm userForm, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		User test;
		test = this.userService.reconstruct(userForm, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(userForm);
		else if (userForm.isAcceptTerms() == false)
			result = this.createEditModelAndView(userForm, "user.notAccepted.error");
		else if (!(userForm.getConfirmPass().equals(userForm.getUserAccount().getPassword())))
			result = this.createEditModelAndView(userForm, "user.differentPass.error");
		else
			try {
				this.userService.save(test);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(userForm, "user.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final UserForm userForm) {
		final ModelAndView result;
		result = this.createEditModelAndView(userForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final UserForm userForm, final String messageCode) {
		final ModelAndView result;
		result = new ModelAndView("user/edit");
		result.addObject("userForm", userForm);
		result.addObject("message", messageCode);
		return result;
	}

	@SuppressWarnings("deprecation")
	private int calculateAge(final Date birthdate) {
		final LocalDate birth = new LocalDate(birthdate.getYear() + 1900, birthdate.getMonth() + 1, birthdate.getDate());
		final LocalDate now = new LocalDate();
		return Years.yearsBetween(birth, now).getYears();
	}

}
