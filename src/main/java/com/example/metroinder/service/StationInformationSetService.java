package com.example.metroinder.service;


import com.example.metroinder.dto.CapitalareaStationDto;
import com.example.metroinder.dto.LineDto;
import com.example.metroinder.dto.StationLineDto;
import com.example.metroinder.model.CapitalareaStation;
import com.example.metroinder.model.Line;
import com.example.metroinder.model.StationLine;
import com.example.metroinder.repository.CapitalareaStationRepository;
import com.example.metroinder.repository.LineRepository;
import com.example.metroinder.repository.StationLineRepository;
import com.example.metroinder.repository.TimeStationPersonnelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StationInformationSetService {

    private final CapitalareaStationRepository capitalareaStationRepository;
    private final LineRepository lineRepository;
    private final StationLineRepository stationLineRepository;
    private final TimeStationPersonnelRepository timeStationPersonnelRepository;

    public void setStationInformation()  throws IOException {
        try {
            JSONParser jsonParser = new JSONParser();
            ClassPathResource resource = new ClassPathResource("static/json/lines.json");
            JSONArray jsonArray = (JSONArray) jsonParser.parse(new InputStreamReader(resource.getInputStream(), "UTF-8"));
            String stnLine = "";
            int routeOrder = 1;

            for (int count = 0; count < jsonArray.size(); count++) {
                JSONObject row = (JSONObject) jsonArray.get(count);
                String jsonStnLine = (String) row.get("stn_line");
                String jsonStnName = (String) row.get("stn_name");
                /*String jsonStnTransfer = (String) row.get("stn_transfer");*/

                if(jsonStnLine.equals(stnLine)) {
                    stnLine = jsonStnLine;
                    routeOrder++;
                } else {
                    stnLine = jsonStnLine;
                    routeOrder = 1;
                }

                CapitalareaStation capitalareaStation = new CapitalareaStation();
                Line line = new Line();

                if(overlapCheck(jsonStnName, "station")) {
                    CapitalareaStationDto capitalareaStationDto = new CapitalareaStationDto();
                    capitalareaStation = capitalareaStationDto.toEntity(CapitalareaStationDto.builder()
                            .station(jsonStnName).build());
                } else {
                    capitalareaStation = capitalareaStationRepository.findByStation(jsonStnName);
                }
                if(overlapCheck(jsonStnLine, "line")) {
                    LineDto lineDto = new LineDto();
                    line = lineDto.toEntity(LineDto.builder()
                            .line(jsonStnLine).build());
                } else {
                    line = lineRepository.findByLine(jsonStnLine);
                }

                StationLineDto stationLineDto = new StationLineDto();
                StationLine stationLine = stationLineDto.toEntity(StationLineDto.builder()
                        .capitalareaStation(capitalareaStation)
                        .line(line)
                        .lineOrder(routeOrder).build());
                capitalareaStationRepository.save(capitalareaStation);
                lineRepository.save(line);
                stationLineRepository.save(stationLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean overlapCheck(String checkValue, String division) {
        if(division.equals("station")) {
            if (capitalareaStationRepository.findByStation(checkValue) != null) {
                return false;
            }
            return true;
        }else {
            if (lineRepository.findByLine(checkValue) != null) {
                return false;
            }
            return true;
        }
    }

    public void setLetLon() throws IOException, ParseException, NullPointerException {
        try {
            JSONParser jsonParser = new JSONParser();
            ClassPathResource resource = new ClassPathResource("static/json/station_coordinate.json");
            JSONArray jsonArray = (JSONArray) jsonParser.parse(new InputStreamReader(resource.getInputStream(), "UTF-8"));
            //log.info(""+ jsonArray);
            for (int count = 0; count < jsonArray.size(); count++) {
                JSONObject row = (JSONObject) jsonArray.get(count);
                String station = (String) row.get("name");
                if(station.equals("서울역")) {
                    station = "서울";
                }
                Double lat = (Double) row.get("lat");
                Double lng = (Double) row.get("lng");
                // log.info(""+ station + " : " + "위도 : " + lat + ", 경도 : " + lng);
                CapitalareaStation capitalareaStation = capitalareaStationRepository.findByStation(station);
                if(capitalareaStation == null || capitalareaStation.getLat() != null && capitalareaStation.getLng() != null) {
                    continue;
                }
                capitalareaStation.setLat(lat);
                capitalareaStation.setLng(lng);
                capitalareaStationRepository.save(capitalareaStation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //혼잡도 평균 List 생성
    public void getStationDegreeOfCongestionAvg() {
        /*List<StationLine> stationLineList = new ArrayList<>();*/
        List<StationLine> stationLines = stationLineRepository.findAll();
        for(StationLine stationLine : stationLines) {
            String stationName = stationLine.getCapitalareaStation().getStation();
            String lineName = stationLine.getLine().getLine();

            TimeStationPersonnelRepository.SameStationPeople sameStationPeople = timeStationPersonnelRepository.stationDegreeOfCongestion(5, stationName, lineName);
            if(sameStationPeople == null) {
                continue;
            }
            stationLine.setOneRide(Long.valueOf(sameStationPeople.getOneRide()).intValue());
            stationLine.setTwoRide(Long.valueOf(sameStationPeople.getTwoRide()).intValue());
            stationLine.setThreeRide(Long.valueOf(sameStationPeople.getThreeRide()).intValue());
            stationLine.setFourRide(Long.valueOf(sameStationPeople.getFourRide()).intValue());
            stationLine.setFiveRide(Long.valueOf(sameStationPeople.getFiveRide()).intValue());
            stationLine.setSixRide(Long.valueOf(sameStationPeople.getSixRide()).intValue());
            stationLine.setSevenRide(Long.valueOf(sameStationPeople.getSevenRide()).intValue());
            stationLine.setEightRide(Long.valueOf(sameStationPeople.getEightRide()).intValue());
            stationLine.setNineRide(Long.valueOf(sameStationPeople.getNineRide()).intValue());
            stationLine.setTenRide(Long.valueOf(sameStationPeople.getTenRide()).intValue());
            stationLine.setElevenRide(Long.valueOf(sameStationPeople.getElevenRide()).intValue());
            stationLine.setTwelveRide(Long.valueOf(sameStationPeople.getTwelveRide()).intValue());
            stationLine.setThirteenRide(Long.valueOf(sameStationPeople.getThirteenRide()).intValue());
            stationLine.setFourteenRide(Long.valueOf(sameStationPeople.getFourteenRide()).intValue());
            stationLine.setFifteenRide(Long.valueOf(sameStationPeople.getFifteenRide()).intValue());
            stationLine.setSixteenRide(Long.valueOf(sameStationPeople.getSixteenRide()).intValue());
            stationLine.setSeventeenRide(Long.valueOf(sameStationPeople.getSeventeenRide()).intValue());
            stationLine.setEighteenRide(Long.valueOf(sameStationPeople.getEighteenRide()).intValue());
            stationLine.setNineteenRide(Long.valueOf(sameStationPeople.getNineteenRide()).intValue());
            stationLine.setTwentyRide(Long.valueOf(sameStationPeople.getTwentyRide()).intValue());
            stationLine.setTwentyoneRide(Long.valueOf(sameStationPeople.getTwentyoneRide()).intValue());
            stationLine.setTwentytwoRide(Long.valueOf(sameStationPeople.getTwentytwoRide()).intValue());
            stationLine.setTwentythreeRide(Long.valueOf(sameStationPeople.getTwentythreeRide()).intValue());
            stationLine.setMidnightRide(Long.valueOf(sameStationPeople.getMidnightRide()).intValue());

            stationLineRepository.save(stationLine);
        }
    }
}
