package com.wanderlust.bf_groupproject_1.service;

import com.wanderlust.bf_groupproject_1.dto.TourDTO;
import com.wanderlust.bf_groupproject_1.entity.Tour;
import com.wanderlust.bf_groupproject_1.entity.TourItinerary;
import com.wanderlust.bf_groupproject_1.enums.ErrorCode;
import com.wanderlust.bf_groupproject_1.enums.TourCategory;
import com.wanderlust.bf_groupproject_1.exception.BusinessException;
import com.wanderlust.bf_groupproject_1.repository.TourItineraryRepository;
import com.wanderlust.bf_groupproject_1.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final TourItineraryRepository itineraryRepository;

    public Page<Tour> getAllToursAdmin(Pageable pageable) {
        return tourRepository.findAll(pageable);
    }

    public Page<Tour> getActiveTours(Pageable pageable) {
        return tourRepository.findByActiveTrue(pageable);
    }

    public Page<Tour> searchActiveTours(String keyword, TourCategory category, Double minPrice, Double maxPrice, Pageable pageable) {
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        return tourRepository.searchToursPublic(keyword, category, minPrice, maxPrice, pageable);
    }

    public Page<Tour> searchAllToursAdmin(String keyword, TourCategory category, Double minPrice, Double maxPrice, Pageable pageable) {
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        return tourRepository.searchToursAdmin(keyword, category, minPrice, maxPrice, pageable);
    }

    public List<Tour> getFeaturedTours() {
        return tourRepository.findTop4ByActiveTrueAndFeaturedTrueOrderByCreatedAtDesc();
    }

    public Tour getTourById(Long id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TOUR_NOT_FOUND));
    }

    public List<TourItinerary> getItinerariesByTourId(Long tourId) {
        return itineraryRepository.findByTourIdOrderByDayNumberAsc(tourId);
    }

    public List<Tour> getRelatedTours(Long currentTourId, TourCategory category) {
        return tourRepository.findTop3ByActiveTrueAndCategoryAndIdNot(category, currentTourId);
    }

    @Transactional
    public void createTour(TourDTO dto) {
        Tour tour = Tour.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .durationDays(dto.getDurationDays())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .category(dto.getCategory())
                .featured(dto.isFeatured())
                .active(dto.isActive())
                .maxPersons(dto.getMaxPersons())
                .minAge(dto.getMinAge())
                .departureCity(dto.getDepartureCity())
                .includedServices(dto.getIncludedServices())
                .build();
        tourRepository.save(tour);

        // Save itineraries
        saveItineraries(tour, dto.getItineraryEntries());
    }

    @Transactional
    public void updateTour(Long id, TourDTO dto) {
        Tour tour = getTourById(id);
        tour.setTitle(dto.getTitle());
        tour.setDescription(dto.getDescription());
        tour.setLocation(dto.getLocation());
        tour.setDurationDays(dto.getDurationDays());
        tour.setPrice(dto.getPrice());
        tour.setImageUrl(dto.getImageUrl());
        tour.setCategory(dto.getCategory());
        tour.setFeatured(dto.isFeatured());
        tour.setActive(dto.isActive());
        tour.setMaxPersons(dto.getMaxPersons());
        tour.setMinAge(dto.getMinAge());
        tour.setDepartureCity(dto.getDepartureCity());
        tour.setIncludedServices(dto.getIncludedServices());
        tourRepository.save(tour);

        // Replace itineraries
        itineraryRepository.deleteByTourId(id);
        saveItineraries(tour, dto.getItineraryEntries());
    }

    private void saveItineraries(Tour tour, List<TourDTO.ItineraryEntry> entries) {
        if (entries != null) {
            for (TourDTO.ItineraryEntry entry : entries) {
                if (entry.getTitle() != null && !entry.getTitle().trim().isEmpty()) {
                    TourItinerary itinerary = TourItinerary.builder()
                            .tour(tour)
                            .dayNumber(entry.getDayNumber())
                            .title(entry.getTitle())
                            .description(entry.getDescription())
                            .build();
                    itineraryRepository.save(itinerary);
                }
            }
        }
    }

    @Transactional
    public void deleteTour(Long id) {
        try {
            itineraryRepository.deleteByTourId(id);
            tourRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.TOUR_DELETE_FAILED);
        }
    }

    public long countTours() {
        return tourRepository.count();
    }

    public long countActiveTours() {
        return tourRepository.countByActiveTrue();
    }
}
