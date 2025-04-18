package Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name ="cours")
@Data
public class Cours {
    @Override
    public String toString() {
        return  "Cours" + " " +nom+" "+"Heure Debut "+ heureDebut+" "+"Heure Fin "+ " "+heureFin;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column()
    private  String nom;
    @Column()
    private  String description;
    @Column(name = "heure_debut")
    private LocalTime heureDebut;

    @Column(name = "heure_fin")
    private LocalTime  heureFin;
    @Column(name = "jour_cours")
    private String jour;
    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;
    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private Utulisateur professeur;
}
