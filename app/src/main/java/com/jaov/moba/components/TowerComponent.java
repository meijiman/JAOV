package com.jaov.moba.components;

import com.badlogic.ashley.core.Component;

public class TowerComponent implements Component {
    public String team; // "blue" hoặc "red"
    public String lane; // "top", "mid", "bot"
    public String rank; // "inner", "outer"

    public TowerComponent(String team, String lane, String rank) {
        this.team = team;
        this.lane = lane;
        this.rank = rank;
    }
}
