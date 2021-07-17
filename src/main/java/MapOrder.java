
import java.io.IOException;
import java.util.Map;

public interface MapOrder {
    String accessSite(String var1) throws IOException;

    default Map<String, Integer> getWordsIntoMap(String[] wordsOfArticle, Map<String, Integer> map) {
        String[] var4 = wordsOfArticle;
        int var5 = wordsOfArticle.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String word = var4[var6];
            int value;
            if (map.containsKey(word)) {
                value = (Integer)map.get(word) + 1;
            } else {
                value = 1;
            }

            map.put(word, value);
        }

        return map;
    }

    default String correctWords(String siteText) {
        siteText = siteText.replaceAll("[-–•<>@&_%():,.?0-9]", " ");
        siteText = siteText.replaceAll("\"\\s|\\s\"", " ");
        siteText = siteText.replaceAll("\\s+", " ");
        return siteText;
    }
}
