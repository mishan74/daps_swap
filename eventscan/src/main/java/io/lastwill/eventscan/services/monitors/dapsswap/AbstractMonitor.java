package io.lastwill.eventscan.services.monitors.dapsswap;

import io.mywish.blockchain.WrapperTransaction;
import io.mywish.scanner.model.NewBlockEvent;
import org.springframework.context.event.EventListener;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AbstractMonitor {
    @EventListener
    public void onBlock(NewBlockEvent newBlockEvent) {
        if (checkCondition(newBlockEvent)) {
            processBlockEvent(newBlockEvent);
        }
    }

    protected boolean checkCondition(NewBlockEvent newBlockEvent) {
        return true;
    }

    protected abstract void processBlockEvent(NewBlockEvent newBlockEvent);

    protected Stream<WrapperTransaction> filterTransactionsByAddress(NewBlockEvent blockEvent, String filterAddress) {
        return blockEvent
                .getTransactionsByAddress()
                .entrySet()
                .stream()
                .filter(entry -> filterAddress.equalsIgnoreCase(entry.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream);
    }
}
