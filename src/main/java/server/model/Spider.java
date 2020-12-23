package server.model;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tartarus.snowball.ext.PorterStemmer;
import server.io.FileWork;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Spider {
    private final ArrayList<WebSiteItem> websites;

    public Spider(){
        websites = new ArrayList<>();
    }

    public ArrayList<WebSiteItem> getWebsites(){
        return this.websites;
    }

    public void addWebSite(String url){
        WebSiteItem newWs = new WebSiteItem(url);
        this.websites.add(newWs);
    }

    public void work() {
        for(WebSiteItem item : websites){
            try {
                String[] words = parsing(item);
                getKeyWords(item, words);
            }
            catch (Exception ignore) {}
        }

        FileWork.saveToFile("utils\\indexes.json", this);
    }

    private String[] parsing(WebSiteItem website) throws IOException {

        Document doc = Jsoup.connect(website.getUrl()).ignoreHttpErrors(true).get();
        website.setTitle(doc.title());
        StringBuilder sb = new StringBuilder();
        Elements elements = doc.body().select("*");
        for (Element element : elements) {
            if (element.is("[href]")) continue;
            sb.append(element.ownText());
            sb.append(' ');
        }

        String str = sb.toString();
        str = str.replaceAll("[,.:*;&?!©]", "");
        str = str.replaceAll("[\\s]+", " ");
        str = str.replaceAll("\n", " ");
        str = str.toLowerCase();
        System.out.println("parse : " + str);

        String delimeter = " ";
        String[] subStr = str.split(delimeter);
        return subStr;
    }

    private void getKeyWords(WebSiteItem website, String[] subStr) throws IOException {
        Map<String, Integer> wordCounts = new ConcurrentHashMap<>();
        for (String word : subStr) {
            if (word.length() > 0 && word.matches("^[a-zA-Z]+$")) {
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }
        }

        //удалить предлоги и часто встречающие слова не несущие информации
        ArrayList<String> unnecessary_words = new ArrayList<>();
        String inputFileName = "utils\\unnecessaryWords";
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line = reader.readLine();
            unnecessary_words.add(line);
            while ((line = reader.readLine()) != null){
                unnecessary_words.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for(String item : unnecessary_words){
            if(wordCounts.containsKey(item)) wordCounts.remove(item);
        }

        //слова мн и ед числа подсчитываются вместе
        TreeSet<String> copy_words = new TreeSet<>(wordCounts.keySet());
        PorterStemmer stemmer1 = new PorterStemmer();
        PorterStemmer stemmer2 = new PorterStemmer();
        for(String item : wordCounts.keySet()){
            stemmer1.setCurrent(item);
            stemmer1.stem();
            copy_words.remove(item);
            for(String temp : copy_words){
                stemmer2.setCurrent(temp);
                stemmer2.stem();
                if(stemmer1.getCurrent().equals(stemmer2.getCurrent())){
                    int k = wordCounts.get(temp);
                    int k1 = wordCounts.get(item);
                    wordCounts.remove(temp);
                    wordCounts.remove(item);
                    copy_words.remove(temp);
                    wordCounts.put(item, k + k1);
                    break;
                }
            }
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordCounts.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue() - a.getValue();
            }
        });
        System.out.println(list);

        List<Map.Entry<String, Integer>> key_words = list.subList(0,6);
        ArrayList<WebSiteItem.WordDescription> res = new ArrayList<>();
        for(int i = 0; i < key_words.size(); i++){
            WebSiteItem.WordDescription word = new WebSiteItem.WordDescription(key_words.get(i).getValue(), key_words.get(i).getKey());
            res.add(word);
        }
        website.setKeyWords(res);
    }

}
