package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.domain.orders.Orders;
import com.example.transactionmanagementdemo.domain.orders.OrdersResponse;
import com.example.transactionmanagementdemo.domain.product.AllProductsResponse;
import com.example.transactionmanagementdemo.domain.product.Product;
import com.example.transactionmanagementdemo.domain.product.ProductResponse;
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
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);

        Orders orders = ordersService.getOrdersById(orderId);
        if (orders == null)
            return OrdersResponse.builder().message("Order not found!").build();
        if (user.getId()!=orders.getUser().getId() || !user.isSeller())
            return OrdersResponse.builder().message("No permission!").build();

        return ordersService.updateOrdersStatus(orderId, status);
    }

    @GetMapping("/user/watchList")
    public AllUserProductResponse getWatchList(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user==null) return AllUserProductResponse.builder().message("No permission!").build();

        Set<Product> productSet = user.getProducts().stream()
                .filter(p->p.getStockQuantity()>0)
                .collect(Collectors.toSet());

        List<UserProduct> userProducts = new ArrayList<>();
        for (Product p: productSet){
            UserProduct up = new UserProduct();
            up.setId(p.getId());
            up.setName(p.getName());
            up.setDescription(p.getDescription());
            up.setPrice(p.getRetailPrice());
            userProducts.add(up);
        }

        return AllUserProductResponse.builder().userProducts(userProducts).message("Watch list for user " + user.getUsername()).build();
    }

    @PostMapping("/user/watchList/new/{productId}")
    public AllUserProductResponse addProductToWatchList(@PathVariable int productId){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user==null) return AllUserProductResponse.builder().message("User does not exist!").build();

        Product product = productService.getProductById(productId);
        if (product==null) return AllUserProductResponse.builder().message("Product does not exist!").build();

        userService.addProductToWatchList(user, product);
        return AllUserProductResponse.builder().message("Product added to watch list!").build();
    }

    @DeleteMapping("/user/watchList/delete/{productId}")
    public AllUserProductResponse deleteProductFromWatchList(@PathVariable int productId){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);

        userService.deleteProductFromWatchList(user, productId);
        return AllUserProductResponse.builder().message("Product deleted from watch list!").build();
    }

    @GetMapping("/user/top3FrequentProducts")
    public AllUserProductResponse getTop3FrequentProducts(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(username);
        int userId = user.getId();

        List<Map.Entry<Product, Integer>> top3Frequent = ordersService.top3Frequent(userId);
        List<UserProduct> top3Products = new ArrayList<>();
        for (Map.Entry<Product, Integer> e: top3Frequent){
            Product p = e.getKey();
            UserProduct up = new UserProduct();
            up.setId(p.getId());
            up.setName(p.getName());
            up.setDescription(p.getDescription());
            up.setPrice(p.getRetailPrice());
            top3Products.add(up);
        }
        return AllUserProductResponse.builder()
                .userProducts(top3Products)
                .message("Top 3 frequently bought products found!")
                .build();
    }

    @GetMapping("/user/top3RecentProducts/{userId}")
    public AllUserProductResponse getTop3RecentProducts(@PathVariable int userId){
        List<Product> products = ordersService.top3Recent(userId);
        List<UserProduct> top3Recent = new ArrayList<>();
        for (Product p : products){
            UserProduct up = new UserProduct();
            up.setId(p.getId());
            up.setName(p.getName());
            up.setDescription(p.getDescription());
            up.setPrice(p.getRetailPrice());
            top3Recent.add(up);
        }
        return AllUserProductResponse.builder()
                .userProducts(top3Recent)
                .message("Top 3 recently bought products found!")
                .build();
    }

}
