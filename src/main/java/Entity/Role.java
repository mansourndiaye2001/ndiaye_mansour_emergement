package Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name ="role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column()
    private  String type;
    @Override
    public String toString() {
        return  type;


    }
}
