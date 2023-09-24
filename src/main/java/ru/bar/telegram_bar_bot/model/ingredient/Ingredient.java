package ru.bar.telegram_bar_bot.model.ingredient;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.NaturalId;
import ru.barbot.model.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Ingredient extends BaseEntity {
    @NaturalId
    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Positive
    @Column(nullable = false)
    private Integer alcohol;

    @Positive
    @Column(nullable = false)
    private Integer calories;

    @Positive
    @Column(nullable = false)
    private Integer volume;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CountType countType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        Ingredient that = (Ingredient) o;

        return name != null && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
