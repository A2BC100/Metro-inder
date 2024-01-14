package com.example.metroinder.service;

import com.example.metroinder.dto.StationScheduleDto;
import com.example.metroinder.model.StationSchedule;
import com.example.metroinder.repository.CapitalareaStationRepository;
import com.example.metroinder.repository.StationScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
@RequiredArgsConstructor
@Slf4j
public class StationScheduleService {
    @Value("${generalKey}")
    private String generalKey;

    private final StationScheduleRepository stationScheduleRepository;
    private final CapitalareaStationRepository capitalareaStationRepository;

    /* 입력한 역이름으로 역코드목록을 json으로 받아옴 */
    public String getStationCode(String station) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openAPI.seoul.go.kr:8088");
        urlBuilder.append("/" + URLEncoder.encode(generalKey, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("SearchInfoBySubwayNameService", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("5", "UTF-8"));

        /* 서비스별 추가 요청인자*/
        /* 전철역이름 */
        if(station.equals("서울")) {
            station = "서울역";
        }
        urlBuilder.append("/" + URLEncoder.encode(station, "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        //System.out.println("Response code: " + conn.getResponseCode()); /* 연결에 대한 확인*/
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

    public String getStationSchduleAPI(String stationCode, String week, String inout) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openAPI.seoul.go.kr:8088");
        urlBuilder.append("/" + URLEncoder.encode(generalKey, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("SearchSTNTimeTableByIDService", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("5", "UTF-8"));

        /* 서비스별 추가 요청인자*/
        /* 전철역코드 */
        urlBuilder.append("/" + URLEncoder.encode(stationCode, "UTF-8"));
        /* 요일 */
        urlBuilder.append("/" + URLEncoder.encode(week, "UTF-8"));
        /* 상/하행선 */
        urlBuilder.append("/" + URLEncoder.encode(inout, "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        //System.out.println("Response code: " + conn.getResponseCode());
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

    /* JSON List에서 역코드를 뽑아와 열차 시간표 API 호출 */
    public void stationScheduleCall(String json, String week, String inout) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
            JSONObject searchInfoBySubwayName = (JSONObject) jsonObject.get("SearchInfoBySubwayNameService");
            //log.info("json("+ i + ") : " + searchInfoBySubwayName);
            JSONArray jsonArr = (JSONArray) searchInfoBySubwayName.get("row");
            //JSONArray result = (JSONArray) searchInfoBySubwayName.get("RESULT");
            List<String> stationScheduleList = new ArrayList<>();
            for (int count = 0; count < jsonArr.size(); count++) {
                JSONObject row = (JSONObject) jsonArr.get(count);
                String stationCode = (String) row.get("STATION_CD");

                String lineNum = (String) row.get("LINE_NUM");
                if(lineNum.equals("01호선") || lineNum.equals("02호선") || lineNum.equals("03호선") || lineNum.equals("04호선") || lineNum.equals("05호선") || lineNum.equals("06호선") || lineNum.equals("07호선") || lineNum.equals("08호선")) {
                    String stationSchduleJson = getStationSchduleAPI(stationCode, week, inout);
                    // log.info("역 시간표 : " + stationSchduleJson);
                    setStationScheduleSave(stationSchduleJson, lineNum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /* 열차 시간표 저장 */
    public void setStationScheduleSave(String json, String line) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
            //log.info("jsonObject : " + jsonObject);
            JSONObject result = (JSONObject) jsonObject.get("RESULT");
            if(result != null) {
                return;
            }
            JSONObject searchSTNTimeTableByIDService = (JSONObject) jsonObject.get("SearchSTNTimeTableByIDService");
            JSONArray jsonArr = (JSONArray) searchSTNTimeTableByIDService.get("row");
            List<StationScheduleDto> stationScheduleDtoList = new ArrayList<>();
            for (int count = 0; count < jsonArr.size(); count++) {
                JSONObject row = (JSONObject) jsonArr.get(count);
                String week = (String) row.get("WEEK_TAG");
                String upDown = (String) row.get("INOUT_TAG");
                String express = (String) row.get("EXPRESS_YN");
                if(week.equals("1")) {
                    week = "평일";
                }else if (week.equals("2")) {
                    week = "토요일";
                }else {
                    week = "휴일/일요일";
                }
                upDown = upDown.equals("1") ? "상행, 내선" : "하행, 외선";
                express = express.equals("G") ? "일반" : "급행";

                StationScheduleDto stationScheduleDto = StationScheduleDto.builder()
                        .station((String) row.get("STATION_NM"))
                        .line(line)
                        .arrivalTime((String) row.get("LEFTTIME"))
                        .departureTime((String) row.get("ARRIVETIME"))
                        .arrivalStation((String) row.get("SUBWAYSNAME"))
                        .departureStation((String) row.get("SUBWAYENAME"))
                        .week(week)
                        .upDown(upDown)
                        .express(express)
                        .build();
                stationScheduleDtoList.add(stationScheduleDto);
            }
            StationScheduleDto stationScheduleDto = new StationScheduleDto();
            List<StationSchedule> stationScheduleList = stationScheduleDto.toEntityList(stationScheduleDtoList);
            for (StationSchedule stationSchedule : stationScheduleList) {
                stationScheduleRepository.save(stationSchedule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //혼잡도데이터가 담겨있는 Entity에서 겹치지 않는(distinct) 역이름을 list에 담아오기 위한 메소드
    public List<String> stationDistinctList() {
        List<String> stationList = capitalareaStationRepository.findDistinctStation();
        return stationList;
    }

    /*public Map findStationSchedule(String stationName) {
        Map json = new HashMap<String, Object>();
        Map weekDivision = new HashMap<String, Object>();

        JSONArray week = new JSONArray();
        JSONArray direction = new JSONArray();
        JSONObject time = new JSONObject();
        JSONArray minute = new JSONArray();
        List<StationSchedule> stationScheduleList = stationScheduleRepository.findByStation(stationName);

        List<String>

        for(StationSchedule stationSchedule : stationScheduleList) {
            String station = stationSchedule.getStation(); // 역이름
            String express = stationSchedule.getExpress(); // 급행여부 구분
            String upDown = stationSchedule.getUpDown(); // 상,하행 구분
            String departureStation = stationSchedule.getDepartureStation();//출발역
            String arrivalStation = stationSchedule.getArrivalStation(); // 도착역
            String arrivalTime = stationSchedule.getArrivalTime(); //출발시간
            String weekDivision = stationSchedule.getWeek(); //평일, 토요일, 휴일+일요일
            String timeDivision = arrivalTime.substring(0,1);
            String minuteDivision = arrivalTime.substring(3,4);

        }



        return json;
    }*/

}
