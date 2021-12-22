package me.harpylmao.eustaquio.managers.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import fr.javatic.mongo.jacksonCodec.JacksonCodecProvider;
import fr.javatic.mongo.jacksonCodec.ObjectMapperFactory;
import lombok.Getter;
import me.harpylmao.eustaquio.Bot;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by HarpyLMAO
 * at 08/10/2021 20:35
 * all credits reserved
 */

public class MongoConnector {

  public final Bot bot;

  @Getter
  private MongoClient mongoClient;
  @Getter
  private MongoClientURI mongoClientURI;
  @Getter
  private MongoDatabase mongoDatabase;

  public MongoConnector(Bot bot) {
    this.bot = bot;
  }

  public void load() {
    ObjectMapper objectMapper = this.createObjectMapper();
    CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(new JacksonCodecProvider(objectMapper)));

    MongoClientOptions.Builder options = MongoClientOptions.builder()
            .codecRegistry(codecRegistry);

    this.mongoClientURI = new MongoClientURI("mongodb+srv://harpylmao:tuputamadre02@cluster0.pmywd.mongodb.net/eustaquio?retryWrites=true&w=majority", options);
    this.mongoClient = new MongoClient(mongoClientURI);

    this.mongoDatabase = mongoClient.getDatabase("eustaquio");
  }

  public ObjectMapper createObjectMapper() {
    ObjectMapper mapper = ObjectMapperFactory.createObjectMapper();
    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    mapper.registerModule(new Jdk8Module());
    return mapper;
  }

}
