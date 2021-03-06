
package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AuthorRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Author;
import domain.Paper;
import forms.RegisterAuthorForm;

@Service
@Transactional
public class AuthorService {

	// Managed Repository
	@Autowired
	private AuthorRepository		authorRepository;

	// Supporting Services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ConferenceService		conferenceService;

	@Autowired
	private SubmissionService		submissionService;


	// CRUD

	public Author create() {
		Author result;

		result = new Author();

		return result;
	}

	public Author save(final Author author) {
		Assert.notNull(author);
		Author result;

		if (!author.getPhone().equals(""))
			Assert.isTrue(author.getPhone().matches("\\d{4,99}"));

		result = this.authorRepository.save(author);
		this.authorRepository.flush();
		return result;
	}

	public void delete(final Author author) {
		Assert.notNull(author);
		Assert.isTrue(author.getId() != 0);
		Assert.isTrue(this.authorRepository.exists(author.getId()));

		this.authorRepository.delete(author);
	}

	public Collection<Author> findAll() {
		return this.authorRepository.findAll();
	}

	public Author findOne(final int authorId) {
		Author result;

		result = this.authorRepository.findOne(authorId);
		Assert.notNull(result);

		return result;
	}

	public void flush() {
		this.authorRepository.flush();
	}

	//Other business methods
	public void computeScores() {
		this.administratorService.findByPrincipal();

		final Collection<Author> authors = this.findAll();
		final Collection<String> buzzWords = this.conferenceService.findBuzzWords();

		double maxScore = 0;

		for (final Author author : authors) {
			final int authorId = author.getId();
			final Collection<Paper> cameraReadyPapers = this.submissionService.findAllCameraReadyPapersByAuthorId(authorId);

			double score = 0;

			for (final Paper paper : cameraReadyPapers) {
				String title = paper.getTitle().toLowerCase();
				String summary = paper.getSummary().toLowerCase();

				title = title.replaceAll("\\d", "").trim();
				summary = summary.replaceAll("\\d", "").trim();

				final List<String> splitedTitle = Arrays.asList(title.split("\\W+"));
				final List<String> splittedSummary = Arrays.asList(summary.split("\\W+"));

				for (final String buzzWord : buzzWords) {
					if (splitedTitle.contains(buzzWord))
						score++;
					if (splittedSummary.contains(buzzWord))
						score++;
				}
			}

			if (score > maxScore)
				maxScore = score;

			author.setScore(score);
		}

		for (final Author author : authors) {
			final double score = author.getScore();
			final double ratio = maxScore != 0 && score != 0 ? score / maxScore : 0;

			author.setScore(ratio);

			this.authorRepository.save(author);
		}

	}

	public Collection<Actor> findAllAsActor() {
		Collection<Actor> result;

		result = this.authorRepository.findAllAsActor();

		return result;
	}

	public Collection<Actor> findAllWithRegistration() {
		return this.authorRepository.findAllWithRegistration();
	}

	public Collection<Actor> findAllWithSubmission() {
		return this.authorRepository.findAllWithSubmission();
	}

	public Author findByPrincipal() {
		return this.actorService.findPrincipal();
	}

	public Author registerAuthor(final RegisterAuthorForm registerAuthorForm) {
		final Author result = this.create();

		final UserAccount userAccount = new UserAccount();
		final String password = registerAuthorForm.getPassword();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(password, null);
		Assert.isTrue(registerAuthorForm.getPassword().equals(registerAuthorForm.getPassword2()));
		userAccount.setPassword(hashedPassword);
		userAccount.setUsername(registerAuthorForm.getUsername());

		final Authority authority = new Authority();
		authority.setAuthority(Authority.AUTHOR);
		final List<Authority> authorities = new ArrayList<Authority>();
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		if (!registerAuthorForm.getPhone().equals(""))
			Assert.isTrue(registerAuthorForm.getPhone().matches("\\d{4,99}"));

		result.setUserAccount(userAccount);
		result.setAddress(registerAuthorForm.getAddress());
		result.setEmail(registerAuthorForm.getEmail());
		result.setName(registerAuthorForm.getName());
		result.setMiddleName(registerAuthorForm.getMiddleName());
		result.setPhone(registerAuthorForm.getPhone());
		result.setPhoto(registerAuthorForm.getPhoto());
		result.setSurname(registerAuthorForm.getSurname());
		this.save(result);

		return result;
	}


	// Reconstruct

	@Autowired
	private Validator	validator;


	public Author reconstruct(final Author author, final BindingResult binding) {
		Author result;

		if (author.getId() == 0)
			result = author;
		else {
			final Author retrieved = this.authorRepository.findOne(author.getId());
			result = author;

			result.setUserAccount(retrieved.getUserAccount());

		}

		this.validator.validate(result, binding);
		this.flush();
		return result;
	}

}
