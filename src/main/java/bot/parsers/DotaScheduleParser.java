package bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class DotaScheduleParser {
    String dotaURL = "https://www.cybersport.ru/base/match?disciplines=21&status=future&page=";
    ArrayList<String> tempList = new ArrayList<>();


    public String[] getNews() {

        Document document = null;
        try {
            document = Jsoup.connect(dotaURL+1)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements pagination = document.select("a.pagination__item");
        getPageContent(document);
        if(pagination != null && pagination.size() > 2) {
            int pagesCount = Integer.parseInt(pagination.get(pagination.size()-2).text());

            for(int i = 2; i <= pagesCount; i++) {
                try {
                    document = Jsoup.connect(dotaURL+i)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                            .get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getPageContent(document);
            }
        }



        return tempList.toArray(new String[0]);
    }

    private void getPageContent(Document document) {
        Elements titles = document.select("article.matche");
        for (int i = 1; i < titles.size(); i++) {
            StringBuilder resultNews = new StringBuilder();
            Element match = titles.get(i);
            resultNews.append("\u2796\u2796\u2796").append(match.select("time").text()).append("\u2796\u2796\u2796").append("\n");
            Elements teams = match.select("span.d--phone-none");
            String team1 = teams.get(0).text();
            String team2 = teams.get(1).text();
            resultNews.append(team1);
            resultNews.append("\n");
            resultNews.append("\ud83c\udd9a");
            resultNews.append("\n");
            resultNews.append(team2);
            resultNews.append("\n");
            resultNews.append(match.select("a.report").text()).append("\n\n");

            tempList.add(resultNews.toString());
        }
    }
}
