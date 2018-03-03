
package controllers;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.UserService;
import domain.Rendezvous;
import domain.User;

@Controller
@RequestMapping("/user/user")
public class UserUserController extends AbstractController {

	@Autowired
	private UserService	userService;


	public UserUserController() {
		super();
	}

	//Displaying
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView res;
		User r;

		r = this.userService.findByPrincipal();
		final int userId = r.getId();
		final Collection<Rendezvous> rendezvous = r.getReservedRendezvous();
		final boolean rendezEmpty = rendezvous.isEmpty();
		final Date currentTime = new Date(System.currentTimeMillis());
		final Timestamp timestamp = new Timestamp(currentTime.getTime());
		res = new ModelAndView("user/display");
		res.addObject("user", r);
		res.addObject("timestamp", timestamp);
		res.addObject("rendezvous", rendezvous);
		res.addObject("rendezEmpty", rendezEmpty);
		res.addObject("requestURI", "user/user/display.do");
		res.addObject("userId", userId);

		return res;
	}

}
