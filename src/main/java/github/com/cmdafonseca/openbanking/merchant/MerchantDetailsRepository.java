package github.com.cmdafonseca.openbanking.merchant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantDetailsRepository {

    String findLogoByMerchantName(String merchantName);
}
