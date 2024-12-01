package com.bobby.artistweb.controller;

import com.bobby.artistweb.model.MenuItem;
import com.bobby.artistweb.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MenuItemController extends BaseController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/getMenuItems")
    public ResponseEntity<List<MenuItem>> getMenuItems() {
        List<MenuItem> menuItems = this.menuItemService.fetchMenuItems();
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }
}
