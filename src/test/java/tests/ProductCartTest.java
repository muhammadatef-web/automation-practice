package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.CartPage;
import pages.ProductsPage;

@Epic("Catalog & Shopping Cart")
@Feature("Product Search & Cart")
public class ProductCartTest extends BaseTest {

    @Test(groups = {"regression"})
    @Story("Product Search")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that searching by keyword updates the catalog header and returns matching products.")
    public void verifyProductSearchFiltersResults() {
        ProductsPage productsPage = new ProductsPage()
                .openProductsPage()
                .searchFor("Tshirt");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(
                productsPage.getSectionHeader(),
                "SEARCHED PRODUCTS",
                "Catalog section header should switch to SEARCHED PRODUCTS"
        );
        softAssert.assertTrue(
                productsPage.getDisplayedProductCount() > 0,
                "Expected at least one matching product card for keyword 'Tshirt'"
        );
        softAssert.assertAll();
    }

    @Test(groups = {"smoke", "regression"})
    @Story("Add to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Search for a product, add it to the cart from the catalog, and verify item name and quantity in the cart.")
    public void verifyAddingProductToCartDisplaysItemInCart() {
        String targetProduct = "Blue Top";

        CartPage cartPage = new ProductsPage()
                .openProductsPage()
                .searchFor(targetProduct)
                .addFirstProductToCart()
                .goToCartFromModal();

        Assert.assertTrue(cartPage.isCartDisplayed(), "Shopping cart table should be visible");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(
                cartPage.hasProductMatching(targetProduct),
                "Cart should contain product matching: " + targetProduct
        );
        softAssert.assertEquals(
                cartPage.getFirstItemQuantity(),
                "1",
                "Initial cart item quantity should be 1"
        );
        softAssert.assertAll();
    }
}