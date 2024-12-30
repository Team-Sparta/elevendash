package com.example.elevendash.domain.menu.service;

import com.example.elevendash.domain.member.entity.Member;
import com.example.elevendash.domain.member.enums.MemberRole;
import com.example.elevendash.domain.menu.dto.MenuOptionInfo;
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
import com.example.elevendash.global.s3.S3Service;
import com.example.elevendash.global.s3.UploadImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final StoreRepository storeRepository;
    private final StoreService storeService;
    private final S3Service s3Service;

    /**
     * 메뉴 등록 서비스 메소드
     *
     * @param member
     * @param storeId
     * @param requestDto
     * @return
     */
    @Transactional
    public RegisterMenuResponseDto registerMenu(Member member, MultipartFile menuImage, Long storeId, RegisterMenuRequestDto requestDto) {
        // 가게 조회
        Store store = getValidatedStore(member, storeId);
        // 연결할 카테고리 조회
        Category category = categoryRepository.findByCategoryName(requestDto.getMenuCategory())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_CATEGORY));
        Menu saveMenu = Menu.builder()
                .menuName(requestDto.getMenuName())
                .menuPrice(requestDto.getMenuPrice())
                .store(store)
                .category(category)
                .menuImage(convert(menuImage))
                .menuDescription(requestDto.getMenuDescription())
                .build();
        menuRepository.save(saveMenu);
        return new RegisterMenuResponseDto(saveMenu.getId());
    }

    /**
     * 메뉴 삭제 서비스 메소드
     *
     * @param member
     * @param storeId
     * @param menuId
     * @return
     */
    @Transactional
    public DeleteMenuResponseDto deleteMenu(Member member, Long storeId, Long menuId) {
        Store store = getValidatedStore(member, storeId);
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU));
        if (!menu.getStore().getId().equals(store.getId())) {
            throw new BaseException(ErrorCode.NOT_SAME_STORE);
        }
        menuRepository.delete(menu);
        return new DeleteMenuResponseDto(menu.getId());
    }


    /**
     * 메뉴 수정 서비스 메소드
     *
     * @param member
     * @param storeId
     * @param menuId
     * @param requestDto
     * @return
     */
    @Transactional
    public UpdateMenuResponseDto updateMenu(Member member, MultipartFile menuImage, Long storeId, Long menuId, UpdateMenuRequestDto requestDto) {
        Store store = getValidatedStore(member, storeId);
        ;
        Menu updateMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU));
        checkValidMenuAndStore(updateMenu, store);
        Category updateMenuCategory = categoryRepository.findByCategoryName(requestDto.getMenuCategory())
                .orElseThrow(() -> new BaseException(("카테고리를 찾을 수 없습니다: " + requestDto.getMenuCategory()), ErrorCode.NOT_FOUND_ENUM_CONSTANT));
        updateMenu.update(requestDto.getMenuName(), requestDto.getMenuPrice()
                , updateMenuCategory, convert(menuImage), requestDto.getMenuDescription());
        return new UpdateMenuResponseDto(updateMenu.getId());
    }

    /**
     * 메뉴 옵션 추가 서비스 메소드
     *
     * @param member
     * @param storeId
     * @param menuId
     * @param requestDto
     * @return
     */
    @Transactional
    public AddMenuOptionResponseDto addOption(Member member, Long storeId, Long menuId, AddMenuOptionRequestDto requestDto) {
        Store store = getValidatedStore(member, storeId);
        Menu addOptionMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU));
        checkValidMenuAndStore(addOptionMenu, store);
        MenuOption addOption = new MenuOption(requestDto.getContent(), requestDto.getOptionPrice(), addOptionMenu);
        menuOptionRepository.save(addOption);
        return new AddMenuOptionResponseDto(addOption.getId());
    }

    /**
     * 메뉴 옵션 수정 서비스 메소드
     *
     * @param member       요청한 회원
     * @param storeId      상점 ID
     * @param menuId       메뉴 ID
     * @param menuOptionId 메뉴 옵션 ID
     * @param requestDto   수정 요청 데이터
     * @return 수정된 메뉴 옵션의 응답 DTO
     */
    @Transactional(readOnly = true)
    public UpdateMenuOptionResponseDto updateOption(Member member, Long storeId, Long menuId, Long menuOptionId, UpdateMenuOptionRequestDto requestDto) {
        Store store = getValidatedStore(member, storeId);
        Menu updateOptionMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU));
        checkValidMenuAndStore(updateOptionMenu, store);
        MenuOption updateOption = menuOptionRepository.findById(menuOptionId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU_OPTION));

        return updateOptionTransactional(requestDto, updateOption);
    }

    /**
     * 메뉴 옵션 삭제 서비스 메소드
     *
     * @param member
     * @param storeId
     * @param menuId
     * @param menuOptionId
     * @return
     */
    @Transactional
    public DeleteMenuOptionResponseDto deleteOption(Member member, Long storeId, Long menuId, Long menuOptionId) {
        Store store = getValidatedStore(member, storeId);
        Menu updateOptionMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU));
        checkValidMenuAndStore(updateOptionMenu, store);
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
        updateOption.update(requestDto.getContent(), requestDto.getOptionPrice());
        return new UpdateMenuOptionResponseDto(updateOption.getId());
    }

    @Transactional(readOnly = true)
    public FindMenuResponseDto findMenu(Long storeId, Long menuId) {
        Menu findMenu = menuRepository.findById(menuId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MENU));
        Store store = storeRepository.findByIdAndIsDeleted(storeId, Boolean.FALSE).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_STORE));
        checkValidMenuAndStore(findMenu, store);
        List<MenuOptionInfo> menuOptionInfoList = menuOptionRepository.findByMenu(findMenu).stream().map(
                menuOption -> new MenuOptionInfo(menuOption.getId(), menuOption.getContent(), menuOption.getOptionPrice())
        ).toList();
        return new FindMenuResponseDto(
                findMenu.getId(),
                findMenu.getMenuName(),
                findMenu.getCategory().getCategoryName(),
                findMenu.getMenuImage(),
                findMenu.getMenuDescription(),
                findMenu.getMenuPrice(),
                menuOptionInfoList
        );
    }

    /**
     * 유저 및 음식점 유효성 검증
     *
     * @param member
     * @param storeId
     */
    private Store getValidatedStore(Member member, Long storeId) {
        Store store = storeService.getStore(storeId);
        if (!store.getMember().equals(member)) {
            throw new BaseException(ErrorCode.NOT_SAME_MEMBER);
        }
        return store;
    }


    private void checkValidMenuAndStore(Menu menu, Store store) {
        // 가게 메뉴 검증
        if (!store.equals(menu.getStore())) {
            throw new BaseException(ErrorCode.NOT_SAME_STORE);
        }
    }

    private String convert(MultipartFile image) {
        String imageUrl = null;
        if (image != null) {
            UploadImageInfo uploadImageInfo = s3Service.uploadMenuImage(image);
            imageUrl = uploadImageInfo.ImageUrl();
        }
        return imageUrl;
    }

}
