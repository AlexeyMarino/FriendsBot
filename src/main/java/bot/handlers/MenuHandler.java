package bot.handlers;

import bot.Bot;
import bot.services.KeyboardService;
import bot.services.UserSender;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public abstract class MenuHandler implements Handler{
    UserSender sender;
    Long chatId;
    Bot bot;
    KeyboardService keyboardService = new KeyboardService();
    String[] view;
    CallbackQuery callbackQuery;

    int page;
    int lastPage;
    int countOnPage;
    int countOnLastPage;

    public MenuHandler(UserSender sender) {
        this.sender = sender;
        this.chatId = sender.getChatId();
        this.bot = sender.getBot();
    }

    public abstract void operate(Update update);

    public void setSoccerMenu() {
        EditMessageText editMessageText = keyboardService.getEditMessageText(chatId, callbackQuery, "Выберите соревнование");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();

        keyboardButtonsRow1.add(keyboardService.getButton("\ud83c\uddec\ud83c\uddec Английская-Премьер Лига", "APL"));
        keyboardButtonsRow2.add(keyboardService.getButton("\ud83c\uddf7\ud83c\uddfa Российская-Премьер Лига", "RPL"));
        keyboardButtonsRow3.add(keyboardService.getButton("\ud83c\uddea\ud83c\uddfa Лига Европы", "EurL"));
        keyboardButtonsRow4.add(keyboardService.getButton("\ud83c\udfc6 Лига Чемпионов", "ChampL"));

        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);
        inlineKeyboardMarkup.setKeyboard(rowList);

        editMessageText.setReplyMarkup(inlineKeyboardMarkup);

        try {
            bot.sendQueue.put(editMessageText);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public int getLastPage(int countOnPage) {
        int allView = view.length;
        this.countOnPage = countOnPage;
        countOnLastPage = allView % countOnPage;
        if(countOnLastPage == 0) {
            return allView / countOnPage - 1;
        }
        else return allView / countOnPage;

    }

    public StringBuilder getPage(int s) {
            int l = s * countOnPage + countOnPage;
            if (s == lastPage && countOnLastPage != 0) {
                l = s * countOnPage + countOnLastPage;
            }
            StringBuilder page = new StringBuilder();
            for (int i = s * countOnPage; i < l; i++) {
                page.append(view[i]);
            }
            return page;
    }


}
