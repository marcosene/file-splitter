package cl.system.config;

import cl.system.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * Created by marcosene on 19/05/2018.
 */
@Configuration
public class ThymeleafConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ViewResolver thymeleafViewResolver(ITemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setOrder(1);
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding(Constants.CHARSET_DEFAULT.displayName());
        viewResolver.setViewNames(new String[]{"*.html"});
        return viewResolver;
    }

    @Bean
    public ITemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Bean
    public ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/resources/templates/");
        templateResolver.setCharacterEncoding(Constants.CHARSET_DEFAULT.displayName());
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
