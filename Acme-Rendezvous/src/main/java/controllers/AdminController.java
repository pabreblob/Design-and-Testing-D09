
package controllers;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;
import domain.Administrator;
import domain.Rendezvous;

@Controller
@RequestMapping("/admin")
public class AdminController extends AbstractController {

	@Autowired
	private AdminService	adminService;


	public AdminController() {
		super();
	}
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboard() {
		ModelAndView result;
		final DecimalFormat df = new DecimalFormat("#,###,##0.00");
		//Averages and standardDesvs
		final String averageRendezvousCreatedPerUser = df.format(this.adminService.getAverageRendezvousCreatedPerUser());
		final String standarDesvRendezvousCreatedPerUser = df.format(this.adminService.getStandartDeviationRendezvousCreatedPerUser());
		final String averageAttendantsPerRendezvous = df.format(this.adminService.getAverageAttendantsPerRendezvous());
		final String standarDesvAttendantsPerRendezvous = df.format(this.adminService.getStandartDeviationAttendantsPerRendezvous());
		final String averageRendezvousReservedPerUser = df.format(this.adminService.getAverageRendezvousReservedPerUser());
		final String standarDesvRendezvousReservedPerUser = df.format(this.adminService.getStandartRendezvousReservedPerUser());
		final String averageAnnouncementsPerRendezvous = df.format(this.adminService.getAverageAnnouncementsPerRendezvous());
		final String standarDesvAnnouncementsPerRendezvous = df.format(this.adminService.getStandartDeviationAnnouncementsPerRendezvous());
		final String averageQuestionsPerRendezvous = df.format(this.adminService.getAverageQuestionsPerRendezvous());
		final String standarDesvQuestionsPerRendezvous = df.format(this.adminService.getStandartDeviationQuestionsPerRendezvous());
		final String averageAnswersPerRendezvous = df.format(this.adminService.getAverageAnswersPerRendezvous());
		final String standarDesvAnswersPerRendezvous = df.format(this.adminService.getStandartDeviationAnswersPerRendezvous());
		final String averageRepliesPerComment = df.format(this.adminService.getAverageRepliesPerComment());
		final String standarDesvRepliesPerComment = df.format(this.adminService.getStandartDeviationRepliesPerComment());
		final Date currentTime = new Date(System.currentTimeMillis());
		final Timestamp timestamp = new Timestamp(currentTime.getTime());
		result = new ModelAndView("admin/dashboard");
		result.addObject("timestamp", timestamp);
		result.addObject("averageRendezvousCreatedPerUser", averageRendezvousCreatedPerUser);
		result.addObject("standarDesvRendezvousCreatedPerUser", standarDesvRendezvousCreatedPerUser);
		result.addObject("averageAttendantsPerRendezvous", averageAttendantsPerRendezvous);
		result.addObject("standarDesvAttendantsPerRendezvous", standarDesvAttendantsPerRendezvous);
		result.addObject("averageRendezvousReservedPerUser", averageRendezvousReservedPerUser);
		result.addObject("standarDesvRendezvousReservedPerUser", standarDesvRendezvousReservedPerUser);
		result.addObject("averageAnnouncementsPerRendezvous", averageAnnouncementsPerRendezvous);
		result.addObject("standarDesvAnnouncementsPerRendezvous", standarDesvAnnouncementsPerRendezvous);
		result.addObject("averageQuestionsPerRendezvous", averageQuestionsPerRendezvous);
		result.addObject("standarDesvQuestionsPerRendezvous", standarDesvQuestionsPerRendezvous);
		result.addObject("averageAnswersPerRendezvous", averageAnswersPerRendezvous);
		result.addObject("standarDesvAnswersPerRendezvous", standarDesvAnswersPerRendezvous);
		result.addObject("averageRepliesPerComment", averageRepliesPerComment);
		result.addObject("standarDesvRepliesPerComment", standarDesvRepliesPerComment);

		//Ratios
		final Double ratioUsersWithCreatedRendezvous = this.adminService.getRatioUsersWithCreatedRendezvous();
		result.addObject("ratioUsersWithCreatedRendezvous", ratioUsersWithCreatedRendezvous);

		//Rendezvouses
		final List<Rendezvous> mostReserved;
		mostReserved = this.adminService.getMostReservedRendezvous();
		final List<Rendezvous> announcementsOver75;
		announcementsOver75 = this.adminService.getRendezvousesAnnouncementsOver75();
		final List<Rendezvous> linkedPlus10;
		linkedPlus10 = this.adminService.getRendezvousesLinkedPlus10();
		result.addObject("mostReserved", mostReserved);
		result.addObject("announcementsOver75", announcementsOver75);
		result.addObject("linkedPlus10", linkedPlus10);
		result.addObject("requestURI", "admin/dashboard.do");
		return result;
	}
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int adminId) {
		ModelAndView res;
		Administrator r;

		r = this.adminService.findOne(adminId);

		res = new ModelAndView("admin/display");
		res.addObject("administrator", r);
		res.addObject("userId", adminId);

		return res;
	}
}
