package ru.bar.telegram_bar_bot.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.bar.telegram_bar_bot.model.profile.Order;
import ru.bar.telegram_bar_bot.model.profile.Profile;
import ru.bar.telegram_bar_bot.model.recipe.Recipe;
import ru.bar.telegram_bar_bot.repository.OrderRepo;

@RequiredArgsConstructor
@Service
@Log4j2
public class OrderService {
    private final OrderRepo orderRepo;

    public Optional<Order> getById(long id) {
        return orderRepo.findById(id);
    }

    public boolean canOrderAgain(Profile profile) {
        Optional<Order> order = orderRepo.lastForProfile(profile.getId());

        if (order.isPresent()) {
            ZonedDateTime createdAt = order.get().getCreatedAt();

            return createdAt.plusMinutes(5).isBefore(ZonedDateTime.now());
        }

        log.info("Profile:{} not has orders", profile.getName());

        return true;
    }

    public List<Order> getOrdersForProfile(Profile profile) {
        return orderRepo.getApprovedByProfileId(profile.getId());
    }

    public Order create(Order order) {
        return orderRepo.save(order);
    }

    public Order create(Profile profile, Recipe recipe) {
        return orderRepo.save(new Order(profile, recipe));
    }

    public Order update(Order order) {
        return orderRepo.save(order);
    }
}
