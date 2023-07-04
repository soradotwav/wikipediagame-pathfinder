package net.soradotwav;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


// Returns full link
public class WebScraper {

    public static Set<String> cacheSite(String subsite) {
        
        try {
            String url = MySQLConnect.BASE_URL + subsite;
            Document doc = UrlChecker.check(url);

            Set<String> urlSet = new HashSet<>();
            Elements links = doc.select("tr > td > a, tr > td > i > a, p > a, p > i > a");

            for(Element link: links) {

                String currLink = link.attr("href");

                if (currLink != null) {

                    if (currLink.startsWith("/wiki/") && !currLink.contains("/wiki/File:")) {

                        int hashIndex = currLink.indexOf("#");
                        if (hashIndex != -1) {
                            currLink = currLink.substring(0, hashIndex);
                        }

                        currLink = currLink.substring(6);
                        
                        if (!currLink.contains("%")) {
                            urlSet.add(URLEncoder.encode(currLink, "UTF-8"));
                        } else {
                            urlSet.add(currLink); // Still does not grab Pokémon Ruby
                        }

                    }
                }
            }

            return urlSet;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}