package com.example.turfbooking.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.turfbooking.Model.Turf;
import com.example.turfbooking.Repository.TurfRepository;

@Service
public class TurfService {

    private final TurfRepository turfRepo;

    public TurfService(TurfRepository turfRepo) {
        this.turfRepo = turfRepo;
    }

    public Turf addTurf(Turf turf) {
        return turfRepo.save(turf);
    }

    public List<Turf> getAllTurfs() {
        return turfRepo.findAll();
    }

    public List<Turf> getAvailableTurfs() {
        return turfRepo.findByAvailableTrue();
    }

    // 🔴 MUST NOT RETURN NULL
    public Turf getTurfById(Long id) {
        return turfRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Turf not found with id: " + id));
    }

    public void deleteTurf(Long id) {
        turfRepo.deleteById(id);
    }
}
