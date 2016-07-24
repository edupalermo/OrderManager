package org.palermo.service;

import javax.transaction.Transactional;

import org.palermo.entity.Item;
import org.palermo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemService {
    
    @Autowired ItemRepository itemRepository;
    
    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }

}
