package flotsam.server.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDetails {
    String name;
    String short_description;
    String header_image;
    String release_date;
    String developers;
}
