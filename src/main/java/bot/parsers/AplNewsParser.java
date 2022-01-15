package bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class AplNewsParser {
    String newsURL1 = "http://fapl.ru/";
    String newsURL2 = "http://fapl.ru/news/?skip=20";
    String newsURL3 = "http://fapl.ru/news/?skip=40";
    String[] result = new String[60];
    int number = 0;

    public String[] getNews() {
        getResultPage(newsURL1);
        getResultPage(newsURL2);
        getResultPage(newsURL3);

        return result;
    }

    public void getResultPage(String link){
        Document document = null;
        try {
            document = Jsoup.connect(link)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements news = document.select("h3");
        Elements content = document.select("div.content");
        String temp = "";
        for (int i = 0; i < 20; i++) {
            StringBuilder resultNews = new StringBuilder();
            resultNews.append("\u26bd\ufe0f ");
            Element titles = news.get(i);
            Elements links = titles.select("a");
            temp = String.format("[%s](http://fapl.ru%s)", titles.text(), links.attr("href"));
            resultNews.append(temp).append("\n");
            resultNews.append(content.get(i).text()).append("\n\n");
            result[number] = resultNews.toString();
            number++;
        }
    }
}
