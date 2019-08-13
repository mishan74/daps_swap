package io.lastwill.eventscan.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(
        name = "eth_daps_swap_connect_entry",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"symbol", "eth_address"})}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EthToDapsConnectEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false, name = "eth_address")
    private String ethAddress;

    @Column(nullable = false, name = "daps_address")
    private String dapsAddress;

    public EthToDapsConnectEntry(String symbol, String ethAddress, String dapsAddress) {
        this.symbol = symbol;
        this.ethAddress = ethAddress;
        this.dapsAddress = dapsAddress;
    }
}
