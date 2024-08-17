package amirhs.de.stage.rest.general;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import amirhs.de.stage.dto.City;

import java.util.*;

@RestController
@RequestMapping("/api/v1/location")
public class LocationController {

    private static final Map<String, List<String>> countryCitiesMap = new HashMap<>();

    static {
        countryCitiesMap.put("Iran", Arrays.asList("Tehran", "Shiraz", "Mashhad", "Isfahan", "Karaj", "Tabriz"));
        countryCitiesMap.put("Germany", Arrays.asList("Berlin", "Hamburg", "Munich", "Cologne", "Frankfurt"));
        countryCitiesMap.put("USA", Arrays.asList("New York", "Los Angeles", "Chicago", "Houston", "Phoenix"));
        countryCitiesMap.put("Australia", Arrays.asList("Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide", "Canberra"));
        countryCitiesMap.put("Canada", Arrays.asList("Toronto", "Vancouver", "Montreal", "Calgary", "Ottawa", "Edmonton"));
        countryCitiesMap.put("France", Arrays.asList("Paris", "Marseille", "Lyon", "Toulouse", "Nice", "Nantes"));
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getCities(@RequestParam String country) {
        List<String> cities = countryCitiesMap.getOrDefault(country, List.of("No cities found"));
        List<City> cityList = new ArrayList<>();
        for (String city : cities) {
            cityList.add(new City(city));
        }
        return ResponseEntity.ok(cityList);
    }
}
