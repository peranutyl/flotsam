package flotsam.server.Controller;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import flotsam.server.Model.GameCards;
import flotsam.server.Model.GameDetails;
import flotsam.server.Model.GameReview;
import flotsam.server.Model.GameReviewSummary;
import flotsam.server.Repository.S3Repository;
import flotsam.server.Service.SQLService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Controller
public class FlotsamControl {
    @Autowired
    private SQLService SQLServe;
    
    @Autowired
    private S3Repository S3Repo;
    
    RestTemplate restTemplate = new RestTemplate();
    
    
    @GetMapping(path="/searchsteamgames")
    public ResponseEntity<List<GameCards>> searchSteamGames(@RequestParam String searchTerm){
            List<GameCards> gameCards = new ArrayList<>();
            String getAppList = restTemplate.getForObject("https://api.steampowered.com/ISteamApps/GetAppList/v2/", String.class);
                try (JsonReader reader = Json.createReader(new StringReader(getAppList))) {
            JsonObject rootObject = reader.readObject();
            JsonObject applistObject = rootObject.getJsonObject("applist");
            JsonArray appsArray = applistObject.getJsonArray("apps");

            List<Integer> foundGames = new ArrayList<>();

            for (JsonObject appObject : appsArray.getValuesAs(JsonObject.class)) {
                // make both lowercase so it's not case sensitive
                if (appObject.getString("name").toLowerCase().contains(searchTerm.toLowerCase())) {
                    System.out.println(appObject.getString("name") + " " + appObject.getInt("appid"));
                    foundGames.add(appObject.getInt("appid"));

                }
            }
            if (!foundGames.isEmpty()) {
                
                System.out.println("Found games matching the search:");
                int count = 0;
                for (Integer appid : foundGames) {
                    if (count >= 10){
                        break;
                    }
                    String appdetailsURL = "https://store.steampowered.com/api/appdetails?appids=" + appid;
                    String appdetails = restTemplate.getForObject(appdetailsURL, String.class);
                    JsonObject jsonObject = Json.createReader(new StringReader(appdetails)).readObject().getJsonObject(appid.toString());
                    if (jsonObject.getBoolean("success")) {
                    JsonObject gameData = jsonObject.getJsonObject("data");
                    Boolean isGame = gameData.getString("type").equals("game");
                    System.out.println(isGame);
                    
                        if (isGame){
                            String name = gameData.getString("name");
                            String short_description = gameData.getString("short_description");
                            String capsule_image = gameData.getString("capsule_image");
                            String release_date = gameData.getJsonObject("release_date").getString("date");
                            
                            gameCards.add(new GameCards(appid, name, short_description, capsule_image, release_date));

                            System.out.println("ID: " + appid);
                            System.out.println("Name: " + name);
                            System.out.println("Short Description: " + short_description);
                            System.out.println("Header Image URL: " + capsule_image);
                            System.out.println("release_date: " + release_date);
                            count ++;
                    }
                    }
                }
            } else {
                System.out.println("No games matching the search found.");
            }
        }
        return ResponseEntity.ok(gameCards);
    }

    @GetMapping(path="/getgamedetails")
    public ResponseEntity<GameDetails> getgamedetails(@RequestParam Integer id) {
        String appdetailsURL = "https://store.steampowered.com/api/appdetails?appids=" + id;
        String appdetails = restTemplate.getForObject(appdetailsURL, String.class);
        JsonObject gameData = Json.createReader(new StringReader(appdetails)).readObject().getJsonObject(id.toString()).getJsonObject("data");
        String name = gameData.getString("name");
        String short_description = gameData.getString("short_description");
        String header_image = gameData.getString("header_image");
        String release_date = gameData.getJsonObject("release_date").getString("date");
        String developers = gameData.getJsonArray("developers").getString(0);
                            System.out.println("ID: " + id);
                            System.out.println("Name: " + name);
                            System.out.println("Short Description: " + short_description);
                            System.out.println("Header Image URL: " + header_image);
                            System.out.println("release_date: " + release_date);
                            System.out.println(developers);
        GameDetails gameDetails = new GameDetails(name, short_description, header_image, release_date, developers);
        return ResponseEntity.ok(gameDetails);
    }

    @GetMapping(path="/getreviewsummary")
    public ResponseEntity<GameReviewSummary> getreviewsummary(@RequestParam Integer id) {
        String reviewsURL = String.format("https://store.steampowered.com/appreviews/%s?json=1", id);
        String reviews = restTemplate.getForObject(reviewsURL, String.class);
        JsonObject query_summary = Json.createReader(new StringReader(reviews)).readObject().getJsonObject("query_summary");
        String review_score_desc = query_summary.getString("review_score_desc");
        Integer total_reviews = query_summary.getInt("total_reviews");
        Integer positive_percentage = 0;
        if(total_reviews != 0){
            positive_percentage = query_summary.getInt("total_positive") * 100 / total_reviews ;
        }
        GameReviewSummary grw = new GameReviewSummary(review_score_desc, total_reviews, positive_percentage);
        return ResponseEntity.ok(grw);
    }   
    @GetMapping(path="/getreviews")
    public ResponseEntity<List<GameReview>> getreviews(@RequestParam Integer id, @RequestParam String reviewType) {
        List<GameReview> gameReviews = new ArrayList<>();
       String reviewsURL = String.format("https://store.steampowered.com/appreviews/%s?json=1&review_type=%s&day_range=365", id, reviewType);
       System.out.println(reviewsURL);
       String reviewsString = restTemplate.getForObject(reviewsURL, String.class);
       JsonArray reviews = Json.createReader(new StringReader(reviewsString)).readObject().getJsonArray("reviews");

       List<JsonObject> jsonObjectList = new ArrayList<>();

       for (JsonValue jsonValue : reviews) {
           if (jsonValue instanceof JsonObject) {
               jsonObjectList.add((JsonObject) jsonValue);
           }
       }

       jsonObjectList.sort(Comparator.comparingInt(o -> -o.getInt("votes_funny")));

       List<JsonObject> top5FunniestReviews = jsonObjectList.subList(0, Math.min(jsonObjectList.size(), 5));

       for (JsonObject review : top5FunniestReviews) {
           gameReviews.add(new GameReview(review.getJsonObject("author").getInt("playtime_at_review")/ 60,review.getString("review"), review.getInt("votes_funny"))); 
       }

       return ResponseEntity.ok(gameReviews);
    }

    @GetMapping(path="/addmedia")
    public ResponseEntity<String> addgames(@RequestParam String userid, @RequestParam Integer gameid) {
        SQLServe.addmediatouser(Integer.valueOf(userid), "game", gameid);
        return ResponseEntity.ok("IT WORKS");
    }

    @GetMapping(path="/listofgames")
    public ResponseEntity<List<GameCards>> listofmedia(@RequestParam String userid){
        List<Integer> games = new ArrayList<>();
        List<GameCards> gameCards = new ArrayList<>();
        games = SQLServe.getlistofgames(Integer.valueOf(userid));
        for (Integer gameid : games){
            String appdetailsURL = "https://store.steampowered.com/api/appdetails?appids=" + gameid;
            String appdetails = restTemplate.getForObject(appdetailsURL, String.class);
            JsonObject gameData = Json.createReader(new StringReader(appdetails)).readObject().getJsonObject(gameid.toString()).getJsonObject("data");
            String name = gameData.getString("name");
            String short_description = gameData.getString("short_description");
            String capsule_image = gameData.getString("capsule_image");
            String release_date = gameData.getJsonObject("release_date").getString("date");
            
            gameCards.add(new GameCards(gameid, name, short_description, capsule_image, release_date));
        }
        return ResponseEntity.ok(gameCards);
    }
}




