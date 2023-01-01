package com.mehdilagdimi.myrh.repository;

import com.mehdilagdimi.myrh.model.entity.OfferImage;
import com.mehdilagdimi.myrh.model.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Optional<ProfileImage> findByName(String name);
    Optional<ProfileImage> findById(Long id);
}
