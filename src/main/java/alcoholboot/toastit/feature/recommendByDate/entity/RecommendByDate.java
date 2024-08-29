package alcoholboot.toastit.feature.recommendByDate.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.lang.annotation.Target;

@Entity
@Table
@Getter
public class RecommendByDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
