package bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CybersportDotaNewsParser implements Parser{
    String dotaURL;

    public CybersportDotaNewsParser(String dotaURL) {
        this.dotaURL = dotaURL;
    }

    public String[] getNews() {
        Document document = null;
        try {
            document = Jsoup.connect(dotaURL)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements news = document.select("div.card-vertical__text");
        String temp = "";
        String[] allnews = new String[news.size()];
        for(int i = 0; i < news.size(); i++) {
            StringBuilder resultNews = new StringBuilder();
            resultNews.append("\u2694\ufe0f ");
            Elements titles = news.get(i).select("h3");
            Elements links = titles.select("a");
            temp = String.format("[%s](www.cybersport.ru%s)", titles.text(), links.attr("href"));
            resultNews.append(temp).append("\n\n");
            allnews[i] = resultNews.toString();
        }
        return allnews;
    }
}
