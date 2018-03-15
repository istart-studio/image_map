package studio.istart.tile;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import studio.istart.framework.service.BaseServiceResult;
import studio.istart.framework.service.ResultTypeEnum;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Se7en
 * @date 2016/5/13
 * <p>
 * 启动
 * 1.加载程序启动所需资源文件;
 * 跨域：
 * 1.JSONP只能GET，angularjs不能将data放置在body提交
 * 2.服务器允许访问
 */
@Log4j2
@SpringBootApplication
public class Application {

    /**
     * 主程序启动
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        try {
            printDefaultCharset();

            SpringApplication.run(Application.class);

            log.info("application start");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 输出默认编码
     */
    private static void printDefaultCharset() throws IOException {
        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("file.encoding=" + System.getProperty("file.encoding"));
        System.out.println("Default Charset=" + Charset.defaultCharset());
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        System.out.println("Default Charset in Use=" + writer.getEncoding());
        writer.close();
    }


}

/**
 * 跨域支持JSONP
 */
@ControllerAdvice
class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
    public JsonpAdvice() {
        super("callback");
    }
}
/**
 * 跨域请求
 */
@Configuration
class CorsConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowCredentials(true)
            .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
            .maxAge(3600);
    }
}

/**
 * RestController 的全局默认异常处理
 * 封装异常
 */
@Log4j2
@RestControllerAdvice
class RestControllerGlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public BaseServiceResult defaultErrorHandler(HttpServletRequest req, Exception e) {
        String ip = getIpAddress(req);
        String userRequest = "url: " + req.getRequestURL() + "\r\n from:" + ip;
        log.error(userRequest + "\r\n" + e.getMessage(), e);
        return BaseServiceResult.builder().build(ResultTypeEnum.ERROR, e.getMessage());
    }

    public static final String UNKNOWN = "unknown";

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     * @throws IOException
     */
    public final static String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  
        String ip = request.getHeader("X-Forwarded-For");
        try {
            if (log.isInfoEnabled()) {
                log.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
            }

            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                    if (log.isDebugEnabled()) {
                        log.debug("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
                    }
                }
                if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                    if (log.isDebugEnabled()) {
                        log.debug("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
                    }
                }
                if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                    if (log.isDebugEnabled()) {
                        log.debug("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
                    }
                }
                if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                    if (log.isDebugEnabled()) {
                        log.debug("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
                    }
                }
                if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                    if (log.isDebugEnabled()) {
                        log.debug("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
                    }
                }
            } else if (ip.length() > 15) {
                String[] ips = ip.split(",");
                for (int index = 0; index < ips.length; index++) {
                    String strIp = (String) ips[index];
                    if (!("unknown".equalsIgnoreCase(strIp))) {
                        ip = strIp;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ip;
    }
}

/**
 * 在线doc生成配置
 *
 * @author dongyan
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Mr.Dong",
            "",
            "dongyan@istart.studio");
        return new ApiInfoBuilder()
            .title("api online document")
            .description("仅做内部使用，请勿轻易调用大数据量返回的在线接口调试")
            .contact(contact)
            .version("1.0.0")
            .build();
    }
}

/**
 * 定时任务并行运行
 */
@Log4j2
@Configuration
@EnableScheduling
class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }

    /**
     * 并行任务使用策略：多线程处理
     *
     * @return ThreadPoolTaskScheduler 线程池
     */
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);
        scheduler.setThreadNamePrefix("schedule-task-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setErrorHandler(new TaskErrorHandler());
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return scheduler;
    }

    /**
     * 异常处理
     */
    class TaskErrorHandler implements ErrorHandler {
        @Override
        public void handleError(Throwable t) {
            log.error("schedule-task-error", t);
        }
    }
}
@Configuration
class MvcConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error").setViewName("error.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        super.configurePathMatch(configurer);
        configurer.setUseSuffixPatternMatch(false);
    }
}
