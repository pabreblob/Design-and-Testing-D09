
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

import repositories.UserRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Rendezvous;
import domain.User;
import forms.UserForm;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository		userRepository;
	@Autowired
	private UserAccountService	userAccountService;

	//	@Bean
	//	public Validator validator() {
	//		return new LocalValidatorFactoryBean();
	//	}

	@Autowired
	private Validator			validator;


	public UserService() {
		super();
	}

	public User create() {
		User res;
		res = new User();

		final Collection<Rendezvous> createdRendezvous = new ArrayList<Rendezvous>();
		final Collection<Rendezvous> reservedRendezvous = new ArrayList<Rendezvous>();

		res.setCreatedRendezvous(createdRendezvous);
		res.setReservedRendezvous(reservedRendezvous);
		final UserAccount ua = this.userAccountService.create();
		res.setUserAccount(ua);

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.USER);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);

		return res;
	}
	public User save(final User user) {
		Assert.notNull(user);

		if (user.getId() == 0) {
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String hash = encoder.encodePassword(user.getUserAccount().getPassword(), null);
			user.getUserAccount().setPassword(hash);
		}

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.USER);
		authorities.add(auth);
		user.getUserAccount().setAuthorities(authorities);
		final UserAccount ua = this.userAccountService.save(user.getUserAccount());
		user.setUserAccount(ua);
		final User res = this.userRepository.save(user);
		return res;
	}
	public User findOne(final int idUser) {
		Assert.isTrue(idUser != 0);
		final User res = this.userRepository.findOne(idUser);
		return res;
	}

	public Collection<User> findAll() {
		final Collection<User> res;
		res = this.userRepository.findAll();
		return res;
	}

	// Other business methods -------------------------------------------------
	public User findByUserAccountId(final int userAccountId) {
		Assert.isTrue(userAccountId != 0);
		User res;
		res = this.userRepository.findUserByUserAccountId(userAccountId);
		return res;
	}
	public User findByPrincipal() {
		final UserAccount u = LoginService.getPrincipal();
		final User res = this.userRepository.findUserByUserAccountId(u.getId());
		return res;
	}

	public User reconstruct(final UserForm userForm, final BindingResult binding) {

		final User res = this.create();
		res.setName(userForm.getName());
		res.setSurname(userForm.getSurname());
		res.setBirthdate(userForm.getBirthdate());
		res.setAddress(userForm.getAddress());
		res.setPhone(userForm.getPhone());
		res.setEmail(userForm.getEmail());
		res.getUserAccount().setUsername(userForm.getUserAccount().getUsername());
		res.getUserAccount().setPassword(userForm.getUserAccount().getPassword());

		this.validator.validate(res, binding);
		return res;
	}
}
