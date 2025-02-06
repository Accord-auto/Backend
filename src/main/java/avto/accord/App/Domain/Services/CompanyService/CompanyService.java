package avto.accord.App.Domain.Services.CompanyService;

import avto.accord.App.Domain.Models.Company.Adress.Address;
import avto.accord.App.Domain.Models.Company.Company;
import avto.accord.App.Domain.Models.Company.CompanyDto;
import avto.accord.App.Domain.Models.Company.Contact.Contact;
import avto.accord.App.Domain.Repositories.Company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Company getOne(Integer id) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        return companyOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public Company create(Company company) {
        return companyRepository.save(company);
    }

    public Company update(int id, CompanyDto dto) throws IOException {
        try {
            log.info("Updating company with id: {}", id);
            Company company = companyRepository.findById(id).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

            company.setName(dto.getName());

            Address address = company.getAddress();
            if (address == null) {
                address = new Address();
            }
            address.setStreet(dto.getAddress().getStreet());
            address.setCity(dto.getAddress().getCity());
            address.setState(dto.getAddress().getState());
            address.setZipCode(dto.getAddress().getZipCode());
            company.setAddress(address);

            List<Contact> updatedContacts = dto.getContacts().stream()
                    .map(contactDto -> new Contact(contactDto.getPhoneNumber(), contactDto.getEmail()))
                    .collect(Collectors.toList());
            company.setContacts(updatedContacts);

            Company updatedCompany = companyRepository.save(company);
            log.info("Successfully updated company with id: {}", updatedCompany.getId());
            return updatedCompany;

        } catch (ResponseStatusException e) {
            log.error("Error updating company: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating company with id {}: {}", id, e.getMessage());
            throw new IOException("Failed to update company", e);
        }
    }

    public Company delete(Integer id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) {
            companyRepository.delete(company);
        }
        return company;
    }

}
