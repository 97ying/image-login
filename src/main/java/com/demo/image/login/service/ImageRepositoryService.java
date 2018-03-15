package com.demo.image.login.service;

import com.demo.image.login.model.Image;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by ejaiwng on 3/2/2018.
 */
@Service
public class ImageRepositoryService {

    private static Log logger = LogFactory.getLog(ImageRepositoryService.class);

    private static final String SELECT_IMAGE_BY_ID = "SELECT name, image from image where name=?;";
    private static final String INSERT_IMAGE = "INSERT INTO image VALUES(?,?);";
    private static final String UPDATE_IMAGE = "UPDATE image SET image=? where name=?;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<Image> getImage(String userId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_IMAGE_BY_ID, new ImageRowMapper(), userId);
        } catch (EmptyResultDataAccessException ex) {
            logger.warn(ex.getMessage());
            return Optional.empty();
        }
    }

    public boolean saveImage(Image image) {
        if (getImage(image.getName()).isPresent()) {
            jdbcTemplate.update(UPDATE_IMAGE, image.getImage(), image.getName());
        } else {
            jdbcTemplate.update(INSERT_IMAGE, image.getName(), image.getImage());
        }
        return true;
    }

    private static final class ImageRowMapper implements RowMapper<Optional<Image>> {

        @Override
        public Optional<Image> mapRow(ResultSet resultSet, int i) throws SQLException {
            try {
                return Optional.of(new Image(resultSet.getString("name"), resultSet.getBytes("image")));
            } catch (EmptyResultDataAccessException ex) {
                logger.warn(ex.getMessage());
                return Optional.empty();
            }
        }
    }
}
