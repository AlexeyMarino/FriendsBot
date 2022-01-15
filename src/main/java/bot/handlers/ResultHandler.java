package bot.handlers;

import bot.parsers.ApexResultParser;
import bot.parsers.DotaResultParser;
import bot.parsers.SoccerResultParser;
import bot.services.UserSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


import java.util.ArrayList;
import java.util.List;


public class ResultHandler extends MenuHandler {

    public ResultHandler(UserSender sender) {
        super(sender);
    }

    @Override
    public void operate(Update update) {
        callbackQuery = update.getCallbackQuery();
        switch (callbackQuery.getData()) {
            case "Футбол":
                setSoccerMenu();
                break;
            case "Dota 2":
                view = new DotaResultParser().getNews();
                page = 0;
                lastPage = getLastPage(5);
                viewResultPage(0, lastPage);
                break;
            case "APL":
                view = new SoccerResultParser("https://soccer365.ru/competitions/12/2020-2021/results/").getNews();
                if(view[0] != null) {
                    page = 0;
                    lastPage = getLastPage(5);
                    viewResultPage(0, lastPage);
                }
                else {
                    viewNoResult();
                }
                break;

            case "RPL":
                view = new SoccerResultParser("https://soccer365.ru/competitions/13/results/").getNews();
                page = 0;
                lastPage = getLastPage(5);
                viewResultPage(0, lastPage);
                break;

            case "EurL":
                view = new SoccerResultParser("https://soccer365.ru/competitions/20/results/").getNews();
                if(view[0] != null) {
                    page = 0;
                    lastPage = getLastPage(5);
                    viewResultPage(0, lastPage);
                }
                else {
                    viewNoResult();
                }
                break;

            case "ChampL":
                view = new SoccerResultParser("https://soccer365.ru/competitions/19/results/").getNews();
                page = 0;
                lastPage = getLastPage(5);
                viewResultPage(0, lastPage);
                break;
            case "Apex Legends":
                view = new ApexResultParser().getNews();
                page = 0;
                lastPage = getLastPage(5);
                viewResultPage(0, lastPage);

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

        editAPL1page.disableWebPagePreview();
        editAPL1page.setReplyMarkup(inlineKeyboardMarkup1);
        try {
            bot.sendQueue.put(editAPL1page);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void viewNoResult() {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText("Информация отсутствует, возможно соревнование еще не началось");
            bot.sendQueue.put(sendMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
