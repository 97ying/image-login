package com.image.login.tomcat.service;

import com.image.login.tomcat.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ejaiwng on 3/2/2018.
 */
@Service
public class ImageRepositoryService {

    private static final String SELECT_IMAGE_BY_ID = "SELECT name, image from image where name=?;";
    private static final String INSERT_IMAGE = "INSERT INTO image VALUES(?,?);";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public byte[] getImage(String userId) {
        try {
            Image image = jdbcTemplate.queryForObject(SELECT_IMAGE_BY_ID, new ImageRowMapper(), userId);
            return image.getImage();
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public boolean saveImage(String userId, byte[] image) {
        jdbcTemplate.update(INSERT_IMAGE, userId, image);
        return true;
    }

    private static final class ImageRowMapper implements RowMapper<Image> {

        @Override
        public Image mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Image(resultSet.getString("name"), resultSet.getBytes("image"));
        }
    }
}
