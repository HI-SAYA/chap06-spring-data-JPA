package com.ohgiraffers.springdatajpa.common;

import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import org.springframework.data.domain.Page;

public class Pagenation {

    public static PagingButtonInfo getPagingButtonInfo(Page<MenuDTO> menuList) {
        int currentPage = menuList.getNumber() + 1; // 0부터 시작해서 1을 더해준 것
        int defaultButtonCount = 10; // 10개씩 버튼을 배치한다.
        int startPage
                = (int) (Math.ceil((double) currentPage / defaultButtonCount) - 1) * defaultButtonCount + 1;
        int endPage = startPage + defaultButtonCount - 1;
        if(menuList.getTotalPages() < endPage) endPage = menuList.getTotalPages(); // TotalPages = maxPage
        if(menuList.getTotalPages() == 0 && endPage == 0) endPage = startPage;

        return  new PagingButtonInfo(currentPage, startPage, endPage);
    }
}
