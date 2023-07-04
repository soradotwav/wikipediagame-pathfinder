package net.soradotwav;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.soradotwav.HttpApiCall.ApiType;

public class MediaWikiApi {
    
    private static final String CATEGORY_API_TEMPLATE = "https://en.wikipedia.org/w/api.php?action=query&prop=categories&cllimit=100&format=json&utf8=true&clshow=!hidden&titles=";
    private static final String PORTAL_API_TEMPLATE = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=links&pllimit=500&utf8=true&titles=%s&plnamespace=100";
    private static Set<String> visitedSites = new HashSet<>();
    private static Set<String> inputSet;

    // gets input from .toString() from set
    public static Set<String> addToSet(String inputSite) {

        if(inputSite.contains(MySQLConnect.BASE_URL)) {
            inputSite = inputSite.substring(30);
        }

        if(!inputSite.contains("%")) {
            try {
                inputSite = URLEncoder.encode(inputSite, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        
        inputSet = WebScraper.cacheSite(inputSite); // fully encoded subsites

        if (inputSet == null) {
            throw new NullPointerException("Error. Input was invalid and/or did not contain useable information.");
        }

        StringBuilder apiCall = new StringBuilder();

        visitedSites.addAll(inputSet); //adds fully encoded subsites
        Iterator<String> iterator = inputSet.iterator();
        while (iterator.hasNext()) {
            String subsite = iterator.next();
            if (!MySQLConnect.isInDatabase(subsite)) { //checks decoded in database (does not change encoded to decoded in here)
                apiCall.append(subsite).append("|");
            } else {
                iterator.remove();
            }
        }

        if (apiCall.length() > 0) {
            apiCall.deleteCharAt(apiCall.length() - 1);
            processSites(apiCall.toString());
            return inputSet;
        } else {
            return null;
        }
    }

    private static void processSites(String sites) {
        try {
            HashMap<String, Set<String>> tempPortals = new HashMap<>();
            HashMap<String, Set<String>> tempCategories = new HashMap<>();
    
            for (String categoryChunk : splitString(sites, 10)) { //fully decoded chunks
                tempCategories.putAll(HttpApiCall.callAPI(CATEGORY_API_TEMPLATE + categoryChunk, categoryChunk, ApiType.CATEGORY));
            }

            for(String portalChunk : splitString(sites, 20)) {
                tempPortals.putAll(HttpApiCall.callAPI(String.format(PORTAL_API_TEMPLATE, portalChunk), portalChunk, ApiType.PORTAL));
            }
    
            for (String site : inputSet) { //checks for fully decoded site
                Set<String> portals = tempPortals.get(site);
                Set<String> categories = tempCategories.get(site);
    
                if (portals == null || categories == null) {
                    System.out.println("Null set encountered for site: " + site);
                    continue;
                }
    
                if (!portals.isEmpty() || !categories.isEmpty()) {
                    MySQLConnect.addToDB(site, categories.toString(), portals.toString());
                }
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    // Split string into chunks of 10 sites
    private static List<String> splitString(String input, int index) {
        List<String> chunks = new ArrayList<>();
        String[] names = input.split("\\|");
        int length = names.length;
        int chunkCount = (int) Math.ceil((double) length / index);
    
        for (int i = 0; i < chunkCount; i++) {
            int startIndex = i * index;
            int endIndex = Math.min(startIndex + index, length);
            String[] chunkNames = Arrays.copyOfRange(names, startIndex, endIndex);
            String chunk = String.join("|", chunkNames);
    
            if (chunk.startsWith("|")) {
                chunk = chunk.substring(3);
            }
            if (chunk.endsWith("|")) {
                chunk = chunk.substring(0, chunk.length() - 3);
            }

            chunks.add(chunk);

        }
    
        return chunks;
    }
}
