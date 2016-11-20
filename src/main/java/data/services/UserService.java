package data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import data.daos.AuthorizationDao;
import data.daos.UserDao;
import data.entities.Authorization;
import data.entities.Role;
import data.entities.User;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserDao repository;
	
	@Autowired
	private AuthorizationDao authorizationDao;
	
	public Boolean create(User user) {
		User existingUser = repository.findByUsernameOrEmail(user.getEmail());
		if (existingUser != null) 
			return false;
		
		User saved = repository.save(user);
		authorizationDao.save(new Authorization(user, Role.PLAYER));
		if (saved == null) 
			return false;
		
		return true;
	}

}
