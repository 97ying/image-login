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

package com.image.login.tomcat.web;

import com.image.login.tomcat.model.LoginResult;
import com.image.login.tomcat.model.Result;
import com.image.login.tomcat.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login/{userId}/", method = RequestMethod.POST)
    public ResponseEntity<LoginResult> login(@PathVariable String userId, @RequestParam("image") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            LoginResult loginResult = new LoginResult();
            loginResult.setErrorMessage("image was empty");
            return new ResponseEntity<>(loginResult, HttpStatus.BAD_REQUEST);
        }

        LoginResult loginResult = loginService.compareImage(image.getBytes(), userId);
        return new ResponseEntity<>(loginResult, HttpStatus.valueOf(loginResult.getStatusCode()));
    }

    @RequestMapping(value = "image/{userId}/", method = RequestMethod.POST)
    public ResponseEntity<Result> insertImage(@PathVariable String userId, @RequestParam("image") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            Result result = new Result();
            result.setErrorMessage("image was empty");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        Result result = loginService.saveImage(image.getBytes(), userId);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
}
