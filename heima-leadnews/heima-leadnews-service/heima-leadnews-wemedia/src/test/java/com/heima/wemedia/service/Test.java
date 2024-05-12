package com.heima.wemedia.service;

import com.heima.wemedia.WemediaApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class Test {
@Autowired
private WmNewsAutoScanService wmNewsAutoScanService;

    @org.junit.Test
    public void autoScanWmNews() {
     wmNewsAutoScanService.autoScanWmNews(6247);
    }
}