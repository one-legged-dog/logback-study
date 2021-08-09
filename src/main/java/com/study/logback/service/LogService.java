package com.study.logback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public void test() {
        LOG.trace("Trace level 테스트");
        LOG.debug("Debug level 테스트");
        LOG.info("Info level 테스트");
        LOG.warn("Warn level 테스트");
        LOG.error("Error level 테스트");
    }
}
