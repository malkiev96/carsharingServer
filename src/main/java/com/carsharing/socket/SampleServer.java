package com.carsharing.socket;

import com.carsharing.model.Tracker;
import com.carsharing.model.TrackerData;
import com.carsharing.service.TrackerDataService;
import com.carsharing.service.TrackerService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Timestamp;

public class SampleServer extends Thread {


    private TrackerService trackerService;
    private TrackerDataService trackerDataService;
    private Socket s;
    private Tracker tracker;

    String time;//9-16
    String module1;//17-18
    String lat;//27-34 Разделить на 600000
    String lon;//35-42
    String speed;//43-50 Floating Point to Hex Converter
    String mileage;//51-58 Floating Point to Hex Converter
    String stateOut1;//59-60
    String fuelLevel;//61-62

    String hexId;

    public SampleServer(Socket s, TrackerService trackerService, TrackerDataService dataService) {

        this.s = s;
        this.trackerService = trackerService;
        this.trackerDataService = dataService;

        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    public void run() {
        try {
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            //Авторизация трекера
            if (auth(is,os)){

                while (true) {

                    tracker = trackerService.getById(tracker.getId());

                    byte buffer[] = new byte[100];
                    int count;
                    count = is.read(buffer);

                    if (tracker.getAction()==1){
                        String post = "40 4e 54 43 ";
                        post += hexId;
                        post += " 01 00 00 00 04 00 63 7e 2a 21 31 59";
                        System.out.println("Закрываем двери"+post);
                        os.write(fromHexString(post));
                        tracker.setAction(0);
                        trackerService.save(tracker);
                    }
                    if (tracker.getAction()==2){

                        String post = "40 4e 54 43 ";
                        post += hexId;
                        post += " 01 00 00 00 04 00 74 69 2a 21 31 4e";  //40 4e 54 43 01 00 00 00 01 00 00 00 04 00 74 69 2a 21 31 4e
                        System.out.println("Открываем двери"+post);
                        os.write(fromHexString(post));
                        tracker.setAction(0);
                        trackerService.save(tracker);
                    }

                    if (count != 0) {

                        byte getBytes[] = new byte[count];
                        System.arraycopy(buffer, 0, getBytes, 0, count);
                        String text = bytesToHex(getBytes);
                        System.out.println("get:" + text);

                        String head;

                        if (text.equals("7F")){
                            head = "7F";
                            System.out.println("ПИНГ");
                        }else {
                            head = text.substring(0,4);
                        }


                        if (head.equals("7E43")) {
                            //~С Трекер отправил текущее состояние
                            //Считываем данные
                            //В ответ ничего отправлять не надо
                            time = text.substring(16, 24);
                            module1 = text.substring(24, 26);
                            lat = text.substring(34, 42);
                            lon = text.substring(42, 50);
                            speed = text.substring(50, 58);
                            mileage = text.substring(58, 66);
                            stateOut1 = text.substring(66, 68);
                            fuelLevel = text.substring(68, 70);

                            time = time.substring(6, 8) + time.substring(4, 6) + time.substring(2, 4) + time.substring(0, 2);
                            lat = lat.substring(6, 8) + lat.substring(4, 6) + lat.substring(2, 4) + lat.substring(0, 2);
                            lon = lon.substring(6, 8) + lon.substring(4, 6) + lon.substring(2, 4) + lon.substring(0, 2);
                            speed = speed.substring(6, 8) + speed.substring(4, 6) + speed.substring(2, 4) + speed.substring(0, 2);
                            mileage = mileage.substring(6, 8) + mileage.substring(4, 6) + mileage.substring(2, 4) + mileage.substring(0, 2);

                            float floatSpeed = getFloat32(speed);
                            float floatMileage = getFloat32(mileage);

                            int intTime = Integer.parseInt(time, 16);
                            int intModule1 = Integer.parseInt(module1, 16);
                            int intLat = Integer.parseInt(lat, 16);
                            int intLon = Integer.parseInt(lon, 16);
                            int intStageOut1 = Integer.parseInt(stateOut1, 16);
                            int intFuelLevel = Integer.parseInt(fuelLevel, 16);
                            System.out.println("ТОПЛИВО "+intFuelLevel);

                            double doubleLat = (double)intLat/600000;
                            double doubleLon = (double)intLon/600000;


                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            TrackerData data = new TrackerData();
                            data.setSpeed((int) floatSpeed);
                            data.setFuelLevel(intFuelLevel);
                            data.setLat(doubleLat);
                            data.setLon(doubleLon);
                            data.setTimestamp(timestamp);
                            data.setMileage((double) floatMileage);

                            if (intModule1==0){//0 128
                                data.setEngineOn(false);
                            }else data.setEngineOn(true);

                            if (intStageOut1==0){// 0 1
                                data.setOpened(true);
                            }else data.setOpened(false);


                            data.setTracker(tracker);

                            trackerDataService.save(data);

                        }

                    }
                }
            }
        }
        catch(Exception e) {
            System.out.println("init error: "+e);
            e.printStackTrace();
        }finally {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean auth(InputStream is, OutputStream os) throws IOException {
        boolean start = true;
        boolean endAuth = false;
        String imei;


        while (!endAuth) {

            byte buffer[] = new byte[100];
            int count;
            count = is.read(buffer);

            if (count != 0) {
                if (start) {
                    byte inputBytes[] = new byte[count];
                    System.arraycopy(buffer, 0, inputBytes, 0, count);
                    String text = bytesToHex(inputBytes);
                    String dataStr = new String(inputBytes);
                    imei = dataStr.substring(dataStr.length()-15,dataStr.length());
                    String id = text.substring(16,24);
                    hexId = id;
                    id = id.substring(6,8)+id.substring(4,6)+id.substring(2,4)+id.substring(0,2);
                    int intId = Integer.parseInt(id,16);
                    hexId = hexId.substring(0,2)+" "+hexId.substring(2,4)+" "+hexId.substring(4,6)+" "+hexId.substring(6,8);

                    //Проверяем трекер с БД
                    tracker = trackerService.testAuth(intId,imei);

                    if (tracker==null){
                        return false;
                    }

                    System.out.println("Авторизация успешна, IMEI - "+imei+" ID -  "+intId);
                    String post = "40 4e 54 43 01 00 00 00 ";
                    post += hexId;
                    post += " 03 00 45 5e 2a 3c 53";
                    byte outBytes[] = fromHexString(post);
                    os.write(outBytes);
                    start = false;
                } else {
                    String post = "40 4e 54 43 01 00 00 00 ";
                    post += hexId;
                    post += " 09 00 b1 a0 2a 3c 46 4c 45 58 b0 0a 0a";
                    byte postBytes[] = fromHexString(post);
                    os.write(postBytes);
                    endAuth = true;
                }
            }
        }
        return endAuth;
    }

    private static byte[] fromHexString(final String s) {
        String[] v = s.split(" ");
        byte[] arr = new byte[v.length];
        int i = 0;
        for(String val: v) {
            arr[i++] =  Integer.decode("0x" + val).byteValue();

        }
        return arr;
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static byte crc8 (byte[] buffer) {
        byte crc = (byte) 0xFF;
        for (byte b : buffer) {
            crc ^= b;
            for (int i = 0; i < 8; i++) {
                crc = (crc & 0x80) != 0 ? (byte) ((crc << 1) ^ 0x31) : (byte) (crc << 1);
            }
        }
        return crc;
    }

    // Convert the 32-bit binary into the decimal
    private static float getFloat32( String dex ) {
        int i = Integer.parseInt(dex,16);
        dex = Integer.toBinaryString(i);
        int intBits = Integer.parseInt(dex, 2);
        float myFloat = Float.intBitsToFloat(intBits);
        return myFloat;
    }

    // Get 32-bit IEEE 754 format of the decimal value
    private static String getBinary32( float value ) {
        int intBits = Float.floatToIntBits(value);
        String binary = Integer.toBinaryString(intBits);
        return binary;
    }

}
