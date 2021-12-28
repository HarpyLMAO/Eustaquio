package me.harpylmao.eustaquio.managers.car;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import me.harpylmao.eustaquio.managers.model.Model;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
    Author: HarpyLMAO.
    All rights reserved.
    Project: Eustaquio
    26/12/2021 01
*/

@Getter
@Setter
public class Car implements Model {

    @Getter @JsonIgnore private static List<Car> cars = new ArrayList<>();

    private final String id;
    private final String name;

    private int price;

    private int speedPeak;
    private int horsepower;

    //private List<Upgrades> upgradesList = new ArrayList<>();

    public Car(
            String id,
            String name
    ) {
        this.id = id;
        this.name = name;
        cars.add(this);
    }

    @ConstructorProperties({
            "id",
            "name",
            "price",
            "speedPeak",
            "horsepower"
            //"upgrades"
    })
    public Car(
            String id,
            String name,
            int price,
            int speedPeak,
            int horsepower
            //List<Upgrades> upgradesList
    ) {
        this(id, name);
        this.price = price;
        this.speedPeak = speedPeak;
        this.horsepower = horsepower;
        //this.upgradesList = upgradesList;
    }

    @JsonIgnore
    public static Car getCarById(String id) { return cars.stream().filter(car -> car.getId().equalsIgnoreCase(id)).findFirst().orElse(null); }
    @JsonIgnore
    public static Car getCarByName(String name) { return cars.stream().filter(car -> car.getName().equalsIgnoreCase(name)).findFirst().orElse(null); }

    @Override
    public String getId() {
        return id;
    }

    /*@Getter
    public enum Upgrades {
        TURBO("Turbo Level 1", 1000),
        TURBO_2("Turbo Level 2", 1000),
        TURBO_3("Turbo Level 3", 1000),
        DRIFT("Drift Level 1", 2500),
        DRIFT_2("Drift Level 2", 3000),
        DRIFT_3("Drift Level 3", 3500),
        BODYKIT("Bodykit", 5000);

        @Getter private static List<Upgrades> upgrades = new ArrayList<>();

        private String name;
        private int price;

        Upgrades(String name, int price) {
            this.name = name;
            this.price = price;
            getUpgrades().add(this);
        }

        public static Upgrades getUpgrade(String name) { return getUpgrades().stream().filter(upgrade -> upgrade.getName().equalsIgnoreCase(name)).findFirst().orElse(null); }
    }*/
}
