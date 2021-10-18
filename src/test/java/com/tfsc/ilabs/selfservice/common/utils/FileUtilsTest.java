package com.tfsc.ilabs.selfservice.common.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.text.ParseException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUtils.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class FileUtilsTest {

    @Before
    public void setup() {
        PowerMockito.spy(FileUtils.class);
    }

    @Test
    public void getFileExtensionTest() {
        String fileName = "input.txt";
        Assert.assertEquals("txt", FileUtils.getFileExtension(fileName));
    }

    @Test
    public void isValidFileTest() {
        String types = "xls,txt";
        String[] fileNames = {"input.xls", "input.png"};

        Assert.assertTrue(FileUtils.isValidFile(fileNames[0], types));
        Assert.assertFalse(FileUtils.isValidFile(fileNames[1], types));
    }

    @Test
    public void getFileNameTest() {
        String clientId = "test-id";
        String accountId = "test-id";
        String timeStamp = "01-01-01";
        String originalFileName = "test.txt";
        PowerMockito
                .when(FileUtils.getTimeStamp())
                .thenReturn(timeStamp);

        String expectedFileName = clientId + "-" + accountId + "-" + timeStamp + "-" + originalFileName;
        MockMultipartFile mockMultipartFile = new MockMultipartFile("test", originalFileName, "text/plain", "test data".getBytes());
        String fileName = FileUtils.getFileName(clientId, accountId, mockMultipartFile);
        Assert.assertEquals(expectedFileName, fileName);
    }

    @Test
    public void checkMimeTypeTest() throws IOException {
        String mimeTypes = "text/plain,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("test", "test.txt", "text/plain", "test data".getBytes());
        boolean result = FileUtils.checkMimeType(mockMultipartFile, mimeTypes);
        Assert.assertTrue(result);
    }

    @Test
    public void getTimeStampTest() throws ParseException {
        String timestamp = FileUtils.getTimeStamp();
        Assert.assertNotNull(timestamp);
    }
}
