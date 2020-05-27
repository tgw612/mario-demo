package com.mario.web.service.base;

import com.doubo.common.enums.AppName;
import com.doubo.common.threadlocal.SerialNo;
import com.mario.web.support.config.ServiceConfig;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.BeforeClass;

/**
 * Created with IntelliJ IDEA. User: qiujingwang Date: 2016-3-27 Depiction: 测试基类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceConfig.class)
public abstract class BaseSpringTest {

  @BeforeClass
  public void setUp() {
    init();

        /*ActiveProfiles activeProfiles = AnnotationUtils.findAnnotation(this.getClass(), ActiveProfiles.class);
        MockitoAnnotations.initMocks(this);

        if ("mock".equals(activeProfiles.value()[0])) {
            fullMock();
        }*/
    addPrevTest();
  }

  private void init() {
    SerialNo.init(AppName.DOUBO_SC);
  }

  private void fullMock() {
    fullMockInner();
  }

  /**
   * 业务相关的Mock
   */
  public void fullMockInner() {
  }

  ;

  protected void addPrevTest() {
  }

}