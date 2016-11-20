//package data.services;
//
//import org.springframework.dao.DataAccessException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.social.security.SocialUser;
//import org.springframework.social.security.SocialUserDetails;
//import org.springframework.social.security.SocialUserDetailsService;
//import org.springframework.stereotype.Service;
//
//public class SimpleSocialUserDetailsService implements SocialUserDetailsService {
//	 private UserDetailsService userDetailsService;
//	 
//	 public SimpleSocialUserDetailsService(UserDetailsService userDetailsService) {
//	        this.userDetailsService = userDetailsService;
//	}
//
//	@Override
//	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
//		// TODO Auto-generated method stub
//		UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
//		return new SocialUser(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
//	}
//
//}
