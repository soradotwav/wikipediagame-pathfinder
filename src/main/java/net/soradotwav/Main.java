package net.soradotwav;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    /**
    * The main method that starts the website search and path finding.
    *
    * @param args the command line arguments
    * @throws IOException if an I/O error occurs while connecting to a URL
    */
    public static void main(String[] args) throws IOException {
        ArrayList<String> visited = new ArrayList<>();
        HashMap<String, String> path = new HashMap<>();  // hashmap for path tracing
        String startUrl = "https://en.wikipedia.org/wiki/MissingNo.";
        String endUrl = "https://en.wikipedia.org/wiki/The_Legend_of_Zelda:_Tears_of_the_Kingdom";
        Queue<String> queue = new LinkedList<String>();
        LinkList site;

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

    /**
    * Extracts the name from the given URL.
    *
    * @param url the URL to extract the name from
    * @return the extracted name
    */
    public static String getName(String url) {
        url = url.replace("https://en.wikipedia.org/wiki/", "");
        url = url.replace("_", " ");

        return url;
    }

    /**
    * Prints the path from the startUrl to the endUrl.
    * The path is constructed by following the parent-child relationships in the path HashMap.
    *
    * @param startUrl the starting URL
    * @param endUrl the ending URL
    * @param path the HashMap containing the parent-child relationships between URLs
    */
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
