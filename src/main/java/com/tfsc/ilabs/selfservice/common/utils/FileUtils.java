package com.tfsc.ilabs.selfservice.common.utils;

import com.aspose.cells.*;
import com.tfsc.ilabs.selfservice.bulkupload.exception.FileStorageException;
import com.tfsc.ilabs.selfservice.common.models.ErrorObject;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileUtils {

    private FileUtils() {
        // hide implicit public constructor
   }

    public static boolean isValidFile(String fileName, String types) {
        String[] allowedTypes = types.split(",");
        if (fileName.contains("..")) {
            throw new FileStorageException(new ErrorObject("Filename contains invalid path sequence :" + fileName));
        }
        boolean flag = Arrays.asList(allowedTypes).stream().anyMatch(type -> {
            return type.equals(getFileExtension(fileName));
        });
        return flag;
    }

    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf('.') != -1 && fileName.lastIndexOf('.') != 0) {
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        } else return "";
    }

    public static boolean checkMimeType(MultipartFile file, String mimeTypes) throws IOException {
        String mimeType = file.getContentType();
        String[] types = mimeTypes.split(",");
        for (int i = 0; i < types.length; i++) {
            if (mimeType.equals(types[i])) {
                return true;
            }
        }
        return false;
    }

    public static String getTimeStamp() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        return timeStamp;
    }

    public static String getFileName(String clientId, String accountId, MultipartFile file) {
        String fileName = clientId + "-" + accountId + "-" + getTimeStamp() + "-" + StringUtils.cleanPath(file.getOriginalFilename());
        return fileName;
    }

    public static boolean isValidFileName(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[a-zA-Z0-9-_]{1,200}\\.[a-zA-Z0-9]{1,10}");
        Matcher matcher = pattern.matcher(fileName);

        return matcher.matches();
    }
    public static boolean isValidContent(MultipartFile file) {
        boolean safeState = false;
        try {
            if (file != null) {
                   // Load the file into the Excel document parser
                    Workbook book = new Workbook(file.getInputStream());
                    // Get safe state from Macro presence
                    safeState = !book.hasMacro();
                    // If document is safe then we pass to OLE objects analysis
                    if (safeState) {
                        // Search OLE objects in all workbook sheets
                        Worksheet sheet = null;
                        OleObject oleObject = null;
                        int totalOLEObjectCount = 0;
                        for (int i = 0; i < book.getWorksheets().getCount(); i++) {
                            sheet = book.getWorksheets().get(i);
                            for (int j = 0; j < sheet.getOleObjects().getCount(); j++) {
                                oleObject = sheet.getOleObjects().get(j);
                                if (oleObject.getMsoDrawingType() == MsoDrawingType.OLE_OBJECT) {
                                    totalOLEObjectCount++;
                                }
                            }
                        }
                        // Update safe status flag according to number of OLE object found
                        if (totalOLEObjectCount != 0) {
                            safeState = false;
                        }
                    }

            }
        }
        catch (Exception e) {
            safeState = false;
        }
        return safeState;
    }


}
