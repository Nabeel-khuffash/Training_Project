package com.example.MachineService;

import com.example.MachineService.services.WorkService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@EnableJpaAuditing
public class MachineServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MachineServiceApplication.class, args);
		System.out.println("Server is up!");
	}
	@Bean
	CommandLineRunner commandLineRunner(WorkService workService)
	{
		return args -> {
			//run worker (update threads) every 20 seconds
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					workService.runTasks();
				}
				//I made it after 2 seconds, so I don't have to wait while testing
			}, 2*1000, 2*1000);
		};
	}

}
