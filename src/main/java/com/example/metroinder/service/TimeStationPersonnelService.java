package com.example.metroinder.service;


import com.example.metroinder.dto.TimeStationPersonnelDto;
import com.example.metroinder.model.TimeStationPersonnel;
import com.example.metroinder.repository.TimeStationPersonnelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
@RequiredArgsConstructor
@Slf4j
public class TimeStationPersonnelService {
    @Value("${generalKey}")
    public String generalKey;

    @Autowired
    TimeStationPersonnelRepository timeStationPersonnelRepository;

    // 서울시 지하철 호선별 역별 시간대별 승하차 인원 정보 읽기
    public String peopleInformationBySeoulAtTimeRead(String dataScope) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
        urlBuilder.append("/" + URLEncoder.encode(generalKey, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("CardSubwayTime", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("999", "UTF-8"));

        /* 서비스별 추가 요청인자*/
        urlBuilder.append("/" + URLEncoder.encode(dataScope, "UTF-8"));//월별, 최신 2022년 5월까지

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
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            stringBuilder.append(line);
        }
        rd.close();
        conn.disconnect();
        return stringBuilder.toString();
    }

    // 서울시 지하철 호선별 역별 시간대별 승하차 인원 정보 저장
    public void peopleInformationBySeoulAtTimeSave(String json) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
            JSONObject cardSubwayTime = (JSONObject) jsonObject.get("CardSubwayTime");

            JSONArray jsonArr = (JSONArray) cardSubwayTime.get("row");
            List<TimeStationPersonnelDto> jsonSameStationDtoList = new ArrayList<>();
            for (int count = 0; count < jsonArr.size(); count++) {
                JSONObject row = (JSONObject) jsonArr.get(count);
                String station = (String) row.get("SUB_STA_NM");
                if(station.equals("서울역")) {
                    station = "서울";
                }
                TimeStationPersonnelDto timeStationPersonnelDto = TimeStationPersonnelDto.builder()
                        .station(station)
                        .oneRide((int) Math.round((double) row.get("ONE_RIDE_NUM")))
                        .twoRide((int) Math.round((double) row.get("TWO_RIDE_NUM")))
                        .threeRide((int) Math.round((double) row.get("THREE_RIDE_NUM")))
                        .fourRide((int) Math.round((double) row.get("FOUR_RIDE_NUM")))
                        .fiveRide((int) Math.round((double) row.get("FIVE_RIDE_NUM")))
                        .sixRide((int) Math.round((double) row.get("SIX_RIDE_NUM")))
                        .sevenRide((int) Math.round((double) row.get("SEVEN_RIDE_NUM")))
                        .eightRide((int) Math.round((double) row.get("EIGHT_RIDE_NUM")))
                        .nineRide((int) Math.round((double) row.get("NINE_RIDE_NUM")))
                        .tenRide((int) Math.round((double) row.get("TEN_RIDE_NUM")))
                        .elevenRide((int) Math.round((double) row.get("ELEVEN_RIDE_NUM")))
                        .twelveRide((int) Math.round((double) row.get("TWELVE_RIDE_NUM")))
                        .thirteenRide((int) Math.round((double) row.get("THIRTEEN_RIDE_NUM")))
                        .fourteenRide((int) Math.round((double) row.get("FOURTEEN_RIDE_NUM")))
                        .fifteenRide((int) Math.round((double) row.get("FIFTEEN_RIDE_NUM")))
                        .sixteenRide((int) Math.round((double) row.get("SIXTEEN_RIDE_NUM")))
                        .seventeenRide((int) Math.round((double) row.get("SEVENTEEN_RIDE_NUM")))
                        .eighteenRide((int) Math.round((double) row.get("EIGHTEEN_RIDE_NUM")))
                        .nineteenRide((int) Math.round((double) row.get("NINETEEN_RIDE_NUM")))
                        .twentyRide((int) Math.round((double) row.get("TWENTY_RIDE_NUM")))
                        .twentyoneRide((int) Math.round((double) row.get("TWENTY_ONE_RIDE_NUM")))
                        .twentytwoRide((int) Math.round((double) row.get("TWENTY_TWO_RIDE_NUM")))
                        .twentythreeRide((int) Math.round((double) row.get("TWENTY_THREE_RIDE_NUM")))
                        .midnightRide((int) Math.round((double) row.get("MIDNIGHT_RIDE_NUM")))
                        .build();
                jsonSameStationDtoList.add(timeStationPersonnelDto);
            }
            TimeStationPersonnelDto timeStationPersonnelDto = new TimeStationPersonnelDto();
            List<TimeStationPersonnel> jsonSameStationList = timeStationPersonnelDto.toEntityList(jsonSameStationDtoList);
            for (TimeStationPersonnel timeStationPersonnel : jsonSameStationList) {
                timeStationPersonnelRepository.save(timeStationPersonnel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 혼잡도 리턴을 위한 메소드
    public Map findSameStationPeople(String data, int months) {
        Map json = new HashMap<String, Object>();
        List <TimeStationPersonnelRepository.SameStationPeople> sameStationPeoplelist = timeStationPersonnelRepository.findSameStationPeople(months);
        for(TimeStationPersonnelRepository.SameStationPeople sameStationPeople : sameStationPeoplelist) {
            String station = sameStationPeople.getStation();
            if(station.equals(data)) {
                json.put("station", sameStationPeople.getStation());
                json.put("oneRide", Long.valueOf(sameStationPeople.getOneRide()).intValue());
                json.put("twoRide", Long.valueOf(sameStationPeople.getTwoRide()).intValue());
                json.put("threeRide", Long.valueOf(sameStationPeople.getThreeRide()).intValue());
                json.put("fourRide", Long.valueOf(sameStationPeople.getFourRide()).intValue());
                json.put("fiveRide", Long.valueOf(sameStationPeople.getFiveRide()).intValue());
                json.put("sixRide", Long.valueOf(sameStationPeople.getSixRide()).intValue());
                json.put("sevenRide", Long.valueOf(sameStationPeople.getSevenRide()).intValue());
                json.put("eightRide", Long.valueOf(sameStationPeople.getEightRide()).intValue());
                json.put("nineRide", Long.valueOf(sameStationPeople.getNineRide()).intValue());
                json.put("tenRide", Long.valueOf(sameStationPeople.getTenRide()).intValue());
                json.put("elevenRide", Long.valueOf(sameStationPeople.getElevenRide()).intValue());
                json.put("twelveRide", Long.valueOf(sameStationPeople.getTwelveRide()).intValue());
                json.put("thirteenRide", Long.valueOf(sameStationPeople.getThirteenRide()).intValue());
                json.put("fourteenRide", Long.valueOf(sameStationPeople.getFourteenRide()).intValue());
                json.put("fifteenRide", Long.valueOf(sameStationPeople.getFifteenRide()).intValue());
                json.put("sixteenRide", Long.valueOf(sameStationPeople.getSixteenRide()).intValue());
                json.put("seventeenRide", Long.valueOf(sameStationPeople.getSeventeenRide()).intValue());
                json.put("eighteenRide", Long.valueOf(sameStationPeople.getEighteenRide()).intValue());
                json.put("nineteenRide", Long.valueOf(sameStationPeople.getNineteenRide()).intValue());
                json.put("twentyRide", Long.valueOf(sameStationPeople.getTwentyRide()).intValue());
                json.put("twentyoneRide", Long.valueOf(sameStationPeople.getTwentyoneRide()).intValue());
                json.put("twentytwoRide", Long.valueOf(sameStationPeople.getTwentytwoRide()).intValue());
                json.put("twentythreeRide", Long.valueOf(sameStationPeople.getTwentythreeRide()).intValue());
                json.put("midnightRide", Long.valueOf(sameStationPeople.getMidnightRide()).intValue());
            }
        }
        return json;
    }
    //혼잡도데이터가 담겨있는 Entity에서 겹치지 않는(distinct) 역이름을 list에 담아오기 위한 메소드
    public List<String> stationDistinctList() {
        List<String> stationList = timeStationPersonnelRepository.findDistinctStation();
        return stationList;
    }
}