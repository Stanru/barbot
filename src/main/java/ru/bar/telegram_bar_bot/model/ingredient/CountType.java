package ru.bar.telegram_bar_bot.model.ingredient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CountType {
    MILLILITERS("мл."), GRAMS("гр."), PIECES("штук");

    private final String text;

    @Override
    public String toString() {
        return text;
    }
}
