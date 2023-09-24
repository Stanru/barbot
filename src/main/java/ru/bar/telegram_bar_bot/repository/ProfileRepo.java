package ru.bar.telegram_bar_bot.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bar.telegram_bar_bot.model.profile.Profile;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Long> {
    Optional<Profile> findByTelegramId(Long telegramId);

    boolean existsByTelegramId(Long telegramId);

    @Query(value = "SELECT * FROM profile as p where p.role = 'ADMIN'", nativeQuery = true)
    List<Profile> findAllAdmins();
}
