package tobyspring.helloboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloApiTest {

    @Test
    void helloApi() {

        TestRestTemplate rest = new TestRestTemplate();
        ResponseEntity<String> responseEntity = rest.getForEntity(
                "http://localhost:8080/hello?name={name}",
                String.class,
                "Spring");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).
                startsWith(MediaType.TEXT_PLAIN_VALUE);
        assertThat(responseEntity.getBody()).isEqualTo("Hello *Spring*");

    }
}
