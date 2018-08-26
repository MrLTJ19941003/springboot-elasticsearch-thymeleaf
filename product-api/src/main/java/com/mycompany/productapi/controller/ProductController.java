package com.mycompany.productapi.controller;

import com.mycompany.productapi.dto.CreateProductDto;
import com.mycompany.productapi.dto.ResponseProductDto;
import com.mycompany.productapi.dto.SearchDto;
import com.mycompany.productapi.dto.UpdateProductDto;
import com.mycompany.productapi.exception.ProductNotFoundException;
import com.mycompany.productapi.model.Product;
import com.mycompany.productapi.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ma.glasnost.orika.MapperFacade;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final MapperFacade mapperFacade;

    public ProductController(ProductService productService, MapperFacade mapperFacade) {
        this.productService = productService;
        this.mapperFacade = mapperFacade;
    }

    @ApiOperation(value = "Get Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{productId}")
    public ResponseProductDto getProduct(@PathVariable String productId) throws ProductNotFoundException {
        return mapperFacade.map(productService.validateAndGetProductById(productId), ResponseProductDto.class);
    }

    @ApiOperation(value = "Create Product", code = 201)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseProductDto createProduct(@Valid @RequestBody CreateProductDto createProductDto) {
        Product product = mapperFacade.map(createProductDto, Product.class);
        product = productService.saveProduct(product);
        return mapperFacade.map(product, ResponseProductDto.class);
    }

    @ApiOperation(value = "Update Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{productId}")
    public ResponseProductDto updateProduct(@PathVariable String productId, @Valid @RequestBody UpdateProductDto updateProductDto)
            throws ProductNotFoundException {
        Product product = productService.validateAndGetProductById(productId);
        mapperFacade.map(updateProductDto, product);
        product = productService.saveProduct(product);
        return mapperFacade.map(product, ResponseProductDto.class);
    }

    @ApiOperation(value = "Delete Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable String productId) throws ProductNotFoundException {
        Product product = productService.validateAndGetProductById(productId);
        productService.deleteProduct(product);
        return productId;
    }

    @ApiOperation(value = "Search for Products", notes = "This endpoint does a multi_match query for the 'text' informed in the fields 'names' and 'description' ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/search")
    public Page<Product> searchProducts(@Valid @RequestBody SearchDto searchDto) {
        return productService.search(searchDto.getText());
    }

}
