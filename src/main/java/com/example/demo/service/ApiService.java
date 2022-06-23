package com.example.demo.service;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
@Service
public class ApiService {

    @Value("${generalKey}")
    public String generalKey;
    @Value("${realTimeKey}")
    public String realTimeKey;

    // 서울시 지하철 호선별 역별 시간대별 승하차 인원 정보
    public void peopleInformationBySeoulAtTime() throws IOException {
        try {
            StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
            urlBuilder.append("/" + URLEncoder.encode(generalKey, "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode("CardSubwayTime", "UTF-8")); /*서비스명, 대소문자 구분*/
            urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode("1000", "UTF-8"));
            // 상위 5개는 필수적으로 순서바꾸지 않고 호출해야 합니다.

            /* 서비스별 추가 요청인자들*/
            urlBuilder.append("/" + URLEncoder.encode("202205", "UTF-8"));//월별, 최신 2022년 5월까지

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
            BufferedReader rd;

            // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
            JSONObject cardSubwayTime = (JSONObject) jsonObject.get("CardSubwayTime");

            int listTotalCount = Long.valueOf((Long) cardSubwayTime.get("list_total_count")).intValue() ;
            if(listTotalCount > 1000) {

            }

            JSONArray jsonArr = (JSONArray) cardSubwayTime.get("row");
            System.out.println(cardSubwayTime);
            System.out.println(listTotalCount);
            for(int i=0;i<jsonArr.size();i++){
                JSONObject row = (JSONObject)jsonArr.get(i);
                String subStaNm = (String)row.get("SUB_STA_NM");

                System.out.println(subStaNm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 실시간 열차 도착정보
    public void trainArrivalTnformation() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
        urlBuilder.append("/" + URLEncoder.encode(realTimeKey, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("realtimeStationArrival/ALL", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("5", "UTF-8"));


        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
    }
}
