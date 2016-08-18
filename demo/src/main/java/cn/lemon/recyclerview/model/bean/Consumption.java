package cn.lemon.recyclerview.model.bean;

/**
 * Created by linlongxin on 2016/1/26.
 */
public class Consumption {


    /**
     * xm : 林龙鑫
     * sj : 2015-12-18 12:09
     * lx : 消费
     * je : 9.7
     * ye : 24.19
     * sh : 兴业苑食堂三楼
     */

    private String xm;
    private String sj;
    private String lx;
    private float je;
    private float ye;
    private String sh;

    public Consumption(String xm, String sj, String lx, float je, float ye, String sh) {
        this.xm = xm;
        this.sj = sj;
        this.lx = lx;
        this.je = je;
        this.ye = ye;
        this.sh = sh;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public void setJe(float je) {
        this.je = je;
    }

    public void setYe(float ye) {
        this.ye = ye;
    }

    public void setSh(String sh) {
        this.sh = sh;
    }

    public String getXm() {
        return xm;
    }

    public String getSj() {
        return sj;
    }

    public String getLx() {
        return lx;
    }

    public float getJe() {
        return je;
    }

    public float getYe() {
        return ye;
    }

    public String getSh() {
        return sh;
    }
}
