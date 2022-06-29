package com.mju.reviewclassifierjmj.service;

import com.mju.reviewclassifierjmj.model.Item;
import com.mju.reviewclassifierjmj.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public List<Item> findAllByLinkList(List<String> linkList) {
        return itemRepository.findAllByLinkList(linkList);
    }

    public List<String> extractItemsLink(List<Item> items) {
        List<String> linkList = new ArrayList<>();
        for (Item item : items) {
            linkList.add(item.getLink());
        }
        return linkList;
    }

    public void saveAll(List<Item> itemList) {
        itemRepository.saveAll(itemList);
    }
}
