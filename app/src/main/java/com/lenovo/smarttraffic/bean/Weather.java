package com.lenovo.smarttraffic.bean;

import java.util.List;

public class Weather {
    /**
     * ERRMSG : 成功
     * WCurrent : 0
     * ROWS_DETAIL : [{"temperature":"0~6","WData":"2020-03-10","type":"阴"},{"temperature":"-4~4","WData":"2020-03-11","type":"小雨"},{"temperature":"1~5","WData":"2020-03-12","type":"阴"},{"temperature":"-1~2","WData":"2020-03-13","type":"阴"},{"temperature":"1~7","WData":"2020-03-14","type":"晴"},{"temperature":"2~8","WData":"2020-03-15","type":"晴"}]
     * RESULT : S
     */

    private String ERRMSG;
    private int WCurrent;
    private String RESULT;
    private List<ROWSDETAILBean> ROWS_DETAIL;

    public String getERRMSG() {
        return ERRMSG;
    }

    public void setERRMSG(String ERRMSG) {
        this.ERRMSG = ERRMSG;
    }

    public int getWCurrent() {
        return WCurrent;
    }

    public void setWCurrent(int WCurrent) {
        this.WCurrent = WCurrent;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public List<ROWSDETAILBean> getROWS_DETAIL() {
        return ROWS_DETAIL;
    }

    public void setROWS_DETAIL(List<ROWSDETAILBean> ROWS_DETAIL) {
        this.ROWS_DETAIL = ROWS_DETAIL;
    }

    public static class ROWSDETAILBean {
        /**
         * temperature : 0~6
         * WData : 2020-03-10
         * type : 阴
         */

        private String temperature;
        private String WData;
        private String type;

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getWData() {
            return WData;
        }

        public void setWData(String WData) {
            this.WData = WData;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
