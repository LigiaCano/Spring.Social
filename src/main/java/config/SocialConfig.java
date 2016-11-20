package config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ReconnectFilter;
import org.springframework.social.facebook.api.Account;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.facebook.web.DisconnectController;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
//import org.springframework.social.security.AuthenticationNameUserIdSource;
//import org.springframework.social.security.SocialAuthenticationFilter;
//import org.springframework.social.security.SocialAuthenticationServiceLocator;
//import org.springframework.social.security.SocialAuthenticationServiceRegistry;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import business.controller.PostToWallAfterConnectInterceptor;
import business.controller.TweetAfterConnectInterceptor;
import data.services.AccountConnectionSignUpService;
import data.services.SimpleSignInAdapter;

@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

	@Autowired
	DataSource dataSource;
	
	protected final FacebookConnectionFactory getFacebookConnectionFactory(Environment env) {
		FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(env.getProperty("facebook.appKey"),
					env.getProperty("facebook.appSecret"));
		facebookConnectionFactory.setScope(env.getProperty("facebook.scope"));
		return facebookConnectionFactory;
		 
	}
	
	protected final LinkedInConnectionFactory getLinkedInConnectionFactory(Environment env) {
		 return new LinkedInConnectionFactory(env.getProperty("linkedin.appKey"),
					env.getProperty("linkedin.appSecret"));
	}
	
	protected final TwitterConnectionFactory getTwitterConnectionFactory(Environment env) {
		 return new TwitterConnectionFactory(env.getProperty("twitter.appKey"),
					env.getProperty("twitter.appSecret"));
	}

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
		cfConfig.addConnectionFactory(getFacebookConnectionFactory(env));
		cfConfig.addConnectionFactory(getLinkedInConnectionFactory(env));
		cfConfig.addConnectionFactory(getTwitterConnectionFactory(env));
	}

	@Override
	public UserIdSource getUserIdSource() {
		return new UserIdSource() {				
			@Override
			public String getUserId() {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication == null) {
					throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
				}
				return authentication.getName();
			}
		};
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator, Encryptors.noOpText());
//		repository.setConnectionSignUp(new AccountConnectionSignUpService());
		return repository;
	}

	@Bean
	public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository usersConnectionRepository) {
		ProviderSignInController controller =  new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository,
		new SimpleSignInAdapter());
//		controller.setPostSignInUrl(env.getProperty("application.host"));
//		controller.setApplicationUrl(env.getProperty("application.host"));
//		controller.setSignUpUrl("/register");
		 return controller;
	}

	@Bean
	@Scope(value = "request",proxyMode = ScopedProxyMode.INTERFACES)
	public Facebook facebook(ConnectionRepository connectionRepository){
	    Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
	    return connection != null ? connection.getApi() : null;
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public Twitter twitter(ConnectionRepository repository) {
		Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
		return connection != null ? connection.getApi() : null;
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public LinkedIn linkedin(ConnectionRepository repository) {
		Connection<LinkedIn> connection = repository.findPrimaryConnection(LinkedIn.class);
		return connection != null ? connection.getApi() : null;
	}
	
	@Bean
	public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
		ConnectController controller = new ConnectController(connectionFactoryLocator, connectionRepository);
		controller.addInterceptor(new PostToWallAfterConnectInterceptor());
		controller.addInterceptor(new TweetAfterConnectInterceptor());
//        controller.setApplicationUrl(env.getProperty("application.host"));
        return controller;
	}
	
	@Bean
	public DisconnectController disconnectController(UsersConnectionRepository usersConnectionRepository, Environment env) {
		return new DisconnectController(usersConnectionRepository, env.getProperty("facebook.appSecret"));
	}
	
	@Bean
	public ReconnectFilter apiExceptionHandler(UsersConnectionRepository usersConnectionRepository, UserIdSource userIdSource) {
		return new ReconnectFilter(usersConnectionRepository, userIdSource);
	}
	

	
//	 @Bean
//	 @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
//	    public ConnectionRepository connectionRepository() {
//	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//	        if (authentication == null) {
//	            throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
//	        }
//
//	        Account account = (Account) authentication.getPrincipal();
//	        return usersConnectionRepository().createConnectionRepository(authentication.getName());
//	    }
	
//	@Bean
//	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
//	    HiddenHttpMethodFilter filter = new HiddenHttpMethodFilter();
//	    return filter;
//	}
	
//	@Bean
//	public AuthenticationNameUserIdSource authenticationNameUserIdSource(){
//	    return new  AuthenticationNameUserIdSource();
//	}
//	@Bean
//	  public SocialAuthenticationServiceLocator socialAuthenticationServiceLocator() {
//	    SocialAuthenticationServiceRegistry registry = new SocialAuthenticationServiceRegistry();
//	    registry.addConnectionFactory(new FacebookConnectionFactory(env.getProperty("facebook.appKey"),
//				env.getProperty("facebook.appSecret")));
//	    return registry;
//	}
	
//	 @Bean
//	 public SocialAuthenticationFilter socialAuthenticationFilter()
//	 throws Exception {
//	 SocialAuthenticationFilter filter = new SocialAuthenticationFilter(
//	 authenticationManager(), authenticationNameUserIdSource(),
//	 getUsersConnectionRepository(null), socialAuthenticationServiceLocator());
//	 filter.setFilterProcessesUrl("/login");
//	 filter.setSignupUrl("/signup");
//	 filter.setConnectionAddedRedirectUrl("/home");
//	 filter.setPostLoginUrl("/home"); // always open account profile
//	 // page after login
//	 // filter.setRememberMeServices(rememberMeServices());
//	 return filter;
//	 }
//
//	private AuthenticationManager authenticationManager() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
