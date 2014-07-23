package su.systserv.smartBuoy.buoy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для полей-настроек классов: Buoy, MeteoBuoy, EcologBuoy.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface BuoySimpleInfo {
}
