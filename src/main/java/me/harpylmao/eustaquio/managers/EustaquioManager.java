package me.harpylmao.eustaquio.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.harpylmao.eustaquio.managers.repository.MongoRepositoryModel;
import me.harpylmao.eustaquio.managers.repository.ObjectRepository;

@Getter
public class EustaquioManager {

    private final ObjectRepository<Eustaquio> eustaquioObjectRepository;

    public EustaquioManager(MongoDatabase mongoDatabase) {
        MongoCollection<Eustaquio> collection = mongoDatabase.getCollection(
                "eustaquio",
                Eustaquio.class
        );
        this.eustaquioObjectRepository = new MongoRepositoryModel<>(collection);
    }
}
