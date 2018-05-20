package cl.sovos;

import cl.sovos.utils.Constants;
import cl.sovos.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.io.File;
import java.util.EnumSet;

/**
 * Created by marcosene on 19/05/2018.
 */
public class SpringWebInitializer implements WebApplicationInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringWebInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        LOGGER.info("Inicializando la aplicación");
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();

        applicationContext.scan(SpringWebInitializer.class.getPackage().getName());

        servletContext.addListener(new ContextLoaderListener(applicationContext));
        servletContext.addListener(new RequestContextListener());

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
                dispatcherServlet(applicationContext));
        dispatcher.setAsyncSupported(true);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        LOGGER.info("Configurando local de los archivos cargados...");
        File uploadDirectory = Utils.getUploadDirectory();
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(uploadDirectory.getAbsolutePath());
        dispatcher.setMultipartConfig(multipartConfigElement);
        LOGGER.info("uploadDirectory=" + uploadDirectory.getAbsolutePath());

        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("characterEncodingFilter", characterEncodingFilter());
        characterEncodingFilter.setAsyncSupported(true);
        characterEncodingFilter.addMappingForServletNames(dispatcherTypes(true), false, "dispatcher");

        // Disable URL rewriting
        servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
        servletContext.getSessionCookieConfig().setHttpOnly(true);
    }

    private DispatcherServlet dispatcherServlet(WebApplicationContext applicationContext) {
        return new DispatcherServlet(applicationContext);
    }

    private CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(Constants.CHARSET_DEFAULT.displayName());
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    private EnumSet<DispatcherType> dispatcherTypes(boolean asyncSupported) {
        return (asyncSupported ?
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ASYNC) :
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE));
    }
}
