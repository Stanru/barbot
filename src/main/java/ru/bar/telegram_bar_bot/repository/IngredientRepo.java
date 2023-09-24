package ru.bar.telegram_bar_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bar.telegram_bar_bot.model.ingredient.Ingredient;

@Repository
public interface IngredientRepo extends JpaRepository<Ingredient, Long> {

}
