package web.presenter.facebook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.facebook.api.PagePostData;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class FacebookPagesController {
	private static final Logger log = LoggerFactory.getLogger(FacebookPagesController.class);
	private final Facebook facebook;
	
	@Autowired
	public FacebookPagesController(Facebook facebook) {
		this.facebook = facebook;
	}
	
	@RequestMapping(value="/facebook/page", method=RequestMethod.GET)
	public String showPage(Model model) {
		PagedList<Page> page= facebook.pageOperations().search("Paddle");
		log.info("----> " + page.size());
		model.addAttribute("page", page);
		return "facebook/page";
	}
	
	@RequestMapping(value = "facebook/page", method = RequestMethod.POST)
    public String postPage(String pageId, String message) {
		PagePostData pagePostData = new PagePostData(pageId);
		pagePostData.message(message);
		if (facebook.pageOperations().isPageAdmin(pageId)){
			log.info("----> Administrador" );
			facebook.pageOperations().post(pagePostData);
		}
		return "redirect:/facebook/page";

    }

}
