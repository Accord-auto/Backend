package avto.accord.App.Domain.Models.Company;

import avto.accord.App.Domain.Models.Company.Adress.Address;
import avto.accord.App.Domain.Models.Company.Contact.Contact;
import avto.accord.App.Domain.Models.Company.TypeCompany.TypeCompany;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    private TypeCompany typeCompany;
}
