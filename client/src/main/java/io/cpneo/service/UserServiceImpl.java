package io.cpneo.service;

import io.cpneo.client.User;
import io.cpneo.client.UserCredentials;
import io.cpneo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("UserService")
public class UserServiceImpl implements UserService{


    @Autowired
    private UserRepository userDao;

    @Override
    public boolean register(UserCredentials credentials) {
        if(userDao.existsByLogin(credentials.getLogin())) return false;

        userDao.save(new User(credentials.getEmail(),credentials.getLogin(),credentials.getPassword()));
        return true;
    }

    @Override
    public String login(UserCredentials credentials) {

        User user = userDao.findByLogin(credentials.getLogin());

        if(user != null && user.getPassword().equals(credentials.getPassword())) {
            String token = DigestUtils.sha512Hex(UUID.randomUUID().toString());
            user.setToken(token);
            userDao.save(user);
            return token;
        }

        return "";
    }

    @Override
    public boolean loggedIn(String token){
        return userDao.existsByToken(token);
    }
}
