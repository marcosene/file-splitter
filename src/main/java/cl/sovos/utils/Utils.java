package cl.sovos.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by marcosene on 19/05/2018.
 */
public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static String getUploadDirectory() {
        String uploadPath = System.getProperty("upload.path");
        if (StringUtils.isBlank(uploadPath)) {
            uploadPath = System.getProperty("java.io.tmpdir");
        }

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        return uploadDir.getAbsolutePath() + File.separator;
    }

    public static Map<String, String> splitFile(final MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        int filesCounter = Utils.getFilesCounter(file);
        int fileMinSize = fileBytes.length / filesCounter;

        Map<String, String> fileMap = new LinkedHashMap<>();

        // Crea un directorio cuyo nombre sea unico por transaccion
        // garantizando la unicidad en un entorno multithreads
        // por <timestamp con millisegundos> y <nombre del thread>
        String folder = String.format("%s-%s",
                DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmssSSS"),
                Thread.currentThread().getName());

        int fileIndex = 0;
        do {
            String newFilename = String.format("%s%s%s.%03d",
                    folder,
                    File.separator,
                    file.getOriginalFilename(),
                    fileIndex);
            LOGGER.debug(String.format("Creando segmento de archivo [%s]", newFilename));

            File newFile = new File(Utils.getUploadDirectory() + newFilename);
            if (!newFile.exists()) {
                newFile.getParentFile().mkdirs();
                newFile.createNewFile();
            }

            if (!file.isEmpty()) {
                int startArrayIndex = fileIndex * fileMinSize;
                int endArrayIndex = fileIndex + 1 == filesCounter ? fileBytes.length : (fileIndex + 1) * fileMinSize;
                byte[] byteArray = ArrayUtils.subarray(fileBytes, startArrayIndex, endArrayIndex);
                FileCopyUtils.copy(byteArray, newFile);
            }
            LOGGER.debug(String.format("Segmento de archivo [%s] creado [%d bytes]", newFile.getAbsolutePath(), newFile.length()));

            fileMap.put(newFile.getName(), newFilename);
            fileIndex++;
        } while (fileIndex < filesCounter);

        return fileMap;
    }

    private static int getFilesCounter(final MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            // Va a generar un único archivo vacío igual
            return 1;
        }

        // Chequea si la cantidad de bytes es menor que el numero aleatorio de archivos
        return Math.min(
                (RandomUtils.nextInt() % Constants.MAX_SPLIT_FILES) + 1,
                file.getBytes().length);
    }
}
