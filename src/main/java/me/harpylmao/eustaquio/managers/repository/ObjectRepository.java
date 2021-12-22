package me.harpylmao.eustaquio.managers.repository;


import me.harpylmao.eustaquio.managers.model.Model;

/**
 * Created by HarpyLMAO
 * at 09/10/2021 19:47
 * all credits reserved
 */

public interface ObjectRepository<O extends Model> {

  O find(String id);
  void delete(String id);
  void save(O model);

}
