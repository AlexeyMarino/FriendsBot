package bot.services;

import bot.Bot;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class Sender implements Runnable {
    private static final Logger log = Logger.getLogger(Sender.class);

    @Autowired
    private Bot bot;



    @Override
    public void run() {
        log.info("[STARTED] MsgSender.  Bot class: " + bot);
            while (true) {
                Object object = null;
                try {
                    object = bot.sendQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("Get new msg to send " + object);
                send(object);
            }
    }

    private void send(Object object) {
        try {
            MessageType messageType = messageType(object);
            switch (messageType) {
                case EXECUTE:
                    BotApiMethod<Message> message = (BotApiMethod<Message>) object;
                    log.debug("Use Execute for " + object);
                    Message message1 = bot.execute(message);
                    bot.allSendMessage.add(message1);
                    break;
                case STICKER:
                    SendSticker sendSticker = (SendSticker) object;
                    log.debug("Use SendSticker for " + object);
                    bot.executeAsync(sendSticker);
                    break;
                default:
                    log.warn("Cant detect type of object. " + object);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private MessageType messageType(Object object) {
        if (object instanceof SendSticker) return MessageType.STICKER;
        if (object instanceof BotApiMethod) return MessageType.EXECUTE;
        return MessageType.NOT_DETECTED;
    }

    enum MessageType {
        EXECUTE, STICKER, NOT_DETECTED,
    }




}
