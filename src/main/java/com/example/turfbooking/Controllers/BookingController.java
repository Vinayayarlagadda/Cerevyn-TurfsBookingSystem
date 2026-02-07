package com.example.turfbooking.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.turfbooking.Model.Booking;
import com.example.turfbooking.Requests.BookingRequest;
import com.example.turfbooking.Services.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ✅ CREATE BOOKING
    @PostMapping
    public Booking book(@RequestBody BookingRequest request) {
        return bookingService.bookTurf(request);
    }

    // ✅ GET BOOKINGS BY USER
    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingService.getBookingsByUser(userId);
    }

    // ✅ CANCEL BOOKING
    @DeleteMapping("/{bookingId}")
    public String cancel(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return "Booking cancelled successfully";
    }
}
