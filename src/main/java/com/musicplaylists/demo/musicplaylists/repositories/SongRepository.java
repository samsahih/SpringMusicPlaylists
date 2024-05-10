package com.musicplaylists.demo.musicplaylists.repositories;

import com.musicplaylists.demo.musicplaylists.entities.Song;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends CrudRepository<Song, Long> {
    List<Song> findByTitleContainingIgnoreCase(String title);

    List<Song> findByArtistContainingIgnoreCase(String artist);

    Song findByTitleAndArtist(String title, String artist);
}