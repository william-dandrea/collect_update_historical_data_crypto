package collect.update.historical.data.crypto.elements;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author D'Andréa William
 */
public class LastCandleStick implements Serializable {

    @JsonProperty("lastTimeStamp")
    private BigDecimal lastTimeStamp;

    public LastCandleStick() { }

    public LastCandleStick(BigDecimal lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    public BigDecimal getLastTimeStamp() {
        return lastTimeStamp;
    }
}
