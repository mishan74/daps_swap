package io.lastwill.eventscan.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "eth_to_daps_connect")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EthToDapsConnectEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "eth_address")
    private String ethAddress;

    @Column(nullable = false, name = "daps_address")
    private String dapsAddress;

    @Column(nullable = false, name = "connect_tx_hash", unique = true)
    private String connectTxHash;

    public EthToDapsConnectEntry(String ethAddress, String dapsAddress, String connectTxHash) {
        this.ethAddress = ethAddress;
        this.dapsAddress = dapsAddress;
        this.connectTxHash = connectTxHash;
    }
}
