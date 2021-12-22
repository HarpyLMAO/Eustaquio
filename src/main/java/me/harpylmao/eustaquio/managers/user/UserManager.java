package me.harpylmao.eustaquio.managers.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.harpylmao.eustaquio.managers.repository.MongoRepositoryModel;
import me.harpylmao.eustaquio.managers.repository.ObjectRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HarpyLMAO
 * at 09/10/2021 20:51
 * all credits reserved
 */

@Getter
public class UserManager {

  private final Map<String, User> users = new HashMap<>();
  private final ObjectRepository<User> userObjectRepository;

  public UserManager(MongoDatabase mongoDatabase) {
    MongoCollection<User> collection = mongoDatabase.getCollection(
            "users",
            User.class
    );
    this.userObjectRepository = new MongoRepositoryModel<>(collection);
  }
}
