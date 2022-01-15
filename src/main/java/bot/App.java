package bot;

import bot.services.Receiver;
import bot.services.Sender;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class App {
    private static final Logger log = Logger.getLogger(App.class);

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Bot friendsSpbBot = context.getBean("botBeen", Bot.class);

        Receiver messageReceiver = context.getBean("receiver", Receiver.class);
        Sender messageSender = context.getBean("sender", Sender.class);

        friendsSpbBot.connectApi();

        Thread receiver = new Thread(messageReceiver);
        receiver.setDaemon(true);
        receiver.setName("MsgReceiver");
        receiver.start();

        Thread sender = new Thread(messageSender);
        sender.setDaemon(true);
        sender.setName("MsgSender");
        sender.start();
    }
}
