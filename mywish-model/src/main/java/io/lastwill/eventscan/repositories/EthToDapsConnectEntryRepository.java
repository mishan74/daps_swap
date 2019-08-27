package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.EthToDapsConnectEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EthToDapsConnectEntryRepository extends CrudRepository<EthToDapsConnectEntry, Long> {

    EthToDapsConnectEntry findFirstByEthAddressOrderByIdDesc(@Param("ethAddress") String ethAddress);

    EthToDapsConnectEntry findFirstByDapsAddress(@Param("dapsAddress") String dapsAddress);

    List<EthToDapsConnectEntry> findAll();
}
