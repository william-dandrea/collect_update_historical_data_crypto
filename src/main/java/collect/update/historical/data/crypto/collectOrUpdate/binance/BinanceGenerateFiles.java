package collect.update.historical.data.crypto.collectOrUpdate.binance;

import collect.update.historical.data.crypto.collectOrUpdate.Utils;
import collect.update.historical.data.crypto.configs.binance.BinanceConfig;
import collect.update.historical.data.crypto.configs.binance.BinanceIntervals;
import collect.update.historical.data.crypto.configs.binance.BinanceSymbols;
import collect.update.historical.data.crypto.elements.CandleStick;
import collect.update.historical.data.crypto.elements.LastCandleStick;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Cette classe a pour objectif de récuperer les données de l'API de Binance et les stocker dans un fichier sous forme
 * de bougies japonaises. Cette classe a deux principales fonctionnalité :
 *      -> collectAndStoreFromBeginning : va génerer un nouveau fichier et le remplir des bougies japonaises de cet actif
 *      depuis sa premiere edition jusqu'a aujourd'hui
 *      -> updateAndCollectAndStoreUntilNow : va mettre a jour le fichier passé en paramètre et le completer avec les candlesticks
 *      manquants pour que le dernier candlestick present soit celui d'aujourd'hui
 *
 * @author D'Andréa William
 */
public class BinanceGenerateFiles {


    private final Logger logger = Logger.getLogger(BinanceGenerateFiles.class);
    private final String outputFilePath;
    private final String outputDescriberFilePath;
    private final BinanceIntervals candleStickInterval;
    private final BinanceSymbols symbol;

    private final ObjectMapper mapper;


    public BinanceGenerateFiles(BinanceSymbols symbol, BinanceIntervals candleStickInterval, String outputFilePath, String outputDescriberFilePath) {
        this.outputFilePath = outputFilePath;
        this.outputDescriberFilePath = outputDescriberFilePath;
        this.candleStickInterval = candleStickInterval;
        this.symbol = symbol;
        this.mapper = new ObjectMapper();
        this.mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    public void collectAndStoreFromBeginning() {

        long startTimeStamp = (new GregorianCalendar(2009, Calendar.JANUARY,3)).getTimeInMillis();
        long endTimeStamp   = (new GregorianCalendar()).getTimeInMillis();

        collectAndStore(BigDecimal.valueOf(startTimeStamp) , BigDecimal.valueOf(endTimeStamp), candleStickInterval, symbol, false, outputFilePath, outputDescriberFilePath);

    }


    public void updateAndCollectAndStoreUntilNow() {

        LastCandleStick lastCandleStick = readCandleStickDescriberFromFile(outputDescriberFilePath);
        long endTimeStamp = (new GregorianCalendar()).getTimeInMillis();

        collectAndStore(lastCandleStick.getLastTimeStamp(), BigDecimal.valueOf(endTimeStamp), candleStickInterval, symbol, true, outputFilePath, outputDescriberFilePath);

    }



    /**
     * Cette méthode a pour objectif de collecter et stocker toutes les bougies japonaises en partant du startTimeStamp
     * jusqu'au endTimeStamp
     * @param startTimeStamp le timestamp de depart
     * @param endTimeStamp le timestamp de fin
     * @param interval l'interval de temps des bougies japonaises
     * @param symbole le symbole souhaité
     * @param filePathContent le path du fichier ou nous allons enregistrer les valeurs
     * @param filePathTimeStamp le path du fichier ou nous allons enregistrer le dernier timestamp enregistré dans le
     *                          fichier de content
     * @param appendInFile true si on souhaite ajouter au fichier existant les valeurs ou false si l'on souhaite ecraser
     *                     l'ancien fichier
     */
    private void collectAndStore(BigDecimal startTimeStamp, BigDecimal endTimeStamp, BinanceIntervals interval,  BinanceSymbols symbole, boolean appendInFile, String filePathContent, String filePathTimeStamp) {

        try {


            File file = new File(filePathContent);

            FileWriter fileWriter = new FileWriter(file, appendInFile);
            SequenceWriter seqWriter = mapper.writer().writeValuesAsArray(fileWriter);

            BigDecimal timeStamp = startTimeStamp;

            while (timeStamp.compareTo(endTimeStamp) <= 0) {

                String strUrl = BinanceConfig.generateKlineUrl(symbole, interval, timeStamp, endTimeStamp, 1000);

                logger.info("Date of the first timestamp from this url : " + Utils.generateDateString(timeStamp.longValue()));
                logger.info("URL of the call API : " + strUrl);

                BinanceCandleStickParser klineParser = new BinanceCandleStickParser(strUrl);
                List<CandleStick> candleSticks = klineParser.parse();

                if (candleSticks.isEmpty()) break;

                logger.info("This file contain : " + candleSticks.size() + " elements");

                for (CandleStick candleStick : candleSticks) {
                    seqWriter.write(candleStick);
                }

                timeStamp = candleSticks.get(candleSticks.size()-1).getCloseTime();

            }

            seqWriter.close();

            generateLastCandleStickFile(timeStamp, filePathTimeStamp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Cette méthode a pour objectif de créer/editer un fichier qui contiendra le dernier timestamp du symbole placé en
     * paramètre afin de ne pas parcourir tout les élements lorsque l'on veut ajouter des nouvelles valeurs au fichier
     * @param timeStamp le timestamp que l'on souhaite ajouter au fichier - généralement ce sera la dernier timestamp généré par la fonction collectAndStore
     * @param fileLastCandleStickUrl l'URL du fichier qui recevra ce dernier timestamp
     */
    private void generateLastCandleStickFile(BigDecimal timeStamp, String fileLastCandleStickUrl) {

        File file = new File(fileLastCandleStickUrl);


        try {
            FileWriter fileWriter = new FileWriter(file, false);
            SequenceWriter seqWriter = mapper.writer().writeValuesAsArray(fileWriter);

            LastCandleStick lastCandleStick = new LastCandleStick(timeStamp);
            seqWriter.write(lastCandleStick);

            logger.info("The file who describe the last candlestick was write with the candlestick : " + lastCandleStick);

            seqWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * Cette méthode va mettre dans une liste d'objet le contenu d'un fichier généré sous la forme d'un JSON qui decrit des
     * BinanceCandleSticks
     * @param filePath le fichier en entrée
     * @return la liste d'objets qu'il y a dans ce fichier
     */
    private List<CandleStick> readCandleStickFromFile(String filePath) {

        List<CandleStick> candleSticks = null;

        File file = new File(filePath);

        try {

            candleSticks = mapper.readValue(file, new TypeReference<List<CandleStick>>(){} );

        } catch (IOException e) {
            e.printStackTrace();
        }


        return candleSticks;
    }

    /**
     * Cette méthode va lire le dernier objet ecrit dans le describer
     * @param filePath le fichier describer ou se trouve le dernier timestamp
     * @return le dernier LastCandleStick présent
     */

    LastCandleStick readCandleStickDescriberFromFile(String filePath) {

        List<LastCandleStick> candleStick = null;

        File file = new File(filePath);

        try {

            candleStick = mapper.readValue(file, new TypeReference<List<LastCandleStick>>(){} );

        } catch (IOException e) {
            e.printStackTrace();
        }


        assert candleStick != null;

        return candleStick.get(0);
    }








}
