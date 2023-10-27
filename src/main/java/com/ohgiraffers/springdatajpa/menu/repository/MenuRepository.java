package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Category;
import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {


    // 내가 컨트롤하고 싶은 대상 엔티티(Menu), Id 속성(엔티티(Menu의 pk = int menuCode) id 속성)

    /* querymethod와 연결 */
    // 전달 받은 menuPrice보다 큰 menuPrice를 가진 메뉴 목록 조회
     List<Menu> findByMenuPriceGreaterThan(Integer menuPrice);
    // find'Menu'ByMenuPrice, 엔티티명 생략 가능 - 이미 위에 <Menu, Integer>있기 때문에

    // 전달 받은 menuPrice보다 큰 menuPrice를 가진 메뉴 목록을 메뉴 가격 오름차순으로 조회
     List<Menu> findByMenuPriceGreaterThanOrderByMenuPrice(Integer menuPrice);

    // 전달 받은 menuPrice보다 큰 menuPrice를 가진 메뉴 목록을 메뉴 가격 내림차순으로 조회 (Sort)
    List<Menu> findByMenuPriceGreaterThan(Integer menuPrice, Sort sort);

    List<Menu> findByMenuPriceLessThan(Integer menuPrice, Sort menuPrice1);

    List<Menu> findByMenuNameContaining(String menuName);

    List<Menu> findByMenuPriceBetween(Integer menuPrice1, Integer menuPrice2, Sort sort);

    @Query(value = "SELECT m FROM Menu m")
    List<Menu> selectMenuList();

    @Query(value = "SELECT c FROM Category c")
    List<Category> selectCategoryList();
}



    /* QueryMethod
    * JPQL을 메소드 대신 처리할 수 있도록 제공하는 기능
    * find + 엔티티 명 + By + 변수 이름 과 같이 네이밍 룰을 이용해 메소드 이름으로 필요한 쿼리를 만들어 준다.
    * (ex. findMenuByMenuName(String menuName)) -> findByMenuName 처럼 엔티티 명은 없어도 된다. */