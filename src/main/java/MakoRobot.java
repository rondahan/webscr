//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MakoRobot extends BaseRobot implements MapOrder {
    private Map<String, Integer> map = new HashMap();
    private final ArrayList<String> sitesUrl;

    public MakoRobot() throws IOException {
        super("https://www.mako.co.il/");
        String begging = "https://www.mako.co.il/";
        this.sitesUrl = new ArrayList();
        Document mako = Jsoup.connect(this.getRootWebsiteUrl()).get();
        Iterator var4 = mako.getElementsByClass("teasers").iterator();

        String url;
        Element news;
        Iterator var6;
        Element h5;
        while(var4.hasNext()) {
            news = (Element)var4.next();
            var6 = news.children().iterator();

            while(var6.hasNext()) {
                h5 = (Element)var6.next();
                url = h5.child(0).child(0).attributes().get("href");
                if (url.contains(begging)) {
                    this.sitesUrl.add(url);
                } else {
                    this.sitesUrl.add(begging + url);
                }
            }
        }

        var4 = mako.getElementsByClass("neo_ordering scale_image horizontal news").iterator();

        while(var4.hasNext()) {
            news = (Element)var4.next();
            var6 = news.getElementsByTag("h5").iterator();

            while(var6.hasNext()) {
                h5 = (Element)var6.next();
                url = h5.child(0).attributes().get("href");
                if (url.contains(begging)) {
                    this.sitesUrl.add(url);
                } else {
                    this.sitesUrl.add(begging + url);
                }
            }
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

    public int countInArticlesTitles(String text) throws IOException {
        Document mako = Jsoup.connect(this.getRootWebsiteUrl()).get();
        int count = 0;
        Iterator var4 = mako.getElementsByTag("span").iterator();

        while(var4.hasNext()) {
            Element spanElements = (Element)var4.next();
            Iterator var6 = spanElements.getElementsByAttributeValue("data-type", "title").iterator();

            while(var6.hasNext()) {
                Element title = (Element)var6.next();
                if (title.text().contains(text)) {
                    ++count;
                }
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
            String title = ((Element)article.getElementsByTag("h1").get(0)).text();
            StringBuilder siteTextBuilder = new StringBuilder();
            Element articleBody = (Element)article.getElementsByClass("article-body").get(0);
            Iterator var9 = articleBody.getElementsByTag("p").iterator();

            while(var9.hasNext()) {
                Element p = (Element)var9.next();
                siteTextBuilder.append(p.text());
            }

            if (longest < siteTextBuilder.length()) {
                longest = siteTextBuilder.length();
                longestArticleTitle = title;
            }
        }

        return longestArticleTitle;
    }

    public String accessSite(String site) throws IOException {
        StringBuilder siteTextBuilder = new StringBuilder();
        Document article = Jsoup.connect(site).get();
        siteTextBuilder.append(((Element)article.getElementsByTag("h1").get(0)).text());
        siteTextBuilder.append(" ");
        siteTextBuilder.append(article.getElementsByTag("h2").text());
        Element articleBody = (Element)article.getElementsByClass("article-body").get(0);
        Iterator var5 = articleBody.getElementsByTag("p").iterator();

        while(var5.hasNext()) {
            Element p = (Element)var5.next();
            siteTextBuilder.append(" ");
            siteTextBuilder.append(p.text());
        }

        return siteTextBuilder.toString();
    }
}
