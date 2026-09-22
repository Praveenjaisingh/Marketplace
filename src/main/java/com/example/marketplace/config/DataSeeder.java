package com.example.marketplace.config;

import com.example.marketplace.entity.Category;
import com.example.marketplace.entity.Inventory;
import com.example.marketplace.entity.Product;
import com.example.marketplace.entity.Seller;
import com.example.marketplace.entity.User;
import com.example.marketplace.repository.CategoryRepository;
import com.example.marketplace.repository.InventoryRepository;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.SellerRepository;
import com.example.marketplace.repository.UserRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Populates the database with realistic sample data (categories, users,
 * sellers, products with real image URLs, and stock) the first time the app
 * starts against an empty database. Runs once — if any users already exist
 * it does nothing, so it is always safe to restart the app.
 *
 * Saves are done directly through the repositories (not the Services) so
 * seeding never fires the registration welcome email and always succeeds
 * regardless of mail configuration.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public DataSeeder(UserRepository userRepository, SellerRepository sellerRepository,
            CategoryRepository categoryRepository, ProductRepository productRepository,
            InventoryRepository inventoryRepository) {
        this.userRepository = userRepository;
        this.sellerRepository = sellerRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already has data — checking if Clothes section needs to be seeded.");
            seedClothesIfMissing();
            return;
        }
        log.info("Seeding sample marketplace data...");

        Map<String, Category> categories = seedCategories();
        Map<String, User> sellerUsers = seedSellerUsers();
        seedAdminAndCustomers();
        Map<String, Seller> sellers = seedSellers(sellerUsers);
        seedProductsAndInventory(categories, sellers);

        log.info("Sample data seeding complete: {} categories, {} users, {} sellers, {} products.",
                categoryRepository.count(), userRepository.count(), sellerRepository.count(), productRepository.count());
    }

    private Map<String, Category> seedCategories() {
        String[][] defs = {
                {"Clothes", "Handcrafted apparel, khadi shirts, ethnic wear and sustainable clothing"},
                {"Textiles", "Handloom fabrics, throws and table linen"},
                {"Ceramics", "Studio pottery, stoneware and glazed decor"},
                {"Spices", "Whole and ground spices sourced from small farms"},
                {"Lighting", "Hand-cast lamps and ambient lighting"},
                {"Leather Goods", "Vegetable-tanned bags, wallets and accessories"},
                {"Candles", "Soy and beeswax candles in small-batch scents"},
                {"Coffee & Tea", "Single-origin coffee and loose-leaf tea"},
                {"Woven Decor", "Rattan, jute and wall-hanging decor"},
        };
        Map<String, Category> saved = new LinkedHashMap<>();
        for (String[] d : defs) {
            Category c = new Category(d[0], d[1], true);
            saved.put(d[0], categoryRepository.save(c));
        }
        return saved;
    }

    private Map<String, User> seedSellerUsers() {
        String[][] defs = {
                {"amara_textiles", "owner@amaratextiles.com", "9810000001"},
                {"kiln_and_co", "hello@kilnandco.com", "9810000002"},
                {"ghat_spice_traders", "contact@ghatspice.com", "9810000003"},
                {"foundry_light_co", "sales@foundrylight.com", "9810000004"},
                {"norra_leather", "studio@norraleather.com", "9810000005"},
                {"ember_and_ash", "hi@emberandash.com", "9810000006"},
                {"highland_brews", "team@highlandbrews.com", "9810000007"},
                {"studio_meadow", "shop@studiomeadow.com", "9810000008"},
        };
        Map<String, User> saved = new LinkedHashMap<>();
        for (String[] d : defs) {
            User u = new User(d[0], d[1], "Seller@123", "SELLER", d[2], true);
            saved.put(d[0], userRepository.save(u));
        }
        return saved;
    }

    private void seedAdminAndCustomers() {
        userRepository.save(new User("admin", "admin@bazaari.com", "Admin@123", "ADMIN", "9800000000", true));
        String[][] customers = {
                {"r_kapoor", "r.kapoor@example.com", "9820000001"},
                {"s_iyer", "s.iyer@example.com", "9820000002"},
                {"m_fernandes", "m.fernandes@example.com", "9820000003"},
        };
        for (String[] d : customers) {
            userRepository.save(new User(d[0], d[1], "Customer@123", "CUSTOMER", d[2], true));
        }
    }

    private Map<String, Seller> seedSellers(Map<String, User> sellerUsers) {
        String[][] defs = {
                {"amara_textiles", "Amara Textiles", "27AACCA1234B1Z5", "Handloom weavers working with 40+ artisan families."},
                {"kiln_and_co", "Kiln & Co.", "27AACCK5678B1Z2", "Small-batch stoneware thrown and glazed in-house."},
                {"ghat_spice_traders", "Ghat Spice Traders", "27AACCG9012B1Z8", "Whole spices sourced directly from Western Ghats farms."},
                {"foundry_light_co", "Foundry Light Co.", "27AACCF3456B1Z1", "Hand-cast brass and iron lighting fixtures."},
                {"norra_leather", "Norra Leather", "27AACCN7890B1Z4", "Vegetable-tanned leather goods, made to last."},
                {"ember_and_ash", "Ember & Ash", "27AACCE2345B1Z7", "Soy wax candles hand-poured in small batches."},
                {"highland_brews", "Highland Brews", "27AACCH6789B1Z3", "Estate teas and single-origin coffee."},
                {"studio_meadow", "Studio Meadow", "27AACCS0123B1Z6", "Woven wall decor and natural-fibre homeware."},
        };
        Map<String, Seller> saved = new LinkedHashMap<>();
        for (String[] d : defs) {
            User owner = sellerUsers.get(d[0]);
            Seller s = new Seller(owner.getId(), d[1], d[2], d[3], true, true);
            saved.put(d[1], sellerRepository.save(s));
        }
        return saved;
    }

    private void seedProductsAndInventory(Map<String, Category> categories, Map<String, Seller> sellers) {
        // {sellerName, categoryName, productName, description, price, brand, imageUrl, stock}
        Object[][] defs = {
                // Amara Textiles
                {"Amara Textiles", "Textiles", "Handwoven Block-Print Throw",
                        "Cotton throw, hand block-printed with natural dyes.", 2450.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1600369671236-e74521d4b6ad?w=800&q=80", 14},
                {"Amara Textiles", "Woven Decor", "Handloom Cotton Table Runner",
                        "Handloom-woven table runner in indigo and cream.", 980.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800&q=80", 17},
                {"Amara Textiles", "Textiles", "Ikat Cotton Cushion Cover Set",
                        "Set of two hand-dyed ikat cushion covers, 45x45cm.", 890.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1584100936595-c0654b55a2e6?w=800&q=80", 21},
                {"Amara Textiles", "Textiles", "Handloom Linen Bedcover",
                        "Pure linen bedcover, hand-loomed in soft earth tones.", 3150.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1522771930-78848d9293e8?w=800&q=80", 9},
                {"Amara Textiles", "Textiles", "Block-Print Cotton Napkin Set",
                        "Set of six hand block-printed cotton dinner napkins.", 640.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1600166898405-da9535204843?w=800&q=80", 33},
                {"Amara Textiles", "Woven Decor", "Handwoven Cotton Dhurrie Rug",
                        "Flat-woven cotton dhurrie, striped indigo and natural.", 4200.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1600166898405-e7f3ceea7a24?w=800&q=80", 5},

                // Kiln & Co.
                {"Kiln & Co.", "Ceramics", "Stoneware Pour-Over Set",
                        "Hand-thrown stoneware dripper with matching cup.", 1890.0, "Kiln & Co.",
                        "https://images.unsplash.com/photo-1517256064527-09c73fc73e38?w=800&q=80", 6},
                {"Kiln & Co.", "Ceramics", "Glazed Ceramic Planter Trio",
                        "Set of three reactive-glaze planters, food-safe finish.", 1450.0, "Kiln & Co.",
                        "https://images.unsplash.com/photo-1485955900006-10f4d324d411?w=800&q=80", 0},
                {"Kiln & Co.", "Ceramics", "Hand-Thrown Dinner Plate Set",
                        "Set of four stoneware dinner plates, matte glaze.", 2680.0, "Kiln & Co.",
                        "https://images.unsplash.com/photo-1603199506016-b9a594b593c0?w=800&q=80", 11},
                {"Kiln & Co.", "Ceramics", "Speckled Stoneware Mug",
                        "Single hand-thrown mug with a speckled oatmeal glaze.", 480.0, "Kiln & Co.",
                        "https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?w=800&q=80", 40},
                {"Kiln & Co.", "Ceramics", "Ceramic Serving Bowl, Large",
                        "Wide stoneware serving bowl, hand-glazed in slate blue.", 1620.0, "Kiln & Co.",
                        "https://images.unsplash.com/photo-1591129841117-3adfd313e34f?w=800&q=80", 8},
                {"Kiln & Co.", "Ceramics", "Ceramic Oil Burner",
                        "Hand-thrown ceramic oil burner with matte finish.", 720.0, "Kiln & Co.",
                        "https://images.unsplash.com/photo-1578500494198-246f612d3b3d?w=800&q=80", 15},

                // Ghat Spice Traders
                {"Ghat Spice Traders", "Spices", "Single-Origin Malabar Peppercorns",
                        "Sun-dried black peppercorns from the Malabar coast, 100g.", 340.0, "Ghat Spice Traders",
                        "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=800&q=80", 52},
                {"Ghat Spice Traders", "Spices", "Whole Green Cardamom, 100g",
                        "Aromatic whole green cardamom pods, hand-sorted.", 280.0, "Ghat Spice Traders",
                        "https://images.unsplash.com/photo-1599909533144-cd2c04d68d63?w=800&q=80", 44},
                {"Ghat Spice Traders", "Spices", "Ceylon Cinnamon Sticks, 150g",
                        "True Ceylon cinnamon quills, sourced from small estates.", 310.0, "Ghat Spice Traders",
                        "https://images.unsplash.com/photo-1587132137056-bfbf0166836e?w=800&q=80", 38},
                {"Ghat Spice Traders", "Spices", "Turmeric Root Powder, 200g",
                        "Stone-ground turmeric powder, high curcumin content.", 220.0, "Ghat Spice Traders",
                        "https://images.unsplash.com/photo-1615485500704-8e990f9900f7?w=800&q=80", 60},
                {"Ghat Spice Traders", "Spices", "Kashmiri Red Chilli Powder, 200g",
                        "Mild, vibrant Kashmiri chilli powder for colour and flavour.", 260.0, "Ghat Spice Traders",
                        "https://images.unsplash.com/photo-1583119022894-919a68a3d0e3?w=800&q=80", 47},
                {"Ghat Spice Traders", "Spices", "Garam Masala Blend, 100g",
                        "House-roasted garam masala, ground in small batches.", 295.0, "Ghat Spice Traders",
                        "https://images.unsplash.com/photo-1532336414038-cf19250c5757?w=800&q=80", 29},

                // Foundry Light Co.
                {"Foundry Light Co.", "Lighting", "Brass Table Lamp, Hand-Cast",
                        "Solid brass table lamp with a hand-cast base.", 3200.0, "Foundry Light Co.",
                        "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=800&q=80", 3},
                {"Foundry Light Co.", "Lighting", "Iron Pendant Light",
                        "Hand-forged iron pendant light with an amber glass shade.", 4650.0, "Foundry Light Co.",
                        "https://images.unsplash.com/photo-1524634126442-357e0eac3c14?w=800&q=80", 4},
                {"Foundry Light Co.", "Lighting", "Cast Brass Wall Sconce",
                        "Pair of hand-cast brass wall sconces with warm LED fitting.", 2980.0, "Foundry Light Co.",
                        "https://images.unsplash.com/photo-1513506003901-1e6a229e2d15?w=800&q=80", 7},
                {"Foundry Light Co.", "Lighting", "Rattan-Shade Floor Lamp",
                        "Iron-framed floor lamp with a hand-woven rattan shade.", 5400.0, "Foundry Light Co.",
                        "https://images.unsplash.com/photo-1543198126-cbaeda1e5c00?w=800&q=80", 2},
                {"Foundry Light Co.", "Lighting", "Brass Desk Lamp, Articulated",
                        "Adjustable brass desk lamp with a hand-cast joint.", 2450.0, "Foundry Light Co.",
                        "https://images.unsplash.com/photo-1544816155-12df9643f363?w=800&q=80", 6},

                // Norra Leather
                {"Norra Leather", "Leather Goods", "Vegetable-Tanned Tote",
                        "Full-grain leather tote, vegetable-tanned and hand-stitched.", 2890.0, "Norra Leather",
                        "https://images.unsplash.com/photo-1591561954557-26941169b49e?w=800&q=80", 9},
                {"Norra Leather", "Leather Goods", "Leather Card Wallet",
                        "Slim bifold card wallet in tan vegetable-tanned leather.", 990.0, "Norra Leather",
                        "https://images.unsplash.com/photo-1627123424574-724758594e93?w=800&q=80", 26},
                {"Norra Leather", "Leather Goods", "Leather Messenger Bag",
                        "Full-grain leather messenger bag with brass hardware.", 4250.0, "Norra Leather",
                        "https://images.unsplash.com/photo-1590874103328-eac38a683ce7?w=800&q=80", 6},
                {"Norra Leather", "Leather Goods", "Handstitched Leather Belt",
                        "Vegetable-tanned leather belt with a solid brass buckle.", 1180.0, "Norra Leather",
                        "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=800&q=80", 18},
                {"Norra Leather", "Leather Goods", "Leather Passport Holder",
                        "Compact hand-stitched leather passport cover.", 720.0, "Norra Leather",
                        "https://images.unsplash.com/photo-1622560480605-d83c853bc5c3?w=800&q=80", 24},
                {"Norra Leather", "Leather Goods", "Leather Laptop Sleeve",
                        "Padded vegetable-tanned leather sleeve, fits 14-inch laptops.", 2150.0, "Norra Leather",
                        "https://images.unsplash.com/photo-1585155770447-2f66e2a397b5?w=800&q=80", 13},

                // Ember & Ash
                {"Ember & Ash", "Candles", "Soy Wax Sandalwood Candle",
                        "Hand-poured soy candle, sandalwood and amber scent.", 520.0, "Ember & Ash",
                        "https://images.unsplash.com/photo-1602874801007-bd458bb1b8b6?w=800&q=80", 31},
                {"Ember & Ash", "Candles", "Beeswax Pillar Candle Set",
                        "Set of three unscented beeswax pillar candles.", 640.0, "Ember & Ash",
                        "https://images.unsplash.com/photo-1602523961358-f9f03dd557db?w=800&q=80", 27},
                {"Ember & Ash", "Candles", "Soy Wax Fig & Cedar Candle",
                        "Hand-poured soy candle in a reusable ceramic vessel.", 590.0, "Ember & Ash",
                        "https://images.unsplash.com/photo-1608181831718-c9ffd8829358?w=800&q=80", 22},
                {"Ember & Ash", "Candles", "Taper Candle Pair, Beeswax",
                        "Hand-dipped beeswax taper candles, natural honey scent.", 380.0, "Ember & Ash",
                        "https://images.unsplash.com/photo-1633934542430-0926a52aaf1a?w=800&q=80", 35},
                {"Ember & Ash", "Candles", "Soy Wax Travel Tin Candle",
                        "Small-batch soy candle in a portable travel tin.", 340.0, "Ember & Ash",
                        "https://images.unsplash.com/photo-1636374861478-c8b7a1c72f92?w=800&q=80", 41},

                // Highland Brews
                {"Highland Brews", "Coffee & Tea", "Estate Assam Loose-Leaf Tea",
                        "Single-estate Assam black tea, malty and full-bodied, 250g.", 610.0, "Highland Brews",
                        "https://images.unsplash.com/photo-1544787219-7f47ccb76574?w=800&q=80", 22},
                {"Highland Brews", "Coffee & Tea", "Single-Origin Coorg Coffee Beans",
                        "Medium-roast Coorg arabica beans, 250g.", 690.0, "Highland Brews",
                        "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=800&q=80", 30},
                {"Highland Brews", "Coffee & Tea", "Darjeeling First Flush Tea",
                        "Delicate first-flush Darjeeling black tea, 100g.", 780.0, "Highland Brews",
                        "https://images.unsplash.com/photo-1597318181409-cf64d0b5d8a2?w=800&q=80", 19},
                {"Highland Brews", "Coffee & Tea", "Nilgiri Green Tea",
                        "Bright, grassy Nilgiri green tea, hand-plucked, 200g.", 470.0, "Highland Brews",
                        "https://images.unsplash.com/photo-1627435601361-ec25f5b1d0e5?w=800&q=80", 26},
                {"Highland Brews", "Coffee & Tea", "Dark Roast Filter Coffee Powder",
                        "Traditional South Indian filter coffee blend, 500g.", 520.0, "Highland Brews",
                        "https://images.unsplash.com/photo-1509785307050-d4066910ec1e?w=800&q=80", 34},

                // Studio Meadow
                {"Studio Meadow", "Woven Decor", "Woven Rattan Wall Hanging",
                        "Handwoven natural rattan wall hanging, 60cm.", 1720.0, "Studio Meadow",
                        "https://images.unsplash.com/photo-1567016432779-094069958ea5?w=800&q=80", 8},
                {"Studio Meadow", "Woven Decor", "Jute Storage Basket Set",
                        "Set of two hand-braided jute storage baskets.", 1290.0, "Studio Meadow",
                        "https://images.unsplash.com/photo-1622650796454-3d0b6d6a1a1a?w=800&q=80", 16},
                {"Studio Meadow", "Woven Decor", "Rattan Pendant Lampshade",
                        "Hand-woven open-weave rattan lampshade, 35cm.", 1560.0, "Studio Meadow",
                        "https://images.unsplash.com/photo-1615874959474-d609969a20ed?w=800&q=80", 10},
                {"Studio Meadow", "Woven Decor", "Woven Seagrass Placemats",
                        "Set of four hand-woven seagrass placemats.", 780.0, "Studio Meadow",
                        "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800&q=80", 25},
                {"Studio Meadow", "Woven Decor", "Macrame Wall Hanging",
                        "Hand-knotted cotton macrame wall hanging, 90cm.", 1980.0, "Studio Meadow",
                        "https://images.unsplash.com/photo-1615529162924-f8605388461d?w=800&q=80", 7},

                // Clothes (Amara Textiles)
                {"Amara Textiles", "Clothes", "Handloom Khadi Cotton Shirt",
                        "Pure handspun khadi cotton shirt in natural ivory with mother-of-pearl buttons.", 1450.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=800&q=80", 24},
                {"Amara Textiles", "Clothes", "Indigo Block-Printed Quilted Jacket",
                        "Reversible quilted cotton jacket hand-block printed using authentic natural indigo dyes.", 2850.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1544441893-675973e31985?w=800&q=80", 12},
                {"Amara Textiles", "Clothes", "Organic Linen Relaxed Kurta",
                        "Breathable lightweight organic linen kurta with subtle mandarin collar and welt pockets.", 1890.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1618354691373-d851c5c3a990?w=800&q=80", 18},
                {"Amara Textiles", "Clothes", "Pure Chanderi Silk Sari with Zari",
                        "Exquisite handwoven Chanderi silk saree with delicate gold zari border and matching blouse piece.", 4990.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=800&q=80", 8},
                {"Amara Textiles", "Clothes", "Hand-Dyed Bandhani Cotton Dupatta",
                        "Artisan tie-dyed fine cotton dupatta with traditional Gujarati bandhej patterns.", 980.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1609357605129-26f69add5d6e?w=800&q=80", 30},
                {"Amara Textiles", "Clothes", "Ajrakh Print Kaftan Dress",
                        "Flowing relaxed-fit kaftan dress patterned with authentic 16-stage Sindhi Ajrakh block prints.", 1650.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=800&q=80", 15},
        };

        for (Object[] d : defs) {
            Seller seller = sellers.get((String) d[0]);
            Category category = categories.get((String) d[1]);
            Product product = new Product(
                    seller.getId(),
                    category.getId(),
                    (String) d[2],
                    (String) d[3],
                    (double) d[4],
                    (String) d[5],
                    (String) d[6],
                    true);
            Product savedProduct = productRepository.save(product);

            int stock = (int) d[7];
            Inventory inventory = new Inventory(savedProduct.getId(), stock, 0, "Main Warehouse");
            inventoryRepository.save(inventory);
        }
    }

    private void seedClothesIfMissing() {
        if (categoryRepository.existsByCategoryName("Clothes")) {
            log.info("Clothes category already exists — no additional seeding required.");
            return;
        }
        log.info("Seeding Clothes category and sample apparel products...");
        Category clothesCat = categoryRepository.save(
                new Category("Clothes", "Handcrafted apparel, khadi shirts, ethnic wear and sustainable clothing", true));
        Seller seller = sellerRepository.findAll().stream().findFirst().orElse(null);
        if (seller == null) {
            log.warn("No seller found to associate with Clothes items.");
            return;
        }

        Object[][] apparel = {
                {"Handloom Khadi Cotton Shirt",
                        "Pure handspun khadi cotton shirt in natural ivory with mother-of-pearl buttons.", 1450.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=800&q=80", 24},
                {"Indigo Block-Printed Quilted Jacket",
                        "Reversible quilted cotton jacket hand-block printed using authentic natural indigo dyes.", 2850.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1544441893-675973e31985?w=800&q=80", 12},
                {"Organic Linen Relaxed Kurta",
                        "Breathable lightweight organic linen kurta with subtle mandarin collar and welt pockets.", 1890.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1618354691373-d851c5c3a990?w=800&q=80", 18},
                {"Pure Chanderi Silk Sari with Zari",
                        "Exquisite handwoven Chanderi silk saree with delicate gold zari border and matching blouse piece.", 4990.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=800&q=80", 8},
                {"Hand-Dyed Bandhani Cotton Dupatta",
                        "Artisan tie-dyed fine cotton dupatta with traditional Gujarati bandhej patterns.", 980.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1609357605129-26f69add5d6e?w=800&q=80", 30},
                {"Ajrakh Print Kaftan Dress",
                        "Flowing relaxed-fit kaftan dress patterned with authentic 16-stage Sindhi Ajrakh block prints.", 1650.0, "Amara Textiles",
                        "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=800&q=80", 15}
        };

        for (Object[] d : apparel) {
            Product product = new Product(
                    seller.getId(),
                    clothesCat.getId(),
                    (String) d[0],
                    (String) d[1],
                    (double) d[2],
                    (String) d[3],
                    (String) d[4],
                    true);
            Product savedProduct = productRepository.save(product);
            int stock = (int) d[5];
            inventoryRepository.save(new Inventory(savedProduct.getId(), stock, 0, "Main Warehouse"));
        }
        log.info("Clothes category and {} sample apparel items seeded successfully.", apparel.length);
    }
}