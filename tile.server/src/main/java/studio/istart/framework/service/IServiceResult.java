package studio.istart.framework.service;

/**
 * Project:Se7en
 * Version:V1.0.0.0
 * Desc: service result 's interface
 * Created by Dongyan on 2015/8/17.
 */
public interface IServiceResult {

    int getReCode();

    void setReCode(int reCode);

    ResultTypeEnum getReType();

    void setReType(ResultTypeEnum reType);

    Object getReData();

    void setReData(Object reData);

    String getReMsg();

    void setReMsg(String reMsg);

    boolean equals(ResultTypeEnum resultTypeEnum);

    /**
     * could be override this method for json ,xml
     * default return itself
     *
     * @return Json, Xml ,Custom String
     */
    String format();
}
