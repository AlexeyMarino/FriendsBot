package bot.services;


import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class KeyboardService {
    public InlineKeyboardButton getButton(String text, String callBack) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callBack);
        return inlineKeyboardButton;
    }
    public EditMessageText getEditMessageText(Long chatId, CallbackQuery callbackQuery, String newText) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(newText);
        return editMessageText;
    }


}
