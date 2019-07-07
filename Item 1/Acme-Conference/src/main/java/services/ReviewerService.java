
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ReviewerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Reviewer;
import forms.RegisterReviewerForm;

@Service
@Transactional
public class ReviewerService {

	//Managed Repository
	@Autowired
	private ReviewerRepository	reviewerRepository;


	// Supported Services

	// CRUD

	public Reviewer create() {
		Reviewer result;

		result = new Reviewer();

		return result;
	}

	public Reviewer save(final Reviewer reviewer) {
		Assert.notNull(reviewer);
		Reviewer result;
		result = this.reviewerRepository.save(reviewer);
		this.reviewerRepository.flush();
		return result;
	}

	public void delete(final Reviewer reviewer) {
		Assert.notNull(reviewer);
		Assert.isTrue(reviewer.getId() != 0);
		Assert.isTrue(this.reviewerRepository.exists(reviewer.getId()));

		this.reviewerRepository.delete(reviewer);
	}

	public Collection<Reviewer> findAll() {
		Collection<Reviewer> result;

		result = this.reviewerRepository.findAll();

		return result;
	}

	public Reviewer findOne(final int reviewerId) {
		Reviewer result;

		result = this.reviewerRepository.findOne(reviewerId);
		Assert.notNull(result);

		return result;
	}

	public void flush() {
		this.reviewerRepository.flush();
	}

	//Other business methods

	public Reviewer findByPrincipal() {
		Reviewer result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Reviewer findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Reviewer result;

		result = this.reviewerRepository.findbyUserAccountID(userAccount.getId());

		return result;
	}

	public Reviewer registerReviewer(final RegisterReviewerForm registerReviewerForm) {
		final Reviewer result = this.create();

		final UserAccount userAccount = new UserAccount();
		final String password = registerReviewerForm.getPassword();
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(password, null);
		Assert.isTrue(registerReviewerForm.getPassword().equals(registerReviewerForm.getPassword2()));
		userAccount.setPassword(hashedPassword);
		userAccount.setUsername(registerReviewerForm.getUsername());

		final Authority authority = new Authority();
		authority.setAuthority(Authority.REVIEWER);
		final List<Authority> authorities = new ArrayList<Authority>();
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		result.setUserAccount(userAccount);
		result.setAddress(registerReviewerForm.getAddress());
		result.setEmail(registerReviewerForm.getEmail());
		result.setName(registerReviewerForm.getName());
		result.setMiddleName(registerReviewerForm.getMiddleName());
		result.setPhone(registerReviewerForm.getPhone());
		result.setPhoto(registerReviewerForm.getPhoto());
		result.setSurname(registerReviewerForm.getSurname());
		result.setKeywords(registerReviewerForm.getKeywords());
		this.save(result);

		return result;
	}

}