package com.example.turfbooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.turfbooking.Model.Turf;

import java.util.List;

public interface TurfRepository extends JpaRepository<Turf, Long> {

    // 1️⃣ Find turfs by exact location
    List<Turf> findByLocation(String location);

    // 2️⃣ Find turfs with price per hour less than or equal to given price
    List<Turf> findByPricePerHourLessThanEqual(double price);

    // 3️⃣ Search turfs by name containing a keyword (case-insensitive)
    List<Turf> findByNameContainingIgnoreCase(String keyword);

    // 4️⃣ Custom query using JPQL to find turfs in a location with price less than given amount
    @Query("SELECT t FROM Turf t WHERE t.location = :location AND t.pricePerHour <= :price")
    List<Turf> findAffordableTurfsInLocation(@Param("location") String location, @Param("price") double price);

    // 5️⃣ Get top 5 cheapest turfs
    @Query("SELECT t FROM Turf t ORDER BY t.pricePerHour ASC")
    List<Turf> findTop5CheapestTurfs();
    
    List<Turf> findByAvailable(boolean available);

	List<Turf> findByAvailableTrue();
}