package com.mall.discover.request.product;

import com.doubo.common.model.request.common.CommonRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("关联文章")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductAddOrEditRequestDto extends CommonRequest {

    private static final long serialVersionUID = -5812562090356119390L;
    @NotNull(message = "文章id不能为空")
    @ApiModelProperty(value = "文章ID", notes = "", required = true)
    private Long articleId;

    @NotNull(message = "商品编码不能为空")
    @ApiModelProperty(value = "商品编码集合", notes = "", required = true)
    private List<String> productNo;
}
