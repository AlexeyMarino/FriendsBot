package bot.services;

import bot.Bot;
import bot.handlers.MenuHandler;
import bot.handlers.NewsHandler;
import bot.handlers.ResultHandler;
import bot.handlers.ScheduleHandler;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class UserSender {
    Bot bot;
    Long chatId;
    Message deleteMessage;
    MenuHandler handler;

    public UserSender(Long chatId, Bot bot) {
        this.chatId = chatId;
        this.bot=bot;
    }

    void operate(Update update) {
        if(!update.hasCallbackQuery()) {
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                Long chatId = message.getChatId();
                switch (message.getText()) {
                    case "/start":
                        try {
                            SendMessage sendMessage = setMenu();
                            sendMessage.setChatId(String.valueOf(chatId));
                            sendMessage.setText("Привет! Этот бот покажет тебе расписание и результаты футбольных матчей!" +
                                    " Воспользуйся кнопочным меню или специальными командами, для этого начни вводить их с символа \"/\".");
                            bot.execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "📅 Расписание":
                        deleteAllSendMessage();
                        handler = new ScheduleHandler(this);
                        createCallBack(chatId);
                        deleteMessage = message;
                        break;

                    case "\ud83c\udfaf Результаты":
                        deleteAllSendMessage();
                        handler = new ResultHandler(this);
                        createCallBack(chatId);
                        deleteMessage = message;
                        break;
                    case "\ud83d\udce2 Новости":
                        deleteAllSendMessage();
                        handler = new NewsHandler(this);
                        createCallBack(chatId);
                        deleteMessage = message;
                        break;
                    case "\ud83e\udd1d Обратная связь":
                        deleteAllSendMessage();
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(String.valueOf(chatId));
                        sendMessage.setText("С вопросами, пожеланиями, информацией об ошибках просьба обращаться к @MarinoSpb");
                        try {
                            bot.sendQueue.put(sendMessage);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        deleteMessage = message;
                        break;
                    default:
                }
            }
        }
        else if (update.hasCallbackQuery()) {
            handler.operate(update);
        }
    }


    private void createMsg(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.disableWebPagePreview();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);
        try {
            bot.sendQueue.put(sendMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createCallBack(Long chatId) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("\u26bd\ufe0f Футбол");
        inlineKeyboardButton.setCallbackData("Футбол");

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("\u2694\ufe0f Dota 2");
        inlineKeyboardButton2.setCallbackData("Dota 2");

        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText("\ud83d\udd2b Apex Legends");
        inlineKeyboardButton3.setCallbackData("Apex Legends");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        keyboardButtonsRow3.add(inlineKeyboardButton3);

        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Выберите вид спорта");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            bot.sendQueue.put(sendMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private SendMessage setMenu() {
        SendMessage sendMessage = new SendMessage();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();
        firstRow.add(new KeyboardButton("\ud83d\udcc5 Расписание"));
        firstRow.add(new KeyboardButton("\ud83c\udfaf Результаты"));
        secondRow.add(new KeyboardButton("\ud83d\udce2 Новости"));
        secondRow.add(new KeyboardButton("\ud83e\udd1d Обратная связь"));
        keyboardRowList.add(firstRow);
        keyboardRowList.add(secondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return sendMessage;
    }

    // метод который удаляет все предыдущие сообщения отправленные ботом (используется при выборе одного из пунктов основоного меню)
    private void deleteAllSendMessage() {
        if(bot.allSendMessage != null) {
            for (int i = 0; i < bot.allSendMessage.size(); i++) {
                Message message = bot.allSendMessage.get(i);
                if(message.getChatId().equals(chatId)) {
                    DeleteMessage deleteMessage = new DeleteMessage();
                    deleteMessage.setChatId(String.valueOf(chatId));
                    deleteMessage.setMessageId(message.getMessageId());
                    try {
                        bot.sendQueue.put(deleteMessage);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // bot.allSendMessage.remove(i);
                }
            }
        }
        if(deleteMessage != null) {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(chatId));
            deleteMessage.setMessageId(this.deleteMessage.getMessageId());
            try {
                bot.sendQueue.put(deleteMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public Bot getBot() {
        return bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Message getDeleteMessage() {
        return deleteMessage;
    }

    public void setDeleteMessage(Message deleteMessage) {
        this.deleteMessage = deleteMessage;
    }

    public MenuHandler getHandler() {
        return handler;
    }

    public void setHandler(MenuHandler handler) {
        this.handler = handler;
    }
}
