package com.zacharyohearn.sbme.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceClient {

    @Value("${userServiceURL}")
    private String userServiceURL;

    private RestTemplate restTemplate;

    /**
     * Calls to the User web service to search all of the users and return the one with a matching {@code firstName} and {@code lastName}
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @return The found {@code User} or {@code null} if no users were found
     */
    public Optional<User> getUser(String firstName, String lastName, String dateOfBirth) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(userServiceURL)
                .queryParam("firstName", firstName)  .queryParam("lastName", lastName).queryParam("dateOfBirth", dateOfBirth);

        try {
            return Optional.ofNullable(restTemplate.getForObject(builder.buildAndExpand().toUri(), User.class));
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException ex) {
            return Optional.empty();
        }
    }
}
