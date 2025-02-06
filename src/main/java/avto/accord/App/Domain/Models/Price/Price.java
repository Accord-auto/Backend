package avto.accord.App.Domain.Models.Price;

import avto.accord.App.Domain.Models.Product.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(mappedBy = "price")
    @JsonBackReference
    private Product product;
    private int value;
    private int discount;

    public Price(int value, int discount) {
        this.value = value;
        this.discount = discount;
    }
}
