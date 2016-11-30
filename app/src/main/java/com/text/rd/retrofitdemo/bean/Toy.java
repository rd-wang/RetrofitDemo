package com.text.rd.retrofitdemo.bean;

import java.util.List;

/**
 * Created by rd on 2016/11/22.
 */
public class Toy {
    public boolean sw;
    public boolean isHot;
    public boolean isInRent;
    public boolean isLatest;
    public boolean isRecommend;
    public boolean isSpecialMoney;
    public int isReserved;
    public int toyNum;
    public int stockNum;
    public int rentPeriodType;
    public long price;
    public long toyId;
    public long rentMoney;
    public long realRentMoney;
    public long specialMoney;
    public long endTime;
    public String url;
    public String image;
    public String limit;
    public String brand;
    public String title;
    public String format;
    public String weight;
    public String ability;
    public String content;
    public String toySize;
    public String toyName;
    public String material;
    public String ageRange;
    public String brandOrigin;
    public String limitWeight;
    public String disinfection;
    public List<String> images;
    //这个字段是换租里面用的 由于没有检查库存 只能用本地的数据来判断.
    public boolean isChecked;
    public int toyLimit;
    public boolean canSelect;
    public String stockText;
}
