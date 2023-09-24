package ru.bar.telegram_bar_bot.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bar.telegram_bar_bot.model.profile.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    @Query(value = """
            SELECT TOP 1 *
            FROM user_order AS uo
            LEFT JOIN recipe AS r ON r.id = uo.recipe_id
            WHERE uo.profile_id = :profileId AND r.alcoholic = true
            ORDER BY created_at DESC
            """, nativeQuery = true)
    Optional<Order> lastForProfile(Long profileId);

    @Query(value = """
            FROM user_order o
            LEFT JOIN FETCH o.recipe r
            WHERE o.profile.id = :profileId AND o.approved = true
            """)
    List<Order> getApprovedByProfileId(Long profileId);
}
