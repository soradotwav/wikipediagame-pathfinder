package net.soradotwav;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    // Implement hashmap with url to get to it being the key for path tracing
    public static void main(String[] args) throws IOException {
        ArrayList<String> visited = new ArrayList<>();
        String startUrl = "https://en.wikipedia.org/wiki/The_Legend_of_Zelda:_Tears_of_the_Kingdom";
        String endUrl = "https://en.wikipedia.org/wiki/List_of_video_game_magazines";
        Queue<String> queue = new LinkedList<String>();
        LinkList site;
        

        LinkList startingSite = new LinkList();
        startingSite.initializeList(startUrl);
        visited.add(startUrl);

        if (startingSite.getUrls().contains(endUrl)) {
            System.out.println("Website found; checked " + visited.size() + " websites to find.");
            System.exit(1);
        }
        
        for (int i = 0; i < startingSite.getUrls().size(); ++i) {
            if (!queue.contains(startingSite.getUrl(i))) {
                queue.add(startingSite.getUrl(i));
            }
        }

        while(!queue.isEmpty()) {
            String currSite = queue.poll();
            site = new LinkList();
            site.initializeList(currSite);
            visited.add(currSite);

            if (site.getUrls().contains(endUrl)) {
                break;
            }

            for (int i = 0; i < site.getUrls().size(); ++i) {
                if (!visited.contains(site.getUrl(i)) && !queue.contains(site.getUrl(i))) {
                    queue.add(site.getUrl(i));
                }
            }
            System.out.println(currSite + " visited.");

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Website found; checked " + visited.size() + " websites to find.");
        System.exit(1);
    }
}