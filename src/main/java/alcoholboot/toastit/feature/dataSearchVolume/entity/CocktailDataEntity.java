package alcoholboot.toastit.feature.dataSearchVolume.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "Cocktail_search_data")
public class CocktailDataEntity {
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
