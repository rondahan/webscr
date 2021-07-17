//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Driver {
    static Scanner scanner;

    public Driver() {
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        int points = 0;
        BaseRobot site = getSiteSelection();
        if (site != null) {
            points = points + guessCommonWords(site);
            String userText = getHeadlinesText();
            System.out.println("how many time it will appears?:");
            int quantity = scanner.nextInt();

            try {
                points += chuckText(quantity, userText, site);
            } catch (IOException var6) {
                var6.printStackTrace();
            }

            System.out.println("you achieved " + points + " points!");
        } else {
            System.out.println("cant access website, sorry.");
        }

    }

    private static int chuckText(int quantity, String userText, BaseRobot site) throws IOException {
        int realQuantity = site.countInArticlesTitles(userText);
        return Math.abs(quantity - realQuantity) <= 2 ? 250 : 0;
    }

    private static String getHeadlinesText() {
        String userText;
        for(userText = ""; userText.length() < 1 || userText.length() > 20; userText = scanner.nextLine()) {
            System.out.println("Enter any text that you think should appear in the headlines on the site,");
            System.out.println("the text should be between 1 and 20 chars:");
        }

        return userText;
    }

    private static BaseRobot getSiteSelection() {
        int input;
        for(input = 0; input < 1 || input > 3; input = scanner.nextInt()) {
            System.out.println("which site do you want to scan?");
            System.out.println("\t1. Mako");
            System.out.println("\t2. Ynet");
            System.out.println("\t3. Walla");
        }

        scanner.nextLine();

        try {
            switch(input) {
                case 1:
                    return new MakoRobot();
                case 2:
                    return new YnetRobot();
                default:
                    return new WallaRobot();
            }
        } catch (IOException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    private static int guessCommonWords(BaseRobot site) {
        int points = 0;

        try {
            String longestArticle = site.getLongestArticleTitle();
            System.out.println("Please guess what the most common words on the site are?");
            System.out.println("hint:\n" + longestArticle);
            Map<String, Integer> wordsInSite = site.getWordsStatistics();

            for(int i = 1; i <= 5; ++i) {
                System.out.println("guess number " + i + ":");
                String guess = scanner.nextLine();
                points += (Integer)wordsInSite.getOrDefault(guess, 0);
            }
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return points;
    }
}
