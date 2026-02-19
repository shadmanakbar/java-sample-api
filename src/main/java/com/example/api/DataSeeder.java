package com.example.api;

import com.example.api.model.Product;
import com.example.api.model.User;
import com.example.api.repository.ProductRepository;
import com.example.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository products;
    private final UserRepository users;

    @Override
    public void run(String... args) {
        if (products.count() == 0) {
            products.save(Product.builder().name("Laptop Pro 16").description("High-performance laptop")
                    .price(new BigDecimal("1299.99")).stock(50).category("electronics").build());
            products.save(
                    Product.builder().name("Wireless Headphones").description("Noise-cancelling over-ear headphones")
                            .price(new BigDecimal("249.99")).stock(120).category("electronics").build());
            products.save(Product.builder().name("Standing Desk").description("Electric height-adjustable desk")
                    .price(new BigDecimal("599.00")).stock(30).category("furniture").build());
            products.save(Product.builder().name("Mechanical Keyboard").description("TKL layout, Cherry MX switches")
                    .price(new BigDecimal("129.99")).stock(200).category("electronics").build());
            products.save(Product.builder().name("4K Monitor 27\"").description("IPS panel, 144Hz, HDR400")
                    .price(new BigDecimal("429.99")).stock(75).category("electronics").build());
            products.save(Product.builder().name("Ergonomic Chair").description("Lumbar support, mesh back")
                    .price(new BigDecimal("349.00")).stock(40).category("furniture").build());
            products.save(Product.builder().name("USB-C Hub 10-in-1").description("HDMI, USB-A, SD card, Ethernet")
                    .price(new BigDecimal("49.99")).stock(300).category("accessories").build());
            products.save(Product.builder().name("Webcam 4K").description("Auto-focus, built-in ring light")
                    .price(new BigDecimal("89.99")).stock(150).category("electronics").build());
            log.info("✅ Seeded {} products", products.count());
        }

        if (users.count() == 0) {
            users.save(User.builder().name("Alice Johnson").email("alice@example.com").phone("+1-555-0101")
                    .role("admin").build());
            users.save(User.builder().name("Bob Smith").email("bob@example.com").phone("+1-555-0102").role("user")
                    .build());
            users.save(User.builder().name("Carol White").email("carol@example.com").phone("+1-555-0103").role("user")
                    .build());
            users.save(User.builder().name("David Brown").email("david@example.com").phone("+1-555-0104")
                    .role("manager").build());
            users.save(User.builder().name("Eve Davis").email("eve@example.com").phone("+1-555-0105").role("user")
                    .build());
            log.info("✅ Seeded {} users", users.count());
        }
    }
}
