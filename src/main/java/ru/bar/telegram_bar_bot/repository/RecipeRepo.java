package ru.bar.telegram_bar_bot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bar.telegram_bar_bot.model.recipe.Recipe;

@Repository
public interface RecipeRepo extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByAlcoholic(boolean isAlcoholic);
}
