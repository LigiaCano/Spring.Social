package web.presenter;

import java.util.GregorianCalendar;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import data.daos.AuthorizationDao;
import data.daos.UserDao;
import data.entities.Authorization;
import data.entities.Role;
import data.entities.SignupForm;
import data.entities.User;
import data.services.SignInUtils;
import data.services.UserService;

@Controller
public class SignupController {
	private final UserService userService;;

	private final ProviderSignInUtils providerSignInUtils;
	@Autowired
	public SignupController(UserService userService, 
            ConnectionFactoryLocator connectionFactoryLocator,
            UsersConnectionRepository connectionRepository) {
		this.userService = userService;
this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
}
	
	@RequestMapping(value="/signup", method=RequestMethod.GET)
	public SignupForm signupForm(WebRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		if (connection != null) {
//			request.setAttribute("message", new Message(MessageType.INFO, "Your " + StringUtils.capitalize(connection.getKey().getProviderId()) + " account is not associated with a Spring Social Showcase account. If you're new, please sign up."), WebRequest.SCOPE_REQUEST);
			return SignupForm.fromProviderUser(connection.fetchUserProfile());
		} else {
			return new SignupForm();
		}
	}
	
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request) {
		if (formBinding.hasErrors()) {
			return null;
		}
		User account = createAccount(form, formBinding);
		if (account != null) {
			SignInUtils.signin(account.getUsername());
			providerSignInUtils.doPostSignUp(account.getUsername(), request);
			return "redirect:/";
		}
		return null;
	}
	
	private User createAccount(SignupForm form, BindingResult formBinding) {
			User user = new User(form.getUsername(),form.getEmail(), form.getPassword(), new GregorianCalendar(1979, 07, 22));
			userService.create(user);
			return user;
	}

}
