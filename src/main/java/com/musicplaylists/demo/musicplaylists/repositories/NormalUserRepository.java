package com.musicplaylists.demo.musicplaylists.repositories;

import com.musicplaylists.demo.musicplaylists.entities.NormalUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NormalUserRepository extends CrudRepository<NormalUser, Long> {
    NormalUser findByUserId(Long id);
}