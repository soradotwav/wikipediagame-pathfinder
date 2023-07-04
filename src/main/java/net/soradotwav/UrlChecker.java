package net.soradotwav;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class UrlChecker {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
    private static final String WIKIPEDIA_URL = "https://en.wikipedia.org/";

    public static Document check(String url) throws IOException {
        validateUrl(url);

        Connection connection = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .referrer(WIKIPEDIA_URL);

        try {
            return connection.get();

        } catch (HttpStatusException e) {
            System.out.println("Invalid Wikipedia URL. URL does not exist");
            throw e;
        }
    }

    private static void validateUrl(String url) {
        
        if(!url.startsWith(WIKIPEDIA_URL)) {
            System.out.println("Invalid URL. Must start with " + WIKIPEDIA_URL);
            return;
        }
    }
}