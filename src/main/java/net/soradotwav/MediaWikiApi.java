package net.soradotwav;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MediaWikiApi {

    private static String apiTemplate = "https://en.wikipedia.org/w/api.php?action=query&prop=categories&cllimit=100&format=json&clshow=!hidden&titles=";
    private static HashMap<String, ArrayList<String>> categoryHashMap = new HashMap<>();

    public static HashMap<String, ArrayList<String>> setCategoryMap(String urlApiString) {

        String[] individualItems = urlApiString.split("\\|");
        for (int i = 0; i < individualItems.length; ++i) {
            if (individualItems.length == 1) {
                break;
            } else if (categoryHashMap.containsKey(individualItems[i]) && i == individualItems.length - 1) {
                urlApiString.replace(individualItems[i], "");
            } else if (categoryHashMap.containsKey(individualItems[i])) {
                urlApiString.replace(individualItems[i] + "\\|", "");
            }
        }

        if (urlApiString.isEmpty()) {
            return categoryHashMap;
        }
    
        try {
            URL url = new URL(apiTemplate + urlApiString);
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
    
                processApiResponse(response.toString(), urlApiString); // Update the categoryHashMap directly
    
            } else {
                System.out.println("API request failed. Response Code: " + responseCode);
            }
    
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryHashMap;
    }

    public static HashMap<String, ArrayList<String>> getMap() {
        return categoryHashMap;
    }

    public static ArrayList<String> getCategoriesForItem(String url) {
        HashMap<String, ArrayList<String>> map = setCategoryMap(url);

        return map.get(url);
    }

    private static void processApiResponse(String apiResponse, String urlApiString) {
        try {
            urlApiString = URLDecoder.decode(urlApiString, "UTF-8");
    
            String[] nameList = urlApiString.split("\\|");
    
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(apiResponse);
    
            JsonNode pagesNode = jsonNode.path("query").path("pages");
    
            pagesNode.fields().forEachRemaining(pageEntry -> {
                String pageTitle = pageEntry.getValue().path("title").asText();
    
                for (String name : nameList) {
                    if (pageTitle.equalsIgnoreCase(name.replace('_', ' '))) {
                        JsonNode pageData = pageEntry.getValue();
    
                        ArrayList<String> categories = categoryHashMap.getOrDefault(name, new ArrayList<>());
    
                        JsonNode categoriesNode = pageData.path("categories");
                        categoriesNode.forEach(categoryNode -> {
                            String categoryName = categoryNode.path("title").asText();
                            categoryName = categoryName.replace("Category:", "");
                            categories.add(categoryName);
                        });
    
                        categoryHashMap.put(name, categories);
                    }
                }
            });
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
