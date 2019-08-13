package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.EthToDapsSwapEntry;
import io.lastwill.eventscan.model.TransferStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EthToDapsSwapEntryRepository extends CrudRepository<EthToDapsSwapEntry, Long> {
    @Transactional
    @Query("update EthToDapsSwapEntry e set e.dapsTxHash = :txHash")
    boolean setDapsTxHash(@Param("txHash") String txHash);

    @Query("select e from EthToDapsSwapEntry e where lower(e.ethTxHash) = lower(:ethTxHash)")
    EthToDapsSwapEntry findByEthTxHash(@Param("ethTxHash") String hash);

    List<EthToDapsSwapEntry> findEthToDapsSwapEntriesByTransferStatus(@Param("transfer_status") TransferStatus status);

    boolean existsByTransferStatus(@Param("transfer_status") TransferStatus transferStatus);
}
