package com.gloiot.hygooilstation.utils.bluetoothprint;

import android.graphics.Bitmap;

import com.gloiot.hygooilstation.utils.printer.PrinterUtils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class ESCUtil {

    public static final byte ESC = 27;// 换码
    public static final byte FS = 28;// 文本分隔符
    public static final byte GS = 29;// 组分隔符
    public static final byte DLE = 16;// 数据连接换码
    public static final byte EOT = 4;// 传输结束
    public static final byte ENQ = 5;// 询问字符
    public static final byte SP = 32;// 空格
    public static final byte HT = 9;// 横向列表
    public static final byte LF = 10;// 打印并换行（水平定位）
    public static final byte CR = 13;// 归位键
    public static final byte FF = 12;// 走纸控制（打印并回到标准模式（在页模式下） ）
    public static final byte CAN = 24;// 作废（页模式下取消打印数据 ）

    // ------------------------打印机初始化-----------------------------

    /**
     * 打印机初始化
     *
     * @return
     */
    public static byte[] init_printer() {
        byte[] result = new byte[2];
        result[0] = ESC;
        result[1] = 64;
        return result;
    }

    // ------------------------换行-----------------------------

    /**
     * 换行
     *
     * @param
     * @return
     */
    public static byte[] nextLine(int lineNum) {
        byte[] result = new byte[lineNum];
        for (int i = 0; i < lineNum; i++) {
            result[i] = LF;
        }

        return result;
    }

    // ------------------------下划线-----------------------------

    /**
     * 绘制下划线（1点宽）
     *
     * @return
     */
    public static byte[] underlineWithOneDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        return result;
    }

    /**
     * 绘制下划线（2点宽）
     *
     * @return
     */
    public static byte[] underlineWithTwoDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        return result;
    }

    /**
     * 取消绘制下划线
     *
     * @return
     */
    public static byte[] underlineOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 0;
        return result;
    }

    // ------------------------加粗-----------------------------

    /**
     * 选择加粗模式
     *
     * @return
     */
    public static byte[] boldOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }

    /**
     * 取消加粗模式
     *
     * @return
     */
    public static byte[] boldOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        return result;
    }

    // ------------------------对齐-----------------------------

    /**
     * 左对齐
     *
     * @return
     */
    public static byte[] alignLeft() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 0;
        return result;
    }

    /**
     * 居中对齐
     *
     * @return
     */
    public static byte[] alignCenter() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 1;
        return result;
    }

    /**
     * 右对齐
     *
     * @return
     */
    public static byte[] alignRight() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 2;
        return result;
    }

    /**
     * 水平方向向右移动col列
     *
     * @param col
     * @return
     */
    public static byte[] set_HT_position(byte col) {
        byte[] result = new byte[4];
        result[0] = ESC;
        result[1] = 68;
        result[2] = col;
        result[3] = 0;
        return result;
    }
    // ------------------------字体变大-----------------------------

    /**
     * 字体变大为标准的n倍
     *
     * @param num
     * @return
     */
    public static byte[] fontSizeSetBig(int num) {
        byte realSize = 0;
        switch (num) {
            case 1:
                realSize = 0;
                break;
            case 2:
                realSize = 17;
                break;
            case 3:
                realSize = 34;
                break;
            case 4:
                realSize = 51;
                break;
            case 5:
                realSize = 68;
                break;
            case 6:
                realSize = 85;
                break;
            case 7:
                realSize = 102;
                break;
            case 8:
                realSize = 119;
                break;
        }
        byte[] result = new byte[3];
        result[0] = 29;
        result[1] = 33;
        result[2] = realSize;
        return result;
    }

    // ------------------------字体变小-----------------------------

    /**
     * 字体取消倍宽倍高
     *
     * @param num
     * @return
     */
    public static byte[] fontSizeSetSmall(int num) {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;

        return result;
    }

    // ------------------------切纸-----------------------------

    /**
     * 进纸并全部切割
     *
     * @return
     */
    public static byte[] feedPaperCutAll() {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 65;
        result[3] = 0;
        return result;
    }

    /**
     * 进纸并切割（左边留一点不切）
     *
     * @return
     */
    public static byte[] feedPaperCutPartial() {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 66;
        result[3] = 0;
        return result;
    }

    // ------------------------切纸-----------------------------
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static byte[] byteMerger(byte[][] byteList) {

        int length = 0;
        for (int i = 0; i < byteList.length; i++) {
            length += byteList[i].length;
        }
        byte[] result = new byte[length];

        int index = 0;
        for (int i = 0; i < byteList.length; i++) {
            byte[] nowByte = byteList[i];
            for (int k = 0; k < byteList[i].length; k++) {
                result[index] = nowByte[k];
                index++;
            }
        }
        for (int i = 0; i < index; i++) {
            // CommonUtils.LogWuwei("", "result[" + i + "] is " + result[i]);
        }
        return result;
    }

    // --------------------使用虚拟数据测试
    public static byte[] generateMockData() {
        try {
            byte[] next2Line = ESCUtil.nextLine(2);
            byte[] title = "出餐单（午餐）**万通中心店".getBytes("gb2312");

            byte[] boldOn = ESCUtil.boldOn();
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);
            byte[] center = ESCUtil.alignCenter();
            byte[] Focus = "网 507".getBytes("gb2312");
            byte[] boldOff = ESCUtil.boldOff();
            byte[] fontSize2Small = ESCUtil.fontSizeSetSmall(3);

            byte[] left = ESCUtil.alignLeft();
            byte[] orderSerinum = "订单编号：11234".getBytes("gb2312");
            boldOn = ESCUtil.boldOn();
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);
            byte[] FocusOrderContent = "韭菜鸡蛋饺子-小份（单）".getBytes("gb2312");
            boldOff = ESCUtil.boldOff();
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);

            next2Line = ESCUtil.nextLine(2);

            byte[] priceInfo = "应收:22元 优惠：2.5元 ".getBytes("gb2312");
            byte[] nextLine = ESCUtil.nextLine(1);

            byte[] priceShouldPay = "实收:19.5元".getBytes("gb2312");
            nextLine = ESCUtil.nextLine(1);

            byte[] takeTime = "取餐时间:2015-02-13 12:51:59".getBytes("gb2312");
            nextLine = ESCUtil.nextLine(1);
            byte[] setOrderTime = "下单时间：2015-02-13 12:35:15".getBytes("gb2312");

            byte[] tips_1 = "微信关注\"**\"自助下单每天免1元".getBytes("gb2312");
            nextLine = ESCUtil.nextLine(1);
            byte[] tips_2 = "饭后点评再奖5毛".getBytes("gb2312");
            byte[] next4Line = ESCUtil.nextLine(4);

            byte[] breakPartial = ESCUtil.feedPaperCutPartial();

            byte[][] cmdBytes = {title, nextLine, center, boldOn, fontSize2Big, Focus, boldOff, fontSize2Small,
                    next2Line, left, orderSerinum, nextLine, center, boldOn, fontSize1Big, FocusOrderContent, boldOff,
                    fontSize1Small, nextLine, left, next2Line, priceInfo, nextLine, priceShouldPay, next2Line, takeTime,
                    nextLine, setOrderTime, next2Line, center, tips_1, nextLine, center, tips_2, next4Line,
                    breakPartial};

            return ESCUtil.byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------生成加油订单（原来的打印格式，没有油站编号，折扣比例，实付金额，备注等内容）
    public static byte[] generateRefuelOrderform(String orderNumString, String oilStationString, String payTypeString,
                                                 String oilgunNumString, String oilsTypeString, String businessTimeString,
                                                 String businessMoneyString) {
        try {
            byte[] next2Line = ESCUtil.nextLine(2);
            byte[] title = "加油订单".getBytes("gb2312");

            byte[] starLine = "********************************".getBytes("gb2312");

            byte[] orderNumText = "订单编号：".getBytes("gb2312");

            byte[] orderNum = orderNumString.getBytes("gb2312");

            byte[] oilStation = ("交易油站：" + oilStationString).getBytes("gb2312");

            byte[] payType = ("付款方式：" + payTypeString).getBytes("gb2312");

            byte[] oilgunNum = ("油枪号：" + oilgunNumString).getBytes("gb2312");

            byte[] oilsType = ("油品类型：" + oilsTypeString).getBytes("gb2312");

            byte[] businessTime = ("交易时间：" + businessTimeString).getBytes("gb2312");

            byte[] businessMoneyText = "加油金额：".getBytes("gb2312");

            byte[] businessMoney = ("￥" + businessMoneyString).getBytes("gb2312");

            byte[] nullString = ("").getBytes("gb2312");

            byte[] boldOn = ESCUtil.boldOn();
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);
            byte[] center = ESCUtil.alignCenter();
            byte[] boldOff = ESCUtil.boldOff();
            byte[] fontSize2Small = ESCUtil.fontSizeSetSmall(3);
            byte[] left = ESCUtil.alignLeft();
            byte[] right = ESCUtil.alignRight();
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);
            byte[] nextLine = ESCUtil.nextLine(1);
            byte[] next4Line = ESCUtil.nextLine(4);
            byte[] breakPartial = ESCUtil.feedPaperCutPartial();
//            byte[] breakAll = ESCUtil.feedPaperCutAll();


            byte[][] cmdBytes1 = {next2Line, fontSize2Big, title, nextLine, fontSize2Small, starLine, nextLine, left, orderNumText,
                    nextLine, left, orderNum, nextLine, left, oilStation, nextLine, left, payType, nextLine, left, oilgunNum, nextLine, left, oilsType,
                    nextLine, left, businessTime, nextLine, left, boldOn, fontSize1Big, businessMoneyText, nextLine, alignRight(), businessMoney, next4Line, breakPartial
            };

            byte[][] cmdBytes2 = {center, fontSize1Big, title, nextLine, fontSize1Small, starLine, nextLine, left, orderNumText,
                    nextLine, orderNum, nextLine, oilStation, nextLine, payType, nextLine, oilgunNum, nextLine, oilsType,
                    nextLine, businessTime, nextLine, boldOn, fontSize1Big, businessMoneyText, nextLine, right, businessMoney,
                    nextLine, fontSize1Small, boldOff, left, nextLine, nextLine, nullString, next4Line, breakPartial
            };

            return ESCUtil.byteMerger(cmdBytes2);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Bitmap → byte[]（不是需要的结果，改用PrinterUtils中的方法）
     *
     * @param bm
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //----------------生成加油订单(20170605修改格式，加入油站编号，折扣，用户实付，备注信息等内容)
    public static byte[] generateRefuelOrderformNew(String orderNumString, String stationNumString, String oilStationString, String payTypeString,
                                                    String oilgunNumString, String oilsTypeString, String businessTimeString, String discountString,
                                                    String businessMoneyString, String outOfPocketString, String remarkString) {
        try {
            byte[] next2Line = ESCUtil.nextLine(2);
            byte[] title = "加油订单".getBytes("gb2312");

            byte[] starLine = "********************************".getBytes("gb2312");

            byte[] orderNumText = "订单编号：".getBytes("gb2312");

            byte[] orderNum = orderNumString.getBytes("gb2312");

            byte[] stationNum = ("油站编号：" + stationNumString).getBytes("gb2312");

            byte[] oilStation = ("交易油站：" + oilStationString).getBytes("gb2312");

            byte[] payType = ("付款方式：" + payTypeString).getBytes("gb2312");

            byte[] oilgunNum = ("油枪号：" + oilgunNumString).getBytes("gb2312");

            byte[] oilsType = ("油品类型：" + oilsTypeString).getBytes("gb2312");

            byte[] businessTime = ("交易时间：" + businessTimeString).getBytes("gb2312");

            byte[] discount = ("折扣：" + discountString).getBytes("gb2312");

            byte[] businessMoneyText = "加油金额：".getBytes("gb2312");

            byte[] businessMoney = ("￥" + businessMoneyString).getBytes("gb2312");

            byte[] outOfPocketText = "用户实付：".getBytes("gb2312");

            byte[] outOfPocket = ("￥" + outOfPocketString).getBytes("gb2312");

            byte[] endLine = "********************************".getBytes("gb2312");

            byte[] remarkText = "备注信息：".getBytes("gb2312");

            byte[] remark = remarkString.getBytes("gb2312");

            byte[] nullString = ("").getBytes("gb2312");


            byte[] boldOn = ESCUtil.boldOn();
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);
            byte[] center = ESCUtil.alignCenter();
            byte[] boldOff = ESCUtil.boldOff();
            byte[] fontSize2Small = ESCUtil.fontSizeSetSmall(3);
            byte[] left = ESCUtil.alignLeft();
            byte[] right = ESCUtil.alignRight();
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);
            byte[] nextLine = ESCUtil.nextLine(1);
            byte[] next4Line = ESCUtil.nextLine(4);
            byte[] breakPartial = ESCUtil.feedPaperCutPartial();
//            byte[] breakAll = ESCUtil.feedPaperCutAll();


            byte[][] cmdBytes = {center, fontSize1Big, title, nextLine, fontSize1Small, starLine, nextLine, left, orderNumText,
                    nextLine, orderNum, nextLine, stationNum, nextLine, oilStation, nextLine, payType, nextLine, oilgunNum, nextLine, oilsType,
                    nextLine, businessTime, nextLine, discount, nextLine, fontSize1Big, businessMoneyText, nextLine, right, businessMoney,
                    nextLine, left, outOfPocketText, nextLine, right, outOfPocket, nextLine, fontSize1Small, center, endLine, nextLine, left, remarkText,
                    nextLine, remark, nextLine, nextLine, nullString, next4Line, breakPartial
            };


            return ESCUtil.byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    //--------------------------在生成加油订单的基础上打印一张二维码图片，测试打印图片的功能
    public static byte[] generateRefuelOrderformNew1(String orderNumString, String stationNumString, String oilStationString, String payTypeString,
                                                     String oilgunNumString, String oilsTypeString, String businessTimeString, String discountString,
                                                     String businessMoneyString, String outOfPocketString, String remarkString, Bitmap bitmapImage) {
        try {
            byte[] next2Line = ESCUtil.nextLine(2);
            byte[] title = "加油订单".getBytes("gb2312");

            byte[] starLine = "********************************".getBytes("gb2312");

            byte[] orderNumText = "订单编号：".getBytes("gb2312");

            byte[] orderNum = orderNumString.getBytes("gb2312");

            byte[] stationNum = ("油站编号：" + stationNumString).getBytes("gb2312");

            byte[] oilStation = ("交易油站：" + oilStationString).getBytes("gb2312");

            byte[] payType = ("付款方式：" + payTypeString).getBytes("gb2312");

            byte[] oilgunNum = ("油枪号：" + oilgunNumString).getBytes("gb2312");

            byte[] oilsType = ("油品类型：" + oilsTypeString).getBytes("gb2312");

            byte[] businessTime = ("交易时间：" + businessTimeString).getBytes("gb2312");

            byte[] discount = ("折扣：" + discountString).getBytes("gb2312");

            byte[] businessMoneyText = "加油金额：".getBytes("gb2312");

            byte[] businessMoney = ("￥" + businessMoneyString).getBytes("gb2312");

            byte[] outOfPocketText = "用户实付：".getBytes("gb2312");

            byte[] outOfPocket = ("￥" + outOfPocketString).getBytes("gb2312");

            byte[] endLine = "********************************".getBytes("gb2312");

            byte[] remarkText = "备注信息：".getBytes("gb2312");

            byte[] remark = remarkString.getBytes("gb2312");


//            byte[] image = Bitmap2Bytes(bitmapImage);//测试打印图片（出错了，弃用该方法）
            byte[] image = PrinterUtils.decodeBitmap(bitmapImage);//测试打印图片


            byte[] nullString = ("").getBytes("gb2312");


            byte[] boldOn = ESCUtil.boldOn();
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);
            byte[] center = ESCUtil.alignCenter();
            byte[] boldOff = ESCUtil.boldOff();
            byte[] fontSize2Small = ESCUtil.fontSizeSetSmall(3);
            byte[] left = ESCUtil.alignLeft();
            byte[] right = ESCUtil.alignRight();
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);
            byte[] nextLine = ESCUtil.nextLine(1);
            byte[] next4Line = ESCUtil.nextLine(4);
            byte[] breakPartial = ESCUtil.feedPaperCutPartial();
//            byte[] breakAll = ESCUtil.feedPaperCutAll();


//            byte[][] cmdBytes = {center, fontSize1Big, title, nextLine, fontSize1Small, starLine, nextLine, left, orderNumText,
//                    nextLine, orderNum, nextLine, stationNum, nextLine, oilStation, nextLine, payType, nextLine, oilgunNum, nextLine, oilsType,
//                    nextLine, businessTime, nextLine, discount, nextLine, fontSize1Big, businessMoneyText, nextLine, right, businessMoney,
//                    nextLine, left, outOfPocketText, nextLine, right, outOfPocket, nextLine, fontSize1Small, center, endLine, nextLine, left, remarkText,
//                    nextLine, remark, nextLine, nextLine, nullString, next4Line, breakPartial
//            };


            byte[][] cmdBytes = {center, fontSize1Big, title, nextLine, fontSize1Small, starLine, nextLine, left, orderNumText,
                    nextLine, orderNum, nextLine, stationNum, nextLine, oilStation, nextLine, payType, nextLine, oilgunNum, nextLine, oilsType,
                    nextLine, businessTime, nextLine, discount, nextLine, fontSize1Big, businessMoneyText, nextLine, right, businessMoney,
                    nextLine, left, outOfPocketText, nextLine, right, outOfPocket, nextLine, fontSize1Small, center, endLine, nextLine, left, remarkText,
                    nextLine, remark, nextLine,center, image,left, nextLine, nextLine, nextLine, nullString, next4Line, breakPartial
            };

            return ESCUtil.byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //----------------生成结算订单
    public static byte[] generateSettlementOrderform(String orderNumString, String personNameString, String amountString,
                                                     String jiaoxiMoneyString, String daozhangMoneyString, String startTimeString,
                                                     String endTimeString) {
        try {
            byte[] next2Line = ESCUtil.nextLine(2);
            byte[] title = "结算单".getBytes("gb2312");

            byte[] starLine = "********************************".getBytes("gb2312");

            byte[] orderNumText = "结算单号：".getBytes("gb2312");

            byte[] orderNum = orderNumString.getBytes("gb2312");

            byte[] personName = ("结算人：" + personNameString).getBytes("gb2312");

            byte[] amount = ("交易累计：" + amountString + "笔").getBytes("gb2312");

            byte[] jiaoxiMoney = ("交易金额：￥" + jiaoxiMoneyString).getBytes("gb2312");

            byte[] daozhangMoney = ("到账金额：￥" + daozhangMoneyString).getBytes("gb2312");

            byte[] startTime = ("开始时间：" + startTimeString).getBytes("gb2312");
            byte[] endTime = ("结束时间：" + endTimeString).getBytes("gb2312");
            byte[] nullString = ("").getBytes("gb2312");

            byte[] boldOn = ESCUtil.boldOn();
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);
            byte[] center = ESCUtil.alignCenter();
            byte[] boldOff = ESCUtil.boldOff();
            byte[] fontSize2Small = ESCUtil.fontSizeSetSmall(3);
            byte[] left = ESCUtil.alignLeft();
            byte[] right = ESCUtil.alignRight();
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);
            byte[] nextLine = ESCUtil.nextLine(1);
            byte[] next4Line = ESCUtil.nextLine(4);
            byte[] breakPartial = ESCUtil.feedPaperCutPartial();
//            byte[] breakAll = ESCUtil.feedPaperCutAll();


            byte[][] cmdBytes = {center, fontSize1Big, title, nextLine, fontSize1Small, starLine, nextLine, left, orderNumText,
                    nextLine, orderNum, nextLine, personName, nextLine, amount, nextLine, jiaoxiMoney, nextLine, daozhangMoney,
                    nextLine, startTime, nextLine, endTime, nextLine, nextLine, nextLine, nextLine, nullString, next4Line, breakPartial
            };

            return ESCUtil.byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


}