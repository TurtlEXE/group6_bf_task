package com.wanderlust.bf_groupproject_1.repository;

import com.wanderlust.bf_groupproject_1.entity.Tour;
import com.wanderlust.bf_groupproject_1.enums.TourCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    Page<Tour> findByActiveTrue(Pageable pageable);

    Page<Tour> findByActiveTrueAndTitleContainingIgnoreCaseOrActiveTrueAndLocationContainingIgnoreCase(String title,
            String location, Pageable pageable);

    Page<Tour> findByActiveTrueAndCategoryAndTitleContainingIgnoreCase(TourCategory category, String title,
            Pageable pageable);

    List<Tour> findTop4ByActiveTrueAndFeaturedTrueOrderByCreatedAtDesc();

    Page<Tour> findByActiveTrueAndCategory(TourCategory category, Pageable pageable);

    List<Tour> findTop3ByActiveTrueAndCategoryAndIdNot(TourCategory category, Long id);

    long countByActiveTrue();
}
