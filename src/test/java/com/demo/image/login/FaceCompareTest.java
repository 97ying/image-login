package com.demo.image.login;

import com.demo.image.login.model.LoginResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by ejaiwng on 3/2/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FaceCompareTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testFaceCompare() {
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("image", new FileSystemResource(FaceCompareTest.class.getClassLoader().getResource("jinhai-1.jpg").getPath()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

        ResponseEntity<LoginResult> response = restTemplate.exchange("/login/jinhai/",
                HttpMethod.POST, requestEntity, LoginResult.class);

        assertEquals("status code should be " + HttpStatus.OK.value(), HttpStatus.OK.value(), response.getStatusCode().value());
        assertTrue("'pass' should be true", response.getBody().isPass());
    }

}
