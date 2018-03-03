
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
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UserServiceTest extends AbstractTest {

	@Autowired
	private UserService	userService;


	@Test
	public void testCreate() {
		final User res = this.userService.create();
		Assert.notNull(res);
	}

	@Test
	public void testSave() {
		final User res = this.userService.create();
		res.setName("name1");
		res.setSurname("surname1");
		res.setEmail("email1@ejemplo.com");
		res.setPhone("123456792");
		res.setAddress("test");
		final Date birthdate = new Date(System.currentTimeMillis() - 100000000);
		res.setBirthdate(birthdate);
		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.USER);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		final User saved = this.userService.save(res);
		Assert.notNull(saved);
	}

	@Test
	public void testFindOne() {
		final User res = this.userService.create();
		res.setName("name1");
		res.setSurname("surname1");
		res.setEmail("email1@ejemplo.com");
		res.setPhone("123456792");
		res.setAddress("test");
		final Date birthdate = new Date(System.currentTimeMillis() - 100000000);
		res.setBirthdate(birthdate);
		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.USER);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		final User saved = this.userService.save(res);
		final User found = this.userService.findOne(saved.getId());
		Assert.isTrue(found.equals(saved));
	}

	@Test
	public void testFindAll() {
		final User res = this.userService.create();
		res.setName("name1");
		res.setSurname("surname1");
		res.setEmail("email1@ejemplo.com");
		res.setPhone("123456792");
		res.setAddress("test");
		final Date birthdate = new Date(System.currentTimeMillis() - 100000000);
		res.setBirthdate(birthdate);
		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.USER);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		final User saved = this.userService.save(res);
		Assert.isTrue(this.userService.findAll().contains(saved));
	}

	@Test
	public void testFindByPrincipal() {
		super.authenticate("user1");
		final User principal = this.userService.findByPrincipal();
		final User aux = this.userService.findOne(principal.getId());
		Assert.isTrue(principal.equals(aux));
	}

	@Test
	public void testFindByUserAccount() {
		super.authenticate("user1");
		final UserAccount ua = this.userService.findByPrincipal().getUserAccount();
		final User found = this.userService.findByUserAccountId(ua.getId());
		Assert.notNull(found);
		super.authenticate(null);
	}

}
