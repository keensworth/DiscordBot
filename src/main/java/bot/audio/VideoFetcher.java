package bot.audio;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class VideoFetcher {
    public static String fetchIDFromQuery(String query, String youtubeKey){
        query = query.replace(" ", "+");
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=relevance&q=" + query + "&key=" + youtubeKey;

        JSONObject json = readJsonFromUrl(url);  // calling method in order to read.
        JSONArray items = json.getJSONArray("items");
        JSONObject id = items.getJSONObject(0).getJSONObject("id");

        return id.getString("videoId");
    }

    private static JSONObject readJsonFromUrl(String url) {
        try {
            try (InputStream is = new URL(url).openStream()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                return new JSONObject(jsonText);
            } catch (Exception cum) {
                return null;
            }
        } catch (Exception e){
            return null;
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
