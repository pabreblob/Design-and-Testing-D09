
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private Collection<Rendezvous>	createdRendezvous;
	private Collection<Rendezvous>	reservedRendezvous;


	public User() {
		super();
	}

	@NotNull
	@OneToMany(mappedBy = "creator")
	public Collection<Rendezvous> getCreatedRendezvous() {
		return this.createdRendezvous;
	}

	public void setCreatedRendezvous(final Collection<Rendezvous> createdRendezvous) {
		this.createdRendezvous = createdRendezvous;
	}

	@NotNull
	@ManyToMany(mappedBy = "attendants")
	public Collection<Rendezvous> getReservedRendezvous() {
		return this.reservedRendezvous;
	}

	public void setReservedRendezvous(final Collection<Rendezvous> reservedRendezvous) {
		this.reservedRendezvous = reservedRendezvous;
	}

}
