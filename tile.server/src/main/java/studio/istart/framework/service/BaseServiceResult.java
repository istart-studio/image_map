package studio.istart.framework.service;

import com.google.common.base.Joiner;
import studio.istart.framework.tool.JsonHelper;

/**
 * Project:Se7en
 * Version:V1.0.0.0
 * Desc: service's result object
 * Created by Dongyan on 2015/8/17.
 */
public abstract class BaseServiceResult implements IServiceResult {

    protected int reCode;
    protected ResultTypeEnum reType;
    protected Object reData;
    protected String reMsg;



    @Override
    public int getReCode() {
        return this.reCode;
    }

    @Override
    public void setReCode(int reCode) {
        this.reCode = reCode;
    }

    @Override
    public ResultTypeEnum getReType() {
        return this.reType;
    }

    @Override
    public void setReType(ResultTypeEnum reType) {
        this.reType = reType;
    }

    @Override
    public Object getReData() {
        return this.reData;
    }

    @Override
    public void setReData(Object reData) {
        this.reData = reData;
    }

    @Override
    public String getReMsg() {
        return this.reMsg;
    }

    @Override
    public void setReMsg(String reMsg) {
        this.reMsg = reMsg;
    }

    @Override
    public boolean equals(ResultTypeEnum resultTypeEnum) {
        return this.getReType() == resultTypeEnum;
    }

    public abstract String format();

    public BaseServiceResult build(){
        return build(ResultTypeEnum.ERROR);
    }

    public BaseServiceResult build(ResultTypeEnum reType){
        return build(reType,null);
    }

    public BaseServiceResult build(ResultTypeEnum reType, Object data){

        return this.build(reType,reType.code,data,reType.desc);
    }

    public BaseServiceResult build(ResultTypeEnum reType, Object data, String reMsg){

        return this.build(reType,reType.code,data,reMsg);
    }

    public BaseServiceResult build(ResultTypeEnum reType, int reCode, Object reData, String reMsg){
        this.setReType(reType);
        this.setReCode(reCode);
        this.setReMsg(reMsg);
        this.setReData(reData);
        return this;
    }




    public static BaseServiceResult builder(){
        return new JsonResult();
    }

    public static BaseServiceResult builder(String separator){
        return new CustomResult(separator);
    }

    private static class CustomResult extends BaseServiceResult {

        private String separator;

        CustomResult(String separator){
            this.separator = separator;
        }

        @Override
        public String format() {
            return Joiner.on(separator).join(this.getReCode(),this.getReData(),this.getReMsg());
        }
    }

    private static class JsonResult extends BaseServiceResult {
        @Override
        public String format() {
            return JsonHelper.serialize(this);
        }

        @Override
        public String toString(){return format();}
    }


}