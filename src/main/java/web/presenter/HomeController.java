package web.presenter;

import java.security.Principal;
import java.util.List;

import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Reference;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import data.daos.UserDao;

@Controller
public class HomeController {
	private final Provider<ConnectionRepository> connectionRepositoryProvider;
	
	private final UserDao accountRepository;
	
	@Autowired
	public HomeController(Provider<ConnectionRepository> connectionRepositoryProvider, UserDao accountRepository) {
		this.connectionRepositoryProvider = connectionRepositoryProvider;
		this.accountRepository = accountRepository;
	}
	

	@RequestMapping(value ="/home", method = RequestMethod.GET)
	public String home(Principal currentUser, Model model) {
		if(accountRepository.findByUsernameOrEmail(currentUser.getName()) == null)
			return "redirect:/signup";
		model.addAttribute("connectionsToProviders", getConnectionRepository().findAllConnections());
		model.addAttribute(accountRepository.findByUsernameOrEmail(currentUser.getName()));
		return "home";
	}
	
	private ConnectionRepository getConnectionRepository() {
		return connectionRepositoryProvider.get();
	}
    
}
