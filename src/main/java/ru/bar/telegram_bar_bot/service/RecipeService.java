package ru.bar.telegram_bar_bot.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bar.telegram_bar_bot.model.ingredient.CountType;
import ru.bar.telegram_bar_bot.model.ingredient.Ingredient;
import ru.bar.telegram_bar_bot.model.recipe.Recipe;
import ru.bar.telegram_bar_bot.model.recipe.RecipeIngredient;
import ru.bar.telegram_bar_bot.repository.RecipeRepo;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepo recipeRepo;

    public List<Recipe> getAllAlcoholic() {
        return recipeRepo.findAllByAlcoholic(true);
    }

    public List<Recipe> getAllNonAlcoholic() {
        return recipeRepo.findAllByAlcoholic(false);
    }

    public Optional<Recipe> getById(long id) {
        return recipeRepo.findById(id);
    }

    //Возвращает количество коктелей которые можно приготовить, исходя из остатков
    public int getMaxCount(Long id) {
        Optional<Recipe> recipeOptional = getById(id);

        return recipeOptional.map(RecipeService::getMaxCount).orElse(0);
    }

    public static int getMaxCount(Recipe recipe) {
        return recipe.getIngredients().stream()
                .mapToInt(RecipeService::getMaxForIngredient)
                .min()
                .orElse(0);
    }

    public static int getAlcohol(Recipe recipe) {
        if (recipe.getAlcoholic() == false) {
            return 0;
        }

        int alcoholCount = 0;
        int totalCount = 0;

        for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
            Ingredient ingredient = recipeIngredient.getIngredient();

            if (CountType.MILLILITERS.equals(ingredient.getCountType())) {
                if (ingredient.getAlcohol() > 0) {
                    alcoholCount += ingredient.getAlcohol() * recipeIngredient.getCount();
                }

                totalCount += recipeIngredient.getCount();
            }
        }

        return alcoholCount / totalCount;
    }

    public static int getVolume(Recipe recipe) {
        return recipe.getIngredients().stream()
                .mapToInt(RecipeIngredient::getCount)
                .sum();
    }

    private static int getMaxForIngredient(RecipeIngredient recipeIngredient) {
        Integer countInRecipe = recipeIngredient.getCount();
        Integer totalVolume = recipeIngredient.getIngredient().getVolume();

        return getMaxDivider(totalVolume, countInRecipe, 0);
    }

    private static int getMaxDivider(int a, int b, int maxDivider) {
        if (a - b * maxDivider >= 0) {
            maxDivider++;
        } else {
            return maxDivider - 1;
        }

        return getMaxDivider(a, b, maxDivider);
    }
}
