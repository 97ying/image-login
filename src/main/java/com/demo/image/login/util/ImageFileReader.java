package com.demo.image.login.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * Created by ejaiwng on 3/2/2018.
 */
public class ImageFileReader {

    public static byte[] readImageFromClassPath(String filePath) {
        try {
            return IOUtils.toByteArray(ImageFileReader.class.getClassLoader().getResourceAsStream(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
