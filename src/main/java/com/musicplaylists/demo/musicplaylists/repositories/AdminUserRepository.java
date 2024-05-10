package com.musicplaylists.demo.musicplaylists.repositories;

import com.musicplaylists.demo.musicplaylists.entities.AdminUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminUserRepository extends CrudRepository<AdminUser, Long> {
}