package ru.bar.telegram_bar_bot.model.profile;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.bar.telegram_bar_bot.model.recipe.Recipe;
import ru.barbot.model.BaseEntity;

@Entity(name = "user_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Order extends BaseEntity {
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REMOVE })
    @JoinColumn(name="profile_id", nullable=false)
    private Profile profile;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REMOVE })
    @JoinColumn(name="recipe_id", nullable=false)
    private Recipe recipe;

    @NotNull
    @Column(nullable = false)
    private Boolean approved = false;

    public Order(Profile profile, Recipe recipe) {
        this.profile = profile;
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        Order that = (Order) o;

        return Objects.equals(this.profile.getId(), ((Order) o).getProfile().getId())
                && Objects.equals(this.recipe.getId(), ((Order) o).getRecipe().getId()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.profile.getId(), this.recipe.getId());
    }
}
