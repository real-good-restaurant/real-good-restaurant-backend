package com.mju.reviewclassifierjmj.repository;

import com.mju.reviewclassifierjmj.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "SELECT i FROM Item i WHERE i.link IN (:linkList)")
    List<Item> findAllByLinkList(List<String> linkList);
}
