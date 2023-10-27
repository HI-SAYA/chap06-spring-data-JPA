package com.ohgiraffers.springdatajpa.menu.controller;

import com.ohgiraffers.springdatajpa.common.Pagenation;
import com.ohgiraffers.springdatajpa.common.PagingButtonInfo;
import com.ohgiraffers.springdatajpa.menu.dto.CategoryDTO;
import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }


    @GetMapping("/{menuCode}")
    public String findMenuByCode(@PathVariable int menuCode, Model model) {  // @Pathvariable로 원하는 menuCode를 뽑아온다.

        MenuDTO menu = menuService.findMenuByCode(menuCode);
        model.addAttribute("menu", menu);

        return "menu/detail";
    }

    /* 페이징 이전 버전 */
/*    @GetMapping("/list")
    public String findMenuList(Model model) {
        List<MenuDTO> menuList = menuService.findMenuList();
        model.addAttribute("menuList", menuList);
        return "menu/list";
    }*/


    /* 페이징 이후 버전 */
    @GetMapping("/list")
    public String findMenuList(@PageableDefault Pageable pageable, Model model) {

        /* page -> number, size, sort 파라미터가 Pageable 객체에 담긴다. */
        log.info("pageable : {}:", pageable);

        Page<MenuDTO> menuList = menuService.findMenuList(pageable);

        /* Page 객체가 담고 있는 정보 - 간단한 page 정보만 넘겼지만 많은 정보로 가공해서 다시 알려준다. */
        log.info("조회한 내용 목록 : {}", menuList.getContent());
        log.info("총 페이지 수 : {}", menuList.getTotalPages());
        log.info("총 메뉴 수 : {}", menuList.getTotalElements());
        log.info("해당 페이지에 표시 될 요소 수 : {}", menuList.getSize());
        log.info("해당 페이지에 실제 요소 수 : {}", menuList.getNumberOfElements());
        log.info("첫 페이지 여부 : {}", menuList.isFirst());
        log.info("마지막 페이지 여부 : {}", menuList.isLast());
        log.info("정렬 방식 : {}", menuList.getSort());
        log.info("여러 페이지 중 현재 인덱스 : {}", menuList.getNumber());

        /* 페이징 바 */
        PagingButtonInfo paging = Pagenation.getPagingButtonInfo(menuList); // Pagenation에 getPagingButtonInfo를 만들어 달라.

        model.addAttribute("menuList", menuList);
        model.addAttribute("paging", paging);

        return "menu/list";
    }


    /* 쿼리 메소드로 조회하기 */
    @GetMapping("/querymethod")
    public void queryMethodPage(){}


    @GetMapping("/search")
    public String findByMenuPrice(@RequestParam Integer menuPrice, Model model) { // querymethod.html의 menuPrice와 같음

        List<MenuDTO> menuList = menuService.findByMenuPrice(menuPrice);
        model.addAttribute("menuList", menuList);

        return "menu/searchResult";
    }


    /* 메뉴 등록 페이지로 이동 */
    @GetMapping("/regist")
    public void registPage(){}


    @GetMapping("/category")
    @ResponseBody // 응답 바디에 들어갈 데이터는 CategoryDTO
    public List<CategoryDTO> findCategoryList(){
        return menuService.findAllCategory();
    }


    /* 메뉴 등록 */
    @PostMapping("/regist")
    public String registMenu(MenuDTO menu) {

        menuService.registNewMenu(menu);

        return "redirect:/menu/list";
    }


    /* 메뉴 수정 페이지로 이동 */
    @GetMapping("/modify")
    public void modifyPage() {}


    /* 메뉴 수정 */
    @PostMapping("/modify")
    public String modifyMenu(MenuDTO menu) {

        menuService.modifyMenu(menu);

        return "redirect:/menu/" + menu.getMenuCode();
    }


    /* 메뉴 삭제 페이지로 이동 */
    @GetMapping("/delete")
    public void deletePage(){}


    /* 메뉴 삭제 */
    @PostMapping("/delete")
    public String deleteMenu(@RequestParam Integer menuCode) {

        menuService.deleteMenu(menuCode);

        return "redirect:/menu/list";
    }


    /* 숙제 1 - 메뉴 가격 x 미만의 메뉴 목록 */
    @GetMapping("/querymethod2")
    public void queryMethodPage2(){}

    @GetMapping("/searchLessThan")
    public String findByMenuPrice2(@RequestParam Integer menuPrice, Model model) {

        List<MenuDTO> menuList = menuService.findByMenuPrice2(menuPrice);
        model.addAttribute("menuList", menuList);

        return "menu/searchResult2";
    }


    /* 숙제 2 - 메뉴 이름으로 조회하기 */
    @GetMapping("/menuNameContaining")
    public void menuNamePage(){}

    @GetMapping("/searchMenuName")
    public String findByMenuNameContaining(@RequestParam(name = "menuName") String menuName, Model model) {

        List<MenuDTO> menuList = menuService.findByMenuNameContaining(menuName);
        model.addAttribute("menuList", menuList);

        return "menu/menuNameResult";
    }


    /* 숙제 3 - 메뉴 가격대( ? ~ ?) 조회하기 */
    @GetMapping("/menuPriceBetween")
    public void menuPriceBetweenPage(){}

    @GetMapping("/searchMenuPrice")
    public String findByMenuPriceBetween(@RequestParam Integer menuPrice1, Integer menuPrice2, Model model) {

        List<MenuDTO> menuList = menuService.findByMenuPriceBetween(menuPrice1, menuPrice2);
        model.addAttribute("menuList", menuList);

        return "menu/menuPriceResult";
    }


    /* 숙제 4 - jpql 메뉴 */
    @GetMapping("/searchMenuList")
    public String MenuListPage(Model model){

        List<MenuDTO> menuList = menuService.selectMenuList();
        model.addAttribute("menuList", menuList);

        return "menu/searchMenuList";
    }


    /* 숙제 5 - jpql 카테고리 */
    @GetMapping("/searchCategoryList")
    public String CategoryListPage(Model model) {

        List<CategoryDTO> categoryList = menuService.selectCategoryList();
        model.addAttribute("categoryList", categoryList);

        return "menu/searchCategoryList";
    }



}
