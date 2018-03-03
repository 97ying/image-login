/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.image.login.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.image.login.model.LoginResult;
import com.demo.image.login.model.Result;
import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static final String CONFIDENCE = "confidence";
    private static final String TIME_USED = "time_used";
    private static final String ERROR_MESSAGE = "error_message";

    @Value("${image.login.match.pass.threshold:80}")
    private int matchPassThreshold;

    @Autowired
    private ImageRepositoryService imageRepositoryService;

    @Autowired
    private CommonOperate commonOperate;

    private ObjectMapper mapper = new ObjectMapper();

    public Result saveImage(byte[] image, String userId) {
        Result result = new Result();
        if (imageRepositoryService.saveImage(userId, image)) {
            result.setStatusCode(HttpStatus.OK.value());
        } else {
            result.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
        return result;
    }

    public LoginResult compareImage(byte[] imageToBeCheck, String userId) {

        byte[] srcImage = imageRepositoryService.getImage(userId);

        if (srcImage == null) {
            LoginResult loginResult = new LoginResult();
            loginResult.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            loginResult.setErrorMessage("user not found");
            return loginResult;
        }

        try {
            Response response = commonOperate.compare(null, null, imageToBeCheck, null, null, null, srcImage, null);

            if (response.getContent() != null && response.getContent().length > 1) {
                JsonNode contentNode = mapper.readTree(response.getContent());
                return buildLoginResultFrom(response.getStatus(), contentNode);
            } else {
                throw new RuntimeException("Image compare content is empty!");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    private LoginResult buildLoginResultFrom(int status, JsonNode contentNode) {
        LoginResult loginResult = new LoginResult();
        loginResult.setStatusCode(status);
        loginResult.setMatchThreshold(matchPassThreshold);
        loginResult.setTimeUsed(contentNode.get(TIME_USED).asInt());

        if (status == HttpStatus.OK.value()) {
            loginResult.setMatch(contentNode.get(CONFIDENCE).asDouble());
            if (contentNode.get(CONFIDENCE).asDouble() > matchPassThreshold) {
                loginResult.setPass(true);
            } else {
                loginResult.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            }

        } else {
            loginResult.setPass(false);
            loginResult.setErrorMessage(contentNode.get(ERROR_MESSAGE).asText());
        }
        return loginResult;
    }

}