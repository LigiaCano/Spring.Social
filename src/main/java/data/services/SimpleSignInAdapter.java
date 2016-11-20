package data.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;


@Component
public class SimpleSignInAdapter implements SignInAdapter {
	
	private static final Logger log = LoggerFactory.getLogger(SimpleSignInAdapter.class);
	
	@Override
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		log.info("SingIn "  + userId);
		SignInUtils.signin(userId);
		return null;
	}
}
