package ru.bar.telegram_bar_bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.bar.telegram_bar_bot.bot.ChatState;
import ru.bar.telegram_bar_bot.model.profile.Gender;
import ru.bar.telegram_bar_bot.model.profile.Order;
import ru.bar.telegram_bar_bot.model.profile.Profile;
import ru.bar.telegram_bar_bot.model.recipe.Recipe;
import ru.bar.telegram_bar_bot.repository.ProfileRepo;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepo profileRepo;
    private final OrderService orderService;

    public Optional<Profile> getByTelegramId(Long telegramId) {
        return profileRepo.findByTelegramId(telegramId);
    }

    public List<Profile> getAll() {
        return profileRepo.findAll();
    }

    public Profile createOrGetProfile(long chatId, User user, Map<Long, ChatState> chatStateMap) {
        Optional<Profile> profileOptional = profileRepo.findByTelegramId(user.getId());

        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            log.info("Profile for {} already created", profile.getName());

            if (profile.getChatId() == null) {
                log.info("Profile for {} not has chatId, it be added", profile.getName());
                profile.setChatId(chatId);
                profile = profileRepo.save(profile);
            }

            if (profile.getBodyWeight() == null || profile.getBodyWeight() == 0) {
                log.info("Profile for {} not has body weight, wait", profile.getName());
                log.info("Set chat status:{}", ChatState.WAIT_WEIGHT);
                chatStateMap.put(chatId, ChatState.WAIT_WEIGHT);

                return profile;
            }

            if (profile.getGender() == null) {
                log.info("Profile for {} not has body gender, wait", profile.getName());
                log.info("Set chat status:{}", ChatState.WAIT_GENDER);
                chatStateMap.put(chatId, ChatState.WAIT_GENDER);

                return profile;
            }

            log.info("Set chat status:{}", ChatState.READY);
            chatStateMap.put(chatId, ChatState.READY);

            return profile;
        }

        log.info("Set chat status:{}", ChatState.WAIT_WEIGHT);
        chatStateMap.put(chatId, ChatState.WAIT_WEIGHT);
        Profile profile;

        if (StringUtils.isBlank(user.getFirstName())) {
            profile = new Profile(user.getUserName(), user.getId(), chatId);
        } else {
            profile = new Profile(user.getFirstName(), user.getId(), chatId);
        }

        return profileRepo.save(profile);
    }

    public boolean updateBodyWeight(Long userTelegramId, int bodyWeight) {
        Optional<Profile> profileOptional = profileRepo.findByTelegramId(userTelegramId);

        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            profile.setBodyWeight(bodyWeight);
            profileRepo.save(profile);

            return true;
        }

        return false;
    }

    public boolean updateGender(Long userTelegramId, String genderName) {
        Optional<Profile> profileOptional = profileRepo.findByTelegramId(userTelegramId);

        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();

            Gender gender = Gender.valueOf(genderName);
            profile.setGender(gender);
            profileRepo.save(profile);

            return true;
        }

        return false;
    }

    public List<Profile> getAllAdmins() {
        return profileRepo.findAllAdmins();
    }

    public List<String> drunkestRate() {
        List<Profile> profiles = profileRepo.findAll();
        List<String> profilesInfo = new ArrayList<>();

        for (var profile : profiles) {
            try {
                List<Order> orders = orderService.getOrdersForProfile(profile);

                long countAlcoholDrinks = orders.stream()
                        .map(Order::getRecipe)
                        .filter(Recipe::getAlcoholic)
                        .count();
                double avgAlcohol = orders.stream()
                        .map(Order::getRecipe)
                        .filter(Recipe::getAlcoholic)
                        .mapToInt(RecipeService::getAlcohol)
                        .average()
                        .orElse(0);
                double totalAlcoholGrams = orders.stream()
                        .map(Order::getRecipe)
                        .mapToDouble(recipe -> {
                            double alcohol = RecipeService.getAlcohol(recipe);
                            double volume = RecipeService.getVolume(recipe);

                            return volume * (alcohol / 100.0) * 0.8;
                        }).sum();
                //дефицит резорбции (часть спирта не доходит до крови)
                double resorptionDeficiency = 0.8;
                //вес тела
                double bodyWeight = profile.getBodyWeight();
                //коэффициент Видмарка: мужчины - 0.7 женщины - 0.6
                double distributionСoefficient = switch (profile.getGender()) {
                    case MALE -> 0.7;
                    case FEMALE -> 0.6;
                };

                //концентрация алкоголя в крови
                double bloodAlcoholConcentration = totalAlcoholGrams * resorptionDeficiency / (bodyWeight * distributionСoefficient);

                String profileInfo = "\uD83D\uDD39 " + profile.getName() +
                        ": выпил(а) " +
                        countAlcoholDrinks +
                        " алкогольных напитков, средний градус которых " + String.format("%.1f",avgAlcohol) + "%" +
                        ", концентрация алкоголя в крови " + String.format("%.1f",bloodAlcoholConcentration) + "%" +
                        ", что соответствует - " + getDegreeOfIntoxication(bloodAlcoholConcentration);

                profilesInfo.add(profileInfo);
            } catch (Exception e) {
                profilesInfo.add("\uD83D\uDD39 " + profile.getName() + "что то пошло не так, для тебя нет статистики");
                log.error(e);
            }
        }

        return profilesInfo;
    }

    private String getDegreeOfIntoxication(double bloodAlcoholConcentration) {
        if (bloodAlcoholConcentration < 0.5) {
            return "отсутствие влияния алкоголя";
        }

        if (bloodAlcoholConcentration < 1.5) {
            return "лёгкая степень опьянения";
        }

        if (bloodAlcoholConcentration < 2.0) {
            return "средняя степень опьянения (брат, тормозни, плохо будет)";
        }

        if (bloodAlcoholConcentration < 3.0) {
            return "сильная степень опьянения (тебе больше не наливаем)";
        }

        if (bloodAlcoholConcentration < 5.0) {
            return "тяжёлое отравление (надеюсь это никто не прочитает)";
        }

        return "смертельное отравление или я что то напутал в формуле";
    }
}
