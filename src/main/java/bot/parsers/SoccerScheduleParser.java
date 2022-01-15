package bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SoccerScheduleParser implements Parser{
    private String soccerUrl;

    public SoccerScheduleParser(String soccerUrl) {
        this.soccerUrl = soccerUrl;
    }

    public String[] getNews() {
        Document document = null;
        try {
            document = Jsoup.connect(soccerUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> result = new ArrayList<>(150);
        Element schedule = document.getElementById("result_data");
        Elements allSchedule = schedule.select("div.live_comptt_bd:not(.hide)");

        for(int i = 0; i < allSchedule.size(); i++) {
            StringBuilder tourResult = new StringBuilder();
            Element tour = allSchedule.get(i);
            tourResult.append("\u2796\u2796\u2796").append("*").append(tour.getElementsByClass("cmp_stg_ttl").text()).append("*").append("\u2796\u2796\u2796").append("\n");
            Elements gamesTour = tour.select("div.game_block");
            for(int j = 0; j < gamesTour.size(); j++) {
                Element game = gamesTour.get(j);
                tourResult.append("\u26bd\ufe0f").append(game.getElementsByClass("game_link").attr("title")).append("\n");
                tourResult.append("\ud83d\udd53").append(game.getElementsByClass("status").text()).append("\n\n");
            }
            result.add(tourResult.toString());
        }
        return result.toArray(new String[0]);
    }
}
