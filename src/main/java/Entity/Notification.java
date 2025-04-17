package Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name ="notification")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column()
    private  String message;
    @ManyToOne
    @JoinColumn(name = "destinaire_id")
    private Utulisateur professeur;
    @Column(name = "date_envoie")
    private LocalDateTime date_envoie;
}
