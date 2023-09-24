package ru.bar.telegram_bar_bot.bot.handler;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bar.telegram_bar_bot.bot.ChatState;
import ru.bar.telegram_bar_bot.service.ProfileService;

@Component
@Setter
@RequiredArgsConstructor
@Log4j2
public class StatisticHandler {
    private SilentSender sender;
    private Map<Long, ChatState> chatStateMap;
    private final ProfileService profileService;

    public void handler(long chatId) {
        log.info("Ability statistic replay");

        if (!chatStateMap.containsKey(chatId) || !ChatState.READY.equals(chatStateMap.get(chatId))) {
            log.info("Ability statistic replay - profile not ready");
            ChatState chatState = chatStateMap.get(chatId);
            String status;

            if (chatState == null) {
                status = " просто нажми /start";
            } else {
                status = chatState.getText();
            }

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Сори, но нужно заполнить профиль, сейчас " + status)
                    .build();

            sender.execute(sendMessage);
            return;
        } else {
            String message = "Как же я люблю БТС, вот они слева направо:\n"
                    + String.join("\n", profileService.drunkestRate());
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(message)
                    .build();

            sender.execute(sendMessage);
        }
    }
}
