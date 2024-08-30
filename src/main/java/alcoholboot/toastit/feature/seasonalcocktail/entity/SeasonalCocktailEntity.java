package alcoholboot.toastit.feature.seasonalcocktail.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Getter
@Entity
@Table(name = "seasonal_cocktails")
public class SeasonalCocktailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
