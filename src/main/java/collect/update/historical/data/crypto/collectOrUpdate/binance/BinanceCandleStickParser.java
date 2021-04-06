package collect.update.historical.data.crypto.collectOrUpdate.binance;

import collect.update.historical.data.crypto.elements.CandleStick;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author D'Andréa William
 */
public class BinanceCandleStickParser {


    private final String url;
    private final ObjectMapper om;

    public BinanceCandleStickParser(String url) {
        this.url = url;
        om = new ObjectMapper();
    }


    public List<CandleStick> parse() {

        try {
            List<String> inputLines = generateLinesFromUrl();
            assert inputLines != null;


            BigDecimal[][] a = om.readValue(inputLines.get(0), BigDecimal[][].class);

            List<CandleStick> binanceCandleSticks = new ArrayList<>();

            for (BigDecimal[] element : a) {

                /*
                element[0],     // Open time
                element[1],     // Open price
                element[2],     // High Price
                element[3],     // Low Price
                element[4],     // Close Price
                element[5],     // Volume
                element[6],     // Close Time
                element[7],     // Quote asset volume
                element[8],     // Number of trades
                element[9],     // Taker buy base asset volume
                element[10],    // Taker buy quote asset volume
                element[11]     // Ignore
                 */

                CandleStick binanceCandleStick = new CandleStick(element[1], element[4], element[2], element[3], element[0], element[6], element[5]);

                binanceCandleSticks.add(binanceCandleStick);
            }

            return binanceCandleSticks;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }



    private List<String> generateLinesFromUrl() {
        try {

            URL realUrl = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(realUrl.openStream()));

            List<String> inputLines = new ArrayList<>();

            String inputLine;
            while ((inputLine = in.readLine()) != null){
                inputLines.add(inputLine);
            }
            in.close();

            return inputLines;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
