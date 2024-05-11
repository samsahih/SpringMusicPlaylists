package com.musicplaylists.demo.musicplaylists.integrationtests;

import com.musicplaylists.demo.musicplaylists.dtos.*;
import com.musicplaylists.demo.musicplaylists.entities.Song;
import com.musicplaylists.demo.musicplaylists.entities.User;
import com.musicplaylists.demo.musicplaylists.entities.UserType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BusinessLogicIntegrationTest {

    @LocalServerPort
    private Integer port;
    private final RestClient restClient = RestClient.create();

    private final RestTemplate restTemplate = new RestTemplate();

    private String BASEURI;

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @BeforeEach
    void testSetUp() {

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        messageConverters.add(converter);
        this.restTemplate.setMessageConverters(messageConverters);

        BASEURI = "http://localhost:" + port;
    }

    @BeforeAll
    static void beforeAll() {
        mysqlContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mysqlContainer.stop();
    }

    @Test
    public void testUserManagementAndActions() {

        // Create multiple users
        Long userId1 = createUser("user1@example.com", "user1", "password1", UserType.NORMAL);
        Long userId2 = createUser("user2@example.com", "user2", "password2", UserType.NORMAL);

        // Authenticate user
        String authToken = authenticateUser("user1", "password1");

        // Create songs
        Long songId1 = createSong("Song 1", "Artist 1", 300, "Rock", authToken);
        Long songId2 = createSong("Song 2", "Artist 2", 240, "Pop", authToken);

        // Create playlists
        Long playlistId1 = createPlaylist("user1", "Playlist 1", "Description 1", authToken);
        Long playlistId2 = createPlaylist("user1", "Playlist 2", "Description 2", authToken);

        // Add songs to playlists
        addSongToPlaylist(songId1, Collections.singletonList(playlistId1), authToken);
        addSongToPlaylist(songId2, Collections.singletonList(playlistId2), authToken);

        // Remove songs from playlists
        removeSongFromPlaylist(songId1, Collections.singletonList(playlistId1), authToken);
        removeSongFromPlaylist(songId2, Collections.singletonList(playlistId2), authToken);
    }

    @Test
    public void testManagerFunctionality() {

        // Create user
        Long userId = createUser("manager@example.com","Batal", "123456", UserType.ADMIN);

        // Authenticate user
        String authToken = authenticateUser("Batal", "123456");
        System.out.println("Token: " + authToken);

        // List all subscriptions
        ResponseEntity<List<SubscriptionResponseDTO>> response = restTemplate.exchange(
                BASEURI + "/api/subscriptions",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(authToken)),
                new ParameterizedTypeReference<List<SubscriptionResponseDTO>>() {}
                );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }



    // Helper methods to interact with controllers (ideally separated into a helper class)

    private HttpHeaders createHeaders(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private Long createUser(String email, String username, String password, UserType type) {
        System.out.println("Creating user: " + email);
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO(email, username, password, password, type);
        ResponseEntity<User> response = restTemplate.postForEntity(BASEURI + "/api/auth/register", registrationDTO, User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return response.getBody().getId();
    }

    private String authenticateUser(String username, String password) {
        UserLoginDTO loginDTO = new UserLoginDTO(username, password);

        ResponseEntity<UserLoginResponseDTO> responseEntity = restClient.post()
                .uri(BASEURI + "/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(loginDTO)
                .retrieve()
                .toEntity(UserLoginResponseDTO.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody().getToken();
        }
        else
            throw new RuntimeException("Authorization header not found in response.");
    }

    private Long createSong(String title, String artist, int duration, String genre, String accessToken) {
        SongCreationDTO songDTO = new SongCreationDTO();
        songDTO.title = title;
        songDTO.artist = artist;
        songDTO.duration = duration;
        songDTO.genre = genre;

        ResponseEntity<Song> response = restTemplate.postForEntity(BASEURI + "/api/songs", new HttpEntity<>(songDTO, createHeaders(accessToken)), Song.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return response.getBody().getId();
    }

    private Long createPlaylist(String username, String name, String description, String accessToken) {
        PlaylistCreationDTO playlistDTO = new PlaylistCreationDTO();
        playlistDTO.username = username;
        playlistDTO.playlistName = name;
        playlistDTO.description = description;

        ResponseEntity<PlaylistCreationResponseDTO> response = restTemplate.postForEntity(BASEURI + "/api/playlists", new HttpEntity<>(playlistDTO, createHeaders(accessToken)), PlaylistCreationResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return response.getBody().getId();
    }

    private void addSongToPlaylist(Long songId, List<Long> playlistId, String accessToken) {
        String url = String.format(BASEURI + "/api/playlists/song/%d", songId);
        restTemplate.postForEntity(url, new HttpEntity<>(playlistId, createHeaders(accessToken)), ResponseEntity.class);
    }

    private void removeSongFromPlaylist(Long songId, List<Long> playlistId, String accessToken) {
        String url = String.format(BASEURI + "/api/playlists/song/%d", songId);
        restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(playlistId, createHeaders(accessToken)), ResponseEntity.class);
    }
}