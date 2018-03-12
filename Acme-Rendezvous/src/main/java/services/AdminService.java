
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdminRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Administrator;
import domain.Manager;
import domain.Rendezvous;

@Service
@Transactional
public class AdminService {

	@Autowired
	private AdminRepository		adminRepository;
	@Autowired
	private UserAccountService	userAccountService;
	@Autowired
	private RendezvousService	rendezvousService;


	public AdminService() {
		super();
	}

	public Administrator create() {
		Administrator res;
		res = new Administrator();
		final UserAccount ua = this.userAccountService.create();
		res.setUserAccount(ua);

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);

		return res;
	}
	public Administrator save(final Administrator admin) {
		Assert.notNull(admin);

		if (admin.getId() == 0) {
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String hash = encoder.encodePassword(admin.getUserAccount().getPassword(), null);
			admin.getUserAccount().setPassword(hash);
		}

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		authorities.add(auth);
		admin.getUserAccount().setAuthorities(authorities);
		final UserAccount ua = this.userAccountService.save(admin.getUserAccount());
		admin.setUserAccount(ua);
		final Administrator res = this.adminRepository.save(admin);
		return res;
	}
	public Administrator findOne(final int idAdmin) {
		Assert.isTrue(idAdmin != 0);
		final Administrator res = this.adminRepository.findOne(idAdmin);
		return res;
	}

	public Collection<Administrator> findAll() {
		final Collection<Administrator> res;
		res = this.adminRepository.findAll();
		return res;
	}

	// Other business methods -------------------------------------------------
	public Administrator findByUserAccountId(final int adminAccountId) {
		Assert.isTrue(adminAccountId != 0);
		Administrator res;
		res = this.adminRepository.findAdminByUserAccountId(adminAccountId);
		return res;
	}
	public Administrator findByPrincipal() {
		final UserAccount u = LoginService.getPrincipal();
		final Administrator res = this.adminRepository.findAdminByUserAccountId(u.getId());
		return res;
	}
	public Double getAverageRendezvousCreatedPerUser() {
		Double res;
		res = this.adminRepository.averageRendezvousCreatedPerUser();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getStandartDeviationRendezvousCreatedPerUser() {
		Double res;
		res = this.adminRepository.standartDeviationRendezvousCreatedPerUser();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getRatioUsersWithCreatedRendezvous() {
		Double res;
		res = this.adminRepository.ratioUsersWithCreatedRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getAverageAttendantsPerRendezvous() {
		Double res;
		res = this.adminRepository.averageAttendantsPerRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getStandartDeviationAttendantsPerRendezvous() {
		Double res;
		res = this.adminRepository.standartDeviationAttendantsPerRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getAverageRendezvousReservedPerUser() {
		Double res;
		res = this.adminRepository.averageRendezvousReservedPerUser();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getStandartRendezvousReservedPerUser() {
		Double res;
		res = this.adminRepository.standartDeviationRendezvousReservedPerUser();
		if (res == null)
			res = 0.0;
		return res;
	}
	public List<Rendezvous> getMostReservedRendezvous() {
		final List<Rendezvous> rendezvous = this.adminRepository.mostReservedRendezvous();
		final List<Rendezvous> res;
		if (rendezvous.isEmpty())
			res = new ArrayList<Rendezvous>();
		else if (rendezvous.size() < 10)
			res = rendezvous.subList(0, rendezvous.size() - 1);
		else
			res = rendezvous.subList(0, 9);
		return res;
	}
	public Double getAverageAnnouncementsPerRendezvous() {
		Double res;
		res = this.adminRepository.averageAnnouncementsPerRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getStandartDeviationAnnouncementsPerRendezvous() {
		Double res;
		res = this.adminRepository.standartDeviationAnnouncementsPerRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public List<Rendezvous> getRendezvousesAnnouncementsOver75() {
		final List<Rendezvous> rendezvous = this.adminRepository.rendezvousesAnnouncementsOver75();
		final List<Rendezvous> res;
		if (rendezvous.isEmpty())
			res = new ArrayList<Rendezvous>();
		else
			res = rendezvous;
		return res;
	}
	public List<Rendezvous> getRendezvousesLinkedPlus10() {
		final List<Rendezvous> rendezvous = this.adminRepository.rendezvousesLinkedPlus10();
		final List<Rendezvous> res;
		if (rendezvous.isEmpty())
			res = new ArrayList<Rendezvous>();
		else
			res = rendezvous;
		return res;
	}
	public Double getAverageQuestionsPerRendezvous() {
		Double res;
		res = this.adminRepository.averageQuestionsPerRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getStandartDeviationQuestionsPerRendezvous() {
		Double res;
		res = this.adminRepository.standartDeviationQuestionsPerRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getAverageAnswersPerRendezvous() {
		Double res;
		res = this.adminRepository.averageAnswersPerRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getStandartDeviationAnswersPerRendezvous() {
		Double res;
		res = this.adminRepository.standartDeviationAnswersPerRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getAverageRepliesPerComment() {
		Double res;
		res = this.adminRepository.averageRepliesPerComment();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getStandartDeviationRepliesPerComment() {
		Double res;
		res = this.adminRepository.standartDeviationRepliesPerComment();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getAverageRequestsPerRendezvous() {
		Double res;
		res = this.adminRepository.averageRequestsPerRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getStandartRequestsPerRendezvous() {
		Double res;
		res = this.adminRepository.standartDeviationRequestsPerRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getMinRequestsPerRendezvous() {
		Double res;
		res = this.adminRepository.averageRequestsPerRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getMaxRequestsPerRendezvous() {
		Double res;
		res = this.adminRepository.standartDeviationRequestsPerRendezvous();
		if (res == null)
			res = 0.0;
		return res;
	}
	public Double getAverageServicePerCategory() {
		Double res;
		res = this.adminRepository.averageServicePerCategory();
		if (res == null)
			res = 0.0;
		return res;
	}
	public List<domain.Service> getBestSellingServices() {
		final List<domain.Service> services = this.adminRepository.bestSellingServices();
		List<domain.Service> res = new ArrayList<domain.Service>();
		if (services.isEmpty())
			res = new ArrayList<domain.Service>();
		else
			res.add(services.get(0));
		for (final domain.Service s : services)
			if (s.getRequests().size() == services.get(0).getRequests().size() && s != services.get(0))
				res.add(s);
		//		while (i < services.size() - 1)
		//			if (services.get(i).getRequests().size() == services.get(0).getRequests().size())
		//				res.add(services.get(i));
		//		i++;
		return res;
	}
	public List<domain.Service> getTopSellingServices() {
		final List<domain.Service> services = this.adminRepository.bestSellingServices();
		final List<domain.Service> res;
		if (services.isEmpty())
			res = new ArrayList<domain.Service>();
		else if (services.size() < 10)
			res = services.subList(0, services.size() - 1);
		else
			res = services.subList(0, 9);
		return res;
	}
	public List<Manager> getManagersMoreThanAvgService() {
		final List<Manager> managers = this.adminRepository.managersMoreThanAvgService();
		final List<Manager> res;
		if (managers.isEmpty())
			res = new ArrayList<Manager>();
		else
			res = managers;
		return res;
	}
	public Double avgCategoriesPerRendezvous() {
		final Double res = 0.0;

		return res;
	}
	public Double getAverageCategoryPerRendezvous() {
		Double sum = 0.0;
		Double res = 0.0;
		final int size = this.rendezvousService.findAll().size();
		final List<Long> categoriesNum = this.adminRepository.averageCategoriesPerRendezvous();

		for (final Long i : categoriesNum)
			sum += i;
		if (this.findAll().size() != 0)
			res = sum / size;

		return res;
	}
}
