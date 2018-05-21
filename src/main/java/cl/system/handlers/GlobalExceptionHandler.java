package cl.system.handlers;

import cl.system.utils.WebPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by marcosene on 19/05/2018.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public String handleError1(Exception e, Model model) {
        LOGGER.error("Ocurrió un error interno", e);
        model.addAttribute("error", true);
        model.addAttribute("message", "Proceso fallido. Favor contactar soporte.");
        return WebPath.MVC_VIEW_UPLOAD;

    }
}
