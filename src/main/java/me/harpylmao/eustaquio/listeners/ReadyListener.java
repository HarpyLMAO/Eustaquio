package me.harpylmao.eustaquio.listeners;

import com.mongodb.client.MongoCollection;
import me.harpylmao.eustaquio.Bot;
import me.harpylmao.eustaquio.managers.Eustaquio;
import me.harpylmao.eustaquio.managers.EustaquioManager;
import me.harpylmao.eustaquio.managers.car.Car;
import me.harpylmao.eustaquio.managers.car.CarManager;
import me.harpylmao.eustaquio.managers.repository.ObjectRepository;
import me.harpylmao.eustaquio.managers.user.User;
import me.harpylmao.eustaquio.managers.user.UserManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;

import java.util.function.Consumer;

public class ReadyListener extends ListenerAdapter {

    private final UserManager userManager;
    private final ObjectRepository<User> userObjectRepository;
    private final EustaquioManager eustaquioManager;
    private final ObjectRepository<Eustaquio> eustaquioObjectRepository;
    private final CarManager carManager;
    private final ObjectRepository<Car> carObjectRepository;

    public ReadyListener() {
        this.userManager = Bot.getInstance().getUserManager();
        this.userObjectRepository = userManager.getUserObjectRepository();
        this.eustaquioManager = Bot.getInstance().getEustaquioManager();
        this.eustaquioObjectRepository = eustaquioManager.getEustaquioObjectRepository();
        this.carManager = Bot.getInstance().getCarManager();
        this.carObjectRepository = carManager.getCarObjectRepository();
    }

    @Override
    public void onReady(ReadyEvent event) {
        String id = "6yY=@u3!x^j%e5&g!$=d+s%yvrwf%QB*z";
        Eustaquio eustaquio = this.eustaquioObjectRepository.find(id);

        event.getJDA().getUsers().forEach(users -> {
            event.getJDA().retrieveUserById(users.getId()).queue(discordUser -> {
                User user = this.userObjectRepository.find(discordUser.getId());

                if (user == null) {
                    user = new User(discordUser.getId(), discordUser.getName());
                    this.userObjectRepository.save(user);
                    System.out.println("user created: " + user.getName());
                }
            });
        });

        MongoCollection collection = Bot.getInstance().getMongoConnector().getMongoDatabase().getCollection("cars");
        collection.find().forEach((Consumer<? super Document>) document -> {
            String carId = document.getString("_id");
            String name = document.getString("name");
            int price = document.getInteger("price");
            int horsepower = document.getInteger("horsepower");
            int speedpeak = document.getInteger("speedPeak");

            Car car = new Car(carId, name);
            car.setPrice(price);
            car.setHorsepower(horsepower);
            car.setSpeedPeak(speedpeak);
            Car.getCars().add(car);
        });

        if (eustaquio == null) {
            eustaquio = new Eustaquio(id);
            eustaquioObjectRepository.save(eustaquio);
        }
    }
}
