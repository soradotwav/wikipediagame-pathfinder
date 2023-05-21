package net.soradotwav;

import java.io.IOException;
import java.util.ArrayList;

public class LinkList {

    private ArrayList<String> siteUrls;

    /**
    * Constructs an empty LinkList object.
    */
    public LinkList() {
        this.siteUrls = new ArrayList<String>();
    }


    /**
    * Initializes the list of URLs by scraping a specified web page using the Scraper class.
    *
    * @param url the URL of the web page to be scraped
    * @throws IOException if an I/O error occurs while connecting to the URL
    */
    public void initializeList(String url) throws IOException {
        siteUrls = Scraper.cacheSite(url);
    }

    /**
     * Returns the ArrayList containing the URLs.
    *
     * @return an ArrayList of Strings representing the URLs
     */
    public ArrayList<String> getUrls() {
        return siteUrls;
    }

    /**
    * Retrieves the URL at the specified index in the list.
    *
    * @param index the index of the URL to retrieve
    * @return the URL at the specified index
    */
    public String getUrl(int indx) {
        return siteUrls.get(indx);
    }

    /**
    * Clears the list of URLs.
    */
    public void clear() {
        siteUrls = new ArrayList<String>();
    }
}
