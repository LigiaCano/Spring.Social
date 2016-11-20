package web.presenter.facebook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FacebookFeedController {
	private static final Logger log = LoggerFactory.getLogger(FacebookFeedController.class);
	private final Facebook facebook;

	@Autowired
	public FacebookFeedController(Facebook facebook) {
		this.facebook = facebook;
	}

	@RequestMapping(value="/facebook/feed", method=RequestMethod.GET)
	public String showFeed(Model model) {
		PagedList<Post> feed = facebook.feedOperations().getFeed();
		
		log.info("----> " + feed.size());
		
		model.addAttribute("feed", feed);
		return "facebook/feed";
	}
	
	@RequestMapping(value="/facebook/feed", method=RequestMethod.POST)
	public String postUpdate(String message) {
		log.info("----> " + message);
		facebook.feedOperations().updateStatus(message);
		
		return "redirect:/facebook/feed";
	}
}
