package io.lastwill.eventscan.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "last_block")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LastBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private NetworkType network;

    @Column(name = "block_number", nullable = false)
    private Long blockNumber;

    public LastBlock(NetworkType network, Long blockNumber) {
        this.network = network;
        this.blockNumber = blockNumber;
    }
}
