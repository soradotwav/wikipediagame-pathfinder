package net.soradotwav;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

    private static Document doc = null;

    /**
    * Retrieves a list of URLs found on the specified web page.
    * Checks the validity of the URL and establishes a connection using the UrlChecker class.
    * Scrapes the web page for URLs and stores them in an ArrayList.
    *
    * @param url the URL of the web page to be scraped
    * @return an ArrayList of Strings containing the scraped URLs
    * @throws IOException if an I/O error occurs while connecting to the URL
    */
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

    /**
    * Retrieves all HTML elements containing links found on the specified web page.
    * Checks the validity of the URL and establishes a connection using the UrlChecker class.
    * Returns the selected HTML elements.
    *
    * @param url the URL of the web page to be scraped
    * @return a Jsoup Elements object containing the selected HTML elements
    * @throws IOException if an I/O error occurs while connecting to the URL
    */
    public static Elements getElements(String url) throws IOException {
        UrlChecker.checkMain(url);
        doc = UrlChecker.checkConnect(url);

        Elements links = doc.select("tr > td > a, tr > td > i > a, p > a, p > i > a");

        return links;
    }
}