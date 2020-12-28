package com.aquilaflycloud.mdc.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.gitee.sop.servercommon.bean.ServiceConfig;
import com.gitee.sop.servercommon.configuration.AlipayServiceConfiguration;
import com.gitee.sop.servercommon.manager.EnvironmentContext;
import com.gitee.sop.servercommon.swagger.SwaggerApiDocsController;
import com.gitee.sop.servercommon.swagger.SwaggerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenServiceConfig
 *
 * @author star
 * @date 2019-09-20
 */
@Configuration
public class OpenServiceConfiguration extends AlipayServiceConfiguration {

    static {
        ServiceConfig.getInstance().getI18nModules().add("i18n/isp/mdc_error");
    }

    @RestController
    public static class SopDocController extends SwaggerApiDocsController {
        @Override
        public ResponseEntity<String> getApiDocs(String time, String sign) {
            String body = "";
            String query = "?time=" + time + "&sign=" + sign;
            String port = EnvironmentContext.getEnvironment().getProperty("local.server.port");
            String result = HttpUtil.get("http://localhost:" + port + "/v2/api-docs" + query);
            if (StrUtil.isNotBlank(result)) {
                body = result;
            }
            return ResponseEntity.ok().header("Content-Type", MediaType.TEXT_PLAIN_VALUE + ";charset=" + StandardCharsets.UTF_8.toString()).body(body);
        }
    }

    /**
     * 开启文档，本地微服务文档地址：http://localhost:2222/doc.html
     * http://ip:port/v2/api-docs
     */
    @Configuration
    @EnableSwagger2
    public static class Swagger2 extends SwaggerSupport {
        @Override
        protected String getDocTitle() {
            return "会员服务API";
        }

        @Override
        protected boolean swaggerAccessProtected() {
            return false;
        }

        @Override
        @Bean
        public Docket createRestApi() {
            return getDocket().securitySchemes(securitySchemes()).securityContexts(securityContexts());
        }

        private List<ApiKey> securitySchemes() {
            //设置请求头信息
            List<ApiKey> result = new ArrayList<>();
            ApiKey apiKey = new ApiKey("backend.Authorization", "Authorization", "header");
            result.add(apiKey);
            ApiKey memberSession = new ApiKey("MemberSession", "MemberSession", "header");
            result.add(memberSession);
            return result;
        }

        private List<SecurityContext> securityContexts() {
            //设置需要登录认证的路径
            List<SecurityContext> result = new ArrayList<>();
            result.add(getContextByPath("/backend.*"));
            return result;
        }

        private SecurityContext getContextByPath(String pathRegex) {
            return SecurityContext.builder()
                    .securityReferences(defaultAuth())
                    .forPaths(PathSelectors.regex(pathRegex))
                    .build();
        }

        private List<SecurityReference> defaultAuth() {
            List<SecurityReference> result = new ArrayList<>();
            AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
            AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
            authorizationScopes[0] = authorizationScope;
            result.add(new SecurityReference("Authorization", authorizationScopes));
            return result;
        }
    }
}

/**
 * 使用淘宝开放平台功能
 *
 * @author tanghc
 */
//@Configuration
//public class OpenServiceConfig extends TaobaoServiceConfiguration {
//
//}
