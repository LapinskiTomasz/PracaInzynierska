package io.cpneo.repository;


import io.cpneo.client.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    public User findByToken(String token);

    public boolean existsByLogin(String login);

    public User findByLogin(String login);

    public boolean existsByToken(String token);

}
