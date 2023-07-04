package net.soradotwav;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

public class HttpApiCall {

    private static OkHttpClient client = new OkHttpClient();
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static HashMap<String, Set<String>> returnStringMap;
    private static String[] inputArr;

    public enum ApiType {
        CATEGORY,
        PORTAL
    }

    public static HashMap<String, Set<String>> callAPI(String inputStr, String inputNames, ApiType apiType) { //inputstr sites are fully encoded
        inputArr = inputNames.split("\\|");
        returnStringMap = new HashMap<>();
        Request request = new Request.Builder()
                .url(inputStr)
                .build();

        try (Response response = client.newCall(request).execute()) {
            int count = 0;
            processJson(response, apiType, count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnStringMap;
    }

    private static void processJson(Response apiResponse, ApiType apiType, int count) {
        try (ResponseBody responseBody = apiResponse.body()) {

            if (apiResponse.isSuccessful() && responseBody != null) {
                JsonNode jsonNode = objectMapper.readTree(responseBody.byteStream());
                JsonNode pagesNode = jsonNode.path("query").path("pages");
    
                if (pagesNode.size() > 0) {
                    Iterator<JsonNode> pageNodes = pagesNode.elements();
    
                    while (pageNodes.hasNext()) {
                        JsonNode tempNode = pageNodes.next();
                        Set<String> tempObject = null;

                        if (apiType.equals(ApiType.PORTAL)) {
                            JsonNode objectsNode = tempNode.path("links");
                            tempObject = jsonObjectProcess(objectsNode, 7);

                        } else if (apiType.equals(ApiType.CATEGORY)) {
                            JsonNode objectsNode = tempNode.path("categories");
                            tempObject = jsonObjectProcess(objectsNode, 9);
                        }
    
                        returnStringMap.put(inputArr[count], tempObject);
                        count++;

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Set<String> jsonObjectProcess(JsonNode objectsNode, int index) {
        Set<String> tempCategories = new HashSet<>();

        for (JsonNode tempCategory : objectsNode) {
            tempCategories.add(tempCategory.path("title").asText().substring(index));
        }
        return tempCategories;
    }
}
