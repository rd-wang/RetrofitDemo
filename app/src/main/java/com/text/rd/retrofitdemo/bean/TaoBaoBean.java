package com.text.rd.retrofitdemo.bean;

import java.util.List;

/**
 * Created by rd on 2016/11/24.
 */
public class TaoBaoBean {
    public String toyName;
    public String toyUrl;
    public List<String> attributes;
    public List<String> topImageList;
    public List<String> detailImageList;

    public String toJson() {
        StringBuilder sb = new StringBuilder();
//        {
//            "name": "name1",
//                "Address": "add1",
//                "user_detail": {
//            "Qualification": B.E,
//                    "DOB": 11/2/1990,
//        }
        sb.append("{\"名称\":\"" + toyName + "\",");
        sb.append("\"参考链接\":\"" + toyUrl + "\",");
        if (attributes != null) {
            for (int i = 0; i < attributes.size(); i++) {
                sb.append("\"value" + i + "\":\"" + attributes.get(i) + "\",");
            }
        }
        sb.substring(0, sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }
}
