package com.wanderlust.bf_groupproject_1.controller;

import com.wanderlust.bf_groupproject_1.entity.Tour;
import com.wanderlust.bf_groupproject_1.entity.TourItinerary;
import com.wanderlust.bf_groupproject_1.enums.TourCategory;
import com.wanderlust.bf_groupproject_1.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping("/")
    public String home(Model model) {
        List<Tour> featuredTours = tourService.getFeaturedTours();
        model.addAttribute("featuredTours", featuredTours);
        return "index";
    }

    @GetMapping("/tours")
    public String listToursPublic(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            HttpServletRequest request) {
        
        TourCategory cat = null;
        if (category != null && !category.isEmpty()) {
            try {
                cat = TourCategory.valueOf(category);
            } catch (IllegalArgumentException ignored) {}
        }

        Page<Tour> tours = tourService.searchActiveTours(keyword, cat, minPrice, maxPrice, PageRequest.of(page, 8));
        model.addAttribute("tours", tours);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("categories", TourCategory.values());
        
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return "tours/list :: tourListFragment";
        }
        return "tours/list";
    }

    @GetMapping("/tours/{id}")
    public String tourDetail(@PathVariable Long id, Model model) {
        Tour tour = tourService.getTourById(id);
        List<TourItinerary> itineraries = tourService.getItinerariesByTourId(id);
        List<Tour> relatedTours = tourService.getRelatedTours(id, tour.getCategory());
        
        model.addAttribute("tour", tour);
        model.addAttribute("itineraries", itineraries);
        model.addAttribute("relatedTours", relatedTours);
        return "tours/detail";
    }

    @GetMapping("/destinations")
    public String destinations() {
        return "destinations";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}
