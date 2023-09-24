package ru.bar.telegram_bar_bot.model.recipe;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.NaturalId;
import ru.bar.telegram_bar_bot.model.profile.Order;
import ru.bar.telegram_bar_bot.service.RecipeService;
import ru.barbot.model.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Recipe extends BaseEntity {
    @NaturalId
    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @NotEmpty
    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @ToString.Exclude
    private List<RecipeIngredient> ingredients;

    @NotNull
    @Column(nullable = false)
    private Boolean alcoholic;

    @NotBlank
    private String instruction;

    @NotEmpty
    @OneToMany(mappedBy = "recipe", cascade = { CascadeType.MERGE, CascadeType.REMOVE })
    @ToString.Exclude
    private List<Order> orders;

    public Recipe(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        Recipe that = (Recipe) o;

        return name != null && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\uD83C\uDF78 ").append(name).append(":\n")
                .append("Осталось ").append(RecipeService.getMaxCount(this)).append(" шт.\n")
                .append("Крепость ").append(RecipeService.getAlcohol(this)).append("%\n")
                .append("Объем без льда ").append(RecipeService.getVolume(this)).append(" мл.\n\n");

        for (RecipeIngredient ingredient : ingredients) {
            stringBuilder.append("\uD83D\uDD38 " + ingredient + "\n");
        }

        stringBuilder.append("\n\uD83D\uDCC3 " + instruction);

        return stringBuilder.toString();
    }
}
