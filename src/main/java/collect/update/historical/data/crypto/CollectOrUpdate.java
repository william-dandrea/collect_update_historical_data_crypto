package collect.update.historical.data.crypto;

import collect.update.historical.data.crypto.collectOrUpdate.binance.BinanceCollectOrUpdate;
import org.apache.log4j.BasicConfigurator;

/**
 * @author D'Andréa William
 */
public class CollectOrUpdate {

    public static void main(String[] args) {

        BasicConfigurator.configure();
        BinanceCollectOrUpdate binanceCollectOrUpdate = new BinanceCollectOrUpdate("src/main/resources/binance/candlesticks/", "src/main/resources/binance/describer/");
        binanceCollectOrUpdate.binanceCollectOrUpdate();

    }
}
