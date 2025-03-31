package no.vinny.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class NightflyRunner implements CommandLineRunner {

    @Autowired
    private final NightflyService nightflyService;

    public NightflyRunner(NightflyService nightflyService) {
        this.nightflyService = nightflyService;
    }


    public static void main(String[] args) {
        new SpringApplicationBuilder(NightflyRunner.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        //Recipe recipe = nightflyService.getRecipe(100001L);
        //System.out.println(recipe);
        String auth = nightflyService.auth();
        System.out.println(auth);
    }
}
