package io.lastwill.eventscan.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "eth_to_daps_transition")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EthToDapsTransitionEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private EthToDapsConnectEntry connectEntry;

    private BigInteger amount;

    @Column(name = "eth_tx_hash")
    private String ethTxHash;

    @Setter
    @Column(name = "daps_tx_hash")
    private String dapsTxHash;

    @Setter
    @Column(name = "transfer_status")
    @Enumerated(EnumType.STRING)
    private TransferStatus transferStatus = TransferStatus.WAIT_FOR_CONFIRM;

    public EthToDapsTransitionEntry(EthToDapsConnectEntry connectEntry, BigInteger amount, String ethTxHash) {
        this.connectEntry = connectEntry;
        this.amount = amount;
        this.ethTxHash = ethTxHash;
    }
}
