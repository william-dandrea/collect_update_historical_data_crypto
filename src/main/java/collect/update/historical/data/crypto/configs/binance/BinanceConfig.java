package collect.update.historical.data.crypto.configs.binance;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author D'Andréa William
 */
public class BinanceConfig {

    public static final String URL_NAME = "binance.com";
    public static final String API_URL = "https://api.binance.com/api/v3";
    public static final String WSS_API_STREAM = "wss://stream.binance.com:9443/ws";
    public static final String ASSET_INFO_URL = "https://binance.com/";


    // Klines elements : https://github.com/binance/binance-spot-api-docs/blob/master/rest-api.md#klinecandlestick-data
    public static final String KLINE_URL = "/klines?";
    public static final String KLINE_SYMBOL = "symbol=";
    public static final String KLINE_INTERVAL = "interval=";
    public static final String KLINE_START_TIME = "startTime=";
    public static final String KLINE_END_TIME = "endTime=";
    public static final String KLINE_LIMIT = "limit=";




    // https://api.binance.com/api/v3/klines?symbol=BTCUSDT&interval=1h&startTime=2674800000&endTime=1616152560231
    public static String generateKlineUrl(BinanceSymbols symbol, BinanceIntervals interval, Calendar startTime, Calendar endTime, int limit) {

        if (limit >= 1000) limit = 1000;

        String symbolComplete = KLINE_SYMBOL + symbol.getId();
        String intervalComplete = KLINE_INTERVAL +  interval.getId();
        String startTimeComplete = KLINE_START_TIME + startTime.getTimeInMillis();
        String endTimeComplete = KLINE_END_TIME + endTime.getTimeInMillis();
        String limitComplete = KLINE_LIMIT + limit;

        return API_URL + KLINE_URL
                + symbolComplete + "&"
                + intervalComplete + "&"
                + startTimeComplete + "&"
                + endTimeComplete + "&"
                + limitComplete;

    }

    public static String generateKlineUrl(BinanceSymbols symbol, BinanceIntervals interval, BigDecimal startTime, BigDecimal endTime, int limit) {


        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startTime.longValue());

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endTime.longValue());

        return generateKlineUrl(symbol, interval, startCalendar, endCalendar, limit);
    }

    public static String generateKlineUrl(BinanceSymbols symbol, BinanceIntervals interval, long startTime, long endTime, int limit) {

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endTime);

        return generateKlineUrl(symbol, interval, startCalendar, endCalendar, limit);
    }



}
