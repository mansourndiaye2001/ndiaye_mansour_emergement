package Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="utulisateur")
@Data
public class Utulisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column()
    private  String nom;
    @Column()
    private  String prenom;
    @Column()
    private  String email;
    @Column()
    private  String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @Override
    public String toString() {
        return
             "MR"+' '+   nom +' '+ prenom;

    }
}
