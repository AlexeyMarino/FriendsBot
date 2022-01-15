package bot.services;

import bot.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.util.HashMap;

@Component
public class Receiver implements Runnable{

    public Receiver() {

    }
    @Autowired
    private Bot bot;
    public HashMap<Long, UserSender> userMap = new HashMap<>();



    @Override
    public void run() {
        while (true) {
            Update update = null;
            try {
                update = (Update) bot.receiveQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(update != null && update.hasMessage()) {
                Long chatId = update.getMessage().getChatId();
                if(userMap.containsKey(chatId)) {
                    userMap.get(chatId).operate(update);
                }
                else {
                    userMap.put(chatId, new UserSender(chatId, bot));
                    userMap.get(chatId).operate(update);
                    System.out.println(chatId);
                }
            }

            if(update != null && update.hasCallbackQuery()) {
                    if(!userMap.isEmpty()) {
                        userMap.get(update.getCallbackQuery().getMessage().getChatId()).operate(update);
                    }
            }
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
