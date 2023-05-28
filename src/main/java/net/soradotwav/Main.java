package net.soradotwav;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    private static ArrayList<String> endCategories;
    public static void main(String[] args) throws IOException {

        // Testing Area //

        String startUrl = "https://en.wikipedia.org/wiki/The_Legend_of_Zelda:_Tears_of_the_Kingdom"; // Change to choose starting url
        String endUrl = "https://en.wikipedia.org/wiki/Pok%C3%A9mon"; // Change to choose end url
        MediaWikiApi.setCategoryMap(getName(startUrl, true));
        MediaWikiApi.setCategoryMap(getName(endUrl, true));
        endCategories = MediaWikiApi.getMap().get(decode(getName(endUrl, true)));

        String temp = "Nintendo_EPD|Nintendo|Hidemaro_Fujibayashi|Eiji_Aonuma|The_Legend_of_Zelda|Nintendo_Switch|Action-adventure|Single-player|Action-adventure_game|Nintendo|Nintendo_Switch|The_Legend_of_Zelda:_Breath_of_the_Wild|Open_world|Hyrule|Link_(The_Legend_of_Zelda)|Princess_Zelda|Ganon|Downloadable_content|Entertainment_Planning_%26_Development|Hidemaro_Fujibayashi|Eiji_Aonuma|E3_2019|E3_2021|Breath_of_the_wild|Link_(The_Legend_of_Zelda)|Hyrule|Master_Sword|Prince_Sidon|Astral_projection|E3_2019|E3_2021|Nintendo_Direct|Disk_image|Hidemaro_Fujibayashi|Eiji_Aonuma|Hyrule|The_Legend_of_Zelda:_Skyward_Sword|Shigeru_Miyamoto|Illumination_(company)|The_Super_Mario_Bros._Movie|Wii_Sports_Resort|Red_Dead_Redemption_2|The_Elder_Scrolls_V:_Skyrim|The_Washington_Post|Metacritic|Destructoid|Digital_Trends|Edge_(magazine)|Eurogamer|Famitsu|Game_Informer|GameSpot|GamesRadar%2B|IGN|Nintendo_Life|The_Daily_Telegraph|The_Guardian|Video_Games_Chronicle|VG247|Review_aggregator|Metacritic|Open_world|IGN|Eurogamer|GameSpot|Game_Informer|IGN|GameSpot|Polygon_(website)|Nintendo_Life|Polygon_(website)|GameSpot|The_Legend_of_Zelda|The_Game_Awards_2020|Golden_Joystick_Awards|The_Game_Awards_2021|Golden_Joystick_Awards|The_Game_Awards_2022";
        setCategories(temp);

        ArrayList<String> list = MediaWikiApi.getMap().get("The_Game_Awards_2021");
        System.out.println(list);
        System.out.println(getComparator("Pokémon"));

        // Next: Implement PriorityQueue.

        //run(startUrl, endUrl);

    }

    public static void run(String startUrl, String endUrl) throws IOException {
        ArrayList<String> visited = new ArrayList<>();
        HashMap<String, String> path = new HashMap<>();
        Queue<String> queue = new LinkedList<String>();
        LinkList site;

        // Getting end Categories for Priority Check
        endCategories = MediaWikiApi.getCategoriesForItem(getName(endUrl, true));

        // Initializing the starting site
        LinkList startingSite = new LinkList();
        startingSite.initializeList(startUrl);
        visited.add(startUrl);
        path.put(startUrl, null);


        // Checking if the endUrl is present in the starting site
        if (startingSite.getUrls().contains(endUrl)) {
            System.out.println("Website found; checked " + visited.size() + " websites to find.");
            printPath(startUrl, endUrl, path);
            System.exit(1);
        }

        // Adding Categories from starting site URLs to Map
        MediaWikiApi.setCategoryMap(getApiString(startingSite));

        
        // Adding URLs from the starting site to the queue
        for (String url : startingSite.getUrls()) {
            if (!queue.contains(url)) {
                queue.add(url);
                path.put(url, startUrl);
            }
        }

        while(!queue.isEmpty()) {
            String currSite = queue.poll();
            site = new LinkList();
            site.initializeList(currSite);


            visited.add(currSite);

            if (site.getUrls().contains(endUrl)) {
                path.put(endUrl, currSite);
                break;
            }

            // Adding URLs from the current site to the queue
            for (String url : site.getUrls()) {
                if (!visited.contains(url) && !queue.contains(url)) {
                    queue.add(url);
                    path.put(url, currSite);
                }
            }
            System.out.println(currSite + " visited.");

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        
        System.out.println();
        printPath(startUrl, endUrl, path);
        System.out.println("Website found; checked " + visited.size() + " websites to find.");
        System.exit(1);
    }

    private static String getName(String url, boolean forApi) {

        url = url.replace("https://en.wikipedia.org/wiki/", "");
        if (!forApi) {
            url = url.replace("_", " ");
        }

        return url;
    }

    public static String decode(String input) {
        String output = "";
        try {
            output = URLDecoder.decode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return output;
    }

    private static void printPath(String startUrl, String endUrl, HashMap<String, String> path) {
        LinkedList<String> finalPath = new LinkedList<>();
        String url = endUrl;

        while (url != null) {
            finalPath.addFirst(decode(getName(url, false)));
            url = path.get(url);
        }

        System.out.println("Path: " + String.join(" --> ", finalPath));
    }

    public static String getApiString(LinkList list) {
        String apiString = "";
        int i = 0;
        for (i = 0; i < list.getUrls().size() - 1; ++i) {
            if (!apiString.contains(list.getUrl(i))) {
                apiString = apiString + getName(list.getUrl(i), true) + "|";
            }
        }
        apiString = apiString + getName(list.getUrl(i), true);
        return apiString;
    }

    public static double getComparator(String key) {
        int count = 0;
        int endCategoryNum = endCategories.size();

        ArrayList<String> list = MediaWikiApi.getMap().get(key);
        for (String item : list) {
            if (endCategories.contains(item)) {
                count++;
            }
        }

        if (count == 0) {
            return 0;
        }

        return (double) count / endCategoryNum;
    }

    public static void setCategories(String input) {
        String[] parts = input.split("\\|");
        int startIndex = 0;
        int endIndex = Math.min(startIndex + 10, parts.length);

        while (startIndex < parts.length) {
            String group = String.join("|", Arrays.copyOfRange(parts, startIndex, endIndex));
            MediaWikiApi.setCategoryMap(group);

            startIndex = endIndex;
            endIndex = Math.min(startIndex + 10, parts.length);
        }
    }
}
