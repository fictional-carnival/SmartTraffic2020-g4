package com.lenovo.smarttraffic.bean;

import java.util.List;

public class Ditu {
    /**
     * img : /images/parkzone/parkzone_001.png
     * ERRMSG : 成功
     * latitude : 39.877603
     * ROWS_DETAIL : [{"name":"南京工业职业技术学院停车场","address":"南京栖霞区洋山北路1号","distance":311,"open":1,"remarks":"停车场收费标准由全市停车系统统一定价，最高40元/天。","EmptySpace":30,"AllSpace":503},{"name":"南京信息职业技术学院停车场","address":"南京栖霞区文澜路99号","distance":511,"open":1,"remarks":"停车场收费标准由全市停车系统统一定价，最高41元/天。","EmptySpace":32,"AllSpace":146},{"name":"羊山壹号小型车辆停车场","address":"南京栖霞区仙林大学城羊山北路18号","distance":711,"open":0,"remarks":"停车场收费标准由全市停车系统统一定价，最高43元/天。","EmptySpace":8,"AllSpace":88}]
     * RESULT : S
     * longitude : 116.347593
     */

    private String img;
    private String ERRMSG;
    private String latitude;
    private String RESULT;
    private String longitude;
    private List<ROWSDETAILBean> ROWS_DETAIL;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getERRMSG() {
        return ERRMSG;
    }

    public void setERRMSG(String ERRMSG) {
        this.ERRMSG = ERRMSG;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<ROWSDETAILBean> getROWS_DETAIL() {
        return ROWS_DETAIL;
    }

    public void setROWS_DETAIL(List<ROWSDETAILBean> ROWS_DETAIL) {
        this.ROWS_DETAIL = ROWS_DETAIL;
    }

    public static class ROWSDETAILBean {
        /**
         * name : 南京工业职业技术学院停车场
         * address : 南京栖霞区洋山北路1号
         * distance : 311
         * open : 1
         * remarks : 停车场收费标准由全市停车系统统一定价，最高40元/天。
         * EmptySpace : 30
         * AllSpace : 503
         */

        private String name;
        private String address;
        private int distance;
        private int open;
        private String remarks;
        private int EmptySpace;
        private int AllSpace;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getOpen() {
            return open;
        }

        public void setOpen(int open) {
            this.open = open;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getEmptySpace() {
            return EmptySpace;
        }

        public void setEmptySpace(int EmptySpace) {
            this.EmptySpace = EmptySpace;
        }

        public int getAllSpace() {
            return AllSpace;
        }

        public void setAllSpace(int AllSpace) {
            this.AllSpace = AllSpace;
        }
    }
}
