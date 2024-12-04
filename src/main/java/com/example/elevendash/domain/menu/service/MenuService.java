package com.example.elevendash.domain.menu.service;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.enums.MemberRole;
import com.example.elevendash.domain.menu.dto.request.RegisterMenuRequestDto;
import com.example.elevendash.domain.menu.dto.response.DeleteMenuResponseDto;
import com.example.elevendash.domain.menu.dto.response.RegisterMenuResponseDto;
import com.example.elevendash.domain.menu.entity.Category;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.menu.repository.CategoryRepository;
import com.example.elevendash.domain.menu.repository.MenuOptionRepository;
import com.example.elevendash.domain.menu.repository.MenuRepository;
import com.example.elevendash.domain.store.entity.Store;
import com.example.elevendash.domain.store.repository.StoreRepository;
import com.example.elevendash.domain.store.service.StoreService;
import com.example.elevendash.global.exception.BaseException;
import com.example.elevendash.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    @Transactional
    public RegisterMenuResponseDto registerMenu (Member member, MultipartFile menuImage, Long storeId, RegisterMenuRequestDto requestDto) {
        // 가게 조회
        Store addMenuStore = storeRepository.findByIdAndIsDeleted(storeId,Boolean.FALSE)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_STORE));
        // 유저 및 음식점 유효성 검증
        isValidMemberAndStore(member, addMenuStore);
        // 연결할 카테고리 조회
        Category category = categoryRepository.findByCategoryName(requestDto.getMenuCategory())
                .orElseThrow(() -> new BaseException(("카테고리를 찾을 수 없습니다: " + requestDto.getMenuCategory()),ErrorCode.NOT_FOUND_ENUM_CONSTANT));
        Menu saveMenu = Menu.builder()
                .menuImage(requestDto.getMenuImage())
                .menuName(requestDto.getMenuName())
                .menuPrice(requestDto.getMenuPrice())
                .store(addMenuStore)
                .category(category)
                .menuImage(StoreService.convert(menuImage))
                .build();
        menuRepository.save(saveMenu);
        return new RegisterMenuResponseDto(saveMenu.getId());
    }

    @Transactional
    public DeleteMenuResponseDto deleteMenu (Member member, Long storeId, Long menuId) {
        Store deleteMenuStore = storeRepository.findByIdAndIsDeleted(storeId, Boolean.FALSE)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_STORE));
        isValidMemberAndStore(member, deleteMenuStore);
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU));
        if(!menu.getStore().getId().equals(deleteMenuStore.getId())) {
            throw new BaseException(ErrorCode.NOT_SAME_STORE);
        }
        menuRepository.delete(menu);
        return new DeleteMenuResponseDto(menu.getId());
    }

    /**
     * 유저 및 음식점 유효성 검증
     * @param member
     * @param store
     */
    private final void isValidMemberAndStore (Member member, Store store) {
        // 가게 소유자 검증
        if(!store.getMember().equals(member)){
            throw new BaseException(ErrorCode.NOT_SAME_MEMBER);
        }
        // 유저 권한 검증
        if(!member.getRole().equals(MemberRole.OWNER)){
            throw new BaseException("OWNER만이 상점을 개설할 수 있습니다",ErrorCode.DISABLE_ACCOUNT);
        }
    }
}
