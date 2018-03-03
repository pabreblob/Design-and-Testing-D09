
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Administrator;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AdminServiceTest extends AbstractTest {

	@Autowired
	private AdminService	adminService;


	@Test
	public void testCreate() {
		final Administrator res = this.adminService.create();
		Assert.notNull(res);
	}

	@Test
	public void testSave() {
		final Administrator res = this.adminService.create();
		res.setName("name1");
		res.setSurname("surname1");
		res.setEmail("email1@ejemplo.com");
		res.setPhone("123456792");
		res.setAddress("test");
		final Date birthdate = new Date(System.currentTimeMillis() - 100000000);
		res.setBirthdate(birthdate);
		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		final Administrator saved = this.adminService.save(res);
		Assert.notNull(saved);
	}

	@Test
	public void testFindOne() {
		final Administrator res = this.adminService.create();
		res.setName("name1");
		res.setSurname("surname1");
		res.setEmail("email1@ejemplo.com");
		res.setPhone("123456792");
		res.setAddress("test");
		final Date birthdate = new Date(System.currentTimeMillis() - 100000000);
		res.setBirthdate(birthdate);
		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		final Administrator saved = this.adminService.save(res);
		final Administrator found = this.adminService.findOne(saved.getId());
		Assert.isTrue(found.equals(saved));
	}

	@Test
	public void testFindAll() {
		final Administrator res = this.adminService.create();
		res.setName("name1");
		res.setSurname("surname1");
		res.setEmail("email1@ejemplo.com");
		res.setPhone("123456792");
		res.setAddress("test");
		final Date birthdate = new Date(System.currentTimeMillis() - 100000000);
		res.setBirthdate(birthdate);
		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		final Administrator saved = this.adminService.save(res);
		Assert.isTrue(this.adminService.findAll().contains(saved));
	}

	@Test
	public void testFindByPrincipal() {
		super.authenticate("admin");
		final Administrator principal = this.adminService.findByPrincipal();
		final Administrator aux = this.adminService.findOne(principal.getId());
		Assert.isTrue(principal.equals(aux));
	}

	@Test
	public void testFindByUserAccount() {
		super.authenticate("admin");
		final UserAccount ua = this.adminService.findByPrincipal().getUserAccount();
		final Administrator found = this.adminService.findByUserAccountId(ua.getId());
		Assert.notNull(found);
		super.authenticate(null);
	}
	//Dashboard
	@Test
	public void testGetAverageRendezvousCreatedPerUser() {
		super.authenticate("Admin");
		final Double avg = this.adminService.getAverageRendezvousCreatedPerUser();
		Assert.isTrue(avg != null);
		super.authenticate(null);
	}
	@Test
	public void testGetStandartDeviationRendezvousCreatedPerUser() {
		super.authenticate("Admin");
		final Double st = this.adminService.getStandartDeviationRendezvousCreatedPerUser();
		Assert.isTrue(st != null);
		super.authenticate(null);
	}
	@Test
	public void testGetRatioUserCreatedRendezvous() {
		super.authenticate("Admin");
		final Double st = this.adminService.getRatioUsersWithCreatedRendezvous();
		Assert.isTrue(st != null);
		super.authenticate(null);
	}
	@Test
	public void testGetAverageAttendantsPerRendezvous() {
		super.authenticate("Admin");
		final Double avg = this.adminService.getAverageAttendantsPerRendezvous();
		Assert.isTrue(avg != null);
		super.authenticate(null);
	}
	@Test
	public void testGetStandartDeviationAttendantsPerRendezvous() {
		super.authenticate("Admin");
		final Double st = this.adminService.getStandartDeviationAttendantsPerRendezvous();
		Assert.isTrue(st != null);
		super.authenticate(null);
	}
	@Test
	public void testGetAverageRendezvousReservedPerUser() {
		super.authenticate("Admin");
		final Double avg = this.adminService.getAverageRendezvousReservedPerUser();
		Assert.isTrue(avg != null);
		super.authenticate(null);
	}
	@Test
	public void testGetStandartDeviationRendezvousReservedPerUser() {
		super.authenticate("Admin");
		final Double st = this.adminService.getStandartRendezvousReservedPerUser();
		Assert.isTrue(st != null);
		super.authenticate(null);
	}
	@Test
	public void testGetMostReservedRendezvous() {
		super.authenticate("Admin");
		final List<Rendezvous> res = this.adminService.getMostReservedRendezvous();
		Assert.isTrue(res != null);
		super.authenticate(null);
	}
	@Test
	public void testGetAverageAnnouncementsPerRendezvous() {
		super.authenticate("Admin");
		final Double avg = this.adminService.getAverageAnnouncementsPerRendezvous();
		Assert.isTrue(avg != null);
		super.authenticate(null);
	}

	@Test
	public void testGetStandartDeviationAnnouncementsPerRendezvous() {
		super.authenticate("Admin");
		final Double st = this.adminService.getStandartDeviationAnnouncementsPerRendezvous();
		Assert.isTrue(st != null);
		super.authenticate(null);
	}
	@Test
	public void testGetRendezvousesAnnouncementsOver75() {
		super.authenticate("Admin");
		final List<Rendezvous> res = this.adminService.getRendezvousesAnnouncementsOver75();
		Assert.isTrue(res != null);
		super.authenticate(null);
	}
	@Test
	public void testGetRendezvousesLinkedPlus10() {
		super.authenticate("Admin");
		final List<Rendezvous> res = this.adminService.getRendezvousesLinkedPlus10();
		Assert.isTrue(res != null);
		super.authenticate(null);
	}
	@Test
	public void testGetAverageQuestionsPerRendezvous() {
		super.authenticate("Admin");
		final Double avg = this.adminService.getAverageQuestionsPerRendezvous();
		Assert.isTrue(avg != null);
		super.authenticate(null);
	}
	@Test
	public void testGetStandartDeviationQuestionsPerRendezvousr() {
		super.authenticate("Admin");
		final Double st = this.adminService.getStandartDeviationQuestionsPerRendezvous();
		Assert.isTrue(st != null);
		super.authenticate(null);
	}
	@Test
	public void testGetAverageAnswersPerRendezvous() {
		super.authenticate("Admin");
		final Double avg = this.adminService.getAverageAnswersPerRendezvous();
		Assert.isTrue(avg != null);
		super.authenticate(null);
	}
	@Test
	public void testGetStandartDeviationAnswersPerRendezvous() {
		super.authenticate("Admin");
		final Double st = this.adminService.getStandartDeviationAnswersPerRendezvous();
		Assert.isTrue(st != null);
		super.authenticate(null);
	}
	@Test
	public void testGetAverageRepliesPerComment() {
		super.authenticate("Admin");
		final Double avg = this.adminService.getAverageRepliesPerComment();
		Assert.isTrue(avg != null);
		super.authenticate(null);
	}
	@Test
	public void testGetStandartDeviationRepliesPerComment() {
		super.authenticate("Admin");
		final Double st = this.adminService.getStandartDeviationRepliesPerComment();
		Assert.isTrue(st != null);
		super.authenticate(null);
	}

}
