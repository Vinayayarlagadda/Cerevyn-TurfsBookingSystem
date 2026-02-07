package com.example.turfbooking.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.turfbooking.Model.Booking;
import com.example.turfbooking.Model.Turf;
import com.example.turfbooking.Model.User;
import com.example.turfbooking.Repository.BookingRepository;
import com.example.turfbooking.Repository.TurfRepository;
import com.example.turfbooking.Repository.UserRepository;
import com.example.turfbooking.Requests.BookingRequest;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;
    private final TurfRepository turfRepo;

    public BookingService(
            BookingRepository bookingRepo,
            UserRepository userRepo,
            TurfRepository turfRepo) {

        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.turfRepo = turfRepo;
    }

    /* ===============================
       BOOK TURF
       =============================== */
    public Booking bookTurf(BookingRequest request) {

        // ✅ BASIC VALIDATION
        if (request.getUserId() == null || request.getTurfId() == null) {
            throw new RuntimeException("User ID or Turf ID is missing");
        }

        if (request.getDate() == null ||
            request.getStartTime() == null ||
            request.getEndTime() == null) {
            throw new RuntimeException("Booking date or time missing");
        }

        // ✅ PARSE STRING → DATE/TIME
        LocalDate bookingDate = LocalDate.parse(request.getDate());
        LocalTime startTime = LocalTime.parse(request.getStartTime());
        LocalTime endTime = LocalTime.parse(request.getEndTime());

        if (!endTime.isAfter(startTime)) {
            throw new RuntimeException("End time must be after start time");
        }

        // ✅ FETCH USER
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ FETCH TURF
        Turf turf = turfRepo.findById(request.getTurfId())
                .orElseThrow(() -> new RuntimeException("Turf not found"));

        // ✅ CHECK OVERLAPPING BOOKINGS
        List<Booking> existingBookings =
                bookingRepo.findBookingsByTurfAndDate(
                        request.getTurfId(),
                        bookingDate
                );

        for (Booking booking : existingBookings) {
            if (isTimeOverlap(startTime, endTime, booking)) {
                throw new RuntimeException("Time slot already booked");
            }
        }

        // ✅ CREATE BOOKING
        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setTurf(turf);
        newBooking.setDate(bookingDate);
        newBooking.setStartTime(startTime);
        newBooking.setEndTime(endTime);
        newBooking.setStatus("BOOKED"); // optional but recommended

        return bookingRepo.save(newBooking);
    }

    /* ===============================
       VIEW BOOKINGS BY USER
       =============================== */
    public List<Booking> getBookingsByUser(Long userId) {

        if (userId == null) {
            throw new RuntimeException("User ID cannot be null");
        }

        return bookingRepo.findByUserId(userId);
    }

    /* ===============================
       CANCEL BOOKING
       =============================== */
    public void cancelBooking(Long bookingId) {

        if (bookingId == null) {
            throw new RuntimeException("Booking ID cannot be null");
        }

        if (!bookingRepo.existsById(bookingId)) {
            throw new RuntimeException("Booking not found");
        }

        bookingRepo.deleteById(bookingId);
    }

    /* ===============================
       TIME OVERLAP CHECK
       =============================== */
    private boolean isTimeOverlap(
            LocalTime start,
            LocalTime end,
            Booking existing) {

        return start.isBefore(existing.getEndTime())
            && end.isAfter(existing.getStartTime());
    }
}
