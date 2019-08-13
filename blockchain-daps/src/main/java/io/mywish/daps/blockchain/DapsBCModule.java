package io.mywish.daps.blockchain;

import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.repositories.LastBlockRepository;
import io.mywish.daps.blockchain.services.DapsNetwork;
import io.mywish.daps.blockchain.services.DapsScanner;
import io.mywish.scanner.services.LastBlockDbPersister;
import io.mywish.scanner.services.LastBlockPersister;
import org.apache.http.impl.client.CloseableHttpClient;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@ComponentScan
public class DapsBCModule {
    /**
     * Solution for test purposes only.
     * When we scan mainnet blocks we build addresses in mainnet format. And is not the same like testnet address.
     * This flag is solve the issue.
     */
    @Value("${etherscanner.daps.treat-testnet-as-mainnet:false}")
    private boolean treatTestnetAsMainnet;

    @ConditionalOnProperty("etherscanner.daps.rpc-url.mainnet")
    @Bean(name = NetworkType.DAPS_MAINNET_VALUE)
    public DapsNetwork dapsNetMain(
            final CloseableHttpClient closeableHttpClient,
            final @Value("${etherscanner.daps.rpc-url.mainnet}") URI rpc
    ) throws Exception {
        String user = null, password = null;
        if (rpc.getUserInfo() != null) {
            String[] credentials = rpc.getUserInfo().split(":");
            if (credentials.length > 1) {
                user = credentials[0];
                password = credentials[1];
            }
        }
        return new DapsNetwork(
                NetworkType.DAPS_MAINNET,
                new BtcdClientImpl(
                        closeableHttpClient,
                        rpc.getScheme(),
                        rpc.getHost(),
                        rpc.getPort(),
                        user,
                        password
                ),
                treatTestnetAsMainnet ? new TestNet3Params() : new MainNetParams()
        );
    }

    @Bean
    public LastBlockPersister dapsMainnetLastBlockPersister(
            LastBlockRepository lastBlockRepository,
            final @Value("${etherscanner.daps.last-block.mainnet:#{null}}") Long lastBlock
    ) {
        return new LastBlockDbPersister(NetworkType.DAPS_MAINNET, lastBlockRepository, lastBlock);
    }

    @ConditionalOnBean(name = NetworkType.DAPS_MAINNET_VALUE)
    @Bean
    public DapsScanner dapsScannerMain(
            final @Qualifier(NetworkType.DAPS_MAINNET_VALUE) DapsNetwork network,
            final @Qualifier("dapsMainnetLastBlockPersister") LastBlockPersister lastBlockPersister,
            final @Value("${etherscanner.daps.polling-interval-ms}") Long pollingInterval,
            final @Value("${etherscanner.daps.commit-chain-length}") Integer commitmentChainLength
    ) {
        return new DapsScanner(
                network,
                lastBlockPersister,
                pollingInterval,
                commitmentChainLength
        );
    }

}
