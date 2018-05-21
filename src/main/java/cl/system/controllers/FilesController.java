package cl.system.controllers;

import cl.system.utils.Utils;
import cl.system.utils.WebPath;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by marcosene on 19/05/2018.
 */
@Controller
public class FilesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesController.class);

    @GetMapping({WebPath.MVC_MAPPING_INDEX, WebPath.MVC_MAPPING_HOME})
    public String index() {
        return WebPath.MVC_VIEW_UPLOAD;
    }

    @PostMapping(WebPath.MVC_MAPPING_UPLOAD_FILE)
    public String uploadFile(@RequestParam final MultipartFile file, final Model model) throws IOException {
        if (StringUtils.isBlank(file.getOriginalFilename())) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Favor seleccionar un archivo y clicar en el botón 'Upload'");
            return WebPath.MVC_VIEW_UPLOAD;
        }
        LOGGER.debug(String.format("Archivo [%s] cargado", file.getOriginalFilename()));

        Map<String, String> fileMap = Utils.splitFile(file);
        model.addAttribute("fileMap", fileMap);
        String messageOut = String.format("Archivo %s cargado con éxito y dividido en %d segmentos",
                file.getOriginalFilename(),
                fileMap.size());
        model.addAttribute("message", messageOut);
        LOGGER.info(messageOut);
        return WebPath.MVC_VIEW_UPLOAD;
    }

    @PostMapping(WebPath.MVC_MAPPING_DOWNLOAD_FILE)
    public void downloadFile(@RequestParam final String downloadFile, final HttpServletResponse response) throws IOException {
        LOGGER.debug(String.format("Descargando archivo [%s]...", downloadFile));
        File file = new File(Utils.getUploadDirectory() + downloadFile);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        try (FileInputStream fis = new FileInputStream(file)) {
            FileCopyUtils.copy(fis, response.getOutputStream());
        }
    }

}
