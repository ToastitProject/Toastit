package alcoholboot.toastit.feature.recommendByLocation.Entity;

import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Table
@Getter
public class RecommendByLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String si;
    private String deo;
    private String ingredient1;
    private String ingredient2;
    private String ingredient3;
    private String ingredient4;
}
