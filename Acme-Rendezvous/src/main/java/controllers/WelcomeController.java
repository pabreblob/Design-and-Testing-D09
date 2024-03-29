/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import services.ConfigurationService;
import services.UserService;
import domain.Announcement;
import domain.Configuration;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	@Autowired
	private AnnouncementService		announcementService;
	@Autowired
	private UserService				userService;
	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index(@RequestParam(required = false, defaultValue = "John Doe") String name) {
		ModelAndView result;
		result = new ModelAndView("welcome/index");
		SimpleDateFormat formatter;
		String moment;
		final Configuration configuration = this.configurationService.find();

		try {
			final Collection<Announcement> announcements = this.announcementService.findAnnouncementsByPrincipal();
			result.addObject("announcements", announcements);
			name = this.userService.findByPrincipal().getName() + " " + this.userService.findByPrincipal().getSurname();
		} catch (final Throwable oops) {

		}

		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());

		result.addObject("name", name);
		result.addObject("moment", moment);
		if (LocaleContextHolder.getLocale().getDisplayLanguage().equals("Spanish"))
			result.addObject("welcomeMessage", configuration.getWelcomeEsp());
		else
			result.addObject("welcomeMessage", configuration.getWelcomeEng());
		return result;
	}
}
