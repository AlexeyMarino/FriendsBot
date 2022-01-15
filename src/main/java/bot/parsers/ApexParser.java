package bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ApexParser implements Parser{

    public String[] getNews() {
        Document document = null;
        try {
            String dotaURL = "https://www.playground.ru/apex_legends/news";
            document = Jsoup.connect(dotaURL)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements titles = document.select("article.post"); // div.post-title > a:not([class])
        Elements time = titles.select("time");
        Elements links = titles.select("div.post-title > a:not([class])");
        StringBuilder resultNews = new StringBuilder();
        String temp;

        for(int i = 0; i < 15; i++) {
            String times = time.get(i).attr("datetime");
            String[] parts = times.split("[\\s\\-:\\+T]");
            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.YEAR, Integer.parseInt(parts[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(parts[1])-1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[3]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(parts[4]));
            calendar.set(Calendar.SECOND, Integer.parseInt(parts[5]));
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm MM.dd.yyyy");
            String formatted = format1.format(calendar.getTime());
            temp = String.format("[%s](%s)\n%s", links.get(i).text(), links.get(i).attr("href"), formatted);
            resultNews.append(temp).append("\n").append("----------------------------------").append("\n");
        }
        return new String[]{resultNews.toString()};
    }
}
