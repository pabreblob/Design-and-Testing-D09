
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import domain.Configuration;

@Controller
@RequestMapping("configuration/admin")
public class ConfigurationAdminController extends AbstractController {

	@Autowired
	private ConfigurationService	configurationService;


	public ConfigurationAdminController() {
		super();
	}

	@RequestMapping("/display")
	public ModelAndView display() {
		final ModelAndView res = new ModelAndView("configuration/list");
		final Configuration conf = this.configurationService.find();
		res.addObject("config", conf);
		return res;
	}

}
