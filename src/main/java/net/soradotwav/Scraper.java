package net.soradotwav;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

    private static Document doc = null;

    public static ArrayList<String> cacheSite(String url) throws IOException {

        // Initializing connection to website
        UrlChecker.checkMain(url);
        doc = UrlChecker.checkConnect(url);

        ArrayList<String> listOfUrls = new ArrayList<String>();
        Elements links = doc.select("tr > td > a, tr > td > i > a, p > a, p > i > a");

        for(Element link: links) {
            try {
                String listLink = link.attr("href");
                if (listLink == null) {
                    throw new NullPointerException("Link value is null");
                }
                if (listLink.contains("+")) {
                    listLink = listLink.replace("+", "%2B");
                }
                if (listLink.startsWith("/wiki/") && !listOfUrls.contains(listLink) && !listLink.contains("/wiki/File:")) {
                    listOfUrls.add("https://en.wikipedia.org" + listLink);
                }

            } catch (NullPointerException e) {
                System.err.println(e.getMessage());
                listOfUrls.add("N/A");
            }
        }

        return listOfUrls;
    }

    public static Elements getElements(String url) throws IOException {
        UrlChecker.checkMain(url);
        doc = UrlChecker.checkConnect(url);

        Elements links = doc.select("tr > td > a, tr > td > i > a, p > a, p > i > a");

        return links;
    }
}