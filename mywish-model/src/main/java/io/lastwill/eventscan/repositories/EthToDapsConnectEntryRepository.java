package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.EthToDapsConnectEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EthToDapsConnectEntryRepository extends CrudRepository<EthToDapsConnectEntry, Long> {
    boolean existsByConnectTxHash(@Param("connectTxHash") String txHash);

    EthToDapsConnectEntry findFirstByEthAddressOrderByIdDesc(@Param("ethAddress") String ethAddress);
}
