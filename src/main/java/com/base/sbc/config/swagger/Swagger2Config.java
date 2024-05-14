package com.base.sbc.config.swagger;

import com.google.common.collect.Lists;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.List;
/**
 * @author Fred
 * @data 创建时间:2020/2/3
 */
@EnableSwagger2WebMvc
@Configuration
public class Swagger2Config {

	 private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	                .title("意丰歌")
	                .description("意丰歌产品管理接口说明文档")
	                .termsOfServiceUrl("")
	                .contact(new Contact("卞康","247967116@qq.com","247967116@qq.com"))
	                .version("1.0")
	                .build();
	    }


		@Bean
	    public Docket api() {
	        return new Docket(DocumentationType.SWAGGER_2).
	                useDefaultResponseMessages(false)
	                .select()
	                .apis(RequestHandlerSelectors.basePackage("com.base.sbc"))
					//regex("/api/.*")
					.paths(PathSelectors.any())
	                .build()
	                .securitySchemes(securitySchemes())
	                .securityContexts(securityContexts())
//	                .globalOperationParameters(pars)
	                .apiInfo(apiInfo());
	    }

	    private List<ApiKey> securitySchemes() {
	    	List<ApiKey> list = Lists.newArrayList();
			//当前用户的访问令牌，内容为：Bearer token
			list.add(new ApiKey("Authorization","Authorization",  "header"));
	        return list;
	    }

	    private AuthorizationScope[] scopes() {
	        AuthorizationScope[] scopes = {
	          new AuthorizationScope("read", "for read operations"),
	          new AuthorizationScope("write", "for write operations"),
	          new AuthorizationScope("foo", "Access foo API") };
	        return scopes;
	    }
	    private List<SecurityContext> securityContexts() {
	        return Lists.newArrayList(
	                SecurityContext.builder()
	                .securityReferences(Lists.newArrayList(new SecurityReference("Authorization", scopes())))
//	              .forPaths(PathSelectors.regex("^(?!auth).*$"))
					//regex("/api/.*"
					.forPaths(PathSelectors.any())
	                .build()
	        );
	    }
}
