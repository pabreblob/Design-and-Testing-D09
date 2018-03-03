
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	private String	bannerUrl;
	private String	welcomeEng;
	private String	welcomeEsp;
	private String	businessName;


	public Configuration() {
		super();
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@URL
	public String getBannerUrl() {
		return this.bannerUrl;
	}

	public void setBannerUrl(final String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeEng() {
		return this.welcomeEng;
	}

	public void setWelcomeEng(final String welcomeEng) {
		this.welcomeEng = welcomeEng;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeEsp() {
		return this.welcomeEsp;
	}

	public void setWelcomeEsp(final String welcomeEsp) {
		this.welcomeEsp = welcomeEsp;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBusinessName() {
		return this.businessName;
	}

	public void setBusinessName(final String businessName) {
		this.businessName = businessName;
	}

}
