package com.projects.services.user.services;

import com.projects.services.user.models.Token;
import com.projects.services.user.models.User;
import com.projects.services.user.repos.TokenRepo;
import com.projects.services.user.repos.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;
    private final TokenRepo tokenRepo;

    public UserService(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder,
                       TokenRepo tokenRepo){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepo = tokenRepo;
    }
    public User signUp(String email, String name, String password) {
        //Add validations
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(passwordEncoder.encode(password));
        return userRepo.save(user);
    }

    public Token login(String email, String password) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("User with Email " + email + " not found.");
        }
        User user = optionalUser.get();
        if(!passwordEncoder.matches(password, user.getHashedPassword())){
            throw new UsernameNotFoundException("User Email & password are not matching");
        }else{
            Token token = generateToken(user);
            return tokenRepo.save(token);
        }
    }

    private Token generateToken(User user) {
        Token token = new Token();
        token.setValue(RandomStringUtils.randomAlphanumeric(10));
        token.setExpiryAt(System.currentTimeMillis() + 3600000);
        token.setUser(user);
        return token;
    }

    public User validateToken(String token) {
        /***
         * A token is valid if, it exists in db
         * Token not expired
         * not marked as deleted.
         */
        Optional<Token> validatedToken = tokenRepo.findByValueAndDeletedAndExpiryAtGreaterThan(token, false, System.currentTimeMillis());
        if(validatedToken.isEmpty()){
            return null;
        }
        return validatedToken.get().getUser();
    }
}
