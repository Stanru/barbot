package ru.bar.telegram_bar_bot;

import java.security.SecureRandom;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bar.telegram_bar_bot.model.profile.Profile;
import ru.bar.telegram_bar_bot.service.ProfileService;

@Component
@Log4j2
public class scheduleFixedDelayTask {
    public static final SecureRandom secureRandom = new SecureRandom();
    private final Iterator<String> iterator;
    private final ProfileService profileService;
    private final BarBot barBot;
    private final ZonedDateTime startAt;
    private final ZonedDateTime stopAt;

    public scheduleFixedDelayTask(ProfileService profileService, BarBot barBot) {
        this.barBot = barBot;
        this.profileService = profileService;
        LinkedList<String> questions = new LinkedList<>();
        questions.add("Вы когда-нибудь отправляли текстовое сообщение не тому человеку по ошибке?");
        questions.add("Какой возраст вы бы выбрали, если бы могли провести неделю в любом другом возрасте?");
        questions.add("Если бы вы могли путешествовать в любую точку мира, куда бы вы поехали?");
        questions.add("Говорят «Вырастешь, поймешь». Кто что понял, когда вырос?");
        questions.add("Какая ваша любимая начинка для пиццы?");
        questions.add("Если бы вы могли стать бессмертным, что бы вы сделали первым делом?");
        questions.add("Что бы вы выбрали: быть супергероем или суперзлодеем? И почему?");
        questions.add("Как бы вы назвали свою космическую станцию, если бы вы отправились в космос?");
        questions.add("Если бы у вас был шанс создать новый вид животных, какой бы он был?");
        questions.add("Если бы вам пришлось жить в прошлом, в какой период вы бы хотели попасть?");
        questions.add("Если бы можно было телепортироваться в любую точку мира, куда бы вы отправились?");
        questions.add("Если бы вы могли выбрать любой супер-способность, что бы это было?");
        questions.add("Если бы вас попросили выбрать между бессмертием и бесконечным запасом денег, что бы вы выбрали?");
        questions.add("Если бы ваша жизнь была фильмом, какой был бы ваш главный злодей?");
        questions.add("Если бы вы были частью фильма «Матрица», какую роль вы бы сыграли?");
        questions.add("Когда крысе бежать с корабля, если она его капитан?");
        questions.add("Почему в сказке о репке только у собаки есть имя?");
        questions.add("Что мешает плохим танцовщицам?");
        questions.add("Можно ли называть лестницей сломанный эскалатор?");
        questions.add("Можно назвать хлопья с молоком супом?");
        iterator = questions.iterator();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ZZ");
        ZoneId zoneId = ZoneId.of( "Europe/Moscow" );
        this.startAt = ZonedDateTime.parse("2023-09-23 19:00:00 +0300", dateTimeFormatter)
                .toInstant()
                .atZone(zoneId);
        this.stopAt = ZonedDateTime.parse("2023-09-23 22:00:00 +0300", dateTimeFormatter)
                .toInstant()
                .atZone(zoneId);
    }

    @Scheduled(fixedRate = 1000*60*15)
    public void scheduleFixedRateTask() {
        List<Profile> profiles = profileService.getAll();

        if (ZonedDateTime.now().isAfter(startAt) && ZonedDateTime.now().isBefore(stopAt)) {
            if (profiles.size() > 3 && iterator.hasNext()) {
                Long chatId = profiles.get(secureRandom.nextInt(profiles.size())).getTelegramId();
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text("❓ Тут нужно обкашлять вопросики. (Прочитай вслух и ответь, конечно можно подумать)\n\n"
                                + iterator.next())
                        .build();

                try {
                    barBot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    log.error(e);
                }
            }
        }
    }
}
