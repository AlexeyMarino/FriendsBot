package bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ApexResultParser {
    String apexResults1 = "https://www.cybersport.ru/base/match?disciplines=10000011&status=past&page=1";
    String apexResults2 = "https://www.cybersport.ru/base/match?disciplines=10000011&status=past&page=2";
    String apexResults3 = "https://www.cybersport.ru/base/match?disciplines=10000011&status=past&page=3";
    ArrayList<String> result = new ArrayList<>();

    public String[] getNews() {

        getResultPage(apexResults1);
        getResultPage(apexResults2);
        getResultPage(apexResults3);
        return result.toArray(new String[0]);
    }

    public void getResultPage(String link) {
        Document document = null;
        try {
            document = Jsoup.connect(link)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements titles = document.select("article.matche");
        String temp = "";
        for(int i = 1; i < titles.size(); i++) {
            StringBuilder resultNews = new StringBuilder();
            Element match = titles.get(i);
            resultNews.append("\u2796\u2796\u2796").append(match.select("time").text()).append("\u2796\u2796\u2796").append("\n");
            Element title = match.selectFirst("a.matches__link");
            Element championat = match.selectFirst("a.report").selectFirst("span");
            temp = String.format("[%s](https://www.cybersport.ru%s)", title.text() + " " + championat.text(), title.attr("href"));
            resultNews.append(temp);
            resultNews.append("\n\n");
            result.add(resultNews.toString());
        }
    }
}
