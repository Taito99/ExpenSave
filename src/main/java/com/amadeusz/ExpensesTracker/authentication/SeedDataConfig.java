package com.amadeusz.ExpensesTracker.authentication;

import com.amadeusz.ExpensesTracker.category.Category;
import com.amadeusz.ExpensesTracker.category.CategoryRepository;
import com.amadeusz.ExpensesTracker.user.Role;
import com.amadeusz.ExpensesTracker.user.User;
import com.amadeusz.ExpensesTracker.user.UserDao;
import com.amadeusz.ExpensesTracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserDao userDao;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {
            User user = User
                    .builder()
                    .firstName("user")
                    .lastName("userowy")
                    .email("user@example.com")
                    .username("userr")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.USER)
                    .build();

            userDao.saveUser(user);
            log.info("Default user created");
        }

        if (categoryRepository.count() == 0) {
            Category foodAndDrinks = Category
                    .builder()
                    .name("Żywność i napoje")
                    .icon("🍗🥤")
                    .build();
            categoryRepository.save(foodAndDrinks);

            Category transport = Category
                    .builder()
                    .name("Transport")
                    .icon("🚗")
                    .build();
            categoryRepository.save(transport);

            Category bills = Category
                    .builder()
                    .name("Rachunki oraz opłaty")
                    .icon("💸")
                    .build();
            categoryRepository.save(bills);

            Category health = Category
                    .builder()
                    .name("Zdrowie")
                    .icon("💊")
                    .build();
            categoryRepository.save(health);

            Category hygieneAndCosmetics = Category
                    .builder()
                    .name("Higiena i kosmetyki")
                    .icon("🛁")
                    .build();
            categoryRepository.save(hygieneAndCosmetics);

            Category entertainment = Category
                    .builder()
                    .name("Rozrywka")
                    .icon("🎮")
                    .build();
            categoryRepository.save(entertainment);

            Category education = Category
                    .builder()
                    .name("Edukacja")
                    .icon("📚🎓")
                    .build();
            categoryRepository.save(education);

            Category shopping = Category
                    .builder()
                    .name("Odzież i obuwie")
                    .icon("👕👟")
                    .build();
            categoryRepository.save(shopping);

            Category finances = Category
                    .builder()
                    .name("Inwestycje")
                    .icon("📈")
                    .build();
            categoryRepository.save(finances);

            Category travels = Category
                    .builder()
                    .name("Podróże")
                    .icon("✈️")
                    .build();
            categoryRepository.save(travels);

            Category family = Category
                    .builder()
                    .name("Rodzina")
                    .icon("👨‍👩‍👧‍👦")
                    .build();
            categoryRepository.save(family);

            Category celebrations = Category
                    .builder()
                    .name("Uroczystości i wydarzenia")
                    .icon("🎉")
                    .build();
            categoryRepository.save(celebrations);

            Category charity = Category
                    .builder()
                    .name("Charytatywność i darowizny")
                    .icon("🤝️")
                    .build();
            categoryRepository.save(charity);

            Category subscriptions = Category
                    .builder()
                    .name("Subskrypcje")
                    .icon("📺")
                    .build();
            categoryRepository.save(subscriptions);

            Category unforeseen = Category
                    .builder()
                    .name("Nieprzewidziane wydatki")
                    .icon("⚠️")
                    .build();
            categoryRepository.save(unforeseen);

            Category others = Category
                    .builder()
                    .name("Inne")
                    .icon("❓")
                    .build();
            categoryRepository.save(others);

            log.info("Default categories created");
        }
    }
}
