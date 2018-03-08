
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import services.ManagerService;
import domain.Manager;
import forms.ManagerForm;

@Controller
@RequestMapping("/manager")
public class ManagerController extends AbstractController {

	@Autowired
	private ManagerService		managerService;
	@Autowired
	private UserAccountService	userAccountService;


	public ManagerController() {
		super();
	}
	@RequestMapping(value = "/list-all", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Manager> managers;

		final Boolean hasQuestions = false;

		managers = this.managerService.findAll();

		result = new ModelAndView("manager/list");
		result.addObject("managers", managers);

		result.addObject("hasQuestions", hasQuestions);
		result.addObject("requestURI", "manager/list-all.do");

		return result;
	}
	//Displaying
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int managerId) {
		ModelAndView res;
		Manager m;

		m = this.managerService.findOne(managerId);
		res = new ModelAndView("manager/display");
		res.addObject("manager", m);
		res.addObject("requestURI", "manager/display.do");

		return res;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final ManagerForm managerForm = new ManagerForm();
		final UserAccount ua = this.userAccountService.create();
		managerForm.setUserAccount(ua);

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.MANAGER);
		authorities.add(auth);
		managerForm.getUserAccount().setAuthorities(authorities);
		//		user = this.userService.create();
		result = this.createEditModelAndView(managerForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ManagerForm managerForm, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		Manager test;
		test = this.managerService.reconstruct(managerForm, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(managerForm);
		else if (managerForm.isAcceptTerms() == false)
			result = this.createEditModelAndView(managerForm, "manager.notAccepted.error");
		else if (!(managerForm.getConfirmPass().equals(managerForm.getUserAccount().getPassword())))
			result = this.createEditModelAndView(managerForm, "manager.differentPass.error");
		else
			try {
				this.managerService.save(test);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(managerForm, "manager.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final ManagerForm managerForm) {
		final ModelAndView result;
		result = this.createEditModelAndView(managerForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ManagerForm managerForm, final String messageCode) {
		final ModelAndView result;
		result = new ModelAndView("manager/edit");
		result.addObject("managerForm", managerForm);
		result.addObject("message", messageCode);
		return result;
	}

	//	@SuppressWarnings("deprecation")
	//	private int calculateAge(final Date birthdate) {
	//		final LocalDate birth = new LocalDate(birthdate.getYear() + 1900, birthdate.getMonth() + 1, birthdate.getDate());
	//		final LocalDate now = new LocalDate();
	//		return Years.yearsBetween(birth, now).getYears();
	//	}

}
