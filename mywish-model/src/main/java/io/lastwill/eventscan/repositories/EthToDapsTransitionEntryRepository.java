package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.EthToDapsTransitionEntry;
import io.lastwill.eventscan.model.TransferStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EthToDapsTransitionEntryRepository extends CrudRepository<EthToDapsTransitionEntry, Long> {
    @Transactional
    @Query("update EthToDapsTransitionEntry e set e.dapsTxHash = :txHash")
    boolean setDapsTxHash(@Param("txHash") String txHash);

    @Query("select e from EthToDapsTransitionEntry e where lower(e.ethTxHash) = lower(:ethTxHash)")
    EthToDapsTransitionEntry findByEthTxHash(@Param("ethTxHash") String hash);

    List<EthToDapsTransitionEntry> findByTransferStatus(@Param("transferStatus") TransferStatus status);

    boolean existsByTransferStatus(@Param("transferStatus") TransferStatus transferStatus);
}
