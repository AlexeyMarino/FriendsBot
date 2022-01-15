package bot.handlers;

import bot.parsers.DotaScheduleParser;
import bot.parsers.SoccerScheduleParser;
import bot.services.UserSender;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


import java.util.ArrayList;
import java.util.List;

public class ScheduleHandler extends MenuHandler{

    public ScheduleHandler(UserSender sender) {
        super(sender);
    }

    @Override
    public void operate(Update update) {
        callbackQuery = update.getCallbackQuery();
        switch (callbackQuery.getData()) {
            case "Футбол":
                setSoccerMenu();
                break;
            case "APL":
                view = new SoccerScheduleParser("https://soccer365.ru/competitions/12/shedule/").getNews();
                page = 0;
                lastPage = getLastPage(1);
                viewResultPage(0, lastPage);
                break;

            case "RPL":
                view = new SoccerScheduleParser("https://soccer365.ru/competitions/13/shedule/").getNews();
                page = 0;
                lastPage = getLastPage(1);
                viewResultPage(0, lastPage);
                break;

            case "EurL":
                view = new SoccerScheduleParser("https://soccer365.ru/competitions/20/shedule/").getNews();
                page = 0;
                lastPage = getLastPage(1);
                viewResultPage(0, lastPage);
                break;

            case "ChampL":
                view = new SoccerScheduleParser("https://soccer365.ru/competitions/19/shedule/").getNews();
                page = 0;
                lastPage = getLastPage(1);
                viewResultPage(0, lastPage);
                break;
            case "Dota 2":
                view = new DotaScheduleParser().getNews();
                page = 0;
                lastPage = getLastPage(5);
                viewResultPage(0, lastPage);
                break;
            case "Apex Legends":
                EditMessageText editAPL1page = keyboardService.getEditMessageText(chatId, callbackQuery, "Информация о запланированных матчах отсутствует");
                editAPL1page.enableMarkdown(true);

                try {
                    bot.sendQueue.put(editAPL1page);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case "вперед":
                page++;
                viewResultPage(page, lastPage);
                break;

            case "назад":
                page--;
                viewResultPage(page, lastPage);
                break;

        }
    }



    public void viewResultPage(int page, int lastPage) {
        StringBuilder pageView = getPage(page);

        EditMessageText editAPL1page = keyboardService.getEditMessageText(chatId, callbackQuery, pageView.toString());
        editAPL1page.enableMarkdown(true);

        InlineKeyboardMarkup inlineKeyboardMarkup1 = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();

        if(page == 0) {
            keyboardButtonsRow.add(keyboardService.getButton("вперед ➡", "вперед"));

        }

        else if (page == lastPage) {
            keyboardButtonsRow.add(keyboardService.getButton("назад \u2b05\ufe0f", "назад"));
        }
        else {
            keyboardButtonsRow.add(keyboardService.getButton("вперед ➡", "вперед"));
            keyboardButtonsRow.add(keyboardService.getButton("назад \u2b05\ufe0f", "назад"));
        }
        List<List<InlineKeyboardButton>> rowList1 = new ArrayList<>();
        rowList1.add(keyboardButtonsRow);
        inlineKeyboardMarkup1.setKeyboard(rowList1);

        editAPL1page.setReplyMarkup(inlineKeyboardMarkup1);
        try {
            bot.sendQueue.put(editAPL1page);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
