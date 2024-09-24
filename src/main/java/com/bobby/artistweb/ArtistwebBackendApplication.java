package com.bobby.artistweb;

import com.bobby.artistweb.model.AboutMe;
import com.bobby.artistweb.model.Applications;
import com.bobby.artistweb.model.UniqueValues;
import com.bobby.artistweb.repo.AboutMeRepo;
import com.bobby.artistweb.repo.ApplicationRepo;
import com.bobby.artistweb.repo.UniqueValuesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ArtistwebBackendApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ArtistwebBackendApplication.class, args);

//		/**
//		 * Initialize some data.
//		 */
//		AboutMeRepo aboutMeRepo = context.getBean(AboutMeRepo.class);
//		UniqueValuesRepo uniqueValuesRepo = context.getBean(UniqueValuesRepo.class);
		ApplicationRepo applicationRepo = context.getBean(ApplicationRepo.class);
//
//		if (aboutMeRepo.findAll().size() == 0) {
//			AboutMe aboutMe = new AboutMe();
//			aboutMe.setId(0);
//			aboutMe.setTitle("Bella the Fairy - Face Painting");
//			aboutMe.setDescription("My name is Bella, I'm a Fairy from Auckland, New Zealand!<br /><br />\n" +
//					"\n" +
//					"I am a talented face painter who is available to do face painting at kids birthday parties in St Heliers and nearby areas. You can see examples of my face painting on this website, all face painting you can see (including pictures of myself that I painted with a mirror!) is my own work. If there is a particular design your child would love (e.g a particular animal) then let me know, and if I don't already know it I can learn it before the party.");
//			aboutMeRepo.save(aboutMe);
//		}
//
//		if (uniqueValuesRepo.findAll().size() == 0) {
//			UniqueValues uv1 = new UniqueValues();
//			uv1.setValue("We make the birthday boy/girl special.");
//
//			UniqueValues uv2 = new UniqueValues();
//			uv2.setValue("We are always on time and have never cancelled a party!");
//
//			UniqueValues uv3 = new UniqueValues();
//			uv3.setValue("No nasty chemicals!");
//
//			UniqueValues uv4 = new UniqueValues();
//			uv4.setValue("Fastest face painters in town.");
//
//			uniqueValuesRepo.save(uv1);
//			uniqueValuesRepo.save(uv2);
//			uniqueValuesRepo.save(uv3);
//			uniqueValuesRepo.save(uv4);
//		}
//
		if (applicationRepo.findAll().size() == 0) {
			Applications app = new Applications();
			applicationRepo.save(app);
		}
	}
}
