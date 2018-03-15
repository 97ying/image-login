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

package com.demo.image.login.controller;

import com.demo.image.login.model.LoginResult;
import com.demo.image.login.model.Result;
import com.demo.image.login.service.LoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class LoginController {

    private Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login/{userId}/", method = RequestMethod.POST)
    public ResponseEntity<LoginResult> login(@PathVariable String userId, @RequestParam("image") MultipartFile image) throws IOException {
        LOGGER.info("received login request for user: {}", userId);
        if (image.isEmpty()) {
            LoginResult loginResult = new LoginResult();
            loginResult.setErrorMessage("image was empty");
            LOGGER.info(objectMapper.writeValueAsString(loginResult));
            return new ResponseEntity<>(loginResult, HttpStatus.BAD_REQUEST);
        }

        LoginResult loginResult = loginService.compareImage(image.getBytes(), userId);
        LOGGER.info(objectMapper.writeValueAsString(loginResult));
        return new ResponseEntity<>(loginResult, HttpStatus.valueOf(loginResult.getStatusCode()));
    }

    @RequestMapping(value = "image/{userId}/", method = RequestMethod.POST)
    public ResponseEntity<Result> insertImage(@PathVariable String userId, @RequestParam("image") MultipartFile image) throws IOException {
        LOGGER.info("received image upload request for user: {}", userId);
        if (image.isEmpty()) {
            Result result = new Result();
            result.setErrorMessage("image was empty");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        Result result = loginService.saveImage(image.getBytes(), userId);
        LOGGER.info(objectMapper.writeValueAsString(result));
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Result> handleException(Exception ex) throws JsonProcessingException {
        LOGGER.error(ex.getMessage(), ex);

        Result result = new Result();
        result.setErrorMessage(ex.getMessage());
        result.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        LOGGER.info("response: " + objectMapper.writeValueAsString(result));
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
