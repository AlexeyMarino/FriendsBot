package bot.handlers;

import bot.parsers.AplNewsParser;
import bot.parsers.CybersportDotaNewsParser;
import bot.parsers.ChampionatComNewsParser;
import bot.services.UserSender;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class NewsHandler extends MenuHandler {

    public NewsHandler(UserSender sender) {
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
                view = new CybersportDotaNewsParser("https://www.cybersport.ru/dota-2").getNews();
                page = 0;
                lastPage = getLastPage(6);
                viewResultPage(0, lastPage);
                break;
            case "Apex Legends":
                view = new CybersportDotaNewsParser("https://www.cybersport.ru/apex-legends").getNews();
                page = 0;
                lastPage = getLastPage(6);
                viewResultPage(0, lastPage);
                break;
            case "APL":
                view = new AplNewsParser().getNews();
                page = 0;
                lastPage = getLastPage(6);
                viewResultPage(0, lastPage);
                break;

            case "RPL":
                view = new ChampionatComNewsParser("https://www.championat.com/news/football/_russiapl/1.html").getNews();
                page = 0;
                lastPage = getLastPage(6);
                viewResultPage(0, lastPage);
                break;

            case "EurL":
                view = new ChampionatComNewsParser("https://www.championat.com/news/football/_europeleague/1.html").getNews();
                page = 0;
                lastPage = getLastPage(6);
                viewResultPage(0, lastPage);
                break;

            case "ChampL":
                view = new ChampionatComNewsParser("https://www.championat.com/news/football/_ucl/1.html").getNews();
                page = 0;
                lastPage = getLastPage(6);
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

        EditMessageText editMessageText = keyboardService.getEditMessageText(chatId, callbackQuery, pageView.toString());
        editMessageText.enableMarkdown(true);

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
        editMessageText.disableWebPagePreview();
        editMessageText.setReplyMarkup(inlineKeyboardMarkup1);
        try {
            bot.sendQueue.put(editMessageText);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
