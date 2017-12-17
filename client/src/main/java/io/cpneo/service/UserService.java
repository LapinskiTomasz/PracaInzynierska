package io.cpneo.service;

import io.cpneo.client.UserCredentials;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public boolean register(UserCredentials credentials);

    public String login(UserCredentials credentials);

    public boolean loggedIn(String token);

}
