package com.guro.kokeetea_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DataController {

    @GetMapping("/data/list")
    public String showDataList(Model model) {
        // 여기에서 데이터를 모델에 추가하거나 필요한 로직을 수행할 수 있습니다.
        // 모델을 사용하여 HTML 템플릿에 데이터를 전달합니다.
        model.addAttribute("message", "Hello from DataController!");

        // 보여줄 HTML 파일의 이름을 반환합니다.
        return "data/list"; // 이는 resources/templates 디렉토리 아래의 "data-list.html" 파일과 매칭됩니다.
    }
}