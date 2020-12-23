package server.model;

import org.tartarus.snowball.ext.PorterStemmer;
import server.io.FileWork;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Searcher {

    public static String[][] searcher(ArrayList<String> words) throws IOException {
        ArrayList<WebSiteItem> result = new ArrayList<>();
        Spider temp_spider = new Spider();
        ArrayList<WebSiteItem> sites = FileWork.fileReader("utils\\indexes.json", temp_spider);
        PorterStemmer stemmer1 = new PorterStemmer();
        PorterStemmer stemmer2 = new PorterStemmer();
        for (WebSiteItem site : sites) {
            if (site.getKeyWords().containsAll(words)) {
                if(!result.contains(site)) result.add(site);
            } else {
                for (String word : words) {
                    for (int j = 0; j < site.getKeyWords().size(); j++) {
                        stemmer1.setCurrent(site.getKeyWords().get(j).word);
                        stemmer1.stem();
                        stemmer2.setCurrent(word);
                        stemmer2.stem();
                        if (stemmer1.getCurrent().equals(stemmer2.getCurrent())) {
                            if(!result.contains(site)) result.add(site);
                        }
                    }
                }
            }
        }

        Map<String, Integer> buildRelevantList = new ConcurrentHashMap<>();
        for (WebSiteItem site : result) {
            int count = 0;
            for (int i = 0; i < site.getKeyWords().size(); i++) {
                for(String word : words){
                    stemmer1.setCurrent(site.getKeyWords().get(i).word);
                    stemmer1.stem();
                    stemmer2.setCurrent(word);
                    stemmer2.stem();
                    if(stemmer1.getCurrent().equals(stemmer2.getCurrent())){
                        count += site.getKeyWords().get(i).count;
                    }
                }
                buildRelevantList.put(site.getUrl(), count);
            }
        }

        List<Map.Entry<String, Integer>> resultRelevantList = new ArrayList<>(buildRelevantList.entrySet());
        Collections.sort(resultRelevantList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue() - a.getValue();
            }
        });

        //ArrayList<String> urls_result = new ArrayList<>();
        String[][] resultResp = new String[resultRelevantList.size()][2];
        for (WebSiteItem site : sites) {
            int i = 0;
            for (Map.Entry<String, Integer> stringIntegerEntry : resultRelevantList) {
                if(site.getUrl().equals(stringIntegerEntry.getKey())){
                    resultResp[i][0] = stringIntegerEntry.getKey();
                    resultResp[i][1] = site.getTitle();
                    System.out.println(site.getUrl() + " " + site.getTitle());
                }
                i++;
                //urls_result.add(stringIntegerEntry.getKey());
            }
        }
        return resultResp;
    }
}
