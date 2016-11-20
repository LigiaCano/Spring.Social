package config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.social.UserIdSource;
//import org.springframework.social.security.AuthenticationNameUserIdSource;
//import org.springframework.social.security.SocialUserDetailsService;
//import org.springframework.social.security.SpringSocialConfigurer;
//
//import data.services.SimpleSocialUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
//    private CsrfTokenRepository csrfTokenRepository() 
//    { 
//        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository(); 
//        repository.setSessionAttributeName("_csrf");
//        return repository; 
//    }
    @Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
				.antMatchers("/resources/**");
	}
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.formLogin()
				.loginPage("/signin")
				.loginProcessingUrl("/signin/authenticate")
				.failureUrl("/signin?param.error=bad_credentials")
			.and()
				.logout()
					.logoutUrl("/signout")
					.deleteCookies("JSESSIONID")
			.and()
				.authorizeRequests()
					.antMatchers("/admin/**", "/favicon.ico", "/resources/**", "/auth/**", "/signin/**", "/signup/**", "/disconnect/facebook").permitAll()
					.antMatchers("/**").authenticated()
			.and()
				.rememberMe();
//		  .and()
//          	  .apply(new SpringSocialConfigurer()
//              .postLoginUrl("/")
//              .alwaysUsePostLoginUrl(true));
//			.and()
//	        .csrf().disable();
//			.and()
//				.csrf().csrfTokenRepository(csrfTokenRepository());
	}
   
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.noOpText();
	}
    
//    @Bean
//    public SocialUserDetailsService socialUsersDetailService() {
//        return new SimpleSocialUserDetailsService(userDetailsService());
//    }
//    
//    @Bean
//	public UserIdSource userIdSource() {
//		return new AuthenticationNameUserIdSource();
//	}

}
