package net.soradotwav;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<String> visited = new ArrayList<>();
        HashMap<String, String> path = new HashMap<>();  // hashmap for path tracing
        String startUrl = "https://en.wikipedia.org/wiki/The_Legend_of_Zelda:_Tears_of_the_Kingdom";
        String endUrl = "https://en.wikipedia.org/wiki/Home_video_game_console";
        Queue<String> queue = new LinkedList<String>();
        LinkList site;
        

        LinkList startingSite = new LinkList();
        startingSite.initializeList(startUrl);
        visited.add(startUrl);
        path.put(startUrl, null);  // startUrl doesn't have a parent


        if (startingSite.getUrls().contains(endUrl)) {
            System.out.println("Website found; checked " + visited.size() + " websites to find.");
            printPath(startUrl, endUrl, path);
            System.exit(1);
        }
        
        for (String url : startingSite.getUrls()) {
            if (!queue.contains(url)) {
                queue.add(url);
                path.put(url, startUrl);  // store path from startUrl to url
            }
        }

        while(!queue.isEmpty()) {
            String currSite = queue.poll();
            site = new LinkList();
            site.initializeList(currSite);
            visited.add(currSite);

            if (site.getUrls().contains(endUrl)) {
                path.put(endUrl, currSite);  // store path from currSite to endUrl
                break;
            }

            for (String url : site.getUrls()) {
                if (!visited.contains(url) && !queue.contains(url)) {
                    queue.add(url);
                    path.put(url, currSite);  // store path from currSite to url
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

    public static String getName(String url) {
        url = url.replace("https://en.wikipedia.org/wiki/", "");
        url = url.replace("_", " ");

        return url;
    }

    public static void printPath(String startUrl, String endUrl, HashMap<String, String> path) {
        LinkedList<String> finalPath = new LinkedList<>();
        String url = endUrl;

        while (url != null) {
            finalPath.addFirst(getName(url));
            url = path.get(url);
        }

        System.out.println("Path: " + String.join(" --> ", finalPath));
    }
}
