package com.dinoTravel.reservations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query(value = "SELECT * FROM reservations WHERE user_id=?1", nativeQuery = true)
    List<Reservation> findByUserId(int user_id);
}
