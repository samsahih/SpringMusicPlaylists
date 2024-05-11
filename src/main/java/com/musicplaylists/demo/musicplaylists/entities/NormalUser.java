package com.musicplaylists.demo.musicplaylists.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("NORMAL")
public class NormalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Playlist> playlists = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Subscription subscription;

    public NormalUser() {}

    public User getUser() {
        return user;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
        playlist.setUser(this);
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
        if (subscription != null) {
            subscription.setUser(this);
        }
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Sets the ID of the NormalUser. This method should only be used for testing purposes.
     */
    public void setId(long l) {
        this.id = l;
    }
}
