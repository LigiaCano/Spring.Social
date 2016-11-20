package data.services;

import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Component;

import data.entities.User;
@Component
public class AccountConnectionSignUpService implements ConnectionSignUp {
	private static final Logger log = LoggerFactory.getLogger(AccountConnectionSignUpService.class);
	@Autowired
	private UserService userService;
	
	@Override
	public String execute(Connection<?> connection) {
		UserProfile profile = connection.fetchUserProfile();
		log.info("SignUp " + profile.getEmail());
		User player = new User(profile.getUsername(), profile.getEmail(), "***",new GregorianCalendar(1979, 07, 22));
		log.info("Usuario regristado " + userService.create(player));
		return player.getUsername();
	}
	

}
