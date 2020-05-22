package com.mall.discover.web.controller;

import com.doubo.common.model.MySession;
import com.doubo.common.model.request.PrimaryIdRequest;
import com.doubo.common.model.request.common.CommonRequest;
import com.doubo.common.model.response.CommonResponse;
import com.mall.common.manage.ResponseManage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * MySession
 * @author qiujingwang
 * @version 1.0
 * @date 2019/01/08 下午3:46
 */
@Api(value = "用户管理", basePath = "/users")
@RestController
@RequestMapping(value="/users")     // 通过这里配置使下面的映射都在/users下，可去除
public class TestSwagger2Controller {

    static Map<Long, MySession> mySessionMap = Collections.synchronizedMap(new HashMap<>());

    @ApiOperation(value="获取用户列表", notes="")
    @GetMapping(value={"/list"})
    public CommonResponse<List<MySession>> getMySessionList(CommonRequest request) {
        List<MySession> r = new ArrayList<MySession>(mySessionMap.values());

        // or ResponseManage.fail("失败信息")
        return ResponseManage.success(r);
    }

    @ApiOperation(value="创建用户", notes="根据MySession对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "MySession")
    @PostMapping(value="/add")
    public CommonResponse<Boolean> postMySession(CommonRequest request, @Valid @RequestBody MySession user) {
        //找不到其他long属性
        mySessionMap.put(user.getTimeout(), user);

        // or ResponseManage.fail("失败信息")
        // or ResponseManage.success(null);
        return ResponseManage.success(true);
    }

    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @GetMapping(value="/{id}")
    public CommonResponse<MySession> getMySession(PrimaryIdRequest request) {

        // or ResponseManage.fail("失败信息")
        return ResponseManage.success(mySessionMap.get(request.getId()));
    }

    @ApiOperation(value="更新用户详细信息", notes="根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "MySession")
    })
    @PutMapping(value="/{id}")
    public CommonResponse<Boolean> putMySession(PrimaryIdRequest request, @Valid @RequestBody MySession user) {
        MySession u = mySessionMap.get(request.getId());
        u.setHost(user.getHost());
        mySessionMap.put(request.getId(), u);

        // or ResponseManage.fail("失败信息")
        // or ResponseManage.success(null);
        return ResponseManage.success(true);
    }

    @ApiOperation(value="删除用户", notes="根据url的id来指定删除对象")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @DeleteMapping(value="/{id}")
    public CommonResponse<Boolean> deleteMySession(PrimaryIdRequest request) {
        mySessionMap.remove(request.getId());

        // or ResponseManage.fail("失败信息")
        // or ResponseManage.success(null);
        return ResponseManage.success(true);
    }
}
