package io.mywish.bot.integration.services;

import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.repositories.NetworkRepository;
import io.mywish.bot.service.InformationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotStatisticProvider implements InformationProvider {
    @Autowired
    private NetworkRepository networkRepository;

    private final Map<NetworkType, String> networkNames = new HashMap<NetworkType, String>() {{
        put(NetworkType.ETHEREUM_MAINNET, "Ethereum");
        put(NetworkType.BINANCE_MAINNET, "Binance");
    }};

    @Override
    public SendMessage getInformation(String userName) {
        StringBuilder stringBuilder = new StringBuilder();

        networkRepository.findAll()
                .forEach(network -> stringBuilder.append("\n*")
                        .append(networkNames.getOrDefault(network.getType(), "Unknown Network"))
                        .append("*\n"));
                    return new SendMessage().setText(stringBuilder.toString()).enableMarkdown(true);
    }

    @Override
    public boolean isAvailable(String userName) {
        return true;
    }
}
