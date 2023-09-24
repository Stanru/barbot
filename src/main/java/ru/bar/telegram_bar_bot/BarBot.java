package ru.bar.telegram_bar_bot;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static ru.bar.telegram_bar_bot.bot.handler.StartHandler.COCKTAILS_BUTTON_DATA;
import static ru.bar.telegram_bar_bot.bot.handler.StartHandler.SOFT_DRINKS_BUTTON_DATA;
import static ru.bar.telegram_bar_bot.bot.handler.StartHandler.START_GENDER;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bar.telegram_bar_bot.bot.ChatState;
import ru.bar.telegram_bar_bot.bot.handler.CocktailHandler;
import ru.bar.telegram_bar_bot.bot.handler.StartHandler;
import ru.bar.telegram_bar_bot.bot.handler.StatisticHandler;

@Component
@Log4j2
public class BarBot extends AbilityBot {
    private final StartHandler startHandler;
    private final CocktailHandler cocktailHandler;
    private final StatisticHandler statisticHandler;

    public BarBot(@Value("${app.telegram-bot.token}") String botToken,
                  @Value("${app.telegram-bot.name}") String botName,
                  StartHandler startHandler,
                  CocktailHandler cocktailHandler,
                  StatisticHandler statisticHandler) {
        super(botToken, botName);
        this.startHandler = startHandler;
        this.cocktailHandler = cocktailHandler;
        this.statisticHandler = statisticHandler;
        init();
    }

    @SneakyThrows
    public void init() {
        List<BotCommand> LIST_OF_COMMAND = List.of(
            new BotCommand("/start", "Запуск бота"),
            new BotCommand("/stat", "Статистика"),
            new BotCommand("/cocktails", "Список алкоголя"),
            new BotCommand("/soft_drinks", "Список напитков")
        );

        this.execute(new SetMyCommands(LIST_OF_COMMAND, new BotCommandScopeDefault(), null));
        Map<Long, ChatState> countMap = db.getMap("CHAT_STATE");
        startHandler.setChatStateMap(countMap);
        cocktailHandler.setChatStateMap(countMap);
        statisticHandler.setChatStateMap(countMap);
        startHandler.setSender(silent);
        cocktailHandler.setSender(silent);
        statisticHandler.setSender(silent);
    }

    @Override
    public long creatorId() {
        return 110437153L;
    }

    public Ability getAlcoholDrinks() {
        return Ability
                .builder()
                .name("cocktails")
                .info("Список коктейлей")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> cocktailHandler.selectCocktails(ctx.chatId()))
                .reply(cocktailHandler::handler, this::answerCallbackRequest)
                .build();
    }

    public Ability getSoftDrinks() {
        return Ability
                .builder()
                .name("soft_drinks")
                .info("Список напитков")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> cocktailHandler.selectSoftDrinks(ctx.chatId()))
                .build();
    }

    public Ability getStatistic() {
        return Ability
                .builder()
                .name("stat")
                .info("Статистика")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> statisticHandler.handler(ctx.chatId()))
                .build();
    }

    public Reply mainTextHandler() {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) -> startHandler.textHandler(upd);
        return Reply.of(action, Flag.TEXT, upd -> {
            if (upd.hasMessage() && StringUtils.isNotBlank(upd.getMessage().getText())) {
                return switch (upd.getMessage().getText()) {
                    case "/stat" -> false;
                    case "/cocktails" -> false;
                    case "/soft_drinks" -> false;
                    default -> true;
                };
            }

            return true;
        });
    }

    public Reply mainCallbackRequestHandler() {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, upd) -> startHandler.callbackRequestHandler(upd);

        return Reply.of(action, Flag.CALLBACK_QUERY, this::answerCallbackRequest);
    }

    public boolean answerCallbackRequest(Update update) {
        log.info("Main auto answer for callback request");
        if (!update.hasCallbackQuery() || StringUtils.isBlank(update.getCallbackQuery().getData())) {
            return false;
        }

        AnswerCallbackQuery answerCallbackQuery = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId())
                .build();

        try {
            sender.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            log.error("Error send answer", e);
        }

        return true;
    }
}
