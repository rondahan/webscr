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

public class YnetRobot extends BaseRobot implements MapOrder {
    private Map<String, Integer> map = new HashMap();
    private final ArrayList<String> sitesUrl = new ArrayList();

    public YnetRobot() throws IOException {
        super("https://www.ynet.co.il/home/0,7340,L-8,00.html");
        Document ynet = Jsoup.connect(this.getRootWebsiteUrl()).get();
        Iterator var3 = ynet.getElementsByClass("TopStory1280Componenta basic").iterator();

        String url;
        Element slotsContent;
        while(var3.hasNext()) {
            slotsContent = (Element)var3.next();
            Element element = (Element)slotsContent.getElementsByClass("slotTitle").get(0);
            url = element.child(0).attributes().get("href");
            this.sitesUrl.add(url);
        }

        var3 = ynet.getElementsByClass("YnetMultiStripComponenta oneRow multiRows").iterator();

        Element slotTitle_small;
        Iterator var7;
        while(var3.hasNext()) {
            slotsContent = (Element)var3.next();
            var7 = slotsContent.getElementsByClass("textDiv").iterator();

            while(var7.hasNext()) {
                slotTitle_small = (Element)var7.next();
                url = slotTitle_small.child(1).attributes().get("href");
                this.sitesUrl.add(url);
            }
        }

        var3 = ((Element)ynet.getElementsByClass("MultiArticleRowsManualComponenta").get(0)).getElementsByClass("slotsContent").iterator();

        while(var3.hasNext()) {
            slotsContent = (Element)var3.next();
            var7 = slotsContent.getElementsByClass("slotTitle medium").iterator();

            while(var7.hasNext()) {
                slotTitle_small = (Element)var7.next();
                url = slotTitle_small.child(0).attributes().get("href");
                this.sitesUrl.add(url);
            }

            var7 = slotsContent.getElementsByClass("slotTitle small").iterator();

            while(var7.hasNext()) {
                slotTitle_small = (Element)var7.next();
                url = slotTitle_small.child(0).attributes().get("href");
                this.sitesUrl.add(url);
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
        int count = 0;
        Document ynet = Jsoup.connect(this.getRootWebsiteUrl()).get();
        Iterator var4 = ynet.getElementsByClass("layoutContainer").iterator();

        while(var4.hasNext()) {
            Element container = (Element)var4.next();
            Iterator var6 = container.getElementsByClass("slotTitle small").iterator();

            Element title_medium;
            while(var6.hasNext()) {
                title_medium = (Element)var6.next();
                if (title_medium.text().contains(text)) {
                    ++count;
                }
            }

            var6 = container.getElementsByClass("slotTitle medium").iterator();

            while(var6.hasNext()) {
                title_medium = (Element)var6.next();
                if (title_medium.text().contains(text)) {
                    ++count;
                }
            }
        }

        if (((Element)ynet.getElementsByClass("slotSubTitle").get(0)).text().contains(text)) {
            ++count;
        }

        if (((Element)ynet.getElementsByClass("slotTitle").get(0)).text().contains(text)) {
            ++count;
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
            String title = article.getElementsByClass("mainTitle").text();
            StringBuilder siteTextBuilder = new StringBuilder();
            Iterator var8 = article.getElementsByClass("text_editor_paragraph rtl").iterator();

            while(var8.hasNext()) {
                Element text_editor_paragraph_rtl = (Element)var8.next();
                siteTextBuilder.append(text_editor_paragraph_rtl.getElementsByTag("span").text());
            }

            if (longest < siteTextBuilder.length()) {
                longest = siteTextBuilder.length();
                longestArticleTitle = title;
            }
        }

        return longestArticleTitle;
    }

    public String accessSite(String site) throws IOException {
        String siteText = "";
        StringBuilder siteTextBuilder = new StringBuilder(siteText);
        Document article = Jsoup.connect(site).get();
        siteTextBuilder.append(article.getElementsByClass("mainTitle").text());
        siteTextBuilder.append(" ");
        siteTextBuilder.append(article.getElementsByClass("subTitle").text());
        siteTextBuilder.append(" ");
        Iterator var5 = article.getElementsByClass("text_editor_paragraph rtl").iterator();

        while(var5.hasNext()) {
            Element text_editor_paragraph_rtl = (Element)var5.next();
            siteTextBuilder.append(" ");
            siteTextBuilder.append(text_editor_paragraph_rtl.getElementsByTag("span").text());
        }

        return siteTextBuilder.toString();
    }
}
