package com.text.rd.retrofitdemo.bean;

import java.util.List;

/**
 * Created by rd on 2016/11/22.
 */
public class RobCouponBean {
    /**
     * type : 1
     * title : 8、14、20点 限量领取优惠
     * desc : 天天抢券 21点结束
     * status : 1
     */

    public int type;
    public String title;
    public String desc;
    public int status;

    /**
     * takeNum : 0
     * id : 1
     * couponStock : 100
     * couponType : 201
     * flashStatus : 4
     * buttonStatus : 0
     * couponTitle : 8点开抢|早鸟券 大额优惠减不停
     * beginTime : 1478649647000
     * couponNeed : 订单满199元可用
     * couponDiscount : 80
     * endTime : 1478653200000
     * couponDetail : ["已下单老用户专享","限量100张","领取后7日内有效"]
     */

    public int takeNum;
    public int id;
    public int couponStock;
    public int couponType;
    public int flashStatus;
    public int buttonStatus;
    public String couponTitle;
    public long beginTime;
    public String couponNeed;
    public long couponDiscount;
    public long endTime;
    public boolean underway;
    public List<String> couponDetail;
}
