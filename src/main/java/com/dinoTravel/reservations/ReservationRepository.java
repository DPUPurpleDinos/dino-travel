package com.dinoTravel.reservations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query(value = "SELECT * FROM reservations WHERE user_id=?1", nativeQuery = true)
    List<Reservation> findByUserId(int user_id);

    @Query(value = "SELECT * FROM auth_reservations WHERE subject_id=?1", nativeQuery = true)
    List<Reservation> findBySubjectId(String subject_id);

    @Query(value = "SELECT DISTINCT subject_id FROM auth_reservations WHERE booking_id=?1", nativeQuery = true)
    String findBookingOwner(long bookingId);

    @Query(value = "SELECT * FROM auth_reservations WHERE booking_id=?1", nativeQuery = true)
    List<Reservation> findByBookingId(long bookingId);
}
