
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.SponsorService;
import forms.RegisterSponsorForm;

@Controller
@RequestMapping("/sponsor")
public class SponsorController extends AbstractController {

	@Autowired
	private SponsorService	sponsorService;


	// Go to registration --------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView createSponsor() {
		RegisterSponsorForm registerSponsorForm;
		final ModelAndView result;

		registerSponsorForm = new RegisterSponsorForm();
		result = new ModelAndView("actor/registerSponsor");

		result.addObject("registerSponsorForm", registerSponsorForm);

		return result;
	}

	// Save --------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RegisterSponsorForm registerSponsorForm, final BindingResult binding) {
		ModelAndView result;
		//SystemConfig systemConfig;

		if (binding.hasErrors())
			result = this.createEditModelAndView(registerSponsorForm);
		else
			try {
				if (registerSponsorForm.getPhone().matches("\\d{4,99}")) {
					/*
					 * systemConfig = this.systemConfigService.findSystemConfiguration();
					 * String newPhone = systemConfig.getPhonePrefix();
					 * newPhone += " " + registerSponsorForm.getPhone();
					 * registerSponsorForm.setPhone(newPhone);
					 */
				}
				this.sponsorService.registerSponsor(registerSponsorForm);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(registerSponsorForm, "actor.commit.error");
			}
		return result;
	}

	// Ancillary methods --------------------------------------------------------

	protected ModelAndView createEditModelAndView(final RegisterSponsorForm registerSponsorForm) {
		return this.createEditModelAndView(registerSponsorForm, null);
	}

	protected ModelAndView createEditModelAndView(final RegisterSponsorForm registerSponsorForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/registerSponsor");
		result.addObject("registerSponsorForm", registerSponsorForm);
		result.addObject("message", messageCode);

		return result;
	}

}