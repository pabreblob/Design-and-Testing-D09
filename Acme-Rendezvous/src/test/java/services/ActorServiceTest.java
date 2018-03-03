
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	@Autowired
	private ActorService	actorService;


	@Test
	public void testFindByPrincipalActor() {
		super.authenticate("admin");
		final Actor res = this.actorService.findByPrincipal();
		Assert.notNull(res);
		super.authenticate(null);
	}

	@Test
	public void testFindOneActor() {
		super.authenticate("admin");
		final Actor result = this.actorService.findOne(this.actorService.findByPrincipal().getId());
		Assert.notNull(result);
		super.authenticate(null);
	}

	@Test
	public void testFindByUserAccountActor() {
		super.authenticate("admin");
		final Actor found = this.actorService.findActorByUserAccountId(this.actorService.findByPrincipal().getUserAccount().getId());
		Assert.isTrue(found != null);
		super.authenticate(null);
	}

	@Test
	public void testFindAllActors() {
		super.authenticate("admin");
		final Collection<Actor> res = this.actorService.findAll();
		Assert.notNull(res);
		super.authenticate(null);
	}

	@Test
	public void testSave() {
		super.authenticate("admin");
		final Actor actor = this.actorService.findByPrincipal();
		actor.setName("Manolito");
		final Actor saved = this.actorService.save(actor);
		Assert.isTrue(saved.getName().equals("Manolito"));
		super.authenticate(null);
	}
}
