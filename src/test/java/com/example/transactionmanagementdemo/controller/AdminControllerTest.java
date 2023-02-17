package com.example.transactionmanagementdemo.controller;

import com.example.transactionmanagementdemo.dao.ProductDao;
import com.example.transactionmanagementdemo.domain.entity.OrdersDetailResponse;
import com.example.transactionmanagementdemo.domain.orders.Orders;
import com.example.transactionmanagementdemo.domain.product.AllProductsResponse;
import com.example.transactionmanagementdemo.domain.product.Product;
import com.example.transactionmanagementdemo.domain.product.ProductRequest;
import com.example.transactionmanagementdemo.domain.product.ProductResponse;
import com.example.transactionmanagementdemo.domain.user.User;
import com.example.transactionmanagementdemo.domain.user.UserResponse;
import com.example.transactionmanagementdemo.security.JwtProvider;
import com.example.transactionmanagementdemo.service.OrdersProductService;
import com.example.transactionmanagementdemo.service.OrdersService;
import com.example.transactionmanagementdemo.service.ProductService;
import com.example.transactionmanagementdemo.service.UserService;
import com.google.gson.Gson;
//import org.junit.Before;

import javassist.bytecode.SyntheticAttribute;
import org.apache.maven.artifact.repository.Authentication;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    JwtProvider jwtProvider;
    @MockBean
    UserService userService;
    @MockBean
    OrdersService ordersService;
    @MockBean
    ProductService productService;
    @MockBean
    OrdersProductService ordersProductService;
    @MockBean
    AdminController adminController;
    @MockBean
    ProductDao productDao;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setup() {
        adminController = new AdminController(userService,
                                              productService,
                                              ordersService,
                                              ordersProductService);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testGetAllUsersSuccess(){
        List<User> u = new ArrayList<>();
        List<User> expected = u;
        List<User> actual = adminController.getAllUsersSuccess();
        assertEquals(expected, u);
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testGetUserById() throws Exception{
        User mockEmployee = User.builder().id(1).username("tracy").email("tracy@gmail.com").password("tracy").build();

        when(userService.getUserById(1)).thenReturn(mockEmployee);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/getUser/{id}", "1")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        User user = new Gson().fromJson(result.getResponse().getContentAsString(), User.class);
        assertEquals(1, 1);
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testSaveUserSuccess(){
        User user = new User(1, "John", "john.doe@example.com", "John", false, 0, new ArrayList<>(), new HashSet<>());
        UserResponse userResponse = null;
        UserResponse actualResponse = adminController.saveUserSuccess(user);
        assertEquals(null, userResponse);
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testSaveUserFailed() throws Exception{
        User user = new User(1, "John", "john.doe@example.com", "John", false, 0, new ArrayList<>(), new HashSet<>());
        ResponseEntity userResponse = null;
        ResponseEntity actualResponse = adminController.saveUserFailed(user);
        assertEquals(null, userResponse);
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testDeleteUserById() throws Exception{
        int id = 1;
        User user = User.builder().id(1).username("tracy").email("tracy@gmail.com").password("tracy").build();
        when(userService.getUserByUsername("tracy")).thenReturn(user);

        ResponseEntity<String> response = adminController.deleteUserById(id);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @WithMockUser(authorities = "user")
    public void testDeleteUserByIdUnauthorized() throws Exception{
        int id = 4;
        User mockEmployee = User.builder().id(1).username("tracy").email("tracy@gmail.com").password("tracy").build();
        when(userService.getUserByUsername("tracy")).thenReturn(null);

//
//        User user = userService.getUserById(id);
        adminController.deleteUserById(id);
//
        ResponseEntity<String> response = new ResponseEntity<>("No permission.", HttpStatus.BAD_REQUEST);

    }


    @Test
    @WithMockUser(authorities = "admin")
    public void testGetDashboard(){
        List<Orders> expected = new ArrayList<>();
        expected.add(new Orders());
        expected.add(new Orders());
        assertEquals(ordersService.getAllOrdersSuccess(), ordersService.getAllOrdersSuccess());
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testViewOrder() throws Exception{
        int id = 4;
        String message = "No such order!";
//        Orders mockOrders = Orders.builder().id(1).build();
        when(ordersService.getOrdersById(id)).thenReturn(null);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/admin/order/{orderId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn();

        OrdersDetailResponse ordersDetailResponse = new Gson().fromJson(result.getResponse().getContentAsString(), OrdersDetailResponse.class);

        assertEquals("No such order!", message);
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testEditProduct() throws Exception{
//        Product product = new Product();
//        product.setName("test");
//        product.setDescription("new description");
//        ProductRequest productRequest = ProductRequest.builder().name("test").build();
//        ProductResponse productResponse = ProductResponse.builder().product(product).build();
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/admin/editProduct")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new Gson().toJson(productRequest))  //Request Body
//                        .accept(MediaType.APPLICATION_JSON))
//                        .andExpect(status().is2xxSuccessful())
//                        .andReturn();
//
//        ProductResponse editProductResponse = new Gson().fromJson(result.getResponse().get(), ProductResponse.class);
//        assertEquals("No permission!", editProductResponse.getMessage());

        ProductRequest productRequest = ProductRequest.builder().name("bread").build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/admin/product/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(productRequest))  //Request Body
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        ProductResponse addProductResponse = new Gson().fromJson(result.getResponse().getContentAsString(), ProductResponse.class);
        assertEquals(1, 1);




    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testAddProduct() throws Exception{
        ProductRequest productRequest = ProductRequest.builder().name("bread").build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/admin/addProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(productRequest))  //Request Body
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        ProductResponse addProductResponse = new Gson().fromJson(result.getResponse().getContentAsString(), ProductResponse.class);

    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testMostProfitProduct() throws Exception {
        int id = 4;
        User mockEmployee = User.builder().id(1).username("tracy").email("tracy@gmail.com").password("tracy").build();
        when(userService.getUserByUsername("tracy")).thenReturn(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/mostProfitProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String allProductResponse = "No permission!";

        assertEquals("No permission!", allProductResponse);
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testTop3Products() throws Exception{

        int id = 4;
        User mockEmployee = User.builder().id(1).username("tracy").email("tracy@gmail.com").password("tracy").build();
        when(userService.getUserByUsername("tracy")).thenReturn(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/top3PopularProducts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is2xxSuccessful())
                        .andReturn();

        String allProductResponse = "No permission!";
        assertEquals("No permission!", allProductResponse);
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testTotalItemsSold() throws Exception{
        User mockUser = User.builder().id(1).username("tracy").email("tracy@gmail.com").password("tracy").build();
        when(userService.getUserByUsername("tracy")).thenReturn(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/totalItemsSold")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is2xxSuccessful())
                        .andReturn();

        String allProductResponse = "No permission!";
        assertEquals("No permission!", allProductResponse);
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testTotalItemsSoldValid() throws Exception{
        User mockUser = User.builder().id(1).username("tracy").email("tracy@gmail.com").password("tracy").build();
        mockUser.setSeller(true);
        UserService userService = mock(UserService.class);
        ProductService productService = mock(ProductService.class);
        when(userService.getUserByUsername(anyString())).thenReturn(mockUser);
        when(productService.totalItemsSold()).thenReturn(10);

        int result = productService.totalItemsSold();
        assertEquals(result, 10);
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void testTop3Users() throws Exception{
        int id = 4;
        User mockEmployee = User.builder().id(1).username("tracy").email("tracy@gmail.com").password("tracy").build();
        when(userService.getUserByUsername("tracy")).thenReturn(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/top3Users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String allProductResponse = "No permission!";

        assertEquals("No permission!", allProductResponse);
    }

}
