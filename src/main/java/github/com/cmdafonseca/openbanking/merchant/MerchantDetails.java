package github.com.cmdafonseca.openbanking.merchant;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MerchantDetails implements MerchantDetailsRepository {

    Map<String, String> merchants = Map.of(
            "acme", "acme-logo.png",
            "globex", "globex-logo.png",
            "morley", "morley-logo.png",
            "contoso", "contoso-logo.png"
        );

    @Override
    public String findLogoByMerchantName(String merchantName) {
        return merchants.getOrDefault(merchantName.toLowerCase(), "");
    }
}