package com.jfireframework.context.test.function.aliastest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Resource;
import com.jfireframework.context.aliasanno.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@TestAlias
public @interface Testalis3
{
    @AliasFor(annotation = TestAlias.class, value = "test")
    public String t();
    
    @AliasFor(annotation = Resource.class, value = "shareable")
    public boolean s() default false;
}
