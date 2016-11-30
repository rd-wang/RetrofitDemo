package com.text.rd.retrofitdemo.bean;

import java.util.List;

/**
 * Created by rd on 2016/11/22.
 */
public class GroupBean {
    private String btnText;
    private int type;
    private String name;
    private int moneyNormal;

    private String captainDesc;
    private int orderType;
    private int moneyMin;
    private int captainDiscount;
    private String orderId;
    private int moneyNow;
    private int groupId;
    private String shareImage;
    private String info;
    private String infoFull;
    private String memberBase;
    private String statusName;
    private String shareContent;
    private int finalPayment;
    private int downPayment;
    private long startTime;
    private int memberNow;
    private int remainMember;
    private int moreSaveMoney;
    private long endTime;
    private long finalTime;
    private String imageList;
    private int groupStatus;
    private int mbStatus;
    private String mbOrderId;

    private int saveMoney;
    private int memberMin;
    private boolean isCaptain;
    private String shareUrl;
    private String shareName;
    private String desc;
    private String statusImage;
    private int outputStatus;
    /**
     * isDefault : true
     * btnType : 1
     * btnDesc : 支付定金
     */

    private List<ButtonBean> button;
    private List<String> userList;

    public static class ButtonBean {
        private boolean isDefault;
        private int btnType;
        private String btnDesc;

        public boolean isDefault() {
            return isDefault;
        }

        public void setDefault(boolean aDefault) {
            isDefault = aDefault;
        }

        public int getBtnType() {
            return btnType;
        }

        public void setBtnType(int btnType) {
            this.btnType = btnType;
        }

        public String getBtnDesc() {
            return btnDesc;
        }

        public void setBtnDesc(String btnDesc) {
            this.btnDesc = btnDesc;
        }
    }
}
