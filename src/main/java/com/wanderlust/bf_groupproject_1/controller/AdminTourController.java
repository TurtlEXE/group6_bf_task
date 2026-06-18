package com.wanderlust.bf_groupproject_1.controller;

import com.wanderlust.bf_groupproject_1.dto.TourDTO;
import com.wanderlust.bf_groupproject_1.entity.Tour;
import com.wanderlust.bf_groupproject_1.entity.TourItinerary;
import com.wanderlust.bf_groupproject_1.repository.UserRepository;
import com.wanderlust.bf_groupproject_1.service.TourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminTourController {

    private final TourService tourService;
    private final UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalTours", tourService.countTours());
        model.addAttribute("activeTours", tourService.countActiveTours());
        model.addAttribute("totalUsers", userRepository.count());
        return "admin/dashboard";
    }

    @GetMapping("/tours")
    public String listTours(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page, 
            Model model, 
            HttpServletRequest request) {
        
        com.wanderlust.bf_groupproject_1.enums.TourCategory cat = null;
        if (category != null && !category.isEmpty()) {
            try {
                cat = com.wanderlust.bf_groupproject_1.enums.TourCategory.valueOf(category);
            } catch (Exception e) {}
        }
        
        Page<Tour> tours = tourService.searchAllToursAdmin(keyword, cat, null, null, PageRequest.of(page, 10));
        model.addAttribute("tours", tours);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("categories", com.wanderlust.bf_groupproject_1.enums.TourCategory.values());
        
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return "admin/tours/list :: tourListFragment";
        }
        return "admin/tours/list";
    }

    @GetMapping("/tours/new")
    public String showCreateForm(Model model) {
        model.addAttribute("tourDTO", new TourDTO());
        return "admin/tours/form";
    }

    @PostMapping("/tours")
    public String createTour(@Valid @ModelAttribute("tourDTO") TourDTO tourDTO) {
        tourService.createTour(tourDTO);
        return "redirect:/admin/tours?success=created";
    }

    @GetMapping("/tours/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Tour tour = tourService.getTourById(id);
        TourDTO dto = new TourDTO();
        dto.setId(tour.getId());
        dto.setTitle(tour.getTitle());
        dto.setDescription(tour.getDescription());
        dto.setLocation(tour.getLocation());
        dto.setDurationDays(tour.getDurationDays());
        dto.setPrice(tour.getPrice());
        dto.setImageUrl(tour.getImageUrl());
        dto.setCategory(tour.getCategory());
        dto.setFeatured(tour.isFeatured());
        dto.setActive(tour.isActive());
        dto.setMaxPersons(tour.getMaxPersons());
        dto.setMinAge(tour.getMinAge());
        dto.setDepartureCity(tour.getDepartureCity());
        dto.setIncludedServices(tour.getIncludedServices());

        // Load itineraries
        List<TourItinerary> itineraries = tourService.getItinerariesByTourId(id);
        List<TourDTO.ItineraryEntry> entries = new ArrayList<>();
        for (TourItinerary it : itineraries) {
            TourDTO.ItineraryEntry entry = new TourDTO.ItineraryEntry();
            entry.setDayNumber(it.getDayNumber());
            entry.setTitle(it.getTitle());
            entry.setDescription(it.getDescription());
            entries.add(entry);
        }
        dto.setItineraryEntries(entries);

        model.addAttribute("tourDTO", dto);
        return "admin/tours/form";
    }

    @PostMapping("/tours/{id}")
    public String updateTour(@PathVariable Long id, @Valid @ModelAttribute("tourDTO") TourDTO tourDTO) {
        tourService.updateTour(id, tourDTO);
        return "redirect:/admin/tours?success=updated";
    }

    @PostMapping("/tours/{id}/delete")
    public String deleteTour(@PathVariable Long id) {
        tourService.deleteTour(id);
        return "redirect:/admin/tours?success=deleted";
    }
}
