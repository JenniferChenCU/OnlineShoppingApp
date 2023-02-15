package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.domain.dashboard.Dashboard;
import com.example.transactionmanagementdemo.domain.dashboard.DashboardResponse;
import com.example.transactionmanagementdemo.domain.orders.Orders;
import com.example.transactionmanagementdemo.domain.product.Product;
import com.example.transactionmanagementdemo.domain.product.ProductRequest;
import com.example.transactionmanagementdemo.domain.product.ProductResponse;
import com.example.transactionmanagementdemo.domain.product.AllProductsResponse;
import com.example.transactionmanagementdemo.domain.user.AllUsersResponse;
import com.example.transactionmanagementdemo.domain.user.User;
import com.example.transactionmanagementdemo.domain.user.UserRequest;
import com.example.transactionmanagementdemo.domain.user.UserResponse;
import com.example.transactionmanagementdemo.exception.UserSaveFailedException;
import com.example.transactionmanagementdemo.service.OrdersService;
import com.example.transactionmanagementdemo.service.ProductService;
import com.example.transactionmanagementdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
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

    @GetMapping("/admin/allUsers")
    public List<User> getAllUsersSuccess(){
        return userService.getAllUsersSuccess();
    }

    @GetMapping("/admin/getUser/{id}")
    public UserResponse getUserById(@PathVariable int id){
        User user = userService.getUserById(id);
        return UserResponse.builder()
                .message("Returning user with id: " + id)
                .user(user)
                .build();
    }

    @PutMapping("/admin/saveUserSuccess")
    public UserResponse saveUserSuccess(@RequestBody User user){
        userService.saveUserSuccess(user);
        return UserResponse.builder()
                .message("User saved, committing...")
                .user(user)
                .build();
    }

    @PutMapping("/admin/failed")
    public ResponseEntity saveUserFailed(@RequestBody User user) throws UserSaveFailedException {
        userService.saveUserFailed(user);
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/deleteUser/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id){
        userService.deleteUserById(id);
        return new ResponseEntity<>("User deleted.", HttpStatus.OK);
    }

    @GetMapping("/admin/dashboard/{userId}")
    public DashboardResponse getDashboard(@PathVariable int userId){
        User user = userService.getUserById(userId);
        if (!user.isSeller()) return DashboardResponse.builder().message("No permission!").build();

        List<Orders> ordersList = ordersService.getAllOrdersSuccess();
        List<Product> productList = productService.getAllProductsSuccess();
        ordersList.addAll(ordersList);
        productList.addAll(productList);

        Dashboard dashboard = new Dashboard();
        dashboard.setOrders(ordersList);
        dashboard.setProducts(productList);
        return DashboardResponse.builder()
                .dashboard(dashboard)
                .message("All orders and products information retrieved!")
                .build();
    }

    @PostMapping("/admin/dashboard/{userId}/edit")
    public ProductResponse editProduct(@PathVariable int userId,
                                       @RequestBody ProductRequest productRequest){
        User user = userService.getUserById(userId);
        if (!user.isSeller()) return ProductResponse.builder().message("No permission").build();

        int productId = productRequest.getId();
        Product product = productService.getProductById(productId);
        if (product==null) return ProductResponse.builder().message("Product " + product + " does not exist!").build();

        String description = productRequest.getDescription();
        float wholesalePrice = productRequest.getWholesalePrice();
        float retailPrice = productRequest.getRetailPrice();
        Integer stockQuantity = productRequest.getStockQuantity();

        if (description!=null) product.setDescription(description);
        if (wholesalePrice!=0) product.setWholesalePrice(wholesalePrice);
        if (retailPrice!=0) product.setRetailPrice(retailPrice);
        if (stockQuantity!=null) product.setStockQuantity(stockQuantity);

        productService.updateProduct(product);
        return ProductResponse.builder().message("Product got updated!").product(product).build();
    }

    @PostMapping("/admin/addProduct/{userId}")
    public ProductResponse addProduct(@PathVariable int userId,
                                      @RequestBody ProductRequest productRequest){
        User user = userService.getUserById(userId);
        if (!user.isSeller()) return ProductResponse.builder().message("No permission!").build();

        Product product = new Product();
        product.setName(productRequest.getName());

        String description = productRequest.getDescription();
        float wholesalePrice = productRequest.getWholesalePrice();
        float retailPrice = productRequest.getRetailPrice();
        Integer stockQuantity = productRequest.getStockQuantity();

        if (description!=null) product.setDescription(description);
        if (wholesalePrice!=0) product.setWholesalePrice(wholesalePrice);
        if (retailPrice!=0) product.setRetailPrice(retailPrice);
        if (stockQuantity!=null) product.setStockQuantity(stockQuantity);

        productService.addProduct(product);
        return ProductResponse.builder().product(product).message("New product created!").build();
    }

    @GetMapping("/admin/mostProfitProduct/{userId}")
    public ProductResponse mostProfitProduct(@PathVariable int userId){
        User user = userService.getUserById(userId);
        if (!user.isSeller()) return ProductResponse.builder().message("No permission!").build();

        Product product = productService.mostProfitProduct();
        return ProductResponse.builder().product(product).message("The most profitable product found!").build();
    }

    @GetMapping("/admin/top3PupolarProducts/{userId}")
    public AllProductsResponse top3Products(@PathVariable int userId){
        User user = userService.getUserById(userId);
        if (!user.isSeller()) return AllProductsResponse.builder().message("No permission!").build();

        List<Product> top3 = productService.getTop3Products();
        return AllProductsResponse.builder().product(top3).message("The most popular 3 products found!").build();
    }

    @GetMapping("/admin/totalItemsSold/{userId}")
    public String totalItemsSold(@PathVariable int userId){
        User user = userService.getUserById(userId);
        if (!user.isSeller()) return "No permission!";

        return "You have sold " + productService.totalItemsSold() + " items in total!";
    }

    @GetMapping("/admin/top3Users/{userId}")
    public AllUsersResponse top3Users(@PathVariable int userId){
        User user = userService.getUserById(userId);
        if (!user.isSeller()) return AllUsersResponse.builder().message("No permission!").build();

        List<User> top3 = userService.getTop3Users();
        return AllUsersResponse.builder().user(top3).message("Top 3 users found!").build();
    }
}
