package alcoholboot.toastit.feature.trendcocktail.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "trend_cocktail")
public class TrendCocktail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = false)
    private String startDate;

    @Column(unique = false)
    private String endDate;

    private String keyword;

    private double searchVolumeTwoMonthAgo;

    private double searchVolumeOneMonthAgo;
}