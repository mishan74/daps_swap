package io.lastwill.eventscan.utils;

import io.lastwill.eventscan.model.CryptoCurrency;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CurrencyUtil {
    public static BigInteger convertEthToDaps(BigInteger amount) {
        return amount
                .multiply(BigInteger.TEN.pow(CryptoCurrency.DAPS.getDecimals()))
                .divide(BigInteger.TEN.pow(CryptoCurrency.ETH_DAPS.getDecimals()));
    }

    @SuppressWarnings("BigDecimalMethodWithoutRoundingCalled")
    public static String toString(BigInteger dapsAmount) {
        int decimals = CryptoCurrency.DAPS.getDecimals();
        BigDecimal bdAmount = new BigDecimal(dapsAmount)
                .divide(BigDecimal.TEN.pow(decimals));

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(decimals);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        DecimalFormatSymbols pointFormatSymbol = new DecimalFormatSymbols(Locale.getDefault());
        pointFormatSymbol.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(pointFormatSymbol);

        return df.format(bdAmount);
    }
}
