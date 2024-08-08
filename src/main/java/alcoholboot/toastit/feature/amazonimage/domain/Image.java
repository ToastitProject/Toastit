package alcoholboot.toastit.feature.amazonimage.domain;

import alcoholboot.toastit.global.Entity.AuditingFields;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Image extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_name", nullable = false)
    private String imageName;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "image_type")
    private String imageType;

    @Column(name = "image_size")
    private String imageSize;

    @Column(name = "image_use")
    private String imageUse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
