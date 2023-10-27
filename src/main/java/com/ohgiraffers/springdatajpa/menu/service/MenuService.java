package com.ohgiraffers.springdatajpa.menu.service;

import com.ohgiraffers.springdatajpa.menu.dto.CategoryDTO;
import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.entity.Category;
import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import com.ohgiraffers.springdatajpa.menu.repository.CategoryRepository;
import com.ohgiraffers.springdatajpa.menu.repository.MenuRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    public MenuService(MenuRepository menuRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    /* 1. id로 entity 조회 : findById */
    public MenuDTO findMenuByCode(int menuCode) {

        // Entity로 조회한 뒤 비영속 객체인 MenuDTO로 변환해서 반환한다.
        Menu menu = menuRepository.findById(menuCode).orElseThrow(IllegalArgumentException::new);
        // 반환타입이 Optional<Menu>.get() - null 처리
        // menuCode가 올바르지 않은 값일 때, 올바르지 않은 아규먼트 IllegalArgumentException 예외 (코드가 없을 경우)

//        MenuDTO menuDTO = new MenuDTO();
//        menuDTO.setMenuCode(menu.getMenuCode());
//        .. 하나하나 처리하기 너무 번거로워서 라이브러리 사용한다. - modelMapper (MVN)

        return modelMapper.map(menu, MenuDTO.class); // Menu 필드에 있는 값들을 MenuDTO에 옮겨 담는 행위 = 필드 값이 똑같을 때 가능
    }


    /* 2-1. 모든 entity 조회 : findAll(sort) */
    public List<MenuDTO> findMenuList() {

        List<Menu> menuList = menuRepository.findAll(Sort.by("menuCode").descending());
        // properties는 필드명을 가져온다. descending 역정렬. 정렬해서 반환받고 싶으면 Sort 메소드 사용해서 매개변수를 넘긴다.

        // List는 map으로 바로 처리 불가능. Stream을 거쳐야한다.
        // stream 사용 이유 : 배열, 컬렉션, 여러가지 데이터 타입들을 동일한 방식으로 취급하기 위해서 사용한다. 배열 안에 요소들을 가공해서 넘긴다.
        // stream 안에 있는 map을 통해서 menu라는 엔티티 타입을 하나하나 modelMapper를 통해서 MenuDTO 타입으로 바꿔야 한다.
        // => Stream<MenuDTO>로 반환받는다.
        // stream은 가공 등의 처리를 하기 위한 타입이지 최종적 반환 타입을 stream으로 해서는 안된다.
        // 때문에 Collectors 컬렉션의 toList() List 타입으로 바꿔서 반환한다. (컬렉션 타입으로 다시 바꾼다)
        return menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }

    /* 2-2. 페이징 된 entity 조회 : findAll(Pageable) */
    public Page<MenuDTO> findMenuList(Pageable pageable) {
        // number -> 0부터 시작하는 숫자, page -> 1부터 시작하는 숫자

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, // 삼항연산자 이용
                // 음수 값이 넘어가면 0으로 처리, 양수 값이 넘어가면 -1
                pageable.getPageSize(),
                Sort.by("menuCode").descending());

        Page<Menu> menuList = menuRepository.findAll(pageable);
        // Page안에 map이 있기 때문에 좀 더 간편하게 변환할 수 있다. Page<Menu> -> Page<MenuDTO>로 반환
        return menuList.map(menu -> modelMapper.map(menu, MenuDTO.class));
    }

    /* 3. Query Method Test - 조회 조건을 가지고 조회 */
    public List<MenuDTO> findByMenuPrice(Integer menuPrice) {

        //List<Menu> menuList = menuRepository.findByMenuPriceGreaterThan(menuPrice);
        //List<Menu> menuList = menuRepository.findByMenuPriceGreaterThanOrderByMenuPrice(menuPrice);
        List<Menu> menuList = menuRepository.findByMenuPriceGreaterThan(menuPrice, Sort.by("menuPrice").descending());

        return menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).collect(Collectors.toList());
        // 비영속 객체로 재 반환
    }




    /* =============== Category =============== */

    /* 4. JPQL or Native Query Test */
    public List<CategoryDTO> findAllCategory() {

        List<Category> categoryList = categoryRepository.findAllCategory();

        return categoryList.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }


    /* 5. Entity 저장 - 메뉴 등록 */
    @Transactional // 어노테이션?
    public void registNewMenu(MenuDTO menu) {

        menuRepository.save(modelMapper.map(menu, Menu.class)); // 엔티티가 아니라 MenuDTO 타입이기 때문에 엔티티 형식으로 변환해준 뒤 저장한다.
    }


    /* 6. Entity 수정 - 메뉴 수정 */
    @Transactional
    public void modifyMenu(MenuDTO menu) {
        Menu foundMenu = menuRepository.findById(menu.getMenuCode()).orElseThrow(IllegalArgumentException::new);
        // 찾는 MenuCode가 없을 경우 IllegalArgumentException
        foundMenu.setMenuName(menu.getMenuName());
    }


    /* 7. Entity 삭제 - 메뉴 삭제 */
    @Transactional
    public void deleteMenu(Integer menuCode) {
        menuRepository.deleteById(menuCode);
    }



    /* 숙제 1 - 메뉴 가격 x 미만의 메뉴 목록 */
    public List<MenuDTO> findByMenuPrice2(Integer menuPrice) {

        List<Menu> menuList = menuRepository.findByMenuPriceLessThan(menuPrice, Sort.by("menuPrice").ascending());

        return menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }

    /* 숙제 2 - 메뉴 이름으로 조회하기 */
    public List<MenuDTO> findByMenuNameContaining(String menuName) {

        List<Menu> menuList = menuRepository.findByMenuNameContaining(menuName);

        return menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }

    /* 숙제 3 - 메뉴 가격대( ? ~ ? ) 조회하기 */
    public List<MenuDTO> findByMenuPriceBetween(Integer menuPrice1, Integer menuPrice2) {

        List<Menu> menuList = menuRepository.findByMenuPriceBetween(menuPrice1, menuPrice2, Sort.by("menuPrice").descending());

        return menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }

    /* 숙제 4 - jpql 메뉴 리스트 조회하기 */
    public List<MenuDTO> selectMenuList() {
        List<Menu> menuList = menuRepository.selectMenuList();

        return menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }

    /* 숙제 5 - jpql 카테고리 리스트 조회하기 */
    public List<CategoryDTO> selectCategoryList() {
        List<Category> categoryList = menuRepository.selectCategoryList();

        return categoryList.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());
    }

    /* 숙제 6 -   */
}
