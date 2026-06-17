package com.wanderlust.bf_groupproject_1.repository;

import com.wanderlust.bf_groupproject_1.entity.TourItinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourItineraryRepository extends JpaRepository<TourItinerary, Long> {
    List<TourItinerary> findByTourIdOrderByDayNumberAsc(Long tourId);
    void deleteByTourId(Long tourId);
}
