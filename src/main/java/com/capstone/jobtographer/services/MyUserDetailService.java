package com.capstone.jobtographer.services;

import com.capstone.jobtographer.models.AppUser;
import com.capstone.jobtographer.models.UserWithRoles;
import com.capstone.jobtographer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


//Step one to security authentication

@Service
public class MyUserDetailService implements UserDetailsService {

//    SQL injection (Bringing in the Users dao)
    @Autowired
    private UserRepository usersDao;


//    Automated current logged in user
    @Override
    public UserDetails loadUserByUsername(String username) {
//        Find by username of logged in user
        AppUser user = usersDao.findByUsername(username);
//        session.setAttribute("user", user);
        System.out.println(user.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new UserWithRoles(user);
    }
}
