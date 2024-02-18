package com.example.metroinder.dataSet.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
public class Station {
    public String name;
    public List<String> line;
    public List<Map<String, Object>> traffic;
    public double lat;
    public double lon;
    public double f;
    public double g;
    public double h;
    //public List<Station> parent; // 부모 노드는 1개?
    public Station parent;
    public List<Station> next;
}
