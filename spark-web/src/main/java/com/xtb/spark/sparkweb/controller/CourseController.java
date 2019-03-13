package com.xtb.spark.sparkweb.controller;

import com.xtb.spark.sparkweb.dao.CourseClickCountDao;
import com.xtb.spark.sparkweb.domain.CourseClickCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "course")
public class CourseController {

    @Autowired private CourseClickCountDao countDao;

    @RequestMapping(value = "dynamic", method = RequestMethod.GET)
    public ModelAndView firstDemo() throws IOException {
        String tableName = "imooc_course_clickcount";
        String day = "20190312";
        List<CourseClickCount> list = countDao.query(tableName, day);
        ModelAndView modelAndView = new ModelAndView("index", "list", list);
        List<String> names = list.stream().map(CourseClickCount::getName).collect(Collectors.toList());
        return modelAndView.addObject("names", names);
    }

}
