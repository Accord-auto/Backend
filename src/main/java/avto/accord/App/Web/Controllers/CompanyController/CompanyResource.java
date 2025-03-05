package avto.accord.App.Web.Controllers.CompanyController;

import avto.accord.App.Domain.Models.Company.Company;
import avto.accord.App.Domain.Models.Company.CompanyDto;
import avto.accord.App.Domain.Services.CompanyService.CompanyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@Tag(name = "Компания", description = "методу управления компанией")
@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyResource {

    private final CompanyService companyService;

    @GetMapping("/{id}")
    public Company getOne(@PathVariable Integer id) {
        return companyService.getOne(id);
    }

    @GetMapping("/header")
    public Company getHeader() {
        return companyService.getHeaderCompany();
    }

    @GetMapping("/departments")
    public List<Company> getDepartments() {
        return companyService.getAllDepartments();
    }

    @PostMapping
    public Company create(@RequestBody CompanyDto company) {
        return companyService.create(company);
    }

    @PutMapping("/{id}")
    public Company patch(@PathVariable Integer id, @RequestBody CompanyDto dto) throws IOException {
        return companyService.update(id, dto);
    }
    @DeleteMapping("/{id}")
    public Company delete(@PathVariable Integer id) {
        return companyService.delete(id);
    }
}
