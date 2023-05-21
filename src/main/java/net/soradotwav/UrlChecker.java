package net.soradotwav;

import java.io.IOException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class UrlChecker {

    /**
    * Checks if the given URL starts with "https://en.wikipedia.org/".
    * If the URL is invalid, an IllegalArgumentException is thrown with an appropriate error message.
    * If the URL is valid, the method continues without any exceptions.
    *
    * @param url the URL to be checked
    */
    public static void checkMain(String url) {
        if(!url.startsWith("https://en.wikipedia.org/")) {
            try {
                throw new IllegalArgumentException("Invalid URL. Must start with 'https://en.wikipedia.org/'");
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
    }

    /**
    * Connects to the specified URL using Jsoup library and retrieves the web page content.
    * The method sets the user agent and referrer to mimic a web browser's request.
    *
    * @param url the URL to be connected and checked
    * @return a Jsoup Document representing the web page content
    * @throws IOException if an I/O error occurs while connecting to the URL
    */
    public static Document checkConnect(String url) throws IOException {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("https://en.wikipedia.org/")
                    .get();
        } catch (HttpStatusException e) {
            System.out.println("Invalid Wikipedia URL. URL does not exist");
            System.exit(1);
        }
        return doc;
    }
}