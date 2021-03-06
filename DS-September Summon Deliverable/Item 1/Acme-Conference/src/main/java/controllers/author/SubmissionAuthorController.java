
package controllers.author;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AuthorService;
import services.ConferenceService;
import services.SubmissionService;
import controllers.AbstractController;
import domain.Author;
import domain.Conference;
import domain.Submission;
import forms.SubmissionCameraReadyPaperForm;
import forms.SubmissionPaperForm;

@Controller
@RequestMapping("/submission/author")
public class SubmissionAuthorController extends AbstractController {

	@Autowired
	private SubmissionService	submissionService;

	@Autowired
	private AuthorService		authorService;

	@Autowired
	private ConferenceService	conferenceService;


	// Listing --------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Submission> submissions;

		final Author principal = this.authorService.findByPrincipal();

		submissions = this.submissionService.findByAuthor(principal);
		final Date today = new Date();

		result = new ModelAndView("submission/list");

		result.addObject("requestURI", "submission/author/list.do");
		result.addObject("submissions", submissions);
		result.addObject("today", today);

		return result;
	}

	// Creating --------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final int conferenceId) {
		ModelAndView result;
		Conference conference;
		final SubmissionPaperForm submissionPaperForm;

		conference = this.conferenceService.findOne(conferenceId);

		submissionPaperForm = new SubmissionPaperForm();
		submissionPaperForm.setConference(conference);

		result = new ModelAndView("submission/create");
		result.addObject("submissionForm", submissionPaperForm);
		result.addObject("action", "edit.do");
		result.addObject("cameraReady", false);
		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("submissionForm") @Valid final SubmissionPaperForm submissionForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createMakeSubmissionModelAndView(submissionForm);
		else
			try {
				this.submissionService.makeSubmission(submissionForm);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createMakeSubmissionModelAndView(submissionForm, "submission.commit.error");
			}
		return result;
	}

	// Go to upload --------------------------------------------------------

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int submissionId) {
		ModelAndView result;
		Submission submission;
		SubmissionCameraReadyPaperForm submissionCameraReadyPaperForm;

		submission = this.submissionService.findOne(submissionId);

		submissionCameraReadyPaperForm = new SubmissionCameraReadyPaperForm();
		submissionCameraReadyPaperForm.setSubmission(submission);

		result = this.createUploadCameraReadyPaperModelAndView(submissionCameraReadyPaperForm);
		return result;
	}

	// Upload --------------------------------------------------------

	@RequestMapping(value = "/upload", method = RequestMethod.POST, params = "save")
	public ModelAndView upload(@ModelAttribute("submissionForm") @Valid final SubmissionCameraReadyPaperForm submissionCameraReadyPaperForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createUploadCameraReadyPaperModelAndView(submissionCameraReadyPaperForm);
		else
			try {
				this.submissionService.uploadCameraReadyPaper(submissionCameraReadyPaperForm);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createUploadCameraReadyPaperModelAndView(submissionCameraReadyPaperForm, "submission.commit.error");
			}
		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createMakeSubmissionModelAndView(final SubmissionPaperForm submissionPaperForm) {
		return this.createMakeSubmissionModelAndView(submissionPaperForm, null);
	}

	protected ModelAndView createMakeSubmissionModelAndView(final SubmissionPaperForm submissionPaperForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("submission/create");
		result.addObject("submissionForm", submissionPaperForm);
		result.addObject("action", "edit.do");
		result.addObject("cameraReady", false);
		result.addObject("message", message);
		return result;
	}

	protected ModelAndView createUploadCameraReadyPaperModelAndView(final SubmissionCameraReadyPaperForm submissionCameraReadyPaperForm) {
		return this.createUploadCameraReadyPaperModelAndView(submissionCameraReadyPaperForm, null);
	}

	protected ModelAndView createUploadCameraReadyPaperModelAndView(final SubmissionCameraReadyPaperForm submissionCameraReadyPaperForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("submission/upload");
		result.addObject("submissionForm", submissionCameraReadyPaperForm);
		result.addObject("action", "upload.do");
		result.addObject("cameraReady", true);
		result.addObject("message", message);
		return result;
	}

}
