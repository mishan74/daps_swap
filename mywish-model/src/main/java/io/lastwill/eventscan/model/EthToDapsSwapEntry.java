package io.lastwill.eventscan.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "eth_daps_swap_swap_entry")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EthToDapsSwapEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private EthToDapsConnectEntry linkEntry;

    private BigInteger amount;

    @Column(name = "eth_tx_hash")
    private String ethTxHash;

    @Setter
    @Column(name = "daps_tx_hash")
    private String dapsTxHash;

    @Setter
    @Column(name = "transfer_status")
    @Enumerated(EnumType.STRING)
    private TransferStatus transferStatus;

    public EthToDapsSwapEntry(EthToDapsConnectEntry linkEntry, BigInteger amount, String ethTxHash) {
        this.linkEntry = linkEntry;
        this.amount = amount;
        this.ethTxHash = ethTxHash;
    }
}
