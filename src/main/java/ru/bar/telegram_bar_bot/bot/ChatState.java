package ru.bar.telegram_bar_bot.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatState {
    WAIT_WEIGHT("ожидание ввода веса"),
    WAIT_GENDER("ожидание указания пола"),
    READY("все готово, можно пользоваться ботом");

    private final String text;
}
