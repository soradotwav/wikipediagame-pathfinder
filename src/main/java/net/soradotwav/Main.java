package net.soradotwav;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Set;

public class Main {
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        Set<String> test = MediaWikiApi.addToSet("https://en.wikipedia.org/wiki/Pokémon");
        if (test == null) {
            System.exit(1);
        }


        for(String site: test) {
            System.out.println("Loading " + URLDecoder.decode(site, "UTF-8"));
            MediaWikiApi.addToSet(site);
            System.out.println("Done with " + URLDecoder.decode(site, "UTF-8"));
        } 
        System.out.println();
        System.out.println("All Done.");
    }
}
