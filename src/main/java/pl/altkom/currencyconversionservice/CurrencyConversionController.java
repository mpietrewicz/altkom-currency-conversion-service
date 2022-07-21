package pl.altkom.currencyconversionservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy currencyExchangeProxy;


    @GetMapping("/currency-calculate2/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateConversion2(@PathVariable(name = "from") String form,
                                                  @PathVariable(name = "to") String to,
                                                  @PathVariable(name = "quantity") BigDecimal quantity) {
        CurrencyConversion responseBody = currencyExchangeProxy.retriveExchangeValue(form, to);
        return new CurrencyConversion(responseBody.getId(), responseBody.getFrom(), responseBody.getTo(), quantity,
                quantity.multiply(responseBody.getConversion()), responseBody.getEnvironment());
    }

    @GetMapping("/currency-calculate/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateConversion(@PathVariable(name = "from") String form,
                                                  @PathVariable(name = "to") String to,
                                                  @PathVariable(name = "quantity") BigDecimal quantity) {
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", form);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate()
                .getForEntity("http://localhost:8083/currency-exchange/from/{from}/to/{to}",
                CurrencyConversion.class, uriVariables);
        CurrencyConversion responseBody = responseEntity.getBody();

        return new CurrencyConversion(responseBody.getId(), responseBody.getFrom(), responseBody.getTo(), quantity,
                quantity.multiply(responseBody.getConversion()), responseBody.getEnvironment());
    }

}
