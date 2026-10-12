package tn.esprit.tpfoyer.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "paiement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long idPaiement;

    @EqualsAndHashCode.Include
    @ToString.Include
    private BigDecimal montant;

    @EqualsAndHashCode.Include
    @ToString.Include
    private LocalDate datePaiement;

    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    @ToString.Include
    private ModePaiement modePaiement;

    @ManyToOne(fetch = FetchType.LAZY)
    private Contrat contrat;
}