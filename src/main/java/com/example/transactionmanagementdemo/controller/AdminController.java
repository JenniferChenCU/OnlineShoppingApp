package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.domain.Orders.Orders;
import com.example.transactionmanagementdemo.domain.Product.Product;
import com.example.transactionmanagementdemo.domain.Product.ProductRequest;
import com.example.transactionmanagementdemo.domain.Product.ProductResponse;
import com.example.transactionmanagementdemo.domain.User.User;
import com.example.transactionmanagementdemo.service.OrdersService;
import com.example.transactionmanagementdemo.service.ProductService;
import com.example.transactionmanagementdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("admin")
public class AdminController {
    private final UserService userService;
    private final ProductService productService;
    private final OrdersService ordersService;

    @Autowired
    public AdminController(UserService userService, ProductService productService, OrdersService ordersService) {
        this.userService = userService;
        this.productService = productService;
        this.ordersService = ordersService;
    }

    @GetMapping("/dashboard/{userId}")
    public Hashtable<String, List<Object>> getDashboard(@PathVariable int userId){
        Hashtable<String, List<Object>> dashboard = new Hashtable<>();
        dashboard.put("order", new ArrayList<>());
        dashboard.put("product", new ArrayList<>());
        User user = userService.getUserById(userId);
        if (!user.isSeller()) {
            return dashboard;
        }

        List<Orders> ordersList = ordersService.getAllOrdersSuccess();
        List<Product> productList = productService.getAllProductsSuccess();
        for (Orders o : ordersList){
            dashboard.get("order").add(o);
        }
        for (Product p : productList){
            dashboard.get("product").add(p);
        }
        return dashboard;
    }

    @PostMapping("/dashboard/{userId}/edit")
    public ProductResponse editProduct(@PathVariable int userId,
                                       @RequestBody ProductRequest productRequest){
        User user = userService.getUserById(userId);
        if (!user.isSeller()){
            return ProductResponse.builder().message("No permission").build();
        }

        int productId = productRequest.getId();
        Product product = productService.getProductById(productId);
        float wholesalePrice = productRequest.getWholesalePrice();
        float retailPrice = productRequest.getRetailPrice();
        String description = productRequest.getDescription();
        Integer stockQuantity = productRequest.getStockQuantity();

        if (wholesalePrice!=0) product.setWholesalePrice(wholesalePrice);
        if (retailPrice!=0) product.setRetailPrice(retailPrice);
        if (description!=null) product.setDescription(description);
        if (stockQuantity!=null) product.setStockQuantity(stockQuantity);

        productService.updateProduct(product);
        return ProductResponse.builder().message("Product got updated!").product(product).build();
    }
}
