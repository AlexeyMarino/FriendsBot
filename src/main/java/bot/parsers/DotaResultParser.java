package bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DotaResultParser {
    String dotaResults1 = "https://www.cybersport.ru/base/match?disciplines=21&status=past&page=1";
    String dotaResults2 = "https://www.cybersport.ru/base/match?disciplines=21&status=past&page=2";
    String dotaResults3 = "https://www.cybersport.ru/base/match?disciplines=21&status=past&page=3";
    String[] result = new String[75];
    int number = 0;


    public String[] getNews() {

        getResultPage(dotaResults1);
        getResultPage(dotaResults2);
        getResultPage(dotaResults3);
        return result;
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
        for(int i = 1; i < titles.size(); i++) {
            StringBuilder resultNews = new StringBuilder();
            Element match = titles.get(i);
            resultNews.append("\u2796\u2796\u2796").append(match.select("time").text()).append("\u2796\u2796\u2796").append("\n");
            Elements teams = match.select("span.d--phone-none");
            String winners = match.select("a.matches__link").text();
            String[] winner = winners.split(" : ");
            int first = Integer.parseInt(winner[0]);
            int second = Integer.parseInt(winner[1]);
            String team1 = teams.get(0).text();
            String team2 = teams.get(1).text();
            if(first > second) {
                resultNews.append("*").append(first).append("*").append("   ").append("*").append(team1).append("*").append("\ud83c\udfc5");
                resultNews.append("\n");
                resultNews.append(second).append("   ").append(team2);
                resultNews.append("\n");
                resultNews.append(match.select("a.report").text()).append("\n\n");
            }
            else if (first < second) {
                resultNews.append(first).append("   ").append(team1);
                resultNews.append("\n");
                resultNews.append("*").append(second).append("*").append("   ").append("*").append(team2).append("*").append("\ud83c\udfc5");
                resultNews.append("\n");
                resultNews.append(match.select("a.report").text()).append("\n\n");
            }
            else {
                resultNews.append(first).append("   ").append(team1);
                resultNews.append("\n");
                resultNews.append(second).append("   ").append(team2);
                resultNews.append("\n");
                resultNews.append(match.select("a.report").text()).append("\n\n");
            }


            result[number] = resultNews.toString();
            number++;
        }
    }

}
