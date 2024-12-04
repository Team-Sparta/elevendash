package com.example.elevendash.global.config;

import com.example.elevendash.domain.menu.entity.Category;
import com.example.elevendash.domain.menu.enums.Categories;
import com.example.elevendash.domain.menu.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer {
    private final CategoryRepository categoryRepository;

    /**
     * 어플리케이션이 실행할때 동작하는 메소드
     */
    @PostConstruct
    public void init(){
        // category 등록
        for(Categories categories : Categories.values()){
            categoryRepository.save(new Category(categories));
        }





    }


}