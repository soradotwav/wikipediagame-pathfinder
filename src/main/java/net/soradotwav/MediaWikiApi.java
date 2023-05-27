package net.soradotwav;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MediaWikiApi {

    private static String apiTemplate = "https://en.wikipedia.org/w/api.php?action=query&prop=categories&cllimit=100&format=json&clshow=!hidden&titles=";
    private static HashMap<String, ArrayList<String>> categoryHashMap = new HashMap<>();

    public static HashMap<String, ArrayList<String>> getCategories(String currUrls) {

        if (categoryHashMap.containsKey(currUrls)) { // !!!!!Edit to check each link in titles !!!!!//
            return categoryHashMap;
        }

        try {
            URL url = new URL(apiTemplate + currUrls);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                categoryHashMap = processApiResponse(response.toString(), currUrls);

            } else {
                System.out.println("API request failed. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categoryHashMap;
    }

    private static HashMap<String, ArrayList<String>> processApiResponse(String apiResponse, String currUrls) {
        HashMap<String, ArrayList<String>> hashMap = new HashMap<>();

        String[] nameList = currUrls.split("\\|");
        final int[] count = { 0 };

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(apiResponse);

            JsonNode pagesNode = jsonNode.path("query").path("pages");

            pagesNode.fields().forEachRemaining(pageEntry -> {
                String pageTitle = nameList[count[0]];
                JsonNode pageData = pageEntry.getValue();

                ArrayList<String> categories = new ArrayList<>();

                JsonNode categoriesNode = pageData.path("categories");
                categoriesNode.forEach(categoryNode -> {
                    String categoryName = categoryNode.path("title").asText();
                    categoryName = categoryName.replace("Category:", "");
                    categories.add(categoryName);
                });

                hashMap.put(pageTitle, categories);

                count[0]++;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hashMap;
    }
}
