package net.soradotwav;

import java.io.IOException;
import java.util.ArrayList;

public class LinkList {

    private ArrayList<String> siteUrls;

    public LinkList() {
        this.siteUrls = new ArrayList<String>();
    }

    public void initializeList(String listUrl) throws IOException {
        siteUrls = Scraper.cacheSite(listUrl);
    }

    public ArrayList<String> getUrls() {
        return siteUrls;
    }

    public String getUrl(int indx) {
        return siteUrls.get(indx);
    }

    public void clear() {
        siteUrls = new ArrayList<String>();
    }
}
