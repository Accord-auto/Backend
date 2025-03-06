package avto.accord.App.Domain.Models.Company.SocialURLs;

import avto.accord.App.Domain.Models.Company.Contact.Contact;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SocialURLs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;
    private String url;

    @ManyToOne
    @JoinColumn(name = "contact_id", insertable = false, updatable = false)
    @JsonBackReference
    private Contact contact;
}
