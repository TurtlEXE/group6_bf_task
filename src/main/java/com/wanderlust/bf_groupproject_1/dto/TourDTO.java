package com.wanderlust.bf_groupproject_1.dto;

import com.wanderlust.bf_groupproject_1.enums.TourCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class TourDTO {
    private Long id;

    @NotBlank(message = "Tour title is required")
    private String title;

    private String description;
    
    private String location;
    
    private Integer durationDays;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    private String imageUrl;
    
    private TourCategory category;
    
    private boolean featured;
    
    private boolean active;

    // New fields
    private Integer maxPersons;
    private Integer minAge;
    private String departureCity;
    private String includedServices;

    // Itinerary entries
    private List<ItineraryEntry> itineraryEntries = new ArrayList<>();

    @Data
    public static class ItineraryEntry {
        private Integer dayNumber;
        private String title;
        private String description;
    }
}
