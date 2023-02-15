package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.domain.orders.OrdersResponse;
import com.example.transactionmanagementdemo.domain.product.AllProductsResponse;
import com.example.transactionmanagementdemo.domain.product.Product;
import com.example.transactionmanagementdemo.domain.user.User;
import com.example.transactionmanagementdemo.domain.user.UserResponse;
import com.example.transactionmanagementdemo.domain.userProduct.UserProduct;
import com.example.transactionmanagementdemo.domain.userProduct.UserProductResponse;
import com.example.transactionmanagementdemo.domain.watchList.WatchListResponse;
import com.example.transactionmanagementdemo.domain.entity.PurchaseRequest;
import com.example.transactionmanagementdemo.security.AuthUserDetail;
import com.example.transactionmanagementdemo.service.OrdersService;
import com.example.transactionmanagementdemo.service.ProductService;
import com.example.transactionmanagementdemo.service.UserService;
import com.example.transactionmanagementdemo.domain.user.UserRequest;
import com.example.transactionmanagementdemo.domain.userProduct.AllUserProductResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
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

    @PostMapping("/registration")
    public UserResponse createNewUser(@Valid @RequestBody UserRequest user,
                                      BindingResult bindingResult){
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
                .isSeller(false)
                .orders(new ArrayList<>())
                .build();

        userService.addUser(newUser);

        return UserResponse.builder()
                .message("New user created")
                .user(newUser)
                .build();
    }

    public Object getAuthUserDetail(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/user/viewAllProducts")
    public AllUserProductResponse viewProductsSuccess(){
        List<Product> allProducts =  productService.getInstockProductsSuccess();
        List<UserProduct> userProducts = new ArrayList<>();
        for (Product product: allProducts){
            UserProduct userProduct = new UserProduct();
            userProduct.setId(product.getId());
            userProduct.setName(product.getName());
            userProduct.setDescription(product.getDescription());
            userProduct.setPrice(product.getRetailPrice());
            userProducts.add(userProduct);
        }
        return AllUserProductResponse.builder()
                .userProducts(userProducts)
                .message("Get all products information successfully!")
                .build();
    }

    @GetMapping("/user/viewProducts/{productId}")
    public UserProductResponse viewProductDetails(@PathVariable int productId){
        Object authUserDetail = getAuthUserDetail();
        System.out.println(authUserDetail);

        int userId = 1;
        Product product = productService.userGetProductById(userId, productId);
        UserProduct userProduct = new UserProduct();
        userProduct.setId(product.getId());
        userProduct.setName(product.getName());
        userProduct.setDescription(product.getDescription());
        userProduct.setPrice(product.getRetailPrice());
        return UserProductResponse.builder()
                .message("Returning product with id: " + productId)
                .userProduct(userProduct)
                .build();
    }

    @PostMapping("/user/newOrder")
    public OrdersResponse createNewOrder(@RequestBody PurchaseRequest purchaseRequest){
        return ordersService.createNewOrders(purchaseRequest.getUserId(), purchaseRequest.getPurchaseDetail());
    }


    @PostMapping("/updateStatus/{orderId}")
    public OrdersResponse updateOrderStatus(@PathVariable int orderId,
                                            @RequestParam("status") Integer status){
        return ordersService.updateOrdersStatus(orderId, status);
    }

    @GetMapping("/user/watchList/{userId}")
    public WatchListResponse getWatchList(@PathVariable int userId){
        User user = userService.getUserById(userId);
        Set<Product> productSet = user.getProducts().stream()
                .filter(p->p.getStockQuantity()>0)
                .collect(Collectors.toSet());
        return WatchListResponse.builder().productSet(productSet).message("Full watch list get!").build();
    }

    @PostMapping("/user/watchList/new/{userId}/{productId}")
    public WatchListResponse addProductToWatchList(@PathVariable int userId,
                                                   @PathVariable int productId){
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        return userService.addProductToWatchList(user, product);
    }

    @DeleteMapping("/user/watchList/delete/{userId}/{productId}")
    public WatchListResponse deleteProductFromWatchList(@PathVariable int userId,
                                                        @PathVariable int productId){
        User user = userService.getUserById(userId);
        return userService.deleteProductFromWatchList(user, productId);
    }

    @GetMapping("/user/top3FrequentProducts/{userId}")
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

    @GetMapping("/user/top3RecentProducts/{userId}")
    public AllProductsResponse getTop3RecentProducts(@PathVariable int userId){
        List<Product> top3Recent = ordersService.top3Recent(userId);
        return AllProductsResponse.builder()
                .product(top3Recent)
                .message("Top 3 recently bought products found!")
                .build();
    }

}
