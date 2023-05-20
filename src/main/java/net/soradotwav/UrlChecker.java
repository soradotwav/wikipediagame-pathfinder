package net.soradotwav;

import java.io.IOException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class UrlChecker {

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