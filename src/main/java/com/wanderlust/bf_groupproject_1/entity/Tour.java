package com.wanderlust.bf_groupproject_1.entity;

import com.wanderlust.bf_groupproject_1.enums.TourCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tours")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 200)
    private String location;

    private Integer durationDays;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private TourCategory category;

    @Builder.Default
    @Column(nullable = false)
    private boolean featured = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    // New fields for detailed tour info
    private Integer maxPersons;

    private Integer minAge;

    @Column(length = 200)
    private String departureCity;

    @Column(columnDefinition = "TEXT")
    private String includedServices; // Comma-separated: "Hotel,Flight,Meals,Transport,Guide"

    @Builder.Default
    @Column(precision = 2, scale = 1)
    private BigDecimal rating = BigDecimal.ZERO;

    @Builder.Default
    private Integer reviewCount = 0;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("dayNumber ASC")
    @Builder.Default
    private List<TourItinerary> itineraries = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
