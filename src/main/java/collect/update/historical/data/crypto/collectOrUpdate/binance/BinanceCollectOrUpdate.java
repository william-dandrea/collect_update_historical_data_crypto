package collect.update.historical.data.crypto.collectOrUpdate.binance;

import collect.update.historical.data.crypto.configs.binance.BinanceIntervals;
import collect.update.historical.data.crypto.configs.binance.BinanceSymbols;

import java.io.File;

/**
 * @author D'Andréa William
 */
public class BinanceCollectOrUpdate {


    private final String initPathCandleStick;
    private final String initPathDescriberFile;

    public BinanceCollectOrUpdate(String initPathCandleStick, String initPathDescriberFile) {
        this.initPathCandleStick = initPathCandleStick;
        this.initPathDescriberFile = initPathDescriberFile;
    }

    public void binanceCollectOrUpdate() {

        for (BinanceSymbols symbol : BinanceSymbols.values()) {
            for (BinanceIntervals interval : BinanceIntervals.values()) {

                String dataFileName = initPathCandleStick + symbol.getId() + "_" + interval.getId() + ".json";
                String infoFileName = initPathDescriberFile + symbol.getId() + "_" + interval.getId() + "_INFO.json";

                System.out.println("===>" + dataFileName);
                System.out.println("===>" + infoFileName);

                File dataFile = new File(dataFileName);
                File infoFile = new File(infoFileName);

                BinanceGenerateFiles klineCollectAndStore = new BinanceGenerateFiles(symbol, interval, dataFileName, infoFileName);

                if (dataFile.exists() && infoFile.exists()) {
                    // Update
                    klineCollectAndStore.updateAndCollectAndStoreUntilNow();

                } else {
                    // Collect from the start
                    klineCollectAndStore.collectAndStoreFromBeginning();
                }
            }
        }
    }


}
