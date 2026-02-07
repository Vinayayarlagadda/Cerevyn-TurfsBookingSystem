package com.example.turfbooking.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.turfbooking.Model.Turf;
import com.example.turfbooking.Services.TurfService;

@RestController
@RequestMapping("/api/turfs")
public class TurfController {

    private final TurfService turfService;

    public TurfController(TurfService turfService) {
        this.turfService = turfService;
    }

    
    @PostMapping(consumes = "multipart/form-data")
    public Turf addTurf(
            @RequestParam String name,
            @RequestParam String location,
            @RequestParam double pricePerHour,
            @RequestParam("image") MultipartFile image
    ) throws Exception {

        String uploadDir = "src/main/resources/static/images/";
        Files.createDirectories(Paths.get(uploadDir));

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.write(filePath, image.getBytes());

        Turf turf = new Turf();
        turf.setName(name);
        turf.setLocation(location);
        turf.setPricePerHour(pricePerHour);
        turf.setImageUrl(fileName);
        turf.setAvailable(true);

        return turfService.addTurf(turf);
    }

    
    @GetMapping
    public List<Turf> getAllTurfs() {
        return turfService.getAllTurfs();
    }

    
    @GetMapping("/available")
    public List<Turf> getAvailableTurfs() {
        return turfService.getAvailableTurfs();
    }

    
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public Turf updateTurf(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String location,
            @RequestParam double pricePerHour,
            @RequestParam(required = false, value = "image") MultipartFile image
    ) throws Exception {

        Turf existingTurf = turfService.getTurfById(id);

        existingTurf.setName(name);
        existingTurf.setLocation(location);
        existingTurf.setPricePerHour(pricePerHour);

       
        if (image != null && !image.isEmpty()) {

            // delete old image
            if (existingTurf.getImageUrl() != null) {
                Path oldImage = Paths.get(
                        "src/main/resources/static/images/" + existingTurf.getImageUrl());
                Files.deleteIfExists(oldImage);
            }

            String uploadDir = "src/main/resources/static/images/";
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, image.getBytes());

            existingTurf.setImageUrl(fileName);
        }

        return turfService.addTurf(existingTurf);
    }

    
    @DeleteMapping("/{id}")
    public String deleteTurf(@PathVariable Long id) {

        Turf turf = turfService.getTurfById(id);

        try {
            if (turf.getImageUrl() != null) {
                Path imagePath = Paths.get(
                    "src/main/resources/static/images/" + turf.getImageUrl()
                );
                Files.deleteIfExists(imagePath);
            }
        } catch (Exception e) {
            System.out.println("Image delete failed: " + e.getMessage());
        }

        turfService.deleteTurf(id);
        return "Turf deleted successfully";
    }

}
