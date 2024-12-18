package avto.accord.App.Domain.Models.Property;


import avto.accord.App.Domain.Models.ProductProperty.ProductProperty;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany(mappedBy = "property", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductProperty> productProperties;
}