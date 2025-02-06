package avto.accord.App.Domain.Services.ContactService;

import avto.accord.App.Domain.Models.Company.Contact.Contact;
import avto.accord.App.Domain.Repositories.Contact.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Optional<Contact> getContactById(int id) {
        return contactRepository.findById(id);
    }

    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public void deleteContact(int id) {
        contactRepository.deleteById(id);
    }
}
