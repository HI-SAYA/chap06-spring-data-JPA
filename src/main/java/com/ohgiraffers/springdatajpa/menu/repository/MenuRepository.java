package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    // 내가 컨트롤하고 싶은 대상 엔티티(Menu), Id 속성(엔티티(Menu의 pk = int menuCode) id 속성)
}
