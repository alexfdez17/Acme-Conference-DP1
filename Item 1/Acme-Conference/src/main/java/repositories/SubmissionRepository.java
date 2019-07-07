
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Submission;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

	@Query("select min(1.0*(select count(s) from Submission s where s.conference.id = c.id)), max(1.0*(select count(s) from Submission s where s.conference.id = c.id)), avg(1.0*(select count(s) from Submission s where s.conference.id = c.id)), stddev(1.0*(select count(s) from Submission s where s.conference.id = c.id)) from Conference c")
	List<Double> minMaxAvgStddevPerConference();

}
