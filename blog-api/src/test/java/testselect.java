
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.zy.blog.dao.mapper.ArticleMapper;
import com.zy.blog.dao.pojo.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;

/**
 * @author 张岩
 * @version 1.0
 */
@SpringBootTest
public class testselect {
    @Autowired
    private ArticleMapper articleMapper;
    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<Article> userList = articleMapper.selectList(null);
        userList.forEach(System.out::println);
    }

}
