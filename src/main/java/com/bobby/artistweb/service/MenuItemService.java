package com.bobby.artistweb.service;

import com.bobby.artistweb.model.MenuItem;
import com.bobby.artistweb.repo.MenuItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepo menuItemRepo;

    public List<MenuItem> fetchMenuItems() {

        if(this.menuItemRepo.findAll().size() == 0) {
            List<MenuItem> menuItems = new ArrayList<MenuItem>();
            MenuItem menuItem = new MenuItem();
            menuItem.setName("PAINTING ART.");
            menuItem.setHref("target-paint");
            menuItem.setDisable(false);
            menuItems.add(menuItem);

            MenuItem menuItem2 = new MenuItem();
            menuItem2.setName("ABOUT ME.");
            menuItem2.setHref("target-about");
            menuItem2.setDisable(false);
            menuItems.add(menuItem2);

            MenuItem menuItem3 = new MenuItem();
            menuItem3.setName("CONTACT.");
            menuItem3.setHref("target-contact");
            menuItem3.setDisable(false);
            menuItems.add(menuItem3);

            MenuItem menuItem4 = new MenuItem();
            menuItem4.setName("SERVICES.");
            menuItem4.setHref("target-services");
            menuItem4.setDisable(true );
            menuItems.add(menuItem4);

            this.menuItemRepo.saveAll(menuItems);
            return this.menuItemRepo.findByParentNull();
        } else {
            return this.menuItemRepo.findByParentNull();
        }
    }

}
