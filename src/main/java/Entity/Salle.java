package Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name="salle")
@Data
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column()
    private  String nom;

    @Override
    public String toString() {
        return
                 nom ;

    }
}
