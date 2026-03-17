package com.example.demo.mapper;

import com.example.demo.model.Product;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("SELECT * FROM products ORDER BY popularity DESC LIMIT #{limit}")
    List<Product> findPopular(@Param("limit") Integer limit);

    @Select("SELECT * FROM products WHERE industry = #{industry} ORDER BY popularity DESC LIMIT #{limit}")
    List<Product> findByIndustry(@Param("industry") String industry, @Param("limit") Integer limit);

    @Select("SELECT * FROM products ORDER BY create_time DESC")
    List<Product> findAll();

    @Select("SELECT * FROM products WHERE id = #{id}")
    Product findById(Long id);

    @Select("SELECT DISTINCT industry FROM products WHERE industry IS NOT NULL")
    List<String> findAllIndustries();
}
