package flotsam.server.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import flotsam.server.Model.Game;

@Repository
public class SQLRepository {
    @Autowired
    private JdbcTemplate template;

    public static final String INSERT_GAME = "INSERT INTO GAMES VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String INSERT_MEDIA_INTO_USER = "INSERT INTO USER_MEDIA VALUES (?, ?, ?)";

    public void insertGame(Game game) {
        template.update(INSERT_GAME, game.id(), game.name(), game.description(), game.developers(), game.image(), game.release_date(), game.score());
    }

    public void addmediatouser(Integer id, String media_type, String media_id) {
        template.update(INSERT_MEDIA_INTO_USER, id, media_type, media_id);
    }
}
