package org.example.searadar.mr231_3.convert;

import org.apache.camel.Exchange;
import ru.oogis.searadar.api.convert.SearadarExchangeConverter;
import ru.oogis.searadar.api.message.InvalidMessage;
import ru.oogis.searadar.api.message.RadarSystemDataMessage;
import ru.oogis.searadar.api.message.SearadarStationMessage;
import ru.oogis.searadar.api.message.TrackedTargetMessage;
import ru.oogis.searadar.api.types.IFF;
import ru.oogis.searadar.api.types.TargetStatus;
import ru.oogis.searadar.api.types.TargetType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Класс конвертера для расшифровки сообщений от станции
 */
public class Mr231_3Converter implements SearadarExchangeConverter {

    /**
     * DISTANCE_SCALE - статичный массив чисел шкалы дальности
     * fields - строковый массив для хранения разобранного сообщения
     * msgType - строковая переменная для хранения типа сообщения от станции
     */
    private static final Double[] DISTANCE_SCALE = {0.125, 0.25, 0.5, 1.5, 3.0, 6.0, 12.0, 24.0, 48.0, 96.0};

    private String[] fields;
    private String msgType;

    @Override
    public List<SearadarStationMessage> convert(Exchange exchange) {
        return convert(exchange.getIn().getBody(String.class));
    }

    /**
     * Функция convert принимает сообщение от станции и разбирает его
     * @param message - сообщение от станции
     * @return - разобранное сообщение от станции
     */
    public List<SearadarStationMessage> convert(String message) {
        List<SearadarStationMessage> msgList = new ArrayList<>();

        readFields(message);

        switch (msgType) {
            case "TTM":
                msgList.add(getTTM());
                break;
            case "RSD":
                RadarSystemDataMessage rsd = getRSD();
                InvalidMessage invalidMessage = checkRSD(rsd);

                if (invalidMessage != null) msgList.add(invalidMessage);
                else msgList.add(rsd);
                break;
        }

        return msgList;
    }

    /**
     * Функция readFields разбивает полученное сообщение на строки записывает в переменную fields
     * В переменную msgType записывается тип сообщения от станции
     * @param msg - сообщение от станции
     */
    private void readFields(String msg) {

        String temp = msg.substring(3, msg.indexOf("*")).trim();

        fields = temp.split(Pattern.quote(","));
        msgType = fields[0];
    }

    /**
     * Функция getTTM разбирает формуляр цели, полученный от станции
     * @return - возвращение расшифрованного сообщения
     */
    private TrackedTargetMessage getTTM() {
        TrackedTargetMessage ttm = new TrackedTargetMessage();
        Long msgRecTimeMills = System.currentTimeMillis();

        ttm.setMsgTime(msgRecTimeMills);
        TargetStatus status = TargetStatus.UNRELIABLE_DATA;
        IFF iff = IFF.UNKNOWN;
        TargetType targetType = TargetType.UNKNOWN;

        /**
         * Статус цели
         */
        switch (fields[12]) {
            case "L":
                status = TargetStatus.LOST;
                break;
            case "Q":
                status = TargetStatus.UNRELIABLE_DATA;
                break;
            case "T":
                status = TargetStatus.TRACKED;
                break;
        }

        /**
         * Признак опознавания цели
         */
        switch (fields[11]) {
            case "b":
                iff = IFF.FRIEND;
                break;
            case "p":
                iff = IFF.FOE;
                break;
            case "d":
                iff = IFF.UNKNOWN;
                break;
        }

        ttm.setMsgRecTime(new Timestamp(System.currentTimeMillis()));
        ttm.setTargetNumber(Integer.parseInt(fields[1]));
        ttm.setDistance(Double.parseDouble(fields[2]));
        ttm.setBearing(Double.parseDouble(fields[3]));
        ttm.setCourse(Double.parseDouble(fields[6]));
        ttm.setSpeed(Double.parseDouble(fields[5]));
        ttm.setStatus(status);
        ttm.setIff(iff);

        ttm.setType(targetType);

        return ttm;
    }

    /**
     * Функция getRSD разбирает формуляр состояния НРЛС, полученный от станции
     * @return - возвращение расшифрованного сообщения
     */
    private RadarSystemDataMessage getRSD() {
        RadarSystemDataMessage rsd = new RadarSystemDataMessage();

        rsd.setMsgRecTime(new Timestamp(System.currentTimeMillis()));
        rsd.setInitialDistance(Double.parseDouble(fields[1]));
        rsd.setInitialBearing(Double.parseDouble(fields[2]));
        rsd.setMovingCircleOfDistance(Double.parseDouble(fields[3]));
        rsd.setBearing(Double.parseDouble(fields[4]));
        rsd.setDistanceFromShip(Double.parseDouble(fields[9]));
        rsd.setBearing2(Double.parseDouble(fields[10]));
        rsd.setDistanceScale(Double.parseDouble(fields[11]));
        rsd.setDistanceUnit(fields[12]);
        rsd.setDisplayOrientation(fields[13]);
        rsd.setWorkingMode(fields[14]);

        return rsd;
    }

    /**
     * Функция для проверки валидности RSD сообщения станции
     * @param rsd - RSD сообщение от станции
     * @return - Если сообщение валидно - null, если нет - сообщение об ошибке и указание шкалы дальности
     */
    private InvalidMessage checkRSD(RadarSystemDataMessage rsd) {

        InvalidMessage invalidMessage = new InvalidMessage();
        String infoMsg = "";

        /**
         * Если шкала дальности rsd не соответствует ни одному элементу массива DISTANCE_SCALE, то выдаём сообщение об ошибке
         */
        if (!Arrays.asList(DISTANCE_SCALE).contains(rsd.getDistanceScale())) {
            infoMsg = "RDS message. Wrong distance scale value: " + rsd.getDistanceScale();
            invalidMessage.setInfoMsg(infoMsg);
            return invalidMessage;
        }

        return null;
    }
}
