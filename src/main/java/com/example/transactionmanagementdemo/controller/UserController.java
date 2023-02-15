package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.domain.orders.OrdersResponse;
import com.example.transactionmanagementdemo.domain.product.AllProductsResponse;
import com.example.transactionmanagementdemo.domain.product.Product;
import com.example.transactionmanagementdemo.domain.product.ProductResponse;
import com.example.transactionmanagementdemo.domain.user.User;
import com.example.transactionmanagementdemo.domain.user.UserResponse;
import com.example.transactionmanagementdemo.domain.userProduct.UserProduct;
import com.example.transactionmanagementdemo.domain.watchList.WatchListResponse;
import com.example.transactionmanagementdemo.domain.entity.PurchaseRequest;
import com.example.transactionmanagementdemo.exception.UserSaveFailedException;
import com.example.transactionmanagementdemo.service.OrdersService;
import com.example.transactionmanagementdemo.service.ProductService;
import com.example.transactionmanagementdemo.service.UserService;
import com.example.transactionmanagementdemo.domain.user.UserRequest;
import com.example.transactionmanagementdemo.domain.userProduct.UserProductResponse;

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



    @GetMapping("/viewProducts/{userId}")
    public UserProductResponse viewProductsSuccess(){
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
        return UserProductResponse.builder()
                .userProducts(userProducts)
                .message("Get all products information successfully!")
                .build();
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
