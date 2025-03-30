package no.vinny.client;

import no.vinny.nightfly.domain.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NightflyRunner implements CommandLineRunner {

    @Autowired
    private final NightflyService nightflyService;

    public NightflyRunner(NightflyService nightflyService) {
        this.nightflyService = nightflyService;
    }


    public static void main(String[] args) {
        SpringApplication.run(NightflyRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //Recipe recipe = nightflyService.getRecipe(100001L);
        //System.out.println(recipe);
        String auth = nightflyService.auth();
        System.out.println(auth);
    }
}
