package com.example.demo.controller;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
//@Controller



public class PythonController {

	@Autowired
    RestTemplate restTemplate;

    @RequestMapping(path = "/callDjangoApi", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Integer> callDjangoApi(@RequestBody Map<String, String> requestData) {
        // DjangoのAPIエンドポイント
    	String djangoApiUrl = "http://localhost:8000/api/AI_Determine/";


        // 新しいMapにenteredPriceを追加
        Map<String, Object> newRequestData = new HashMap<>(requestData);
        newRequestData.put("enteredPrice", Integer.parseInt(requestData.get("enteredPrice")));
        newRequestData.put("conditionLevel", (requestData.get("enteredCondition")));

//        // リクエストヘッダーの設定
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // リクエストボディの作成
//        String requestBody = "{\"enteredPrice\": 100, \"enteredCondition\": \"good\"}";
//
//     // RestTemplateの作成
//        RestTemplate restTemplate = new RestTemplate();
//
//        // POSTリクエストの実行
//        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(djangoApiUrl, requestEntity, String.class);


        // DjangoのAPIにPOSTリクエストを送信
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(djangoApiUrl, newRequestData, Map.class);

        // レスポンスの取得
        return responseEntity.getBody();
    }
////    @GetMapping("/pylisting")
////    public String getSpringData() {
////        // 仮にSpring Bootアプリケーションが返すデータを生成
////        return "{\"message\": \"Hello from Spring Boot!\"}";
////    }
//
//	@Autowired
//	RestTemplate restTemplate;
//
//    @GetMapping("/pylisting")
//    public String getPythonData() {
//
//    	//URL「example」に対し、HTTP通信をGETリクエストで行う。
//    	//渡すパラメータは「enteredPrice」と「enteredCondition」とする。
//    	//返ってきたデータを「result」とする。
//    	//以下のエラーを回避できるプログラム「Consider defining a bean of type 'org.springframework.web.client.RestTemplate' in your configuration.
//
//    	PyObject pyObject = new PyObject();
//
//    	String url = "http://localhost:8000/determine_price/?enteredPrice=100&enteredCondition=新品、未使用";
//    	PyObject result = restTemplate.getForObject(url, pyObject.getClass());
//
//    	String resultStr = pyObject.getAi_determined_price();
//
//	return resultStr;
//}
}