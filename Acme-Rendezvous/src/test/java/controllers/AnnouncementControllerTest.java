
package controllers;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import services.UserService;
import utilities.AbstractTest;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AnnouncementControllerTest extends AbstractTest {

	@Autowired
	private WebApplicationContext	wac;

	@Autowired
	private UserService				userService;

	private MockMvc					mockMvc;


	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	/**
	 * Tests the listing of announcements.
	 * <p>
	 * This method tests the listing of the announcements of the rendezvouses that a user has RSVPd.
	 * <p>
	 * Req 16.5: An actor who is authenticated a user must be able to display a stream of announcements that have been posted to the rendezvouses that he or she's RSVPd. The announcements must be listed chronologically in descending order.
	 * 
	 * @throws Exception
	 *             An exception may be thrown.
	 */
	@Test
	public void getAnnouncementsAuthenticated() throws Exception {
		super.authenticate("user1");
		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcement/user/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("announcement/list"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("announcement/list")).andExpect(MockMvcResultMatchers.model().attribute("requestURI", Matchers.is("announcement/user/list.do")))
			.andExpect(MockMvcResultMatchers.model().attribute("announcements", Matchers.is(Matchers.not(Matchers.empty()))));
		super.authenticate(null);
	}

	/**
	 * Tests the listing of announcements.
	 * <p>
	 * This method tests the listing of the announcements of the rendezvouses that a user has RSVPd, when the user is not authenticated.
	 * <p>
	 * Req 16.5: An actor who is authenticated a user must be able to display a stream of announcements that have been posted to the rendezvouses that he or she's RSVPd. The announcements must be listed chronologically in descending order.
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAnnouncementsUnauthenticated() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcement/user/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("misc/panic")).andExpect(MockMvcResultMatchers.forwardedUrl("misc/panic"))
			.andExpect(MockMvcResultMatchers.model().attribute("name", Matchers.is("IllegalArgumentException")));
	}

	/**
	 * Tests the creation of announcements.
	 * <p>
	 * This method is used to test the creation of empty announcements before passing them to the corresponding views.
	 * <p>
	 * Req 16.1: An actor who is authenticated as a user must be able to create an announcement regarding one of the rendezvouses that he or she's created previously.
	 * 
	 * @throws Exception
	 */
	@Test
	public void createAnnouncement() throws Exception {
		super.authenticate("user1");
		final String rendezId = String.valueOf(new ArrayList<Rendezvous>(this.userService.findByPrincipal().getCreatedRendezvous()).get(0).getId());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcement/user/create").param("rendezvousId", rendezId)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("announcement/edit"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("announcement/edit")).andExpect(MockMvcResultMatchers.model().attributeExists("announcement"));
		super.authenticate(null);
	}
}
