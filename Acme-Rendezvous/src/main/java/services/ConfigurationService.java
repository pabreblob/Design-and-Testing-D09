
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ConfigurationRepository;
import domain.Configuration;

@Service
@Transactional
public class ConfigurationService {

	@Autowired
	private ConfigurationRepository	configurationRepository;


	public ConfigurationService() {
		super();
	}

	public Configuration save(final Configuration configuration) {
		Assert.notNull(configuration);
		Assert.isTrue(!configuration.getBannerUrl().equals(""));
		Assert.notNull(configuration.getBannerUrl());
		Assert.isTrue(!configuration.getCurrency().equals(""));
		Assert.notNull(configuration.getCurrency());
		Assert.isTrue(!configuration.getBusinessName().equals(""));
		Assert.notNull(configuration.getBusinessName());
		Assert.isTrue(!configuration.getWelcomeEng().equals(""));
		Assert.notNull(configuration.getWelcomeEng());
		Assert.isTrue(!configuration.getWelcomeEsp().equals(""));
		Assert.notNull(configuration.getWelcomeEsp());

		return this.configurationRepository.save(configuration);
	}

	public Configuration find() {
		return this.configurationRepository.findAll().get(0);
	}
}
