package bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ChampionatComNewsParser implements Parser{
    private String link;
    private ArrayList<String> result = new ArrayList<>();

    public ChampionatComNewsParser(String link) {
        this.link = link;
    }

    public String[] getNews() {
        Document document = null;
        try {
            document = Jsoup.connect(link)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements news = document.select("a.news-item__title");
        String temp = "";
        for (int i = 0; i < news.size(); i++) {
            StringBuilder resultNews = new StringBuilder();
            resultNews.append("\u26bd\ufe0f ");
            Element titles = news.get(i);
            temp = String.format("[%s](https://www.championat.com%s)", titles.text(), titles.attr("href"));
            resultNews.append(temp).append("\n\n");
            result.add(resultNews.toString());
        }

        return result.toArray(new String[0]);
    }
}


