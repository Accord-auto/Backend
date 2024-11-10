package avto.accord.App.Domain.Models.Price;

import avto.accord.App.Domain.Models.Product.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(mappedBy = "price")
    private Product product;
    private int value;
    private int discount;
}
