package avto.accord.App.Web.Controllers.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/secure")
    @Secured("ROLE_ADMIN")
    public String secureEndpoint() {
        return "This is a secure endpoint for admins only!";
    }
}
