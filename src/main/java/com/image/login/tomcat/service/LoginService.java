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

package com.image.login.tomcat.service;

import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.Response;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginService {

	@Value("${name:World}")
	private String name;

	public String getHelloMessage() {
        CommonOperate commonOperate = new CommonOperate("43q_Tq75C8wVaaPK4zRlRpy7Y7uykzuL", "Sa6DxOjlxBZMnMv9ZBEpkWYS3BC8f67Q", false);

        byte[] image1 = readImage("haolin-1.jpg");
        byte[] image2 = readImage("haolin-1.jpg");
        try {
//            Response response = commonOperate.compare(null, null, image1, null, null, null, image2, null);
            Response response = commonOperate.detectByte(image1, 0, "");
            if (response.getStatus() != HttpStatus.OK.value()) {
                return new String(response.getContent());
            } else {
                return new StringBuilder().append("{'status':").append(response.getStatus()).append("}").toString();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
	}


	public byte[] readImage(String filePath) {
        try {
            return IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
