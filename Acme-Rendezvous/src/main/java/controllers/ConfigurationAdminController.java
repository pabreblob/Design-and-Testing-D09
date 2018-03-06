
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping("/list")
	public ModelAndView display() {
		final ModelAndView res = new ModelAndView("configuration/list");
		final Configuration conf = this.configurationService.find();
		res.addObject("config", conf);
		return res;
	}

	@RequestMapping("/edit")
	public ModelAndView edit() {
		final ModelAndView res = new ModelAndView("configuration/edit");
		final Configuration conf = this.configurationService.find();
		res.addObject("configuration", conf);
		return res;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@Valid final Configuration configuration, final BindingResult binding) {
		ModelAndView res;
		if (binding.hasErrors()) {
			res = new ModelAndView("configuration/edit");
			res.addObject("configuration", configuration);
		} else
			try {
				Assert.notNull(configuration);
				Assert.isTrue(this.configurationService.find().getId() == configuration.getId() && this.configurationService.find().getVersion() == configuration.getVersion());
				this.configurationService.save(configuration);
				res = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				res = new ModelAndView("configuration/edit");
				res.addObject("configuration", configuration);
				res.addObject("message", "configuration.error");
			}
		return res;
	}

}
