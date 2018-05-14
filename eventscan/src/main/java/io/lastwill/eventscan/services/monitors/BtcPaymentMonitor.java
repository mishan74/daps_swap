package io.lastwill.eventscan.services.monitors;

import io.lastwill.eventscan.events.ProductPaymentEvent;
import io.lastwill.eventscan.model.CryptoCurrency;
import io.lastwill.eventscan.repositories.ProductRepository;
import io.lastwill.eventscan.services.Btc2RskNetworkConverter;
import io.mywish.scanner.model.NetworkType;
import io.mywish.scanner.model.NewBtcBlockEvent;
import io.mywish.scanner.services.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.TransactionOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Slf4j
@Component
public class BtcPaymentMonitor {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private Btc2RskNetworkConverter btc2RskNetworkConverter;
    @Autowired
    private EventPublisher eventPublisher;

    @EventListener
    public void handleBtcBlock(NewBtcBlockEvent event) {
        NetworkType targetNetwork = btc2RskNetworkConverter.convert(event.getNetworkType());
        Set<String> addresses = event.getAddressTransactionOutputs().keySet();
        if (addresses.isEmpty()) {
            return;
        }
        productRepository.findLastWillByBtcAddresses(addresses, targetNetwork)
                .forEach(productLastWill -> {
                    List<TransactionOutput> outputs = event.getAddressTransactionOutputs().get(
                            productLastWill.getBtcKey().getAddress()
                    );
                    IntStream.range(0, outputs.size())
                            .forEach(index -> {
                                TransactionOutput output = outputs.get(index);
                                if (output.getParentTransaction() == null) {
                                    log.warn("Skip it. Output {} has not parent transaction.", output);
                                    return;
                                }
                                eventPublisher.publish(new ProductPaymentEvent(
                                        targetNetwork,
                                        null,
                                        productLastWill.getBtcKey().getAddress(),
                                        BigInteger.valueOf(output.getValue().value),
                                        CryptoCurrency.BTC,
                                        true,
                                        productLastWill,
                                        output
                                ));
                            });
                });
    }
}