package avto.accord.App.Domain.Services.PriceService;

import avto.accord.App.Domain.Models.Price.Price;
import avto.accord.App.Domain.Models.Price.PriceRequest;
import avto.accord.App.Domain.Repositories.Price.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService {
    @Autowired
    private final PriceRepository priceRepository;
    // ? нужно ли еще чето
    public Price savePrice(Price request) {
        try {
            return priceRepository.save(request);
        } catch (Exception e) {
            throw e;
        }
    }

}
