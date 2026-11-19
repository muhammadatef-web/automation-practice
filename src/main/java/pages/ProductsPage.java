package pages;

import core.ui.ElementActions;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class ProductsPage {

    private final ElementActions ui = new ElementActions();

    // Navigation & Search Locators
    private final By productsNavLink = By.cssSelector("a[href='/products']");
    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By sectionHeader = By.cssSelector("h2.title.text-center");

    // Product Grid & Added-to-Cart Modal Locators
    private final By productCards = By.cssSelector(".features_items .product-image-wrapper");
    private final By firstAddToCartButton = By.cssSelector(".features_items .productinfo a.add-to-cart");
    private final By modalViewCartLink = By.cssSelector("#cartModal a[href='/view_cart']");

    @Step("Open Products catalog from top navigation bar")
    public ProductsPage openProductsPage() {
        ui.click(productsNavLink);
        return this;
    }

    @Step("Search for product keyword: {keyword}")
    public ProductsPage searchFor(String keyword) {
        ui.type(searchInput, keyword);
        ui.click(searchButton);
        return this;
    }

    @Step("Read catalog section header")
    public String getSectionHeader() {
        return ui.getText(sectionHeader);
    }

    @Step("Count matching product cards displayed")
    public int getDisplayedProductCount() {
        return ui.getCount(productCards);
    }

    @Step("Add first matching product to cart")
    public ProductsPage addFirstProductToCart() {
        ui.scrollAndClick(firstAddToCartButton);
        return this;
    }

    @Step("Click 'View Cart' inside the confirmation modal")
    public CartPage goToCartFromModal() {
        ui.click(modalViewCartLink);
        return new CartPage();
    }
}