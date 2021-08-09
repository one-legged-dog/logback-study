package com.study.logback.controller;

import com.study.logback.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LogController {
    // LoggerFactory 클래스에서 .getLogger(경로) 함수로 로거객체를 가져옴
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @RequestMapping(value = "/test")
    public ModelAndView test() {
        LOG.trace("Trace level 테스트");
        LOG.debug("Debug level 테스트");
        LOG.info("Info level 테스트");
        LOG.warn("Warn level 테스트");
        LOG.error("Error level 테스트");

        logService.test();

        ModelAndView mav = new ModelAndView();

        return mav;
    }
}
