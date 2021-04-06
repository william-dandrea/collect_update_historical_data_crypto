package collect.update.historical.data.crypto.elements;

import java.math.BigDecimal;

/**
 * @author D'Andréa William
 */
public class CandleStick {

    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal openTime;
    private BigDecimal closeTime;
    private BigDecimal volume;

    public CandleStick() {}

    public CandleStick(BigDecimal openPrice, BigDecimal closePrice, BigDecimal highPrice, BigDecimal lowPrice, BigDecimal openTime, BigDecimal closeTime, BigDecimal volume) {
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.volume = volume;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public BigDecimal getOpenTime() {
        return openTime;
    }

    public BigDecimal getCloseTime() {
        return closeTime;
    }

    public BigDecimal getVolume() {
        return volume;
    }
}
