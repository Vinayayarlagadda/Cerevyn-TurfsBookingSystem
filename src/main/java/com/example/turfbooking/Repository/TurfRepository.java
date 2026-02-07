package com.example.turfbooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.turfbooking.Model.Turf;

import java.util.List;

public interface TurfRepository extends JpaRepository<Turf, Long> {

    
    List<Turf> findByLocation(String location);

    
    List<Turf> findByPricePerHourLessThanEqual(double price);

    
    List<Turf> findByNameContainingIgnoreCase(String keyword);

    
    @Query("SELECT t FROM Turf t WHERE t.location = :location AND t.pricePerHour <= :price")
    List<Turf> findAffordableTurfsInLocation(@Param("location") String location, @Param("price") double price);

    
    @Query("SELECT t FROM Turf t ORDER BY t.pricePerHour ASC")
    List<Turf> findTop5CheapestTurfs();
    
    List<Turf> findByAvailable(boolean available);

	List<Turf> findByAvailableTrue();
}
