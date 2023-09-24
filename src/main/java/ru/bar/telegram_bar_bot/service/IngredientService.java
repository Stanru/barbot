package ru.bar.telegram_bar_bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bar.telegram_bar_bot.model.ingredient.Ingredient;
import ru.bar.telegram_bar_bot.model.recipe.Recipe;
import ru.bar.telegram_bar_bot.model.recipe.RecipeIngredient;
import ru.bar.telegram_bar_bot.repository.IngredientRepo;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private  final IngredientRepo ingredientRepo;

    //Вычетает ингредиенты рецепта из общего количества
    public void subtractFromTotalVolume(Recipe recipe) {
        for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
            Ingredient ingredient = recipeIngredient.getIngredient();
            Integer count = recipeIngredient.getCount();

            if (ingredient.getVolume() - count > 0) {
                ingredient.setVolume(ingredient.getVolume() - count);
                ingredientRepo.save(ingredient);
            }
        }
    }
}
