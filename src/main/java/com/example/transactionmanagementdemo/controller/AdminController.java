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
import org.springframework.security.core.context.SecurityContextHolder;
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
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user==null || !user.isSeller()) return UserResponse.builder().message("No permission!").build();

        User todeleteUser = userService.getUserById(id);
        return UserResponse.builder()
                .message("Returning user with id: " + id)
                .user(todeleteUser)
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
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user==null || !user.isSeller()) return new ResponseEntity<>("No permission.", HttpStatus.BAD_REQUEST);

        userService.deleteUserById(id);
        return new ResponseEntity<>("User deleted.", HttpStatus.OK);
    }

    @GetMapping("/admin/dashboard")
    public DashboardResponse getDashboard(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user==null || !user.isSeller()) return DashboardResponse.builder().message("No permission!").build();

        List<Orders> ordersList = ordersService.getAllOrdersSuccess();
        List<Product> productList = productService.getAllProductsSuccess();

        Dashboard dashboard = new Dashboard();
        dashboard.setOrders(ordersList);
        dashboard.setProducts(productList);
        return DashboardResponse.builder()
                .dashboard(dashboard)
                .message("All orders and products information retrieved!")
                .build();
    }

    @PostMapping("/admin/dashboard/edit")
    public ProductResponse editProduct(@RequestBody ProductRequest productRequest){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user==null || !user.isSeller()) return ProductResponse.builder().message("No permission!").build();

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

    @PostMapping("/admin/addProduct")
    public ProductResponse addProduct(@RequestBody ProductRequest productRequest){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user==null || !user.isSeller()) return ProductResponse.builder().message("No permission!").build();

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

    @GetMapping("/admin/mostProfitProduct")
    public ProductResponse mostProfitProduct(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user==null || !user.isSeller()) return ProductResponse.builder().message("No permission!").build();

        Product product = productService.mostProfitProduct();
        return ProductResponse.builder().product(product).message("The most profitable product found!").build();
    }

    @GetMapping("/admin/top3PopularProducts")
    public AllProductsResponse top3Products(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user==null || !user.isSeller())  return AllProductsResponse.builder().message("No permission!").build();

        List<Product> top3 = productService.getTop3Products();
        return AllProductsResponse.builder().product(top3).message("The most popular 3 products found!").build();
    }

    @GetMapping("/admin/totalItemsSold")
    public String totalItemsSold(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user==null || !user.isSeller()) return "No permission!";

        return "You have sold " + productService.totalItemsSold() + " items in total!";
    }

    @GetMapping("/admin/top3Users")
    public AllUsersResponse top3Users(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user==null || !user.isSeller())  return AllUsersResponse.builder().message("No permission!").build();

        List<User> top3 = userService.getTop3Users();
        return AllUsersResponse.builder().user(top3).message("Top 3 users found!").build();
    }
}
