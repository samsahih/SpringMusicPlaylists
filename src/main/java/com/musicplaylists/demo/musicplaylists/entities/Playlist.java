package com.musicplaylists.demo.musicplaylists.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    private NormalUser user;

    // Add the Many-to-Many relationship with songs
    @ManyToMany
    @JoinTable(
            name = "playlist_song",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private Set<Song> songs = new HashSet<>();

    public Playlist() {}

    public Playlist(NormalUser user, String name, String description) {
        this.name = name;
        this.user = user;
        this.description = description;
    }

    public Playlist(NormalUser user, String name) {
        this(user, name, null);
    }

    public String getName() {
        return name;
    }

    public NormalUser getUser(){
        return user;
    }

    public void setUser(NormalUser user) {
        this.user = user;
        if (user != null) {
            user.getPlaylists().add(this);
        }
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        songs.add(song);
        song.getPlaylists().add(this);
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public String getDescription() {
        return description;
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }

    public Object getId() {
        return id;
    }
}