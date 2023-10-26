package com.ohgiraffers.springdatajpa.menu.service;

import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import com.ohgiraffers.springdatajpa.menu.repository.MenuRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;

    public MenuService(MenuRepository menuRepository, ModelMapper modelMapper) {
        this.menuRepository = menuRepository;
        this.modelMapper = modelMapper;
    }

    public MenuDTO findMenuByCode(int menuCode) {

        // Entity로 조회한 뒤 비영속 객체인 MenuDTO로 변환해서 반환한다.
        Menu menu = menuRepository.findById(menuCode).orElseThrow(IllegalArgumentException::new);
        // 반환타입이 Optional<Menu>.get() - null 처리   // menuCode가 올바르지 않은 값일 때 IllegalArgumentException 예외 (코드가 없을 경우)

//        MenuDTO menuDTO = new MenuDTO();
//        menuDTO.setMenuCode(menu.getMenuCode());
//        .. 하나하나 처리하기 너무 번거로워서 라이브러리 사용한다. - modelMapper (MVN)

        return modelMapper.map(menu, MenuDTO.class); // Menu 필드에 있는 값들을 MenuDTO에 옮겨담는 행위
    }
}
