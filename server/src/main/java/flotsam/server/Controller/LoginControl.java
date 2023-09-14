package flotsam.server.Controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import flotsam.server.Model.User;
import flotsam.server.Repository.S3Repository;
import flotsam.server.Service.SQLService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Controller
public class LoginControl {
    @Autowired
    private SQLService SQLServe;
    
    @Autowired
    private S3Repository S3Repo;

    @PostMapping(path="/signup" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signup(@RequestPart String username,  @RequestPart String password,
    @RequestPart(required = false) MultipartFile image) {
        PasswordEncoder pe = new BCryptPasswordEncoder();
        String hashedPassword = pe.encode(password);
        if (image == null) {
            // this is the default profile picture You get
            Integer id = SQLServe.signup( username , hashedPassword , "https://thisisabucket.sgp1.cdn.digitaloceanspaces.com/7d1f3293");
            JsonObject resp = Json.createObjectBuilder()
                .add("id", id)
                .build();
            return ResponseEntity.ok(resp.toString());
        } 
        else{
            try {
                String contentType = image.getContentType();
                InputStream is = image.getInputStream();
                String imageid = S3Repo.S3Upload(contentType, is);
                String imageURL = S3Repo.getURL(imageid);
                Integer id = SQLServe.signup( username , hashedPassword , imageURL);
                JsonObject resp = Json.createObjectBuilder()
                    .add("id", id)
                    .build();
                return ResponseEntity.ok(resp.toString());

            } catch (Exception ex) {
                JsonObject resp = Json.createObjectBuilder()
                    .add("error", ex.getMessage())
                    .build();
                return ResponseEntity.status(500)
                    .body(resp.toString());
            }
        
        }
    }

    @PostMapping(path="/login" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestPart String username,  @RequestPart String password) {
        Integer id = SQLServe.login( username ,password );
        System.out.println(id);
        if(id != null){
            JsonObject resp = Json.createObjectBuilder()
                .add("id", id)
                .build();
            return ResponseEntity.ok(resp.toString());
        
        } else {
            return ResponseEntity.ok("FAILURE");
        }

        }
    @GetMapping(path="/getuserdetails")
    public ResponseEntity<String> getuserdetails(@RequestParam String userid) {
        User user = new User();
        Integer id = Integer.valueOf(userid);
        user = SQLServe.getuserdetails(id);
        JsonObject resp = Json.createObjectBuilder()
            .add("username", user.getUsername())
            .add("imageURL", user.getImageURL())
            .build();
        return ResponseEntity.ok(resp.toString());
    }
}


