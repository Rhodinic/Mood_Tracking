package htl.steyr.mood_tracking;

import htl.steyr.mood_tracking.model.Entry;
import htl.steyr.mood_tracking.model.EntryRepository;
import htl.steyr.mood_tracking.model.User;
import htl.steyr.mood_tracking.model.Weather;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;

@Component
public class EntryWriter {
    private static EntryWriter entryWriter = null;

    @Autowired
    private EntryRepository entryRepository;

    public void saveEntry(int mood, boolean school, int exhaustion, int socialAmount, boolean specialEvent, User user){
        /**
         * If an entry exists from today overwrite that one,
         * if not create a new entity for today
         */
        Optional<Entry> result = entryRepository.findByDateAndUser(new Date(), user);

        Entry entry;

        if(result.isPresent()){
            entry = result.get();
        }else {
            entry = new Entry();
            entry.setWeather(getWeather(user.getLocation()));
        }

        entry.setMood(mood);
        entry.setSchool(school);
        entry.setSchoolIntensity(exhaustion);
        entry.setAmountOfSocialInteraction(socialAmount);
        entry.setSpecialEvent(specialEvent);
        entry.setUser(user);

        entryRepository.save(entry);
    }

    public Weather getWeather(String location) {
        Weather result = new Weather();

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            /**
             * Wenn ein nicht existenter Ort verwendet wird.
             */
            InputStream is;

            try {
                URL url = new URL("https://api.weatherapi.com/v1/history.json?key=d8b250ece95f49e79ea74520221711&q=" + location + "&dt=" + dtf.format(now));
                is = url.openStream();
            }catch (IOException e){
                return null;
            }

            Scanner s = new Scanner(is).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";

            JSONObject json = new JSONObject(response);

            result.setTemperature(json.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getDouble("avgtemp_c"));
            result.setHumidity(json.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getDouble("avghumidity"));
            result.setDescription(json.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getJSONObject("condition").getString("text"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
