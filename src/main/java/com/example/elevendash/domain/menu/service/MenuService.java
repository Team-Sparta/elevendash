package com.example.elevendash.domain.menu.service;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.menu.dto.request.RegisterMenuRequestDto;
import com.example.elevendash.domain.menu.dto.response.RegisterMenuResponseDto;
import com.example.elevendash.domain.menu.entity.Category;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.menu.repository.CategoryRepository;
import com.example.elevendash.domain.menu.repository.MenuOptionRepository;
import com.example.elevendash.domain.menu.repository.MenuRepository;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final StoreRepository storeRepository;

    /**
     * 메뉴 등록 서비스 메소드
     * @param member
     * @param storeId
     * @param requestDto
     * @return
     */
    public RegisterMenuResponseDto registerMenu (Member member,Long storeId,RegisterMenuRequestDto requestDto) {
        // 가게 멤버와 로그인 멤버가 일치하지 않는경우 예외
        Store addMenuStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_SAME_MEMBER));
        // 연결할 카테고리 조회
        Category category = categoryRepository.findByCategoryName(requestDto.getMenuCategory());
        Menu saveMenu = Menu.builder()
                .menuImage(requestDto.getMenuImage())
                .menuName(requestDto.getMenuName())
                .menuPrice(requestDto.getMenuPrice())
                .store(addMenuStore)
                .category(category)
                .build();
        menuRepository.save(saveMenu);
        return new RegisterMenuResponseDto(saveMenu.getId());
    }


}
