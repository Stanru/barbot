package ru.bar.telegram_bar_bot.model.recipe;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecipeIngredientId implements Serializable {
    private Long recipeId;

    private Long ingredientId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        RecipeIngredientId that = (RecipeIngredientId) o;

        return Objects.equals(recipeId, that.recipeId)
                && Objects.equals(ingredientId, that.ingredientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, ingredientId);
    }
}
