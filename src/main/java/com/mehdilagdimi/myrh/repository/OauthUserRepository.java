package com.mehdilagdimi.myrh.repository;

import com.mehdilagdimi.myrh.model.entity.OauthUser;
import com.mehdilagdimi.myrh.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface OauthUserRepository extends JpaRepository<OauthUser, Long> {

    Optional<OauthUser> findByOauthUserId(String oauthUserId);
}