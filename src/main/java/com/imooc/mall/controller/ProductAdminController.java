package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.UpdateProductReq;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * 描述：      后台商品管理controller
 */
@RestController   //等价于=@Controller+@ResponseBoby 方法上
public class ProductAdminController {
    @Autowired
    ProductService productService;


    /**
     * 商品：新增商品
     * @param addProductReq
     * @return
     */
    @PostMapping("/admin/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq){
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    /**
     *
     * 配合新增商品add使用
     * @param httpServletRequest
     * @param file
     * @return
     */
    @PostMapping("/admin/upload/file")
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file){
        //得到原始文件名和后缀
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        //生成文件名称UUID
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString()+suffixName;
        //创建文件
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        if(!fileDirectory.exists()){
            //不存在
            if(!fileDirectory.mkdir()){
                throw new ImoocMallException(ImoocMallExceptionEnum.MKDIR_FAILED);
            }
        }
        try {
            //将文件写到文件夹
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //success中返回新的URI
        try {
            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURI()+""))+"/images/"+newFileName);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.UPLOAD_FAILED);
        }

    }

    /**
     * 配合upload使用
     * @param uri
     * @return
     */
    private URI getHost(URI uri){
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI=null;
        }
        return effectiveURI;
    }

    /**
     * 更新商品
     * @Valid 注解用于校验
     * @param updateProductReq
     * @return
     */
    @ApiOperation("后台更新商品")
    @PostMapping("/admin/product/update")
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq){
        Product product = new Product();
        //拷贝已有的
        BeanUtils.copyProperties(updateProductReq, product);
        productService.update(product);
        return ApiRestResponse.success();
    }

    /**
     * 后台删除商品
     * @param id
     * @return
     */
    @ApiOperation("后台删除商品")
    @PostMapping("/admin/product/delete")
    public ApiRestResponse deleteProduct(@RequestParam Integer id){
        productService.delete(id);
        return ApiRestResponse.success();
    }


    /**
     * 后台批量上下架商品
     * @param ids  数组
     * @param sellStatus   上下架状态，1是上架，0是下架
     * @return
     */
    @ApiOperation("后台批量上下架商品接口")
    @PostMapping("/admin/product/batchUpdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids,@RequestParam Integer sellStatus){
        productService.batchUpdateSellStatus(ids,sellStatus);
        return ApiRestResponse.success();
    }

    /**
     * 后台商品列表接口
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("后台商品列表接口")
    @GetMapping("/admin/product/list")
    public ApiRestResponse list(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = productService.listForAdmin(pageNum,pageSize);
        return ApiRestResponse.success(pageInfo);
    }

}
