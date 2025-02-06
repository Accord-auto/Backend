package avto.accord.App.Domain.Repositories.Contact;

import avto.accord.App.Domain.Models.Company.Contact.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
}