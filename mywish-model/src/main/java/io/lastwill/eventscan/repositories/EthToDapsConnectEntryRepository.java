package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.EthToDapsConnectEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EthToDapsConnectEntryRepository extends CrudRepository<EthToDapsConnectEntry, Long> {
    boolean existsByEthAddressAndSymbol(@Param("ethAddress") String ethAddress, @Param("symbol") String symbol);

    EthToDapsConnectEntry findByEthAddress(@Param("ethAddress") String ethAddress);
}
