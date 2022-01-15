package bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DotaruParser {

    String dotaURL = "https://dota2.ru/news/cybersport/";

    public String getNews() {
        Document document = null;
        try {
            document = Jsoup.connect(dotaURL)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements titles = document.select("article.item");
        Elements news = titles.select("div.title");
        Elements links = document.select("article.item > a");
        StringBuilder resultNews = new StringBuilder();
        String temp;

        for(int i = 0; i < 20; i++) {
            temp = String.format("[%s](https://dota2.ru%s)", news.get(i).text(), links.get(i).attr("href"));
            resultNews.append(temp).append("\n").append("----------------------------------").append("\n");
        }
        return resultNews.toString();
    }
}
