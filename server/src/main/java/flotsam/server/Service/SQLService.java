package flotsam.server.Service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import flotsam.server.Model.Game;
import flotsam.server.Repository.SQLRepository;

@Repository
public class SQLService {
    @Autowired
    private SQLRepository SQLRepo;
    
    public String insertGame(String name, String description, String developers, String image, String release_date, Integer score) {
        String id = "SQL" + UUID.randomUUID().toString().substring(0, 10);
        Game game = new Game(id, name, description, developers, image, release_date, score);
        SQLRepo.insertGame(game);
        return id;
    }

    public void addmediatouser(Integer id, String media_type, String media_id) {
        SQLRepo.addmediatouser(id, media_type, media_id);
    }
}
