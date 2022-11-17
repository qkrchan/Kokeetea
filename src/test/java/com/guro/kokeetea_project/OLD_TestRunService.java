package com.guro.kokeetea_project;

import java.time.LocalDateTime;
import java.util.List;

import com.guro.kokeetea_project.entity.*;
import com.guro.kokeetea_project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.guro.kokeetea_project.constant.RequestStatus;
import com.guro.kokeetea_project.constant.Role;
import com.guro.kokeetea_project.service.MemberService;

@Service
@Profile("test")
public class OLD_TestRunService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    CurrentStockRepository currentStockRepository;
    @Autowired
    RequestRepository requestRepository;
    
    public void init() {
        memberRepository.deleteAll();
        currentStockRepository.deleteAll();
        requestRepository.deleteAll();
        storeRepository.deleteAll();
        warehouseRepository.deleteAll();
        ingredientRepository.deleteAll();
        categoryRepository.deleteAll();
        
        Member admin = new Member();
        admin.setEmail("kokeetea@gmail.com");
        admin.setPassword(passwordEncoder.encode("12345678"));
        admin.setRole(Role.ADMIN);
        memberRepository.save(admin);

        Member user1 = new Member();
        user1.setEmail("honey@gmail.com");
        user1.setPassword(passwordEncoder.encode("12345678"));
        user1.setRole(Role.USER);
        memberRepository.save(user1);

        for (int i=0; i<3; i++) {
            Category category = new Category();
            category.setName(List.of("주재료", "부재료", "과일").get(i));
            category.setIsValid(true);
            categoryRepository.save(category);
        }

        for (int i=0; i<10; i++) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(List.of("커피 원두", "홍차 잎", "녹차 잎",
                            "우유", "초콜릿", "시나몬", "꿀",
                            "오렌지", "포도", "바나나").get(i));
            ingredient.setPrice(1200L + (long) Math.floor(Math.random()*500));
            for (Category category : categoryRepository.listCategory()) {
                if (category.getName()==(i<3?"주재료":(i<7?"부재료":"과일"))) {
                    ingredient.setCategory(category);
                    break;
                }
            }
            ingredient.setIsValid(true);
            ingredientRepository.save(ingredient);
        }

        for (int i=0; i<3; i++) {
            Warehouse warehouse = new Warehouse();
            warehouse.setName(List.of("보스턴1창고", "뉴욕1창고", "시카고1창고").get(i));
            warehouse.setLocation(List.of("보스턴", "뉴욕", "시카고").get(i));
            warehouse.setEmail(List.of("boss", "choo", "polk").get(i)
                            +String.valueOf((long) Math.floor(500+Math.random()*500))+"@gmail.com");
            warehouse.setPhone("010"+String.valueOf((long) Math.floor(1000+Math.random()*9000))+String.valueOf(3200+i*9));
            warehouse.setIsValid(true);
            warehouseRepository.save(warehouse);

            for (Ingredient ingredient : ingredientRepository.listIngredient()) {
                CurrentStock currentStock = new CurrentStock();
                currentStock.setIngredient(ingredient);
                currentStock.setWarehouse(warehouse);
                currentStock.setAmount((long) Math.floor(100+Math.random()*900));
                currentStockRepository.save(currentStock);
            }
        }

        for (int i=0; i<10; i++) {
            Store store = new Store();
            store.setName(List.of("메인 1호점", "메인 2호점", "플라자 1호점",
                            "싱클레어 1호점", "싱클레어 2호점", "브루클린 1호점", "브루클린 2호점",
                            "레베카 1호점", "업타운 1호점", "업타운 2호점").get(i));
            store.setLocation(i<3?"보스턴":(i<7?"뉴욕":"시카고"));
            store.setEmail(List.of("kray", "peon", "cem", "walru", "java",
                            "tutu", "muni", "ban", "vox", "chiwa").get(i)
                            +String.valueOf((long) Math.floor(500+Math.random()*500))+"@gmail.com");
            store.setPhone("010"+String.valueOf((long) Math.floor(1000+Math.random()*9000))+String.valueOf(1500+i*7));
            for (Warehouse warehouse : warehouseRepository.listWarehouse()) {
                if (warehouse.getLocation()==(i<3?"보스턴":(i<7?"뉴욕":"시카고"))) {
                    store.setWarehouse(warehouse);
                    break;
                }
            }
            store.setIsValid(true);
            storeRepository.save(store);
        }

        for (int i=0; i<10; i++) {
            Member user = new Member();
            user.setEmail(storeRepository.listStore().get(i).getEmail());
            user.setPassword(passwordEncoder.encode("12345678"));
            user.setRole(Role.USER);
            memberRepository.save(user);
        }

        for (int i=0; i<400; i++) {
            Request request = new Request();
            request.setDate(LocalDateTime.now().minusMonths((long) Math.floor(0+Math.random()*18)));
            request.setIngredient(ingredientRepository.listIngredient().get((int) Math.floor(Math.random()*ingredientRepository.countIngredient())));
            request.setAmount((long) Math.floor(1+Math.random()*12));
            request.setStore(storeRepository.listStore().get((int) Math.floor(Math.random()*storeRepository.countStore())));
            request.setWarehouse(request.getStore().getWarehouse());
            request.setStatus(RequestStatus.PENDING);
            requestRepository.save(request);
        }
    }
}
