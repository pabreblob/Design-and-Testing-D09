
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ManagerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Manager;
import forms.ManagerForm;

@Service
@Transactional
public class ManagerService {

	@Autowired
	private ManagerRepository	managerRepository;
	@Autowired
	private UserAccountService	userAccountService;

	//	@Bean
	//	public Validator validator() {
	//		return new LocalValidatorFactoryBean();
	//	}

	@Autowired
	private Validator			validator;


	public ManagerService() {
		super();
	}

	public Manager create() {
		Manager res;
		res = new Manager();

		final Collection<domain.Service> services = new ArrayList<domain.Service>();
		res.setServices(services);

		final UserAccount ua = this.userAccountService.create();
		res.setUserAccount(ua);

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.USER);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);

		return res;
	}
	public Manager save(final Manager manager) {
		Assert.notNull(manager);

		if (manager.getId() == 0) {
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String hash = encoder.encodePassword(manager.getUserAccount().getPassword(), null);
			manager.getUserAccount().setPassword(hash);
		}

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.USER);
		authorities.add(auth);
		manager.getUserAccount().setAuthorities(authorities);
		final UserAccount ua = this.userAccountService.save(manager.getUserAccount());
		manager.setUserAccount(ua);
		final Manager res = this.managerRepository.save(manager);
		return res;
	}
	public Manager findOne(final int idManager) {
		Assert.isTrue(idManager != 0);
		final Manager res = this.managerRepository.findOne(idManager);
		return res;
	}

	public Collection<Manager> findAll() {
		final Collection<Manager> res;
		res = this.managerRepository.findAll();
		return res;
	}

	// Other business methods -------------------------------------------------
	public Manager findByManagerAccountId(final int userAccountId) {
		Assert.isTrue(userAccountId != 0);
		Manager res;
		res = this.managerRepository.findManagerByUserAccountId(userAccountId);
		return res;
	}
	public Manager findByPrincipal() {
		final UserAccount u = LoginService.getPrincipal();
		final Manager res = this.managerRepository.findManagerByUserAccountId(u.getId());
		return res;
	}

	public Manager reconstruct(final ManagerForm managerForm, final BindingResult binding) {

		final Manager res = this.create();
		res.setName(managerForm.getName());
		res.setSurname(managerForm.getSurname());
		res.setBirthdate(managerForm.getBirthdate());
		res.setAddress(managerForm.getAddress());
		res.setPhone(managerForm.getPhone());
		res.setEmail(managerForm.getEmail());
		res.setVat(managerForm.getVat());
		res.getUserAccount().setUsername(managerForm.getUserAccount().getUsername());
		res.getUserAccount().setPassword(managerForm.getUserAccount().getPassword());

		this.validator.validate(res, binding);
		return res;
	}
}
