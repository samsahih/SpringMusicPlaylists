package com.musicplaylists.demo.musicplaylists.repositories;

import com.musicplaylists.demo.musicplaylists.entities.Subscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
}