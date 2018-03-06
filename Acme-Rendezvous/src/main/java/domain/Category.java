
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Category extends DomainEntity {

	private String					name;
	private String					description;

	private Collection<Service>		services;
	private Collection<Category>	children;
	private Category				parent;


	public Category() {
		super();
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@OneToMany(mappedBy = "category")
	public Collection<Service> getServices() {
		return this.services;
	}

	public void setServices(final Collection<Service> services) {
		this.services = services;
	}

	@NotNull
	@OneToMany(mappedBy = "parent")
	public Collection<Category> getChildren() {
		return this.children;
	}

	public void setChildren(final Collection<Category> children) {
		this.children = children;
	}

	@Valid
	@ManyToOne
	public Category getParent() {
		return this.parent;
	}

	public void setParent(final Category parent) {
		this.parent = parent;
	}

	//Propiedad derivada. Sirve para mostrar las categorías de forma más ordenada.
	public String getDisplayName() {
		String res;
		if (this.parent != null && this.parent.name != "CATEGORY")
			res = this.name + " (" + this.parent.name + ")";
		else
			res = this.name;
		return res;
	}

}
