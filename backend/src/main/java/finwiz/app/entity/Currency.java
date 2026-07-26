package finwiz.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "currencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {
    @Id
    @Column(length = 3)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer minorUnits;

    @Column(length = 10)
    private String symbol;
}
