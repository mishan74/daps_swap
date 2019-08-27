package io.lastwill.eventscan.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "eth_to_daps_connect")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EthToDapsConnectEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, name = "eth_address")
    private String ethAddress;

    @Column(nullable = false, name = "daps_address")
    private String dapsAddress;

    public EthToDapsConnectEntry(String ethAddress, String dapsAddress) {
        this.ethAddress = ethAddress;
        this.dapsAddress = dapsAddress;
    }
}
