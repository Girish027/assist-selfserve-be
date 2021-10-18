package com.tfsc.ilabs.selfservice.bulkupload.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UploadFileServiceTest {

    @InjectMocks
    UploadFileService uploadFileService = new UploadFileService();

    @Mock
    LocalFileStorageService localFileStorageService;

    @Mock
    CloudUploadService cloudUploadService;

    @Test
    public void uploadTest(){
        UploadService resultForLocal = uploadFileService.upload("local");
        UploadService resultForCloud = uploadFileService.upload("gcp");
        UploadService resultForNull = uploadFileService.upload(null);
        UploadService resultForOther = uploadFileService.upload("other");
        Assert.assertEquals(localFileStorageService,resultForLocal);
        Assert.assertEquals(cloudUploadService,resultForCloud);
        Assert.assertNull(resultForNull);
        Assert.assertNull(resultForOther);
    }

}
