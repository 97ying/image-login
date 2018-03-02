package com.image.login.tomcat.service;

import com.image.login.tomcat.util.ImageFileReader;
import org.springframework.stereotype.Service;

/**
 * Created by ejaiwng on 3/2/2018.
 */
@Service
public class ImageRepositoryService {

    public byte[] getImage(String userId) {
        return ImageFileReader.readImageFromClassPath(userId + ".jpg");
    }
}
