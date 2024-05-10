package com.musicplaylists.demo.musicplaylists.services;

import com.musicplaylists.demo.musicplaylists.dtos.SongCreationDTO;
import com.musicplaylists.demo.musicplaylists.entities.Playlist;
import com.musicplaylists.demo.musicplaylists.entities.Song;
import com.musicplaylists.demo.musicplaylists.entities.User;
import com.musicplaylists.demo.musicplaylists.repositories.PlaylistRepository;
import com.musicplaylists.demo.musicplaylists.repositories.SongRepository;
import com.musicplaylists.demo.musicplaylists.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
public class SongService {

    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;

    @Autowired
    public SongService(SongRepository songRepository, PlaylistRepository playlistRepository, UserRepository userRepository) {
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Song createSong(SongCreationDTO songDTO) {
        Song song = new Song();
        song.setTitle(songDTO.title);
        song.setArtist(songDTO.artist);
        song.setDuration(songDTO.duration);
        song.setGenre(songDTO.genre);

        // Check if a song with the same artist and title already exists
        Song existingSong = songRepository.findByTitleAndArtist(songDTO.title, songDTO.artist);
        if (existingSong != null) {
            throw new IllegalArgumentException("Song already exists");
        }

        songRepository.save(song);

        return song;
    }

    @Transactional
    public void addSongToPlaylists(Long songId, List<Long> playlistIds, String userName) {
        User requestUser = userRepository.findByUsername(userName);
        if(requestUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Find the song by ID
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        // Find and add the song to each playlist
        for (Long playlistId : playlistIds) {
            Playlist playlist = playlistRepository.findById(playlistId)
                    .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

            // Check if the right user is used
            if (!Objects.equals(requestUser.getId(), playlist.getUser().getUser().getId())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            playlist.addSong(song);
            playlistRepository.save(playlist);
        }
    }

    @Transactional
    public void removeSongFromPlaylists(Long songId, List<Long> playlistIds, String userName) {
        User requestUser = userRepository.findByUsername(userName);
        if(requestUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Find the song by ID
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        // Remove the song from each playlist
        for (Long playlistId : playlistIds) {
            Playlist playlist = playlistRepository.findById(playlistId)
                    .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

            // Check if the right user is used
            if (!Objects.equals(requestUser.getId(), playlist.getUser().getUser().getId())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            playlist.removeSong(song);
            playlistRepository.save(playlist);
        }
    }

    @Transactional
    public void deleteSong(Long songId) {
        // Find the song by ID
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        songRepository.delete(song);
    }

    @Transactional
    public List<Song> getAllSongs() {
        return StreamSupport.stream(songRepository.findAll().spliterator(), false)
                .toList();
    }

    @Transactional
    public List<Song> getSongsByTitle(String title) {
        return songRepository.findByTitleContainingIgnoreCase(title);
    }

    @Transactional
    public List<Song> getSongsByArtist(String artist) {
        return songRepository.findByArtistContainingIgnoreCase(artist);
    }
}