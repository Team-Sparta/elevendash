package com.example.elevendash.domain.menu.service;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.enums.MemberRole;
import com.example.elevendash.domain.menu.dto.request.AddMenuOptionRequestDto;
import com.example.elevendash.domain.menu.dto.request.RegisterMenuRequestDto;
import com.example.elevendash.domain.menu.dto.request.UpdateMenuOptionRequestDto;
import com.example.elevendash.domain.menu.dto.request.UpdateMenuRequestDto;
import com.example.elevendash.domain.menu.dto.response.*;
import com.example.elevendash.domain.menu.entity.Category;
import com.example.elevendash.domain.menu.entity.Menu;
import com.example.elevendash.domain.menu.entity.MenuOption;
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
    private final StoreService storeService;

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
                .menuName(requestDto.getMenuName())
                .menuPrice(requestDto.getMenuPrice())
                .store(addMenuStore)
                .category(category)
                .menuImage(storeService.convert(menuImage))
                .build();
        menuRepository.save(saveMenu);
        return new RegisterMenuResponseDto(saveMenu.getId());
    }

    /**
     * 메뉴 삭제 서비스 메소드
     * @param member
     * @param storeId
     * @param menuId
     * @return
     */
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
     * 메뉴 수정 서비스 메소드
     * @param member
     * @param storeId
     * @param menuId
     * @param requestDto
     * @return
     */
    @Transactional
    public UpdateMenuResponseDto updateMenu (Member member, MultipartFile menuImage,Long storeId, Long menuId, UpdateMenuRequestDto requestDto) {
        Store updateMenuStore = storeRepository.findByIdAndIsDeleted(storeId,Boolean.FALSE)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_STORE));
        isValidMemberAndStore(member, updateMenuStore);
        Menu updateMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU));
        isValidMenuAndStore(updateMenu, updateMenuStore);
        Category updateMenuCategory = categoryRepository.findByCategoryName(requestDto.getMenuCategory())
                .orElseThrow(() -> new BaseException(("카테고리를 찾을 수 없습니다: " + requestDto.getMenuCategory()),ErrorCode.NOT_FOUND_ENUM_CONSTANT));
        updateMenu.update(requestDto.getMenuName(), requestDto.getMenuPrice()
                ,updateMenuCategory, storeService.convert(menuImage));
        return new UpdateMenuResponseDto(updateMenu.getId());
    }

    /**
     * 메뉴 옵션 추가 서비스 메소드
     * @param member
     * @param storeId
     * @param menuId
     * @param requestDto
     * @return
     */
    @Transactional
    public AddMenuOptionResponseDto addOption (Member member, Long storeId, Long menuId, AddMenuOptionRequestDto requestDto) {
        Store addOptionStore = storeRepository.findByIdAndIsDeleted(storeId,Boolean.FALSE)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_STORE));
        isValidMemberAndStore(member, addOptionStore);
        Menu addOptionMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU));
        isValidMenuAndStore(addOptionMenu, addOptionStore);
        MenuOption addOption = new MenuOption(requestDto.getContent(),addOptionMenu);
        menuOptionRepository.save(addOption);
        return new AddMenuOptionResponseDto(addOption.getId());
    }

    /**
     * 메뉴 옵션 수정 서비스 메소드
     * @param member 요청한 회원
     * @param storeId 상점 ID
     * @param menuId 메뉴 ID
     * @param menuOptionId 메뉴 옵션 ID
     * @param requestDto 수정 요청 데이터
     * @return 수정된 메뉴 옵션의 응답 DTO
     */
    @Transactional(readOnly = true)
    public UpdateMenuOptionResponseDto updateOption (Member member, Long storeId, Long menuId,Long menuOptionId ,UpdateMenuOptionRequestDto requestDto) {
        Store updateOptionStore = storeRepository.findByIdAndIsDeleted(storeId,Boolean.FALSE)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_STORE));
        isValidMemberAndStore(member, updateOptionStore);
        Menu updateOptionMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU));
        isValidMenuAndStore(updateOptionMenu, updateOptionStore);
        MenuOption updateOption = menuOptionRepository.findById(menuOptionId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU_OPTION));

        return updateOptionTransactional(requestDto,updateOption);
    }

    /**
     * 메뉴 옵션 삭제 서비스 메소드
     * @param member
     * @param storeId
     * @param menuId
     * @param menuOptionId
     * @return
     */
    @Transactional
    public DeleteMenuOptionResponseDto deleteOption (Member member, Long storeId, Long menuId, Long menuOptionId) {
        Store updateOptionStore = storeRepository.findByIdAndIsDeleted(storeId,Boolean.FALSE)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_STORE));
        isValidMemberAndStore(member, updateOptionStore);
        Menu updateOptionMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU));
        isValidMenuAndStore(updateOptionMenu, updateOptionStore);
        MenuOption deleteOption = menuOptionRepository.findById(menuOptionId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU_OPTION));
        menuOptionRepository.delete(deleteOption);
        return new DeleteMenuOptionResponseDto(deleteOption.getId());
    }

    /**
     * 옵션 수정 메소드
     */
    @Transactional
    protected UpdateMenuOptionResponseDto updateOptionTransactional(UpdateMenuOptionRequestDto requestDto, MenuOption updateOption) {
        updateOption.update(requestDto.getContent());
        return new UpdateMenuOptionResponseDto(updateOption.getId());
    }
    /**
     * 유저 및 음식점 유효성 검증
     * @param member
     * @param store
     */
    private void isValidMemberAndStore (Member member, Store store) {
        // 가게 소유자 검증
        if(!store.getMember().equals(member)){
            throw new BaseException(ErrorCode.NOT_SAME_MEMBER);
        }
        // 유저 권한 검증
        if(!member.getRole().equals(MemberRole.OWNER)){
            throw new BaseException("OWNER만이 상점을 개설할 수 있습니다",ErrorCode.DISABLE_ACCOUNT);
        }
    }

    private void isValidMenuAndStore (Menu menu, Store store) {
        // 가게 메뉴 검증
        if(!store.equals(menu.getStore())){
            throw new BaseException(ErrorCode.NOT_SAME_STORE);
        }
    }

}
