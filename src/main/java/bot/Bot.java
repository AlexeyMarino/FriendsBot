package bot;

import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

@Component("botBeen")
public class Bot extends TelegramLongPollingBot {
    private static final Logger log = Logger.getLogger(Bot.class);


    String userName = "FriendsSpbBot";

    public ArrayList<Message> allSendMessage = new ArrayList<>();
    public ArrayBlockingQueue<Object> receiveQueue = new ArrayBlockingQueue<Object>(100);
    public ArrayBlockingQueue<Object> sendQueue = new ArrayBlockingQueue<Object>(100);

    public void connectApi() {
        try {
            TelegramBotsApi botsApi =
                    new TelegramBotsApi(
                            DefaultBotSession.class);
            botsApi.registerBot(this);
            log.info("TelegramAPI started. Look for messages");
        } catch (TelegramApiException e) {
            log.error("Cant Connect. Please try again. Error: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Receive new Update. updateID: " + update.getUpdateId());
        try {
            receiveQueue.put(update);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(receiveQueue.size());
    }


    public String getBotUsername() {
        return userName;
    }

    public String getBotToken() {
        Properties prop = new Properties();
        try {
            //load a properties file from class path, inside static method
            prop.load(App.class.getClassLoader().getResourceAsStream("config.properties"));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop.getProperty("token");
    }
}