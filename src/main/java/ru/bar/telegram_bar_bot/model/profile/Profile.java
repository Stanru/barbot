package ru.bar.telegram_bar_bot.model.profile;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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
public class Profile extends BaseEntity {
    @Column(length = 100, nullable = false)
    @Pattern(regexp = "^[а-яА-ЯёЁ]{3,}$")
    private String name;

    @NaturalId
    @Positive
    @Column(nullable = false, unique = true)
    @NotNull
    private Long telegramId;

    @Positive
    private Long chatId;

    @Positive
    @Min(45)
    @Max(110)
    private Integer bodyWeight;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @NotEmpty
    @OneToMany(mappedBy = "profile", cascade = { CascadeType.MERGE, CascadeType.REMOVE })
    @ToString.Exclude
    private List<Order> orders;

    public Profile(String name, Long telegramId, Long chatId) {
        this.name = name;
        this.telegramId = telegramId;
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        Profile that = (Profile) o;

        return name != null && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
