package com.lenovo.smarttraffic.bean;

public class Violation {


    /**
     * id : 1
     * carnumber : 鲁B10001
     * pcode : 1001A　　
     * pdatetime : 2016-5-21 08:19:21
     * paddr : 学院路
     * premarks : A 驾驶拼装的非汽车类机动车上道路行驶的
     * pmoney : 1000
     * pscore : 0
     * pchuli : 1
     */

    private int id;
    private String carnumber;
    private String pcode;
    private String pdatetime;
    private String paddr;
    private String premarks;
    private String pmoney;
    private String pscore;
    private int pchuli;
    private String carbrand;
    public String getCarbrand() {
        return carbrand;
    }

    public void setCarbrand(String carbrand) {
        this.carbrand = carbrand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getPdatetime() {
        return pdatetime;
    }

    public void setPdatetime(String pdatetime) {
        this.pdatetime = pdatetime;
    }

    public String getPaddr() {
        return paddr;
    }

    public void setPaddr(String paddr) {
        this.paddr = paddr;
    }

    public String getPremarks() {
        return premarks;
    }

    public void setPremarks(String premarks) {
        this.premarks = premarks;
    }

    public String getPmoney() {
        return pmoney;
    }

    public void setPmoney(String pmoney) {
        this.pmoney = pmoney;
    }

    public String getPscore() {
        return pscore;
    }

    public void setPscore(String pscore) {
        this.pscore = pscore;
    }

    public int getPchuli() {
        return pchuli;
    }

    public void setPchuli(int pchuli) {
        this.pchuli = pchuli;
    }
}
