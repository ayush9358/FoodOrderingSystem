package com.admin.Services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.admin.Entities.CriteriaEnum;
import com.admin.Entities.Item;
import com.admin.Entities.ItemEnum;
import com.admin.Entities.StatusEnum;
import com.admin.dao.ItemRepository;

@Component
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    // get All items
    public List<Item> getAllItems(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = null;

        if (sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Item> pageItem = itemRepository.findAll(p);

        List<Item> list = pageItem.getContent();
        return list;
    }

    // searching items by attributes
    public List<Item> searchItems(CriteriaEnum criteria, String val) {

        List<Item> list = new ArrayList<Item>();

        switch (criteria) {
            case ID:
                int i = Integer.parseInt(val);
                list = itemRepository.findById(i);
                break;

            case NAME:
                list = itemRepository.findByItemName(val);
                break;

            case TYPE:
                ItemEnum ie = ItemEnum.valueOf(val);
                list = itemRepository.findByItemType(ie);
                break;

            case STATUS:
                StatusEnum se = StatusEnum.valueOf(val);
                list = itemRepository.findByStatus(se);
                break;

            default:
                break;
        }
        return list;
    }

    // Adding item
    public Item addItem(Item item) {

        Item i = itemRepository.save(item);
        return i;
    }

    // deleting item by id
    public void deleteItem(int id) {

        itemRepository.deleteById(id);
    }

    // update single item
    public Item updateItem(int id, Item item) {

        item.setId(id);
        itemRepository.save(item);
        return item;
    }

    // update multiple items
    public List<Item> updateItems(List<Item> list) {

        for (Item i : list) {
            itemRepository.save(i);
        }
        return list;
    }

    // update items by fields
    public Item updateItemByFields(int id, Map<String, Object> fields) {

        List<Item> i = itemRepository.findById(id);
        Item it = i.get(0);
        if (it != null) {
            fields.forEach((key, value) -> {
                Field f = ReflectionUtils.findField(Item.class, key);
                if (f != null) {
                    f.setAccessible(true);
                    ReflectionUtils.setField(f, it, value);
                }
            });
            itemRepository.save(it);
            return it;
        }
        return null;
    }
}
