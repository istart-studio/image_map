package studio.istart.framework.service;

/**
 * Created by Se7en on 2016/5/23.
 * Desc:
 */
public enum ResultTypeEnum {
    ERROR(999, "未知错误"),
    FAIL(1100),
    SUCCESS(1000),

    NO_FOUND(40400, "资源未找到"),
    //Common
    PARAM_IS_BLANK(50001,"参数为空"),
    PARAM_IS_ILLEGALITY(50002,"非法参数"),
    NOT_ALLOWED(60001,"未被允许访问"),
    IS_UNIQUE(70001,"资源唯一"),
    NOT_UNIQUE(70002,"资源不唯一"),

    //Upgrade Service
    NO_AVAILABLE_UPGRADE(70001,"无可用升级版本"),
    UPGRADE_ATTACHMENT_IS_NULL(70002,"升级附件为空"),
    UPGRADE_VERSION_IS_NULL(70003,"升级版本信息为空"),
    CLIENT_VERSION_IS_NULL(70004,"客户端版本信息未找到"),
    UPGRADE_VERSION_IS_WRONG(70005,"升级版本信息有误"),

    //Static File Service
    FILE_IS_NOT_FOUND(80001,"文件没有找到");

    public int code;
    public String desc;

    ResultTypeEnum(int code) {
        this.code = code;
        this.desc = this.toString();
    }

    ResultTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
