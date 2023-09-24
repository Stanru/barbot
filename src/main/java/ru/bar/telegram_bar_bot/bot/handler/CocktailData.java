package ru.bar.telegram_bar_bot.bot.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CocktailData {
    COCKTAIL_DATA("cocktail:"),
    COCKTAIL_ORDER_DATA("recipe-order:"),
    COCKTAIL_ORDER_READY_DATA("recipe-order-ready:"),
    COCKTAIL_ORDER_CANCEL_DATA("recipe-order-cancel:"),
    COCKTAIL_DELETE_DATA("recipe-delete:");

    private final String dataTemplate;

    @Override
    public String toString() {
        return dataTemplate;
    }
}
