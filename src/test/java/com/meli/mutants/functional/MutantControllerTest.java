package com.meli.mutants.functional;

import com.meli.mutants.MeliMutantsApplicationTests;
import com.meli.mutants.harness.FileReader;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.io.IOException;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;


class MutantControllerTest extends MeliMutantsApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void doPost_withInvalidRequest_dnaNotSquareMatrix_returnBadRequestResponse() throws IOException {
        String request = FileReader.read("./src/test/resources/harness/request/POST_mutants_bad_request_no_square.json");
        String response = FileReader.read("./src/test/resources/harness/response/POST_mutants_bad_request_no_square.json");

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        var httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/mutant/", HttpMethod.POST, httpEntity, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThatJson(responseEntity.getBody()).isEqualTo(response);
    }

    @Test
    void doPost_withInvalidRequest_dnaNull_returnBadRequestResponse() throws IOException {
        String request = FileReader.read("./src/test/resources/harness/request/POST_mutants_bad_request_null_dna.json");
        String response = FileReader.read("./src/test/resources/harness/response/POST_mutants_bad_request_null_dna.json");

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        var httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/mutant/", HttpMethod.POST, httpEntity, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThatJson(responseEntity.getBody()).isEqualTo(response);
    }

    @Test
    void doPost_withInvalidRequest_multipleErrors_returnBadRequestResponse() throws IOException {
        String request = FileReader.read("./src/test/resources/harness/request/POST_mutants_bad_request_multiple_errors.json");
        String response = FileReader.read("./src/test/resources/harness/response/POST_mutants_bad_request_multiple_errors.json");

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        var httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/mutant/", HttpMethod.POST, httpEntity, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThatJson(responseEntity.getBody())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(response);
    }

    @Test
    void doPost_withMutantDna_returnOkResponse() throws IOException {
        String request = FileReader.read("./src/test/resources/harness/request/POST_mutants_mutant_dna.json");
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        var httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/mutant/", HttpMethod.POST, httpEntity, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void doPost_withHumanDna_returnForbiddenResponse() throws IOException {
        String request = FileReader.read("./src/test/resources/harness/request/POST_mutants_human_dna.json");
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        var httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/mutant/", HttpMethod.POST, httpEntity, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
