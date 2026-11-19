package pages;

import core.ui.ElementActions;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CartPage {

    private final ElementActions ui = new ElementActions();

    private final By cartTable = By.id("cart_info_table");
    private final By cartProductTitles = By.cssSelector("#cart_info_table .cart_description h4 a");
    private final By firstItemQuantity = By.cssSelector("#cart_info_table tbody tr:first-child .cart_quantity button");

    @Step("Verify shopping cart table is visible")
    public boolean isCartDisplayed() {
        return ui.isDisplayed(cartTable);
    }

    @Step("Check if cart contains product matching: {expectedKeyword}")
    public boolean hasProductMatching(String expectedKeyword) {
        return ui.getAllTexts(cartProductTitles).stream()
                .anyMatch(title -> title.toLowerCase().contains(expectedKeyword.toLowerCase()));
    }

    @Step("Read quantity of first item in cart")
    public String getFirstItemQuantity() {
        return ui.getText(firstItemQuantity);
    }
}
