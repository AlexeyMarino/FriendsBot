package bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SoccerResultParser {
    String soccerUrl;

    public SoccerResultParser(String soccerUrl) {
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

        Elements titles = document.select("div.game_block > a");

        String[] result = new String[20];
        if (titles != null && !titles.isEmpty()) {
            int a = 20;
            if(titles.size() < 20) {
                a = titles.size();
            }
            for (int i = 0; i < a; i++) {
                StringBuilder online = new StringBuilder();
                Element game = titles.get(i);
                online.append("⚽ ").append("*").append(game.attr("title")).append("*").append("\n");
                String m = game.select("div.status").text();
                if(m.length() < 5) {
                    m = "идет " + m + " матча";
                }
                online.append("   ").append(m).append("\n");
                Elements goals = game.select("div.gls");
                String scoreLeft = goals.get(0).text();
                String scoreRight = goals.get(1).text();
                online.append("   ").append(scoreLeft).append(" - ").append(scoreRight);
                StringBuilder eventsMatch = new StringBuilder();
                if(Integer.parseInt(scoreLeft) > 0 || Integer.parseInt(scoreRight) > 0) {
                    String href = "https://soccer365.ru" + game.attr("href");
                    Document match = null;
                    try {
                        match = Jsoup.connect(href)
                                .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                                .get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Elements events = match.select("div.event_ht");
                    for(int j = 0; j < events.size(); j++) {

                        Element event = events.get(j).parent();
                        if(event.getElementsByClass("live_goal").hasClass("live_goal")) {
                            String eventInfo = "\n" + "   " + event.select("div.event_min").text() + " " + event.select("a").text();
                            eventsMatch.append(eventInfo);
                        }
                        if(event.getElementsByClass("live_owngoal").hasClass("live_owngoal")) {
                            String eventInfo = "\n" + "   " +  event.select("div.event_min").text() + " " + event.select("a").text() + "(автогол)";
                            eventsMatch.append(eventInfo);
                        }
                        if(event.getElementsByClass("live_pengoal").hasClass("live_pengoal")) {
                            String eventInfo = "\n" + "   " + event.select("div.event_min").text() + " " + event.select("a").text() + "(пенальти)";
                            eventsMatch.append(eventInfo);
                        }
                    }
                }
                online.append(eventsMatch).append("\n\n");
                result[i] = online.toString();
            }
        }

        return result;
    }
}
