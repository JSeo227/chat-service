package com.example.chat.chat_service.repository.jpa;

import com.example.chat.chat_service.domain.Login;
import com.example.chat.chat_service.repository.LoginRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaLoginRepository implements LoginRepository {

    private final EntityManager em;

    @Override
    public Boolean findByLoginId(String loginId) {
        Login login = em.find(Login.class, loginId);
        log.info(String.valueOf(login));
        return login.checkLoginId(loginId);
    }

    @Override
    public Boolean findByPassword(String password) {
        Login login = em.createQuery("select l from Login l where l.password = :password", Login.class)
                .setParameter("password", password)
                .getSingleResult();
        log.info(String.valueOf(login));
        return login.checkPassword(password);
    }
}
