package reflection;

import java.lang.annotation.*;

/**
 * 列
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name();
}
