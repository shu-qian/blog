package com.gardenia.blog.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gardenia.blog.dao.mapper.ArticleMapper;
import com.gardenia.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author sq ♥ovo♥
 * @date 2023/12/15 - 14:30
 */
@Component
public class ThreadService {

    /**
     * 期望此操作在线程池执行，不会影响原有的主线程
     * 测试:
     *     睡眠 ThredService中的方法 5秒，不会影响主线程的使用，即文章详情会很快的显示出来，不受影响
     */
    @Async("taskExecutor")  //把它扔到了线程池中去异步执行了
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts+1);  //有什么值就在符合更新条件的数据上改对应的值，不影响没涉及的值

        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        updateWrapper.eq(Article::getViewCounts,viewCounts);
        //之前查询了一次，在修改的时候再查一次，如果没有被修改过再执行+1
        //类似锁,为了多线程的条件下线程安全
        //改之前再确认这个值有没有被其他线程抢先修改，类似于CAS操作 cas加自旋，加个循环就是ca

        //update article set view_count=100 where view_count=99 and id =111
        //实体类加更新条件
        articleMapper.update(articleUpdate,updateWrapper);
//        try {
//            Thread.sleep(5000);
//            System.out.println("更新完成了...");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }
}
