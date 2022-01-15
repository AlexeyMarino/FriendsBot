package bot.handlers;


import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {

        public void operate(Update update);

}
