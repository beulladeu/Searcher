package server.model;

import java.util.ArrayList;
import java.util.List;


public class WebSiteItem {
    private String url;
    private String title;
    private ArrayList<WordDescription> keyWords;

    public static class WordDescription {
        int count;
        String word;

        public WordDescription(int count, String word){
            this.count = count;
            this.word = word;
        }
    }

    public WebSiteItem(String url){
        this.url = url;
        this.keyWords = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<WordDescription> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(ArrayList<WordDescription> keyWords) {
        this.keyWords = keyWords;
    }

    public String getUrl() {
        return url;
    }
}
