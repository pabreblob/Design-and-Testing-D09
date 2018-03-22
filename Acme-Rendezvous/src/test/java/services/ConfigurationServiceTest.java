
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ConfigurationServiceTest extends AbstractTest {

	@Autowired
	private ConfigurationService	configurationService;


	/**
	 * Finding the configurations of the website
	 * <p>
	 * This tests check the method of the service Configuration which finds the configuration of the website
	 * 
	 */
	@Test
	public void testFind() {
		final Configuration config = this.configurationService.find();
		Assert.notNull(config);
	}

	/**
	 * Tests the finding of the configuration of the website
	 * <p>
	 * Case 1-5: All tests are ok
	 * <p>
	 * Case 6: The banner is in blank
	 * <p>
	 * Case 7: The welcome message in English is in blank
	 * <p>
	 * Case 8: The welcome message in Spanish is in blank
	 * <p>
	 * Case 9: The name of the business is in blank
	 * <p>
	 * Case 10: The currency is in blank
	 */
	@Test
	public void driverSaveConfiguration() {
		final Object testingData[][] = {
			{
				"http://www.placeholder.com", "Welcome", "Bienvenido", "Acme", "€", null
			}, {
				"http://www.website.com", "Hello", "Hola", "Acme", "$", null
			}, {
				"http://www.website2.com", "Text-Sample", "Texto", "Acme", "eur.", null
			}, {
				"http://www.website3.com", "Text-Sample2", "Texto2", "Acme", "£", null
			}, {
				"http://www.website4.com", "Text-Sample3", "Text3", "Acme", "¥", null
			}, {
				"", "Welcome", "Bienvenido", "Acme", "€", IllegalArgumentException.class
			}, {
				"http://www.placeholder.com", "", "Bienvenido", "Acme", "€", IllegalArgumentException.class
			}, {
				"http://www.placeholder.com", "Welcome", "", "Acme", "€", IllegalArgumentException.class
			}, {
				"http://www.placeholder.com", "Welcome", "Bienvenido", "", "€", IllegalArgumentException.class
			}, {
				"http://www.placeholder.com", "Welcome", "Bienvenido", "Acme", "", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * Template for testing saving a configuration
	 * <p>
	 * This method defines the template used for the tests that check the saving of configurations
	 * 
	 * @param bannerUrl
	 *            The banner of the website. Cannot be blank
	 * @param welcomeEng
	 *            The welcome message in English. Cannot be blank
	 * @param welcomeEsp
	 *            The welcome message in Spanish. Cannot be blank
	 * @param businessName
	 *            The name of the business. Cannot be blank
	 * @param currency
	 *            The currency we're using. Cannot be blank
	 * @param expected
	 *            The expected exception to be thrown. Use <code>null</code> if no exception is expected.
	 */
	protected void templateSave(final String bannerUrl, final String welcomeEng, final String welcomeEsp, final String businessName, final String currency, final Class<?> expected) {
		Class<?> caught = null;
		try {
			final Configuration configuration = new Configuration();
			configuration.setId(this.configurationService.find().getId());
			configuration.setVersion(this.configurationService.find().getVersion());
			configuration.setBannerUrl(bannerUrl);
			configuration.setWelcomeEng(welcomeEng);
			configuration.setWelcomeEsp(welcomeEsp);
			configuration.setBusinessName(businessName);
			configuration.setCurrency(currency);
			final Configuration saved = this.configurationService.save(configuration);
			Assert.notNull(saved);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
