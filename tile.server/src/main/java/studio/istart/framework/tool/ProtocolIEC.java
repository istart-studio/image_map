package studio.istart.framework.tool;

import com.google.common.primitives.Ints;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Se7en on 2016/5/3.
 * Desc:
 */
public class ProtocolIEC {

    /**
     * IEC 61162-1 - 校验和
     *
     * @param sentence
     * @return
     */
    public static String getCheckSum(String sentence) throws UnsupportedEncodingException {
        int check = 0;
        byte[] chars = sentence.getBytes("UTF-8");
        for (int i = 0; i < chars.length; i++) {
            check ^= chars[i];
        }
        check &= 0xFF;
        return String.format("%1$02X", check);
    }

    public static class IEC61162 {

        /**
         * 获取压缩内容的字节内容
         * @param content 压缩的数据内容
         * @return 6位二进制
         */
        public static String toBits(String content) {
            String bits = "";
            String[] words = content.split("");
            byte[] strBytes = content.getBytes(Charset.forName("UTF-8"));
            for (int i = 0; i < strBytes.length; i++) {

                byte strByte = strBytes[i];
                int flag = Ints.tryParse(String.valueOf(strByte));

                int eBitVal = flag + 40;
                if (eBitVal <= 128) {
                    eBitVal = eBitVal + 40;
                } else {
                    eBitVal = eBitVal + 32;
                }
                /*转换为6位2进制并输出*/
                String sexBitVal = String.valueOf(Integer.toBinaryString(eBitVal)).substring(2, 8);
                bits += sexBitVal;
//                System.out.println(words[i] + " ==>：" + sexBitVal);
            }

//            System.out.println("bits:" + bits);
//            System.out.println("bits length:" + bits.length());
            return bits;
        }

        /*Bits 1-6 = Identifier for this message*/
        public static String getMsgId(String bits) {
            String msgidBit = bits.substring(0, 6);
            String msgid = new BigInteger(msgidBit, 2).toString();
            System.out.println("msgid:" + msgid);
            return msgid;
        }

        /* Bits 9-38 */
        public static String getMMSI(String bits) {
            String mmsiBit = bits.substring(8, 38);
            String mmsi = new BigInteger(mmsiBit, 2).toString();
            System.out.println("mmsi:" + mmsi);
            return mmsi;
        }

        /*Bits 39-42 = Navigational status 0000 = underway using engine*/
        public static String getNavistat(String bits){
            String navistatBit = bits.substring(38, 42);
            String navista = new BigInteger(navistatBit, 2).toString();
            System.out.println("navista:" + navista);
            return navista;
        }

        /*经纬度的整数值为1/10000分，要获得相对应的度，需要除以600000。*/
        /*62-89*/
        public static String getLongitude(String bits){
            String longitudeBit = bits.substring(61, 89);
            String longitude = new BigInteger(longitudeBit, 2).toString();
            System.out.println("longitude:" + longitude);
            return longitude;
        }

        /*90-116*/
        public static String getLatitude(String bits){
            String latitudeBit = bits.substring(89, 116);
            String latitude = new BigInteger(latitudeBit, 2).toString();
            System.out.println("Latitude:" + latitude);
            return latitude;
        }

        /*rate of turn的数值在获得整数内容后，需要进一步转换，对地航速，获得整数内容后，需要除以10获得实际航速。*/
        /*Bits 43-50 = Rate of turn (equation used)*/
        public static String getROT(String bits){
            String rotBit = bits.substring(42, 50);
            String rot = new BigInteger(rotBit, 2).toString();
            System.out.println("rot:" + rot);
            return rot;
        }

        /*Bits 51-60 = Speed over ground*/
        public static String getSOG(String bits){
            String sogBit = bits.substring(50, 60);
            String sog = new BigInteger(sogBit, 2).toString();
            System.out.println("sog:" + sog);
            return sog;
        }

        /*Bits 117-128 = Course over ground in 1/10 degrees 001110111111 = 95,9 degrees true */
        public static String getCOG(String bits){
            String cogBit = bits.substring(116, 128);
            String cog = new BigInteger(cogBit, 2).toString();
            System.out.println("cog:" + cog);
            return cog;
        }

        /*Bits 129-137 = True heading*/
        public static String getHDG(String bits){
            String hdgBit = bits.substring(128, 137);
            String hdg = new BigInteger(hdgBit, 2).toString();
            System.out.println("hdg:" + hdg);
            return hdg;
        }

        /*AIS版本指示 符 bits:2 1-999999999;0 = 不可用 = 默认值*/
        public static String getAISVersion(String bits){
            return print("AIS版本指示:",bits.substring(38, 40));
        }

        /*IMO编号 bits:30 1-999999999;0 = 不可用 = 默认值*/
        public static String getIMO(String bits){
            return print("IMO编号:",bits.substring(40, 70));
        }

        /*呼号 bits:42 */
        public static String getCallsign(String bits){
            System.out.println("呼号:"+ fmtChar(bits.substring(70, 112)));
            return fmtChar(bits.substring(70, 112));
        }

        /*名称 bits:120 */
        public static String getShipName(String bits){
            System.out.println("名称:"+ fmtChar(bits.substring(112, 232)));
            return fmtChar(bits.substring(112, 232));
        }

        /*船舶和货物类型 bits:8 */
        public static String getShipType(String bits){
            return print("船舶和货物类型:",bits.substring(232, 240));
        }

        /*目前最大静态吃水 bits:8 */
        public static String getDraught(String bits){
            return print("目前最大静态吃水:",bits.substring(294, 302));
        }

        /*目的地 的类型 bits:120 */
        public static String getDest(String bits){
            String val =  bits.substring(302, 422);
            System.out.println("目的地:"+ fmtChar(val));
            return val;
        }

        /*ETA的类型 bits:20  估计到达时间;MMDDHHMM UTC ，格式为船讯网格式*/
        public static String getETA(String bits){
            System.out.println("ETA的类型:"+ fmtETA(bits.substring(274, 294)));//06-07 19:00
            return fmtETA(bits.substring(274, 294));
        }

        /*总体尺寸/位置参考 bits:30 *///"length":1360,"width":330,"left":170,"trail":210
        public static int[] getSize(String bits){
            String byteString = bits.substring(240, 270);
            String a =  new BigInteger(byteString.substring(0,9),2).toString();//9 21-29
            String b =  new BigInteger(byteString.substring(9,18),2).toString();//9 12-20
            String c =  new BigInteger(byteString.substring(18,24),2).toString();//6 6-11
            String d =  new BigInteger(byteString.substring(24,30),2).toString();//6 5-0

            int length = Integer.parseInt(a)+Integer.parseInt(b);
            int width = Integer.parseInt(c)+Integer.parseInt(d);
            int left = Integer.parseInt(c);
            int trail = Integer.parseInt(b);

            System.out.println("总体尺寸/位置参考:"+"");
            return new int[]{length, width, left, trail};
        }

        /* 静态ais信息 */
        public static String getIStatic(String bits){
            String val =  getMMSI(bits);
            val =  getAISVersion(bits);
            val =  getIMO(bits);
            val = getCallsign(bits);
            val = getShipName(bits);
            val = getShipType(bits);

            getSize(bits);

            val =  bits.substring(270, 274);/*电子定位装置的类型 bits:4 */
            print("电子定位装置的类型:",val);

            val = getETA(bits);

            val = getDraught(bits);
            val = getDest(bits);

            val =  bits.substring(422, 423);/*DTE 的类型 bits:1 */
            print("DTE:",val);

            return val ;
        }

        /* 转为字符 */
        public static String fmtChar(String byteString){
            String result = "";
            //total: 20 char
            //6bits => 6 ASCII => 32~64 => 8 ASCII => char
            String[] byteStrings = byteString.split("");
            for(int i = 0;i<byteStrings.length;i+=6) {
                String sixBits = byteStrings[i]+byteStrings[i+1]+byteStrings[i+2]+byteStrings[i+3]+byteStrings[i+4]+byteStrings[i+5];
                int sixASCII = Integer.parseInt(new BigInteger(sixBits, 2).toString());
                int eightASCII = sixASCII;
                if (sixASCII < 32) {
                    eightASCII += 64;
                }
                char cr = (char) eightASCII;
                result += cr;
            }
            result  = result.replace("@","");
            return result;
        }

        /* 转为ETA UTC 船讯网模型的eta时间格式 *///06-07 19:00
        public static String fmtETA(String byteString){
            /*比特 19-16:月;1-12;0 = 不可用 = 默认值*/
            String month =   new BigInteger(byteString.substring(0,4),2).toString();//19-16
            /*比特 15-11:天;1-31;0 = 不可用 = 默认值*/
            String date =  new BigInteger(byteString.substring(4,9),2).toString();//15-11
            /*比特 10-6:时;0-23;24 = 不可用 = 默认值*/
            String hour =  new BigInteger(byteString.substring(9,14),2).toString() ;//10-6
            /*比特 5-0:分;0-59;60 = 不可用 = 默认值*/
            String min = new BigInteger(byteString.substring(14,20),2).toString();//5-0

            Calendar calendar= Calendar.getInstance(TimeZone.getTimeZone("GMT+00:00"));
            calendar.set(Calendar.MONTH,Integer.parseInt(month)-1);
            calendar.set(Calendar.DATE,Integer.parseInt(date));
            calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hour));
            calendar.set(Calendar.MINUTE,Integer.parseInt(min));
//            calendar.set(1970,Integer.parseInt(month),Integer.parseInt(date),Integer.parseInt(hour),Integer.parseInt(min));
            Date resultDate = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            return simpleDateFormat.format(resultDate);
        }

        /* 转为数字，并打印 */
        public static String print(String name,String byteString){
            String result = new BigInteger(byteString, 2).toString();
            System.out.println(name+result);
            return  result;
        }

    }
}
