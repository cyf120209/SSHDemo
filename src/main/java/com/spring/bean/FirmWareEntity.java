package com.spring.bean;

/**
 * Created by lenovo on 2017/3/25.
 */
public class FirmWareEntity {

    private boolean isCorrupt;

    private String type;

    private int typeNum;

    private int majorNum;

    private int minorNum;

    private int patchNum;

    public FirmWareEntity(boolean corrupt,String type, int typeNum, int majorNum, int minorNum, int patchNum) {
        this.isCorrupt=corrupt;
        this.type = type;
        this.typeNum = typeNum;
        this.majorNum = majorNum;
        this.minorNum = minorNum;
        this.patchNum = patchNum;
    }

    public static class Builder{
        private boolean isCorrupt;

        private String type;

        private int typeNum;

        private int majorNum;

        private int minorNum;

        private int patchNum;

        public Builder(){

        }

        public boolean isCorrupt() {
            return isCorrupt;
        }

        public Builder setCorrupt(boolean corrupt) {
            isCorrupt = corrupt;
            return this;
        }

        public String getType() {
            return type;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public int getTypeNum() {
            return typeNum;
        }

        public Builder setTypeNum(int typeNum) {
            this.typeNum = typeNum;
            return this;
        }

        public int getMajorNum() {
            return majorNum;
        }

        public Builder setMajorNum(int majorNum) {
            this.majorNum = majorNum;
            return this;
        }

        public int getMinorNum() {
            return minorNum;
        }

        public Builder setMinorNum(int minorNum) {
            this.minorNum = minorNum;
            return this;
        }

        public int getPatchNum() {
            return patchNum;
        }

        public Builder setPatchNum(int patchNum) {
            this.patchNum = patchNum;
            return this;
        }

        public FirmWareEntity create(){
            return new FirmWareEntity(isCorrupt,type,typeNum,majorNum,minorNum,patchNum);
        }
    }

    public boolean isCorrupt() {
        return isCorrupt;
    }

    public void setCorrupt(boolean corrupt) {
        isCorrupt = corrupt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeNum() {
        return typeNum;
    }

    public void setTypeNum(int typeNum) {
        this.typeNum = typeNum;
    }

    public int getMajorNum() {
        return majorNum;
    }

    public void setMajorNum(int majorNum) {
        this.majorNum = majorNum;
    }

    public int getMinorNum() {
        return minorNum;
    }

    public void setMinorNum(int minorNum) {
        this.minorNum = minorNum;
    }

    public int getPatchNum() {
        return patchNum;
    }

    public void setPatchNum(int patchNum) {
        this.patchNum = patchNum;
    }
}
