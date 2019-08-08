package io.mywish.bot.integration;

import io.mywish.bot.BotModule;
import io.mywish.bot.integration.services.BotIntegration;
import io.mywish.bot.integration.services.impl.*;
import io.mywish.bot.service.MyWishBot;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.*;

@ComponentScan
@Configuration
@PropertySource("classpath:bot-integration.properties")
@Import(BotModule.class)
public class BotIntegrationModule {

    @Bean
    public EtherescanExplorer etherescanExplorer() {
        return new EtherescanExplorer();
    }

    @Bean
    public BinanceExplorer binanceExplorer() {
        return new BinanceExplorer();
    }

    @Bean
    @ConditionalOnBean(MyWishBot.class)
    public BotIntegration botIntegration() {
        return new BotIntegration();
    }
}
