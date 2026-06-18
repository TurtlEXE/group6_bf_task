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

    @org.springframework.data.jpa.repository.Query("SELECT t FROM Tour t WHERE t.active = true " +
           "AND (:category IS NULL OR t.category = :category) " +
           "AND (:keyword IS NULL OR :keyword = '' OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.location) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:minPrice IS NULL OR t.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR t.price <= :maxPrice)")
    Page<Tour> searchToursPublic(@org.springframework.data.repository.query.Param("keyword") String keyword, 
                                 @org.springframework.data.repository.query.Param("category") TourCategory category, 
                                 @org.springframework.data.repository.query.Param("minPrice") Double minPrice, 
                                 @org.springframework.data.repository.query.Param("maxPrice") Double maxPrice, 
                                 Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT t FROM Tour t WHERE 1=1 " +
           "AND (:category IS NULL OR t.category = :category) " +
           "AND (:keyword IS NULL OR :keyword = '' OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.location) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:minPrice IS NULL OR t.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR t.price <= :maxPrice)")
    Page<Tour> searchToursAdmin(@org.springframework.data.repository.query.Param("keyword") String keyword, 
                                @org.springframework.data.repository.query.Param("category") TourCategory category, 
                                @org.springframework.data.repository.query.Param("minPrice") Double minPrice, 
                                @org.springframework.data.repository.query.Param("maxPrice") Double maxPrice, 
                                Pageable pageable);
                                
    List<Tour> findTop4ByActiveTrueAndFeaturedTrueOrderByCreatedAtDesc();

    List<Tour> findTop3ByActiveTrueAndCategoryAndIdNot(TourCategory category, Long id);
    
    long countByActiveTrue();
}
