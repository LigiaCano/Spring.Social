package web.presenter.linkedin;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import web.presenter.facebook.FacebookProfileController;

@Controller
public class LinkedInProfileController {
	
	@Autowired
	private ConnectionRepository connectionRepository;
	
	private static final Logger log = LoggerFactory.getLogger(FacebookProfileController.class);
	
	@RequestMapping(value="/linkedin", method=RequestMethod.GET)
	public String profile(Principal currentUser, Model model) {
		Connection<LinkedIn> connection = connectionRepository.findPrimaryConnection(LinkedIn.class);
		if (connection == null) {
			log.info("Connect LinkedIn");
			return "redirect:/connect/linkedin";
		}
		model.addAttribute("profile", connection.getApi().profileOperations().getUserProfileFull());
		return "linkedin/profile";
	}

}
