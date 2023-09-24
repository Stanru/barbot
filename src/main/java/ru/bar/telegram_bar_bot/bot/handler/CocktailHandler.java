package ru.bar.telegram_bar_bot.bot.handler;

import static ru.bar.telegram_bar_bot.bot.handler.CocktailData.COCKTAIL_DATA;
import static ru.bar.telegram_bar_bot.bot.handler.CocktailData.COCKTAIL_DELETE_DATA;
import static ru.bar.telegram_bar_bot.bot.handler.CocktailData.COCKTAIL_ORDER_CANCEL_DATA;
import static ru.bar.telegram_bar_bot.bot.handler.CocktailData.COCKTAIL_ORDER_DATA;
import static ru.bar.telegram_bar_bot.bot.handler.CocktailData.COCKTAIL_ORDER_READY_DATA;
import static ru.bar.telegram_bar_bot.bot.handler.StartHandler.COCKTAILS_BUTTON_DATA;
import static ru.bar.telegram_bar_bot.bot.handler.StartHandler.SOFT_DRINKS_BUTTON_DATA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bar.telegram_bar_bot.bot.ChatState;
import ru.bar.telegram_bar_bot.model.profile.Order;
import ru.bar.telegram_bar_bot.model.profile.Profile;
import ru.bar.telegram_bar_bot.model.recipe.Recipe;
import ru.bar.telegram_bar_bot.service.IngredientService;
import ru.bar.telegram_bar_bot.service.OrderService;
import ru.bar.telegram_bar_bot.service.ProfileService;
import ru.bar.telegram_bar_bot.service.RecipeService;

@Component
@Setter
@RequiredArgsConstructor
@Log4j2
public class CocktailHandler {
    private SilentSender sender;
    private Map<Long, ChatState> chatStateMap;
    private final RecipeService recipeService;
    private final ProfileService profileService;
    private final OrderService orderService;
    private final IngredientService ingredientService;

    public void handler(BaseAbilityBot abilityBot, Update update) {
        log.info("Ability cocktails replay");
        String data = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        if (!update.hasCallbackQuery()) {
            return;
        }

        if ((!chatStateMap.containsKey(chatId) || !ChatState.READY.equals(chatStateMap.get(chatId))
                && !update.hasCallbackQuery())) {
            log.info("Ability cocktails replay - profile not ready");

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
        }

        if (data.equalsIgnoreCase(COCKTAILS_BUTTON_DATA)) {
            selectCocktails(chatId);
        }

        if (data.equalsIgnoreCase(SOFT_DRINKS_BUTTON_DATA)) {
            selectSoftDrinks(chatId);
        }

        if (isValidData(update, CocktailData.COCKTAIL_DATA)) {
            getRecipe(chatId, data);
            return;
        }

        if (isValidData(update, CocktailData.COCKTAIL_ORDER_DATA)) {
            Long userTelegramId = update.getCallbackQuery().getFrom().getId();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            createOrder(chatId, data, userTelegramId);
            deleteLastRecipeMessage(chatId, messageId);
            return;
        }

        if (isValidData(update, COCKTAIL_ORDER_READY_DATA)) {
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            orderReady(chatId, data);
            deleteLastRecipeMessage(chatId, messageId);
            return;
        }

        if (isValidData(update, COCKTAIL_ORDER_CANCEL_DATA)) {
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            orderCancel(chatId, data);
            deleteLastRecipeMessage(chatId, messageId);
            return;
        }

        if (isValidData(update, CocktailData.COCKTAIL_DELETE_DATA)) {
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            deleteLastRecipeMessage(chatId, messageId);
        }
    }

    public void orderCancel(Long chatId, String rawOrderId) {
        long orderId = parseRecipeId(rawOrderId);

        Optional<Order> orderOptional = orderService.getById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setApproved(true);
            orderService.update(order);

            Optional<Profile> profileOptional = profileService.getByTelegramId(order.getProfile().getTelegramId());

            if (profileOptional.isPresent()) {
                Profile profile = profileOptional.get();

                SendMessage sendMessage = SendMessage.builder()
                        .chatId(profile.getChatId())
                        .text("❌ Ваш заказ \"" + order.getRecipe().getName() + "\" отменен администратором")
                        .build();

                sender.execute(sendMessage);
            } else {
                log.info("Не смог найти пользователя с telegramId:{}", order.getProfile().getTelegramId());
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text("Не смог найти пользователя с telegramId:" + order.getProfile().getTelegramId())
                        .build();

                sender.execute(sendMessage);
            }
        } else {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Не смогли найти заказ rawOrderId:" + rawOrderId)
                    .build();

            sender.execute(sendMessage);
        }
    }

    public void orderReady(Long chatId, String rawOrderId) {
        long orderId = parseRecipeId(rawOrderId);

        Optional<Order> orderOptional = orderService.getById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setApproved(true);
            order = orderService.update(order);
            ingredientService.subtractFromTotalVolume(order.getRecipe());

            Optional<Profile> profileOptional = profileService.getByTelegramId(order.getProfile().getTelegramId());

            if (profileOptional.isPresent()) {
                Profile profile = profileOptional.get();

                SendMessage sendMessage = SendMessage.builder()
                        .chatId(profile.getChatId())
                        .text("✅ Ваш заказ \"" + order.getRecipe().getName() + "\" готов")
                        .build();

                sender.execute(sendMessage);
            } else {
                log.info("Не смог найти пользователя с telegramId:{}", order.getProfile().getTelegramId());
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text("Не смог найти пользователя с telegramId:" + order.getProfile().getTelegramId())
                        .build();

                sender.execute(sendMessage);
            }
        } else {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Не смогли найти заказ rawOrderId:" + rawOrderId)
                    .build();

            sender.execute(sendMessage);
        }
    }

    @Transactional
    public void createOrder(Long chatId, String rawRecipeId, Long userTelegramId) {
        log.info("Create order with rawRecipeId:{} for userTelegramId:{}", rawRecipeId, userTelegramId);
        Optional<Profile> profileOptional = profileService.getByTelegramId(userTelegramId);

        if (profileOptional.isPresent()) {
            log.info("Profile with userTelegramId:{} found", userTelegramId);
            Profile profile = profileOptional.get();

            long recipeId = parseRecipeId(rawRecipeId);
            Optional<Recipe> recipeOptional = recipeService.getById(parseRecipeId(rawRecipeId));

            if (recipeOptional.isPresent()) {
                log.info("Recipe with rawRecipeId:{} found", rawRecipeId);
                Recipe recipe = recipeOptional.get();

                if (recipe.getAlcoholic() && !orderService.canOrderAgain(profile)) {
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("\uD83D\uDE97 Воу воу воу, куда гонишь, брат? Давай чуть-чуть подождем")
                            .build();

                    sender.execute(sendMessage);
                    return;
                }

                Order order = orderService.create(profile, recipe);
                log.info("Order created for {} with {}", profile.getName(), recipe.getId());

                if (sendApproveToAdmin(order)) {
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("\uD83D\uDC4D Заказ \"" + recipe.getName().toLowerCase()
                                    + "\" создан, ждите сообщения о готовности")
                            .build();

                    sender.execute(sendMessage);
                } else {
                    SendMessage sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("⛔ ОШИБКА: Никто не может выполнить этот заказ... скажи админу нажать /start")
                            .build();

                    sender.execute(sendMessage);
                }
            } else {
                log.info("Не смог найти рецепт с id:{}", recipeId);
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text("Не сомг найти напиток, что то пошло не так. Сори, но сейчас я уже не могу пофиксить это")
                        .build();

                sender.execute(sendMessage);
            }
        } else {
            log.info("Не смог найти пользователя с telegramId:{}", userTelegramId);
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Не сомг тебе найти, попробуй запустить /start")
                    .build();

            sender.execute(sendMessage);
        }
    }

    public boolean sendApproveToAdmin(Order order) {
        log.info("Send order to admins");
        boolean someoneReceivedOrder = false;
        List<Profile> admins = profileService.getAllAdmins();

        for (Profile admin : admins) {
            if (admin.getChatId() != null) {
                String text = "❗ " + order.getProfile().getName() + " заказал:\n" + order.getRecipe().toString();

                SendMessage sendMessage = SendMessage.builder()
                        .chatId(admin.getChatId())
                        .text(text)
                        .replyMarkup(getApproveKeyboard(order.getId()))
                        .build();

                sender.execute(sendMessage);
                someoneReceivedOrder = true;
            } else {
                log.info("Admin {} not start chat and not has chatId in DB", admin.getName());
            }
        }

        return someoneReceivedOrder;
    }

    public void selectCocktails(long chatId) {
        log.info("Ability cocktails replay [selectCocktails]");

        if (!chatStateMap.containsKey(chatId) || !ChatState.READY.equals(chatStateMap.get(chatId))) {
            log.info("Ability cocktails replay [selectCocktails] - profile not ready");
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
        }

        log.info("Select cocktail");
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("А вот и бухлишко!")
                .replyMarkup(getListRecipeKeyboard(recipeService.getAllAlcoholic()))
                .build();

        sender.execute(sendMessage);
    }

    public void selectSoftDrinks(long chatId) {
        log.info("Ability cocktails replay [selectSoftDrinks]");

        if (!chatStateMap.containsKey(chatId) || !ChatState.READY.equals(chatStateMap.get(chatId))) {
            log.info("Ability cocktails replay [selectSoftDrinks] - profile not ready");
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
        }

        log.info("Select cocktail");
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("А вот и для детей")
                .replyMarkup(getListRecipeKeyboard(recipeService.getAllNonAlcoholic()))
                .build();

        sender.execute(sendMessage);
    }

    public void getRecipe(long chatId, String rawRecipeId) {
        log.info("getRecipe {} for chatId:{}", rawRecipeId, chatId);
        SendMessage sendMessage;
        long id = parseRecipeId(rawRecipeId);
        Optional<Recipe> recipeOptional = recipeService.getById(id);

        if (recipeOptional.isPresent()) {
            sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(recipeOptional.get().toString())
                    .replyMarkup(getRecipeKeyboard(id))
                    .build();
        } else {
            sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Не могу найти рецепт с id:" + id)
                    .build();
        }

        sender.execute(sendMessage);
    }

    public void deleteLastRecipeMessage(long chatId, Integer messageId) {
        log.info("Deleting last message with recipe for chatId:{}", chatId);
        DeleteMessage deleteMessage = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();

        log.info("Delete messageId:{} for chatId: {}", messageId, chatId);
        sender.execute(deleteMessage);
    }

    private long parseRecipeId(String rawId) {
        int index = rawId.lastIndexOf(':');

        return Long.parseLong(rawId.substring(index + 1));
    }

    public boolean isValidData(Update update, CocktailData cocktailData) {
        if (!update.hasCallbackQuery()) {
            log.info("not has callback request for {}", cocktailData);
            return false;
        }

        String rawId = update.getCallbackQuery().getData();

        if (StringUtils.isBlank(rawId)) {
            log.info("empty data for {}", cocktailData);
        }

        if (Pattern.matches("^" + cocktailData + "\\d+$", rawId)) {
            log.info("{} is valid for {}", rawId, cocktailData);

            return true;
        } else  {
            log.info("{} is not valid for {}", rawId, cocktailData);

            return false;
        }
    }

    private ReplyKeyboard getListRecipeKeyboard(List<Recipe> recipes) {
        log.info("Build recipes keyboard for {} elements", recipes.size());
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (int i = 0; i < recipes.size(); i++) {
            List<InlineKeyboardButton> buttons = new ArrayList<>(2);
            InlineKeyboardButton keyboardButton1 = InlineKeyboardButton.builder()
                    .text(recipes.get(i).getName())
                    .callbackData(COCKTAIL_DATA.getDataTemplate() + recipes.get(i).getId())
                    .build();
            buttons.add(keyboardButton1);

            if (i++ < recipes.size() - 1) {
                InlineKeyboardButton keyboardButton2 = InlineKeyboardButton.builder()
                        .text(recipes.get(i).getName())
                        .callbackData(COCKTAIL_DATA.getDataTemplate() + recipes.get(i).getId())
                        .build();
                buttons.add(keyboardButton2);
            }

            rows.add(buttons);
        }

        return new InlineKeyboardMarkup(rows);
    }

    private ReplyKeyboard getRecipeKeyboard(long id) {
        ArrayList<InlineKeyboardButton> buttons = new ArrayList<>();

        if (recipeService.getMaxCount(id) > 0) {
            InlineKeyboardButton order = InlineKeyboardButton.builder()
                    .text("Заказать")
                    .callbackData(COCKTAIL_ORDER_DATA.getDataTemplate() + id)
                    .build();
            buttons.add(order);
        }

        InlineKeyboardButton delete = InlineKeyboardButton.builder()
                .text("Галя, отмена")
                .callbackData(COCKTAIL_DELETE_DATA.getDataTemplate() + id)
                .build();
        buttons.add(delete);

        return new InlineKeyboardMarkup(List.of(buttons));
    }

    private ReplyKeyboard getApproveKeyboard(long orderID) {
        InlineKeyboardButton order = InlineKeyboardButton.builder()
                .text("Готово")
                .callbackData(COCKTAIL_ORDER_READY_DATA.getDataTemplate() + orderID)
                .build();
        InlineKeyboardButton delete = InlineKeyboardButton.builder()
                .text("Отклонить")
                .callbackData(COCKTAIL_ORDER_CANCEL_DATA.getDataTemplate() + orderID)
                .build();

        return new InlineKeyboardMarkup(List.of(List.of(order, delete)));
    }
}
