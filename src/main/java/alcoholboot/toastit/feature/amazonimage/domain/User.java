package alcoholboot.toastit.feature.amazonimage.domain;

import alcoholboot.toastit.global.Entity.AuditingFields;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "authority")
    private String authority;

    @Column(name = "visit_number")
    private Integer visitNumber;

    @Column(name = "user_rank")
    private String userRank;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Image> images;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> follows;


}

