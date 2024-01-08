package com.admin.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.admin.Entities.CriteriaEnum;
import com.admin.Entities.Item;
import com.admin.Services.ItemService;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    // get All items handler
    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

        List<Item> list = itemService.getAllItems(pageNumber, pageSize, sortBy, sortDir);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(list);
    }

    // search items by attributes handler
    @GetMapping("/search-item")
    public ResponseEntity<List<Item>> searchItems(@RequestParam(value = "criteria") CriteriaEnum criteriaEnum,
            @RequestParam(value = "value") String value) {

        List<Item> list = itemService.searchItems(criteriaEnum, value);

        if (list == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok().body(list);
    }

    // Add item handler
    @PostMapping("/item")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {

        try {
            Item i = itemService.addItem(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(i);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // delete by id handler
    @DeleteMapping("/item/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") int id) {

        try {
            itemService.deleteItem(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    // update single item handler
    @PutMapping("/item/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable("id") int id, @RequestBody Item item) {

        try {
            Item i = itemService.updateItem(id, item);
            return ResponseEntity.ok().body(i);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // update multiple items handler
    @PutMapping("/update-item")
    public ResponseEntity<List<Item>> updateItems(@RequestBody List<Item> list) {

        try {
            List<Item> l = itemService.updateItems(list);
            return ResponseEntity.ok().body(l);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // update item by fields handler
    @PatchMapping("/item/{id}")
    public ResponseEntity<Item> updateItemFields(@PathVariable("id") int id,
            @RequestBody Map<String, Object> fields) {

        try {
            Item i = itemService.updateItemByFields(id, fields);
            if (i == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok().body(i);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
