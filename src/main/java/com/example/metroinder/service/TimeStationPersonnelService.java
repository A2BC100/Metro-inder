package com.example.metroinder.service;


import com.example.metroinder.repository.TimeStationPersonnelRepository;
import lombok.AllArgsConstructor;
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
import java.util.ArrayList;
import java.util.List;

@Component
@Service
@AllArgsConstructor
public class TimeStationPersonnelService {
    @Value("${generalKey}")
    public String generalKey;
    @Value("${realTimeKey}")
    public String realTimeKey;
    TimeStationPersonnelRepository timeStationPersonnelRepository;

    // 서울시 지하철 호선별 역별 시간대별 승하차 인원 정보
    public void peopleInformationBySeoulAtTime() {
        try {
            StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
            urlBuilder.append("/" + URLEncoder.encode(generalKey, "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode("CardSubwayTime", "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("/" + URLEncoder.encode("5", "UTF-8"));

            /* 서비스별 추가 요청인자*/
            urlBuilder.append("/" + URLEncoder.encode("202205", "UTF-8"));//월별, 최신 2022년 5월까지

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode()); /* 연결에 대한 확인*/
            BufferedReader rd;

            // 서비스코드가 정상이면 200~300사이의 숫자
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
//            if(listTotalCount > 1000) {
//
//            }

            JSONArray jsonArr = (JSONArray) cardSubwayTime.get("row");
            List<List<JSONObject>> jsonSameStationList = new ArrayList<>();
            for(int i=0;i<jsonArr.size();i++){
                for(int j = 0; j<jsonArr.size(); j++) {
                    JSONObject row = (JSONObject)jsonArr.get(i);
                    String subStaNm = (String)row.get("SUB_STA_NM");
                    int oneRide = (int) Math.round((double)row.get("ONE_RIDE_NUM"));
                    int twoRide = (int) Math.round((double)row.get("TWO_RIDE_NUM"));
                    int threeRide = (int) Math.round((double)row.get("THREE_RIDE_NUM"));
                    int fourRide = (int) Math.round((double)row.get("FOUR_RIDE_NUM"));
                    int fiveRide = (int) Math.round((double)row.get("FIVE_RIDE_NUM"));
                    int sixRide = (int) Math.round((double)row.get("SIX_RIDE_NUM"));
                    int sevenRide = (int) Math.round((double)row.get("SEVEN_RIDE_NUM"));
                    int eightRide = (int) Math.round((double)row.get("EIGHT_RIDE_NUM"));
                    int nineRide = (int) Math.round((double)row.get("NINE_RIDE_NUM"));
                    int tenRide = (int) Math.round((double)row.get("TEN_RIDE_NUM"));
                    int elevenRide = (int) Math.round((double)row.get("ELEVEN_RIDE_NUM"));
                    int twelveRide = (int) Math.round((double)row.get("TWELVE_RIDE_NUM"));
                    int thirteenRide = (int) Math.round((double)row.get("THIRTEEN_RIDE_NUM"));
                    int fourteenRide = (int) Math.round((double)row.get("FOURTEEN_RIDE_NUM"));
                    int fifteenRide = (int) Math.round((double)row.get("FIFTEEN_RIDE_NUM"));
                    int sixteenRide = (int) Math.round((double)row.get("SIXTEEN_RIDE_NUM"));
                    int seventeenRide = (int) Math.round((double)row.get("SEVENTEEN_RIDE_NUM"));
                    int eighteenRide = (int) Math.round((double)row.get("EIGHTEEN_RIDE_NUM"));
                    int nineteenRide = (int) Math.round((double)row.get("NINETEEN_RIDE_NUM"));
                    int twentyRide = (int) Math.round((double)row.get("TWENTY_RIDE_NUM"));
                    int twentyoneRide = (int) Math.round((double)row.get("TWENTY_ONE_RIDE_NUM"));
                    int twentytwoRide = (int) Math.round((double)row.get("TWENTY_TWO_RIDE_NUM"));
                    int twentythreeRide = (int) Math.round((double)row.get("TWENTY_THREE_RIDE_NUM"));
                    int midnightRide = (int) Math.round((double)row.get("MIDNIGHT_RIDE_NUM"));

                }

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
