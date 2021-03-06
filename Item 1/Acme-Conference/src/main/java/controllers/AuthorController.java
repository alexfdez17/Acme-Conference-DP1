
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AuthorService;
import services.SystemConfigurationService;
import domain.Author;
import forms.RegisterAuthorForm;

@Controller
@RequestMapping("/author")
public class AuthorController extends AbstractController {

	@Autowired
	private AuthorService				authorService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Display --------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result;
		Author actor;

		actor = this.authorService.findByPrincipal();

		String newPhone = this.systemConfigurationService.findCountryCode();
		newPhone += " " + actor.getPhone();
		actor.setPhone(newPhone);

		result = new ModelAndView("actor/display");
		result.addObject("actor", actor);

		return result;
	}

	// Go to registration --------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView createAuthor() {
		RegisterAuthorForm registerAuthorForm;
		final ModelAndView result;

		registerAuthorForm = new RegisterAuthorForm();
		result = new ModelAndView("actor/registerAuthor");

		result.addObject("registerForm", registerAuthorForm);
		result.addObject("role", "author");

		return result;
	}

	// Save register --------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("registerForm") @Valid final RegisterAuthorForm registerForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createRegisterModelAndView(registerForm);
		else
			try {
				this.authorService.registerAuthor(registerForm);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				result = this.createRegisterModelAndView(registerForm, "actor.commit.error");
			}
		return result;
	}

	// Edit profile --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		Author author;
		final ModelAndView result;

		author = this.authorService.findByPrincipal();
		result = new ModelAndView("actor/edit");

		result.addObject("actor", author);
		result.addObject("role", "author");

		return result;
	}

	// Save edit profile --------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEdit(@ModelAttribute("actor") Author actor, final BindingResult binding) {
		ModelAndView result;
		//SystemConfig systemConfig;

		actor = this.authorService.reconstruct(actor, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(actor);
		else
			try {
				this.authorService.save(actor);
				result = new ModelAndView("redirect:display.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(actor, "actor.commit.error");
			}
		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createRegisterModelAndView(final RegisterAuthorForm registerAuthorForm) {
		return this.createRegisterModelAndView(registerAuthorForm, null);
	}

	protected ModelAndView createRegisterModelAndView(final RegisterAuthorForm registerAuthorForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/registerAuthor");
		result.addObject("registerForm", registerAuthorForm);
		result.addObject("message", messageCode);
		result.addObject("role", "author");

		return result;
	}

	protected ModelAndView createEditModelAndView(final Author author) {
		return this.createEditModelAndView(author, null);
	}

	protected ModelAndView createEditModelAndView(final Author author, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("actor", author);
		result.addObject("message", messageCode);
		result.addObject("role", "author");

		return result;
	}

}
