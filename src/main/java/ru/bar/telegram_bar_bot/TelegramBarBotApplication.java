package ru.bar.telegram_bar_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelegramBarBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramBarBotApplication.class, args);
	}
}
