package com.bytedusk.dev.plugin.maven.utcg.demo;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class RessCryptoUtils {

    /*
    *  默认使用GBK编码，GB18030为了兼容生僻字
    *  加解密过程中认为：非ASCII为双字节，汉字为双字节，
    *  加密：第一字节 0x81-0xFF，使用字节数组，实际为为负值，
    *  处理后合并原文与密钥，经过7次交错，进行加密得到ASCII字符串，
    *  解密逆向顺序。
    *  ASICII < GB2312 < GBK  <  GB18030
    *  0-127      中    赟（yūn）  䶮（yǎn）
    */

    private final String ZH_CHARSET_NAME = "GB18030";

    private String encrypt(int[] raw, String key){
        int[] enc = new int[raw.length+2];
        if(StringUtils.isEmpty(raw) || StringUtils.isEmpty(key)){
            return "";
        }
        int RAND_MAX = 0x7fff;
        Random r = new Random();
        int rval = r.nextInt(RAND_MAX)%7;
        for(int i=0; i<raw.length; i++){
            enc[i] = raw[i] + key.charAt(i%key.length())%7
                    + i%7 + 17 + rval;
        }
        enc[raw.length] = rval +65;
        enc[raw.length+1] = r.nextInt(RAND_MAX)%10 + 65;
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<enc.length; i++){
            sb.append((char)enc[i]);
        }
        return sb.toString();
    }

    /**
     * a1 raw, a2 key, a3 dest;
     */
    public String fnEncrypt(String raw, String key) {
        String enc = "";
        if (StringUtils.isEmpty(raw) || StringUtils.isEmpty(key)) {
            return enc;
        }
        int[] tmpEnc = new int[raw.length() + key.length()];

        for (int i = 0; i < raw.length(); i++) {
            tmpEnc[i] = raw.charAt(i);
        }

        for (int i = raw.length(); i < raw.length() + key.length(); i++) {
            tmpEnc[i] = key.charAt(i - raw.length());
        }

        int byteCount = 0;
        for(int i=0; i<tmpEnc.length; i++){
            byteCount += (tmpEnc[i]>127 || tmpEnc[i]<0) ? 2 : 1;
        }

        int crossArr[] = new int[byteCount];


        String tmpCn;
        byte[] cnbyte = null;
        for(int i=0, k=0; i<tmpEnc.length; i++){
            //处理中文字符
            if( tmpEnc[i]>127 || tmpEnc[i]<0){
                tmpCn = String.valueOf((char)tmpEnc[i]);
                try {
                    cnbyte = tmpCn.getBytes(ZH_CHARSET_NAME);
                } catch (UnsupportedEncodingException e) {

                }
                crossArr[k] = cnbyte[0]+256;
                crossArr[k+1] = cnbyte[1]+256;
                k+=2;
                continue;
            }
            crossArr[k] = tmpEnc[i];
            k++;
        }

        StringBuilder preCrossSb = new StringBuilder();

        for(int id=0; id<crossArr.length; id++){
            preCrossSb.append(leftZeroPad(crossArr[id]));
        }

        String preCrossStr = preCrossSb.toString();
        crossArr = new int[preCrossStr.length()];

        for(int id = 0; id<crossArr.length; id++){
            crossArr[id] = (int)preCrossStr.charAt(id);
        }

        int crossCount = 7;
        int tempCross[] = new int[crossArr.length];
        int reCross = crossArr.length/2;

        while(crossCount>0){

            for(int id=0, k=0; id<reCross; id++, k+=2){
                tempCross[id] = crossArr[k]; //无符号整数
                tempCross[id+reCross+crossArr.length%2] = crossArr[k+1];
            }

            if(crossArr.length%2 == 1 ){
                tempCross[reCross] = crossArr[crossArr.length-1]; //无符号整数
            }

            for(int id=0; id<crossArr.length; id++){
                crossArr[id] = tempCross[id];
            }
            crossCount--;
        }

        enc = encrypt(crossArr, key);
        return enc;
    }

    private String leftZeroPad(int val){
        String dest = "";
        if( val > 99){
            return String.valueOf(val);
        }
        if( val < 10){
            return "00"+val;
        }
        dest = "0"+val;

        return dest;
    }

    public String fnDecrypt(String raw, String key) throws UnsupportedEncodingException {
        String result = "";
        if(StringUtils.isEmpty(raw) || StringUtils.isEmpty(key)){
            return result;
        }

        int tmpResult[] = decrypt(raw, key);

        int round = 7;
        int reCross = 0;
        int crossArr[] = new int[tmpResult.length];

        reCross = tmpResult.length/2;

        while(round>0){

            for(int i=0, k=0; i<reCross; i++, k+=2){
                crossArr[k] = tmpResult[i]; //无符号整数
                crossArr[k+1] = tmpResult[i+reCross+tmpResult.length%2];
            }

            if(tmpResult.length%2 == 1 ){
                crossArr[tmpResult.length-1] = tmpResult[reCross]; //无符号整数
            }

            for(int i=0; i<tmpResult.length; i++){
                tmpResult[i] = crossArr[i];
            }
            round--;
        }

        StringBuilder numSb = new StringBuilder();
        for(int i=0; i<crossArr.length; i++){
            numSb.append((char)crossArr[i]);
        }
        String numStr = numSb.toString();
        StringBuilder resultSb = new StringBuilder();
        int tmpInt;
        byte[] zhbyte;

        int i=0;
        while(i<numStr.length()){
            try{
                tmpInt = Integer.valueOf(numStr.substring(i, i+3));
            }catch(Exception e){
                //System.out.println(numStr.substring(i, i+3));
                tmpInt = 0;
            }

            if ( tmpInt > 127 ){
                tmpInt -= 256;
            }
            //System.out.print(tmpInt+",");
            if(tmpInt < 0){

                int k=i;
                while(tmpInt < 0 && k < (numStr.length()-3)){
                    k+=3;
                    tmpInt = Integer.parseInt(numStr.substring(k, k+3));
                    if ( tmpInt > 127 ){
                        tmpInt -= 256;
                    }
                    //System.out.println(tmpInt);
                }

                //赟 0xDA53, 汉字双字节, 第一个字节为负值，多取一个
                int len = (k-i)/3;
                len += len % 2 == 0 ? 0:1;

                zhbyte = new byte[len];
                int count = 0;
                while(count < zhbyte.length ) {
                    tmpInt = Integer.parseInt(numStr.substring(i, i+3));
                    tmpInt -= -256;
                    zhbyte[count] = (byte) tmpInt;
                    i+=3;
                    count++;
                }
                resultSb.append(new String(zhbyte, ZH_CHARSET_NAME));
                continue;
            }
            resultSb.append((char)tmpInt);
            i+=3;
        }
        result = resultSb.toString().substring(0,resultSb.length()-key.length());
        return result;
    }

    private int[] decrypt(String raw, String key){
        int[] result = new int [0];
        if(StringUtils.isEmpty(raw) || StringUtils.isEmpty(key)){
            return result;
        }
        //待加密串取倒数第二字符值减去65=‘A’作为固定值
        int last2val = raw.charAt(raw.length()-2) - 65;

        int resultCharArr[] = new int[raw.length()-2];

        for(int i=0; i<raw.length()-2; i++){
            resultCharArr[i] = raw.charAt(i) - last2val
                    - key.charAt(i%key.length())%7 - i%7 - 17 ;
        }
        return resultCharArr;
    }

    public static void main(String args[]) throws UnsupportedEncodingException {
        RessCryptoUtils r = new RessCryptoUtils();
        String key = "mS$B_f5Rk*2g#On@6t";

        String enc = r.fnEncrypt("该剧集由导演伊达永灯Yongdeng Yida指导，十多年前一只拥有巨大威力的妖兽“九尾妖狐”袭击了木叶忍者村，当时的第四代火影拼尽全力，以自己的生命为代价将“九尾妖狐”", key);
        System.out.println(enc);
        System.out.println(r.fnDecrypt(enc, key));
    }
}
