package com.tfsc.ilabs.selfservice.configpuller.models;

import com.tfsc.ilabs.selfservice.configpuller.model.SearchConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author Sushil.Kumar
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchConfigTest {

    @Test
    public void testAll(){
        SearchConfig searchConfig = new SearchConfig();
        searchConfig.setPageSize(10);
        searchConfig.setPageNumber(1);
        searchConfig.setHasFilter(false);

        Assert.assertEquals(10, searchConfig.getPageSize());
        Assert.assertEquals(1, searchConfig.getPageNumber());
        Assert.assertFalse(searchConfig.isHasFilter());
    }
}
