package ru.sitronics.tn.taskservice.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomRestClient {
    @Value("${bpms-proxy.url}")
    private String bpmsProxyUrl;
    private final RestTemplate restTemplate;

    public <T> List<T> getList(String endPointUri, Class<T[]> responseClass) {
        try {
            String url = bpmsProxyUrl + endPointUri;
            T[] array = restTemplate.getForObject(url, responseClass);
            log.info("URL: " + url);
            if (array == null) {
                return new ArrayList<>();
            }
            return Arrays.stream(array).toList();
        } catch (HttpStatusCodeException e) {
            throw e;
        }
    }

    public <T> ResponseEntity<T> post(String endPointUri, Class<T> responseClass, MediaType mediaType) {
        try {
            String url = bpmsProxyUrl + endPointUri;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            RequestEntity<Object> requestEntity = new RequestEntity<>(headers, HttpMethod.POST, URI.create(url));
            log.info("URL: " + url);
            return restTemplate.exchange(requestEntity, responseClass);
        } catch (HttpStatusCodeException e) {
            throw e;
        }
    }

    public <T> ResponseEntity<T> postJson(String endPointUri, Object requestBody, Class<T> responseClass) {
        return post(endPointUri, requestBody, responseClass, MediaType.APPLICATION_JSON);
    }

    public <T> ResponseEntity<T> postJson(String endPointUri,  Class<T> responseClass) {
        return post(endPointUri, responseClass, MediaType.APPLICATION_JSON);
    }

    public <T> ResponseEntity<T> postForm(String endPointUri, Object requestBody, Class<T> responseClass) {
        return post(endPointUri, requestBody, responseClass, MediaType.MULTIPART_FORM_DATA);
    }

    private <T> ResponseEntity<T> post(String endPointUri, Object requestBody, Class<T> responseClass, MediaType mediaType) {
        try {
            String url = bpmsProxyUrl + endPointUri;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            RequestEntity<Object> requestEntity = new RequestEntity<>(requestBody, headers, HttpMethod.POST, URI.create(url));
            log.info("URL: " + url + ", Request body object: " + requestBody);
            return restTemplate.exchange(requestEntity, responseClass);
        } catch (HttpStatusCodeException e) {
            throw e;
        }
    }

    public static Resource makeTempFile(byte[] bytes) throws IOException {
        Path testFile = Files.createTempFile("schema", ".bpmn");
        System.out.println("Creating and Uploading Test File: " + testFile);
        Files.write(testFile, bytes);
        return new FileSystemResource(testFile.toFile());
    }
}
