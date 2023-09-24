package ru.bar.telegram_bar_bot.model.profile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("Мужчина"), FEMALE("Женщина");

    private final String text;

    @Override
    public String toString() {
        return text;
    }
}
