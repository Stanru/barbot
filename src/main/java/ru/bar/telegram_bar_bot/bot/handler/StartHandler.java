package ru.bar.telegram_bar_bot.bot.handler;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bar.telegram_bar_bot.bot.ChatState;
import ru.bar.telegram_bar_bot.model.profile.Gender;
import ru.bar.telegram_bar_bot.model.profile.Profile;
import ru.bar.telegram_bar_bot.service.ProfileService;

@Component
@RequiredArgsConstructor
@Setter
@Log4j2
public class StartHandler {
    public static final String COCKTAILS_BUTTON_DATA = "cocktails";
    public static final String SOFT_DRINKS_BUTTON_DATA = "soft_drinks";
    public static final String START_GENDER = "start-gender:";
    private SilentSender sender;
    private Map<Long, ChatState> chatStateMap;
    private final ProfileService profileService;

    public void textHandler(Update update) {
        log.info("Ability text start replay");
        if (update.hasCallbackQuery()) {
            return;
        }

        Long chatId = update.getMessage().getChatId();
        Long userTelegramId = update.getMessage().getFrom().getId();

        if (!chatStateMap.containsKey(chatId)) {
            Profile profile = profileService.createOrGetProfile(chatId,
                    update.getMessage().getFrom(),
                    chatStateMap);
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(profile.getName() + ", рады видеть тебя здесь =)")
                    .build();

            sender.execute(sendMessage);

            sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Какой у тебя вес? Только честно")
                    .build();

            sender.execute(sendMessage);

            return;
        }

        if (chatStateMap.containsKey(chatId) && !ChatState.READY.equals(chatStateMap.get(chatId))) {
            log.info("User not end registration current status - {}", chatStateMap.get(chatId));

            if (ChatState.WAIT_WEIGHT.equals(chatStateMap.get(chatId))) {
                String message = update.getMessage().getText();

                if (Pattern.matches("^\\d{2}$", message)) {
                    int bodyWeight = Integer.parseInt(message);

                    if (bodyWeight < 40 || bodyWeight > 110) {
                        SendMessage sendMessage = SendMessage.builder()
                                .chatId(chatId)
                                .text("Ну давай серьезно, я жду реальный вес")
                                .build();

                        sender.execute(sendMessage);
                        return;
                    }

                    if (profileService.updateBodyWeight(userTelegramId, bodyWeight)) {
                        log.info("Set chat status:{}", ChatState.WAIT_GENDER);
                        chatStateMap.put(chatId, ChatState.WAIT_GENDER);
                        SendMessage sendMessage = SendMessage.builder()
                                .chatId(chatId)
                                .text("Кто ты, воин?")
                                .replyMarkup(getGenderKeyboard())
                                .build();

                        sender.execute(sendMessage);
                    } else {
                        log.info("Не смог найти пользователя с telegramId:{}", userTelegramId);
                        SendMessage sendMessage = SendMessage.builder()
                                .chatId(chatId)
                                .text("Не сомг тебе найти, попробуй запустить /start")
                                .build();

                        sender.execute(sendMessage);
                    }
                } else {
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("Ну давай серьезно, я жду реальный вес")
                            .build();

                    sender.execute(sendMessage);
                }
            } else if (ChatState.WAIT_GENDER.equals(chatStateMap.get(chatId))) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text("Спрошу еще раз... Кто ты, воин?")
                        .replyMarkup(getGenderKeyboard())
                        .build();

                sender.execute(sendMessage);
            }
        }
    }

    public void callbackRequestHandler(Update update) {
        if (!update.hasCallbackQuery()) {
            return;
        }

        log.info("Ability callback request start replay");
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String data = update.getCallbackQuery().getData();
        Long userTelegramId = update.getCallbackQuery().getFrom().getId();

        if (ChatState.WAIT_GENDER.equals(chatStateMap.get(chatId))) {
            if (StringUtils.isNotBlank(data) && data.contains(START_GENDER)) {
                String genderName = data.replace(START_GENDER, "");

                if (profileService.updateGender(userTelegramId, genderName)) {
                    log.info("Set chat status:{}", ChatState.READY);
                    chatStateMap.put(chatId, ChatState.READY);
                    Profile profile = profileService.getByTelegramId(userTelegramId).get();
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text(profile.getName() + ", что будешь пить?")
                            .replyMarkup(getDrinkKeyboard())
                            .build();
                    sender.execute(sendMessage);
                } else {
                    log.info("Не смог найти пользователя с telegramId:{}", userTelegramId);
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("Не сомг тебе найти, попробуй запустить /start")
                            .build();

                    sender.execute(sendMessage);
                }
            }

            return;
        }

        if (ChatState.READY.equals(chatStateMap.get(chatId))) {
            if (StringUtils.isNotBlank(data)) {
                if (data.contains(START_GENDER)) {
                    String genderName = data.replace(START_GENDER, "");
                    String message = switch (Gender.valueOf(genderName)) {
                        case MALE -> "Я понял, ты мужик, мужик, ок...";
                        case FEMALE -> "Эх женщины...";
                    };

                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text(message)
                            .build();

                    sender.execute(sendMessage);
                }
            }
        }
    }

    private ReplyKeyboard getDrinkKeyboard() {
        InlineKeyboardButton cocktailsButton = InlineKeyboardButton.builder()
                .text("Алкоголь")
                .callbackData(COCKTAILS_BUTTON_DATA)
                .build();

        InlineKeyboardButton softDrinksButton = InlineKeyboardButton.builder()
                .text("Просто напитки")
                .callbackData(SOFT_DRINKS_BUTTON_DATA)
                .build();

        return new InlineKeyboardMarkup(List.of(List.of(cocktailsButton, softDrinksButton)));
    }

    private ReplyKeyboard getGenderKeyboard() {
        InlineKeyboardButton cocktailsButton = InlineKeyboardButton.builder()
                .text(Gender.MALE.getText())
                .callbackData(START_GENDER + Gender.MALE.name())
                .build();

        InlineKeyboardButton softDrinksButton = InlineKeyboardButton.builder()
                .text(Gender.FEMALE.getText())
                .callbackData(START_GENDER + Gender.FEMALE.name())
                .build();

        return new InlineKeyboardMarkup(List.of(List.of(cocktailsButton, softDrinksButton)));
    }
}
