package com.admin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.admin.Entities.Item;
import com.admin.Entities.ItemEnum;
import com.admin.Entities.StatusEnum;

@Component
public interface ItemRepository extends JpaRepository<Item, Integer> {

    public List<Item> findById(int id);

    public List<Item> findByItemName(String name);

    public List<Item> findByItemType(ItemEnum type);

    public List<Item> findByStatus(StatusEnum status);
}
