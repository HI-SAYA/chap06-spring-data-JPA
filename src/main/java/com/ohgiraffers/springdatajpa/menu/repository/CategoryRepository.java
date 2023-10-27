package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /* Category 테이블의 모든 entity를 조회하기 위해서 별도의 JPQL 정의는 필요하지 않지만
    * 예제를 만들기 위해서 작성해 본다. (findAll 기능으로 조회 가능) */

    /* JPQL */
    //@Query(value = "SELECT c FROM Category c ORDER BY c.categoryCode")
    /* Native */
    @Query(value = "SELECT CATEGORY_CODE, CATEGORY_NAME, REF_CATEGORY_CODE FROM TBL_CATEGORY ORDER BY CATEGORY_CODE",
            nativeQuery = true)
    List<Category> findAllCategory();


}
