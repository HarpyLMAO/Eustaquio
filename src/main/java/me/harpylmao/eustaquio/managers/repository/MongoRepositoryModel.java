package me.harpylmao.eustaquio.managers.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.harpylmao.eustaquio.managers.model.Model;

/**
 * Created by HarpyLMAO
 * at 09/10/2021 19:49
 * all credits reserved
 */

public class MongoRepositoryModel<O extends Model> implements ObjectRepository<O> {

  private MongoCollection<O> collection;

  public MongoRepositoryModel(MongoCollection<O> collection) {
    this.collection = collection;
  }

  @Override
  public O find(String id) {
    return collection.find(Filters.eq("_id", id)).first();
  }

  @Override
  public void delete(String id) {
    collection.deleteOne(Filters.eq("_id", id));
  }

  @Override
  public void save(O model) {
    collection.replaceOne(
            Filters.eq("_id", model.getId()),
            model,
            new ReplaceOptions().upsert(true)
    );
  }
}
