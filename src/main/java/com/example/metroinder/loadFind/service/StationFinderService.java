package com.example.metroinder.loadFind.service;

import com.example.metroinder.dataSet.model.CapitalareaStation;
import com.example.metroinder.dataSet.model.Station;
import com.example.metroinder.dataSet.model.StationLine;
import com.example.metroinder.dataSet.repository.CapitalareaStationRepository;
import com.example.metroinder.dataSet.repository.StationLineRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Service
@AllArgsConstructor
@Slf4j
public class StationFinderService {
    private final CapitalareaStationRepository capitalareaStationRepository;
    private final StationLineRepository stationLineRepository;

    private List<Station> allList = new ArrayList<>();
    private List<Station> openList = new ArrayList<>();
    private List<Station> closeList = new ArrayList<>();

    // 역간의 위경도를 이용한 유클리드 거리를 계산하여 반환 (미터 단위), 휴리스틱 추정값
    private double calcDistance(Station curStn, Station nextStn){
        double lat_start = curStn.lat  * Math.PI / 180.0;
        double lon_start = curStn.lon  * Math.PI / 180.0;
        double lat_end   = nextStn.lat * Math.PI / 180.0;
        double lon_end   = nextStn.lon * Math.PI / 180.0;

        return Math.round( 6378.137 * Math.acos( Math.cos( lat_start ) * Math.cos( lat_end ) * Math.cos( lon_end - lon_start ) + Math.sin( lat_start ) * Math.sin( lat_end )) * 1000.0 );
    }
    // 거리 비용이 적은 경로를 탐색하는건 알겠지만 traffic이 사용되지 않음, 미터 단위이기 때문에 double을 사용해서 한 건 알겠음, traffic은 왜 int로 했는지 궁금함, 그냥 traffic도 double로 해서
    // 거리 비용에 더해서 최종 비용 산정할 때 같이 계산하면 되지 않는지, 내가 잘 이해하고 있는지 모르겠음
    /*private double calcTotalDistance(){
        double total_dist = 0.0;

        if( this.closeList.size() > 0 ){
            Station prvStn = null;
            for(Station station : closeList) {
                if(prvStn != null) {
                    total_dist += calcDistance(prvStn, station);
                }
                prvStn = station;
            }
        }
        return total_dist;
    }*/

    public void stationAllListSet(){
        // 기존 역 리스트 정보에 해당 역이 존재 하는지 확인
        List<CapitalareaStation> stationList = capitalareaStationRepository.findAll();
        List<Station> setAllList = new ArrayList<>();
        for(CapitalareaStation capitalareaStation : stationList) {
            Station station = new Station();

            station.setName(capitalareaStation.getStation());

            List<String> lineList = new ArrayList<>();
            List<StationLine> stationLineList = stationLineRepository.findByCapitalareaStation(capitalareaStation);
            List<Map<String, Object>> trafficList = new ArrayList<>();

            for(StationLine stationLine : stationLineList) {
                lineList.add(stationLine.getLine().getLine());
                trafficList.add(setTrafficMap(stationLine));
            }

            station.setLine(lineList);
            station.setTraffic(trafficList);
            station.setLat(capitalareaStation.getLat());
            station.setLon(capitalareaStation.getLng());

            setAllList.add(station);
        }
        this.allList = setAllList;

        for(Station station : allList) {
            CapitalareaStation capitalareaStation = capitalareaStationRepository.findByStation(station.getName());
            List<StationLine> stationLineList = stationLineRepository.findByCapitalareaStation(capitalareaStation);
            List<Station> nextStationList = new ArrayList<>();

            for(int i = 0; i < stationLineList.size(); i++) {
                StationLine nextStation = stationLineRepository.findByLineAndLineOrder(stationLineList.get(i).getLine(), stationLineList.get(i).getLineOrder() + 1);
                if(nextStation == null) {
                    continue;
                }
                nextStationList.add(getStation(nextStation.getCapitalareaStation().getStation()));
            }
            station.setNext(nextStationList);

        }
    }

    public void updateStation(Station curStn){
        // 기존 역 리스트 정보에 해당 역이 존재 하는지 확인
        for(Station station : allList) {
            // 해당 역이 존재한다면? 해당 역 혼잡도(traffic)와 거리 정보등을 업데이트 하고 종료
            if( station.name.equals(curStn.name) ){
                station.name = curStn.name;
                station.traffic = curStn.traffic;
                station.next = curStn.next;
                return;
            }
        }
        // 기존 역 리스트 정보에 해당 이름을 가진 역이 존재하지 않는다면? 새로 추가
        Station new_station = new Station();
        new_station.name = curStn.name;
        new_station.traffic = curStn.traffic;
        new_station.next = curStn.next;
        allList.add(new_station);
    }

    public List<Station> find(Station curStn, Station destStn){
        openList.add(curStn);

        // 다음 역 리스트를 열린 목록(O)에 저장
        for(Station stn : curStn.next) {
            stn.setParent(curStn);
            stn.g = calcDistance(curStn, stn);
            stn.h = calcDistance(stn, destStn);
            stn.f = stn.g + stn.h;
            openList.add(stn);
        }

        openList.remove(0);
        closeList.add(curStn);

        Station min_node = openList.get(0);
        while (true) {
            if(openList.contains(destStn) || openList.size() == 0) {
                break;
            }
            // 열린 목록(O)에서 F값이 가장 작은 노드를 열린 목록(O)에서 삭제하고 닫힌 목록(C)에 저장
            for(Station stn : openList) {
                if(stn.f < min_node.f)
                    min_node = stn;
            }
            /*for(int i = 0; i < openList.size(); i++) {
                Station stn = openList.get(i);
                if(stn.f < min_node.f)
                    min_node = stn;
            }*/
            openList.remove(min_node);
            closeList.add(min_node);

            // F값이 가장 작은 노드와 연결되어 있는 다음 노드들 중 닫힌 목록(C)에 없는 노드 가져오기
            for(Station stn : min_node.next) {
                if(closeList.contains(stn))
                    continue;
                /*if(openList.contains(stn)) {
                    Station updateStn = getStation(stn.getName());
                    updateStn.getG();
                    continue;
                }*/
                stn.setParent(min_node);
                stn.g = calcDistance(min_node, stn);
                stn.h = calcDistance(stn, destStn);
                stn.f = stn.g + stn.h;
                openList.add(stn);
            }
        }
        List<Station> findLoadList = new ArrayList<>();
        Station nowStation = getDestStation(destStn.getName());
        while (true) {
            findLoadList.add(nowStation);
            if (curStn.getName().equals(nowStation.getName())) {
                break;
            }
            nowStation = nowStation.getParent();
        }

        return findLoadList;
    }


    public Map<String, Object> setTrafficMap(StationLine stationLine) {
        Map<String, Object> trafficInfo = new HashMap<>();
        trafficInfo.put("1", stationLine.getOneRide());
        trafficInfo.put("2", stationLine.getTwoRide());
        trafficInfo.put("3", stationLine.getThreeRide());
        trafficInfo.put("4", stationLine.getFourRide());
        trafficInfo.put("5", stationLine.getFiveRide());
        trafficInfo.put("6", stationLine.getSixRide());
        trafficInfo.put("7", stationLine.getSevenRide());
        trafficInfo.put("8", stationLine.getEightRide());
        trafficInfo.put("9", stationLine.getNineRide());
        trafficInfo.put("10", stationLine.getTenRide());
        trafficInfo.put("11", stationLine.getElevenRide());
        trafficInfo.put("12", stationLine.getTwelveRide());
        trafficInfo.put("13", stationLine.getThirteenRide());
        trafficInfo.put("14", stationLine.getFourteenRide());
        trafficInfo.put("15", stationLine.getFifteenRide());
        trafficInfo.put("16", stationLine.getSixteenRide());
        trafficInfo.put("17", stationLine.getSeventeenRide());
        trafficInfo.put("18", stationLine.getEighteenRide());
        trafficInfo.put("19", stationLine.getNineteenRide());
        trafficInfo.put("20", stationLine.getTwentyRide());
        trafficInfo.put("21", stationLine.getTwentyoneRide());
        trafficInfo.put("22", stationLine.getTwentytwoRide());
        trafficInfo.put("23", stationLine.getTwentythreeRide());
        trafficInfo.put("24", stationLine.getMidnightRide());

        return trafficInfo;
    }

    // 같은 이름의 역이 있을 경우 첫번째 역으로 선택됨, - 이유 allList가 stationLine의 전체를 반복해서 만들었기 때문에 같은 역이름 다른 호선의 역정보가 있는 역 개수만큼 역정보가 생성되었을 것임 - 호선별 혼잡도 때문에 꼬이게 된듯 고민중
    public Station getStation(String stationName) {
        for(Station station : allList) {
            if(stationName.equals(station.getName())) {
                return station;
            }
        }
        return null;
    }

    public Station getDestStation(String stationName) {
        for(Station station : openList) {
            if(stationName.equals(station.getName())) {
                return station;
            }
        }
        return null;
    }
}