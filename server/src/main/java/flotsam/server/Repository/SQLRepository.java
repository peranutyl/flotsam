package flotsam.server.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import flotsam.server.Model.GameCards;
import flotsam.server.Model.User;

@Repository
public class SQLRepository {
    @Autowired
    private JdbcTemplate template;

    public static final String INSERT_MEDIA_INTO_USER = "INSERT INTO USERS_MEDIA VALUES (?, ?, ?, ?)";
    public static final String INSERT_SIGN_UP = "INSERT INTO USERS VALUES (null, ?, ?, ?)";
    public static final String SELECT_LOGIN = "SELECT * FROM USERS WHERE username = ?";
    public static final String SELECT_USER_DETAILS = "SELECT * FROM USERS WHERE id = ?";
    public static final String SELECT_USER_MEDIA = "SELECT media_id FROM USERS_MEDIA WHERE user_id = ? AND media_status = ?";
    public static final String UPDATE_USER_MEDIA = "UPDATE USERS_MEDIA SET media_status = ? WHERE user_id = ? AND media_id = ?";
    public static final String DELETE_USER_MEDIA = "DELETE FROM USERS_MEDIA WHERE USER_ID = ? AND MEDIA_ID = ?";
    public static final String CHECK_USER_NAME = "SELECT id FROM USERS WHERE username = ?";

    public void addmediatouser(Integer id, String media_type, Integer media_id) {
        template.update(INSERT_MEDIA_INTO_USER, id, media_type, media_id, "salvaged") ;
    }

    public Integer signup(String username, String hashedpassword, String imageURL) {
        try {
            Integer existid = template.queryForObject(CHECK_USER_NAME, Integer.class, username);
            if (existid != null) {
                return null;
            }
        } catch (EmptyResultDataAccessException ex) {
        }
        KeyHolder generatedKey = new GeneratedKeyHolder();

        PreparedStatementCreator psc = new PreparedStatementCreator() {
        
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                PreparedStatement ps = con.prepareStatement(INSERT_SIGN_UP, new String[] { "id" });
                ps.setString(1, username);
                ps.setString(2, hashedpassword);
                ps.setString(3, imageURL);
                return ps;
            }

        };

        template.update(psc, generatedKey);

        Integer id = generatedKey.getKey().intValue();
        return id;
    }

    public Integer login(String username, String password) {
        PasswordEncoder pe = new BCryptPasswordEncoder();
        User user = new User();
        user = template.queryForObject(SELECT_LOGIN, BeanPropertyRowMapper.newInstance(User.class), username);
        if (user != null){
            if (pe.matches(password, user.getHashed_password())) {
                return user.getId();
            } else {
                return null;
            } 
        } else {
            return null;
        }
    }

    public User getuserdetails(Integer id) {
        User user = new User();
        user = template.queryForObject(SELECT_USER_DETAILS, BeanPropertyRowMapper.newInstance(User.class), id);

        return user;
    }
    
    public List<Integer> getlistofgames(Integer id, String status) {
        return template.queryForList(SELECT_USER_MEDIA, Integer.class, id, status);
    }

    public void updateusermedia(Integer user_id, Integer mediaid, String status) {
        template.update(UPDATE_USER_MEDIA, status, user_id, mediaid);
    }

    public void deletegame(Integer userid, Integer gameid) {
        template.update(DELETE_USER_MEDIA, userid, gameid);
    }
}