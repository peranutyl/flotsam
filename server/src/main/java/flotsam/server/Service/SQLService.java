package flotsam.server.Service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import flotsam.server.Model.User;
import flotsam.server.Repository.SQLRepository;

@Repository
public class SQLService {
    @Autowired
    private SQLRepository SQLRepo;
    

    public void addmediatouser(Integer id, String media_type, Integer media_id) {
        SQLRepo.addmediatouser(id, media_type, media_id);
    }

    public Integer signup(String username, String hashedpassword, String imageURL) {
        return SQLRepo.signup(username, hashedpassword, imageURL);
    }

    public Integer login(String username, String password) {
        return SQLRepo.login(username, password);
    }

    public User getuserdetails(Integer id) {
        return SQLRepo.getuserdetails(id);
    }

    public List<Integer> getlistofgames(Integer id) {
        return SQLRepo.getlistofgames(id);
    }
}
