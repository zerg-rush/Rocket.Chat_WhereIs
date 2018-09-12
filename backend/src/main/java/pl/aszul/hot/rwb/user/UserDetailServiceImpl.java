/*package pl.aszul.hot.rwb.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

*//**
 * This class is used by spring controller to authenticate and authorize user
 **//*
@Service
public class UserDetailServiceImpl implements UserDetailsService  {
*//*	private final SUserRepository repository;

	@Autowired
	public UserDetailServiceImpl(SUserRepository repository) {
		this.repository = repository;
	}*//*

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
    	//SUser curruser = repository.findByUsername(username);

        UserDetails user = new org.springframework.security.core.userdetails.User(username, "$2a$08$bCCcGjB03eulCWt3CY0AZew2rVzXFyouUolL5dkL/pBgFkUH9O4J2", true,
        		true, true, true, AuthorityUtils.createAuthorityList("ADMIN"));
        *//*UserDetails user = new org.springframework.security.core.userdetails.User(username, curruser.getPasswordHash(), true,
                true, true, true, AuthorityUtils.createAuthorityList(curruser.getRole()));*//*

        //System.out.println("ROLE: " + curruser.getRole());
        return user;
    }

}*/
