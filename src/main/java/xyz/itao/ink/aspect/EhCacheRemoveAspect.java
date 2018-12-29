package xyz.itao.ink.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import xyz.itao.ink.annotation.CacheRemove;
import xyz.itao.ink.utils.EhCacheUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hetao
 * @date 2018-12-29
 * @description ehcache 正则cache擦除
 */
@Aspect
@Component
@Slf4j
public class EhCacheRemoveAspect {
    @Pointcut(value = "(execution(* *.*(..)) && @annotation(xyz.itao.ink.annotation.CacheRemove))")
    private void pointcut() {}

    /**
     * 功能描述: 在方法返回后移除ehcache中的复合正则表达式的key
    */
    @AfterReturning(value = "pointcut()")
    private void process(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切面的参数
        Object[] args = joinPoint.getArgs();
        // 获取切面的方法
        Method method = signature.getMethod();
        // 获取注解
        CacheRemove cacheRemove = method.getAnnotation(CacheRemove.class);

        if (cacheRemove != null){
            String value = cacheRemove.value();
            //需要移除的正则key
            String[] keys = cacheRemove.key();

            List cacheKeys = EhCacheUtils.cacheKeys(value);
            for (String key : keys){
                key = parseKey(key, method, args);
                Pattern pattern = Pattern.compile(key);
                for (Object cacheKey: cacheKeys) {
                    String cacheKeyStr = String.valueOf(cacheKey);
                    if (pattern.matcher(cacheKeyStr).find()){
                        EhCacheUtils.remove(value, cacheKeyStr);
                        log.debug("remove value={}, key={}", value, key);
                    }
                }
            }
        }
    }

    /**
     *    获取缓存的key
     *    key 定义在注解上，支持SPEL表达式
     * @return
     */
    private String parseKey(String key,Method method,Object [] args){
        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u =
                new LocalVariableTableParameterNameDiscoverer();
        String [] paraNameArr=u.getParameterNames(method);

        //使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        //SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SPEL上下文中
        for(int i=0;i<paraNameArr.length;i++){
            context.setVariable(paraNameArr[i], args[i]);
        }
        List<String> pList = descFormat(key);//获取#p0这样的表达式
        //将p0作为参数放入SPEL上下文中
        for(String p:pList) {
            context.setVariable(p.substring(1), args[Integer.valueOf(p.substring(2))]);
        }
        return parser.parseExpression(key).getValue(context,String.class);
    }

    /**
     * 提取出#p[数字]这样的表达式
     * @param desc
     * @return
     */
    private static List<String> descFormat(String desc){
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("#p[0-9]+");
        Matcher matcher = pattern.matcher(desc);
        while(matcher.find()){
            String t = matcher.group(0);
            list.add(t);
        }
        return list;
    }


}

