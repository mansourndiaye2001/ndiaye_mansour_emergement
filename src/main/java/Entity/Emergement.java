package Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name ="emergement ")
@Data
public class Emergement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "date_emergement")
    private LocalDateTime date_emergement;
    @Column()
    private  String statut;
    @ManyToOne
    @JoinColumn(name = "cours_id")
    private Cours  cours;
    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private Utulisateur professeur;
}

