
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WallaRobot extends BaseRobot implements MapOrder {
    Map<String, Integer> map = new HashMap();
    ArrayList<String> sitesUrl = new ArrayList();

    public WallaRobot() throws IOException {
        super("https://www.walla.co.il/");
        Document walla = Jsoup.connect(this.getRootWebsiteUrl()).get();
        Iterator var2 = walla.getElementsByClass("with-roof ").iterator();

        while(var2.hasNext()) {
            Element teasers = (Element)var2.next();
            this.sitesUrl.add(teasers.child(0).attributes().get("href"));
        }

        Element section = (Element)walla.getElementsByClass("css-1ugpt00 css-a9zu5q css-rrcue5 ").get(0);
        Iterator var6 = section.getElementsByTag("a").iterator();

        while(var6.hasNext()) {
            Element smallTeasers = (Element)var6.next();
            this.sitesUrl.add(smallTeasers.attributes().get("href"));
        }

    }

    public Map<String, Integer> getWordsStatistics() throws IOException {
        String[] wordsOfArticle;
        for(Iterator var1 = this.sitesUrl.iterator(); var1.hasNext(); this.map = this.getWordsIntoMap(wordsOfArticle, this.map)) {
            String site = (String)var1.next();
            String siteText = this.accessSite(site);
            siteText = this.correctWords(siteText);
            wordsOfArticle = siteText.split(" ");
        }

        return this.map;
    }

    public String accessSite(String site) throws IOException {
        String siteText = "";
        StringBuilder siteTextBuilder = new StringBuilder(siteText);
        Document article = Jsoup.connect(site).get();
        Element titleSection = (Element)article.getElementsByClass("item-main-content").get(0);
        siteTextBuilder.append(((Element)titleSection.getElementsByTag("h1").get(0)).text());
        siteTextBuilder.append(" ");
        Iterator var6 = article.getElementsByClass("css-onxvt4").iterator();

        while(var6.hasNext()) {
            Element subTitle = (Element)var6.next();
            siteTextBuilder.append(" ");
            siteTextBuilder.append(subTitle.text());
        }

        return siteTextBuilder.toString();
    }

    public int countInArticlesTitles(String text) throws IOException {
        int count = 0;
        Document walla = Jsoup.connect(this.getRootWebsiteUrl()).get();
        Iterator var5 = walla.getElementsByClass("with-roof ").iterator();

        String titleFromWeb;
        while(var5.hasNext()) {
            Element teasers = (Element)var5.next();
            titleFromWeb = teasers.getElementsByTag("h2").text();
            if (titleFromWeb.contains(text)) {
                ++count;
            }
        }

        Element section = (Element)walla.getElementsByClass("css-1ugpt00 css-a9zu5q css-rrcue5 ").get(0);
        Iterator var9 = section.getElementsByTag("a").iterator();

        while(var9.hasNext()) {
            Element smallTeasers = (Element)var9.next();
            titleFromWeb = smallTeasers.getElementsByTag("h3").text();
            if (titleFromWeb.contains(text)) {
                ++count;
            }
        }

        return count;
    }

    public String getLongestArticleTitle() throws IOException {
        String longestArticleTitle = "";
        int longest = 0;
        Iterator var4 = this.sitesUrl.iterator();

        while(var4.hasNext()) {
            String site = (String)var4.next();
            Document article = Jsoup.connect(site).get();
            Element titleSection = (Element)article.getElementsByClass("item-main-content").get(0);
            String title = ((Element)titleSection.getElementsByTag("h1").get(0)).text();
            StringBuilder siteTextBuilder = new StringBuilder();
            Iterator var9 = article.getElementsByClass("css-onxvt4").iterator();

            while(var9.hasNext()) {
                Element articleBody = (Element)var9.next();
                siteTextBuilder.append(articleBody.text());
                siteTextBuilder.append(" ");
            }

            if (longest < siteTextBuilder.length()) {
                longest = siteTextBuilder.length();
                longestArticleTitle = title;
            }
        }

        return longestArticleTitle;
    }
}
