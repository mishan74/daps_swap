package io.mywish.bot.integration.services;

import io.lastwill.eventscan.events.model.utility.NetworkStuckEvent;
import io.lastwill.eventscan.events.model.utility.PendingStuckEvent;
import io.lastwill.eventscan.events.model.wishbnbswap.LowBalanceEvent;
import io.lastwill.eventscan.events.model.wishbnbswap.TokensBurnedEvent;
import io.lastwill.eventscan.events.model.wishbnbswap.TokensTransferErrorEvent;
import io.lastwill.eventscan.events.model.wishbnbswap.TokensTransferredEvent;
import io.lastwill.eventscan.model.*;
import io.mywish.bot.service.MyWishBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BotIntegration {
    @Autowired
    private MyWishBot bot;

    @Autowired
    protected BlockchainExplorerProvider explorerProvider;

    private final ZoneId localZone = ZoneId.of("Europe/Moscow");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Map<NetworkType, String> networkName = new HashMap<NetworkType, String>() {{
        put(NetworkType.ETHEREUM_MAINNET, "ETH");
        put(NetworkType.BINANCE_MAINNET, "BNB");
    }};

    private final String defaultNetwork = "unknown";

    @EventListener
    private void onWishSwapLowBalance(final LowBalanceEvent event) {
        Long linkId = event.getSwapEntry().getLinkEntry().getId();
        Long swapId = event.getSwapEntry().getId();
        String fromAddress = event.getFromAddress();
        String fromAddressLink = explorerProvider.getOrStub(NetworkType.BINANCE_MAINNET)
                .buildToAddress(fromAddress);
        String toAddress = event.getSwapEntry().getLinkEntry().getBnbAddress();
        String toAddressLink = explorerProvider.getOrStub(NetworkType.BINANCE_MAINNET)
                .buildToAddress(toAddress);
        String ethAddress = event.getSwapEntry().getLinkEntry().getEthAddress();
        String ethAddressLink = explorerProvider.getOrStub(NetworkType.ETHEREUM_MAINNET)
                .buildToAddress(ethAddress);
        String need = toCurrency(event.getCoin(), event.getDecimals(), event.getNeed());
        String have = toCurrency(event.getCoin(), event.getDecimals(), event.getHave());

        bot.onWishSwapLowBalance(linkId, swapId, fromAddress, fromAddressLink, toAddress, toAddressLink, ethAddress, ethAddressLink, need, have);
    }

    @EventListener
    private void onWishSwapBurn(final TokensBurnedEvent event) {
        Long linkId = event.getSwapEntry().getLinkEntry().getId();
        Long swapId = event.getSwapEntry().getId();
        String ethAddress = event.getEthAddress();
        String ethAddressLink = explorerProvider.getOrStub(NetworkType.ETHEREUM_MAINNET)
                .buildToAddress(ethAddress);
        String bnbAddress = event.getBnbAddress() != null ? event.getBnbAddress() : "not linked";
        String bnbAddressLink = event.getBnbAddress() != null
                ? explorerProvider.getOrStub(NetworkType.BINANCE_MAINNET).buildToAddress(bnbAddress)
                : "";
        String amount = toCurrency(event.getCoin(), event.getDecimals(), event.getSwapEntry().getAmount());
        String burnTx = explorerProvider.getOrStub(NetworkType.ETHEREUM_MAINNET)
                .buildToTransaction(event.getSwapEntry().getEthTxHash());
        bot.onWishSwapBurn(linkId, swapId, ethAddress, ethAddressLink, bnbAddress, bnbAddressLink, amount, burnTx);
    }

    @EventListener
    private void onWishSwapTransferError(final TokensTransferErrorEvent event) {
        Long linkId = event.getEthEntry().getLinkEntry().getId();
        Long swapId = event.getEthEntry().getId();
        String amount = toCurrency(event.getCoin(), event.getDecimals(), event.getEthEntry().getAmount());
        String bnbTxHash = event.getEthEntry().getBnbTxHash();
        String bnbTxHashLink = bnbTxHash != null
                ? explorerProvider.getOrStub(NetworkType.BINANCE_MAINNET).buildToTransaction(bnbTxHash)
                : "";
        String bnbAddress = event.getEthEntry().getLinkEntry().getBnbAddress();
        String bnbAddressLink = bnbAddress != null
                ? explorerProvider.getOrStub(NetworkType.BINANCE_MAINNET).buildToAddress(bnbAddress)
                : "";
        String ethAddress = event.getEthEntry().getLinkEntry().getEthAddress();
        String ethAddressLink = ethAddress != null
                ? explorerProvider.getOrStub(NetworkType.ETHEREUM_MAINNET).buildToAddress(ethAddress)
                : "";
        bot.onWishSwapTransferError(linkId, swapId, amount, bnbTxHashLink, bnbAddress, bnbAddressLink, ethAddress, ethAddressLink);
    }

    @EventListener
    private void onWishSwapTransfer(final TokensTransferredEvent event) {
        Long linkId = event.getEthEntry().getLinkEntry().getId();
        Long swapId = event.getEthEntry().getId();
        String amount = toCurrency(event.getCoin(), event.getDecimals(), event.getEthEntry().getAmount());
        String transferTxLink = explorerProvider.getOrStub(NetworkType.BINANCE_MAINNET)
                .buildToTransaction(event.getEthEntry().getBnbTxHash());
        String bnbAddress = event.getEthEntry().getLinkEntry().getBnbAddress();
        String bnbAddressLink = explorerProvider.getOrStub(NetworkType.BINANCE_MAINNET)
                .buildToAddress(bnbAddress);
        String ethAddress = event.getEthEntry().getLinkEntry().getEthAddress();
        String ethAddressLink = explorerProvider.getOrStub(NetworkType.ETHEREUM_MAINNET)
                .buildToAddress(ethAddress);

        bot.onWishSwapTransfer(linkId, swapId, amount, transferTxLink, bnbAddress, bnbAddressLink, ethAddress, ethAddressLink);
    }


    @EventListener
    private void onNetworkStuck(final NetworkStuckEvent event) {
        final String network = networkName.getOrDefault(event.getNetworkType(), defaultNetwork);
        String lastBlock = formatToLocal(event.getReceivedTime());
        final String blockLink = explorerProvider
                .getOrStub(event.getNetworkType())
                .buildToBlock(event.getLastBlockNo());
        bot.sendToAllWithMarkdown("Network " + network + " *stuck!* Last block was at " + lastBlock + " [" + event.getLastBlockNo() + "](" + blockLink + ").");
    }

    @EventListener
    private void onPendingStuck(final PendingStuckEvent event) {
        final String network = networkName.getOrDefault(event.getNetworkType(), defaultNetwork);
        String lastBlock = formatToLocal(event.getReceivedTime());
        bot.sendToAllWithMarkdown("*No pending transactions* for the network " + network + "! Last pending was at " + lastBlock + ", count: " + event.getCount() + ".");
    }



    private static String toCurrency(CryptoCurrency currency, BigInteger amount) {
        return toCurrency(currency.toString(), currency.getDecimals(), amount);
    }

    @SuppressWarnings("BigDecimalMethodWithoutRoundingCalled")
    private static String toCurrency(String currency, int decimals, BigInteger amount) {
        BigDecimal bdAmount = new BigDecimal(amount)
                .divide(BigDecimal.TEN.pow(decimals));

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(decimals);
        df.setMinimumFractionDigits(0);

        return df.format(bdAmount) + " " + currency;
    }

    private String formatToLocal(LocalDateTime localDateTime) {
        return ZonedDateTime.ofInstant(
                localDateTime.toInstant(ZoneOffset.UTC),
                this.localZone
        )
                .format(dateFormatter);
    }
}
