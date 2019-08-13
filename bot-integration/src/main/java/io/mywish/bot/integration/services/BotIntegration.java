package io.mywish.bot.integration.services;

import io.lastwill.eventscan.events.model.dapsswap.LowBalanceEvent;
import io.lastwill.eventscan.events.model.dapsswap.TokensBurnedEvent;
import io.lastwill.eventscan.events.model.dapsswap.TokensTransferErrorEvent;
import io.lastwill.eventscan.events.model.dapsswap.TokensTransferredEvent;
import io.lastwill.eventscan.events.model.utility.NetworkStuckEvent;
import io.lastwill.eventscan.model.CryptoCurrency;
import io.lastwill.eventscan.model.NetworkType;
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
        put(NetworkType.DAPS_MAINNET, "DAPS");
    }};

    private final String defaultNetwork = "unknown";

    @EventListener
    private void onWishSwapLowBalance(final LowBalanceEvent event) {
        Long connectId = event.getSwapEntry().getLinkEntry().getId();
        Long swapId = event.getSwapEntry().getId();
        String fromAddress = event.getFromAddress();
        String fromAddressLink = explorerProvider.getOrStub(NetworkType.DAPS_MAINNET)
                .buildToAddress(fromAddress);
        String toAddress = event.getSwapEntry().getLinkEntry().getDapsAddress();
        String toAddressLink = explorerProvider.getOrStub(NetworkType.DAPS_MAINNET)
                .buildToAddress(toAddress);
        String ethAddress = event.getSwapEntry().getLinkEntry().getEthAddress();
        String ethAddressLink = explorerProvider.getOrStub(NetworkType.ETHEREUM_MAINNET)
                .buildToAddress(ethAddress);
        String need = toCurrency(event.getCoin(), event.getDecimals(), event.getNeed());
        String have = toCurrency(event.getCoin(), event.getDecimals(), event.getHave());

        bot.onWishSwapLowBalance(connectId, swapId, fromAddress, fromAddressLink, toAddress, toAddressLink, ethAddress, ethAddressLink, need, have);
    }

    @EventListener
    private void onWishSwapBurn(final TokensBurnedEvent event) {
        Long linkId = event.getSwapEntry().getLinkEntry().getId();
        Long swapId = event.getSwapEntry().getId();
        String ethAddress = event.getEthAddress();
        String ethAddressLink = explorerProvider.getOrStub(NetworkType.ETHEREUM_MAINNET)
                .buildToAddress(ethAddress);
        String bnbAddress = event.getDapsAddress() != null ? event.getDapsAddress() : "not connected";
        String bnbAddressLink = event.getDapsAddress() != null
                ? explorerProvider.getOrStub(NetworkType.DAPS_MAINNET).buildToAddress(bnbAddress)
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
        String bnbTxHash = event.getEthEntry().getDapsTxHash();
        String bnbTxHashLink = bnbTxHash != null
                ? explorerProvider.getOrStub(NetworkType.DAPS_MAINNET).buildToTransaction(bnbTxHash)
                : "";
        String bnbAddress = event.getEthEntry().getLinkEntry().getDapsAddress();
        String bnbAddressLink = bnbAddress != null
                ? explorerProvider.getOrStub(NetworkType.DAPS_MAINNET).buildToAddress(bnbAddress)
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
        String transferTxLink = explorerProvider.getOrStub(NetworkType.DAPS_MAINNET)
                .buildToTransaction(event.getEthEntry().getDapsTxHash());
        String bnbAddress = event.getEthEntry().getLinkEntry().getDapsAddress();
        String bnbAddressLink = explorerProvider.getOrStub(NetworkType.DAPS_MAINNET)
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
