package com.revature.eeecommerce.util.auth;

import com.revature.eeecommerce.User.User;
import com.revature.eeecommerce.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class AuthService {
    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User login(String email, String password) throws AuthenticationException {
        return userService.findByEmailAndPassword(email, password);
    }
}
