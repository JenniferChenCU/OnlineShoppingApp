package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.domain.Orders.OrdersResponse;
import com.example.transactionmanagementdemo.domain.Product.AllProductsResponse;
import com.example.transactionmanagementdemo.domain.Product.Product;
import com.example.transactionmanagementdemo.domain.Product.ProductResponse;
import com.example.transactionmanagementdemo.domain.User.User;
import com.example.transactionmanagementdemo.domain.User.UserResponse;
import com.example.transactionmanagementdemo.domain.WatchList.WatchListResponse;
import com.example.transactionmanagementdemo.domain.entity.PurchaseRequest;
import com.example.transactionmanagementdemo.exception.UserSaveFailedException;
import com.example.transactionmanagementdemo.service.OrdersService;
import com.example.transactionmanagementdemo.service.ProductService;
import com.example.transactionmanagementdemo.service.UserService;
import com.example.transactionmanagementdemo.domain.User.UserRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final ProductService productService;
    private final OrdersService ordersService;

    @Autowired
    public UserController(UserService userService, ProductService productService, OrdersService ordersService) {
        this.userService = userService;
        this.productService = productService;
        this.ordersService = ordersService;
    }

    @PostMapping("/new")
    public UserResponse createNewUser(
            @Valid @RequestBody UserRequest user,
            BindingResult bindingResult
    ){
        // perform validation check
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(error -> System.out.println(
                    "ValidationError in " + error.getObjectName() + ": " + error.getDefaultMessage()));
            return UserResponse.builder()
                    .message("Validation Error")
                    .build();
        }

        // unique username & email check
        if (userService.getUserByUsername(user.getUsername())!=null) {
            return UserResponse.builder().message("Username already exist!").build();
        }
        if (userService.getUserByEmail(user.getEmail())!=null) {
            return UserResponse.builder().message("Email already exist!").build();
        }

        // validation passed, create new user
        User newUser = User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .orders(new ArrayList<>())
                .build();

        userService.addUser(newUser);

        return UserResponse.builder()
                .message("New user created")
                .user(newUser)
                .build();
    }

    @GetMapping("/success")
    public List<User> getAllUsersSuccess(){
        return userService.getAllUsersSuccess();
    }

    @GetMapping("/id/{id}")
    public UserResponse getUserById(@PathVariable int id){
        User user = userService.getUserById(id);
        return UserResponse.builder()
                .message("Returning user with id: " + id)
                .user(user)
                .build();
    }

    @PutMapping("/success")
    public UserResponse saveUserSuccess(@RequestBody User user){
        userService.saveUserSuccess(user);
        return UserResponse.builder()
                .message("User saved, committing...")
                .user(user)
                .build();
    }

    @PutMapping("/failed")
    public ResponseEntity saveUserFailed(@RequestBody User user) throws UserSaveFailedException {
        userService.saveUserFailed(user);
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id){
        userService.deleteUserById(id);
        return new ResponseEntity<>("User deleted.", HttpStatus.OK);
    }

    @GetMapping("/viewProducts")
    public List<Product> viewProductsSuccess(){
        return productService.getInstockProductsSuccess();
    }

    @GetMapping("/viewProducts/{userId}/{productId}")
    public ProductResponse viewProductDetails(@PathVariable int userId, @PathVariable int productId){
        Product product = productService.userGetProductById(userId, productId);
        return ProductResponse.builder()
                .message("Returning product with id: " + productId)
                .product(product)
                .build();
    }

    @PostMapping("/newOrder")
    public OrdersResponse createNewOrder(@RequestBody PurchaseRequest purchaseRequest){
        return ordersService.createNewOrders(purchaseRequest.getUserId(), purchaseRequest.getPurchaseDetail());
    }

    @PostMapping("/updateStatus/{orderId}/{isAdmin}")
    public OrdersResponse updateOrderStatus(@PathVariable int orderId,
                                            @PathVariable boolean isAdmin,
                                            @RequestParam("status") Integer status){
        return ordersService.updateOrdersStatus(orderId, status, isAdmin);
    }

    @GetMapping("/watchList/{userId}")
    public WatchListResponse getWatchList(@PathVariable int userId){
        User user = userService.getUserById(userId);
        Set<Product> productSet = user.getProducts().stream()
                .filter(p->p.getStockQuantity()>0)
                .collect(Collectors.toSet());
        return WatchListResponse.builder().productSet(productSet).message("Full watch list get!").build();
    }

    @PostMapping("/watchList/new/{userId}/{productId}")
    public WatchListResponse addProductToWatchList(@PathVariable int userId,
                                                   @PathVariable int productId){
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        return userService.addProductToWatchList(user, product);
    }

    @DeleteMapping("/watchList/delete/{userId}/{productId}")
    public WatchListResponse deleteProductFromWatchList(@PathVariable int userId,
                                                        @PathVariable int productId){
        User user = userService.getUserById(userId);
        return userService.deleteProductFromWatchList(user, productId);
    }

    @GetMapping("/top3FrequentProducts/{userId}")
    public AllProductsResponse getTop3FrequentProducts(@PathVariable int userId){
        List<Map.Entry<Product, Integer>> top3Frequent = ordersService.top3Frequent(userId);
        List<Product> top3Products = new ArrayList<>();
        for (Map.Entry<Product, Integer> e: top3Frequent){
            top3Products.add(e.getKey());
        }
        return AllProductsResponse.builder()
                .product(top3Products)
                .message("Top 3 frequently bought products found!")
                .build();
    }

    @GetMapping("/top3RecentProducts/{userId}")
    public AllProductsResponse getTop3RecentProducts(@PathVariable int userId){
        List<Product> top3Recent = ordersService.top3Recent(userId);
        return AllProductsResponse.builder()
                .product(top3Recent)
                .message("Top 3 recently bought products found!")
                .build();
    }

}
