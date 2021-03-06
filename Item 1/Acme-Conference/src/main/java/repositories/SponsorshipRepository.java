
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	@Query("select s from Sponsorship s where s.sponsor.id=?1")
	Collection<Sponsorship> findBySponsorId(int id);

	@Query("select s from Sponsorship s where s.conference.id=?1")
	Collection<Sponsorship> findByConference(int id);

}
