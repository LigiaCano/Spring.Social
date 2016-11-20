package web.presenter.facebook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FacebookProfileController {
	@Autowired
	private ConnectionRepository connectionRepository;
	
	private static final Logger log = LoggerFactory.getLogger(FacebookProfileController.class);
	
	@RequestMapping(value="/facebook", method=RequestMethod.GET) 	
	public String profile(Model model) {
		Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
		log.info("----> " + connection);
		
		if (connection == null) {
			return "redirect:/connect/facebook";
		}
		model.addAttribute("profile", connection.getApi().userOperations().getUserProfile());
		return "facebook/profile";
	}

}
