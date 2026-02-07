package com.example.turfbooking.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.turfbooking.Model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    
    @Query("SELECT b FROM Booking b WHERE b.turf.id = :turfId AND b.date = :date")
    List<Booking> findBookingsByTurfAndDate(Long turfId, LocalDate date);

    
    List<Booking> findByUserId(Long userId);
}
