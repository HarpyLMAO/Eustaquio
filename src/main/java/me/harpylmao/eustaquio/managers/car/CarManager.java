package me.harpylmao.eustaquio.managers.car;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.harpylmao.eustaquio.managers.repository.MongoRepositoryModel;
import me.harpylmao.eustaquio.managers.repository.ObjectRepository;

/*
    Author: HarpyLMAO.
    All rights reserved.
    Project: Eustaquio
    26/12/2021 01
*/

@Getter
public class CarManager {

    private final ObjectRepository<Car> carObjectRepository;

    public CarManager(MongoDatabase mongoDatabase) {
        MongoCollection<Car> collection = mongoDatabase.getCollection(
                "cars",
                Car.class
        );
        this.carObjectRepository = new MongoRepositoryModel<>(collection);
    }
}
