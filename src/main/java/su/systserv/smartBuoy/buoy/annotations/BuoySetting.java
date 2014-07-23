package su.systserv.smartBuoy.buoy.annotations;

import java.lang.annotation.*;

/**
 * Аннотация для полей-настроек классов: Buoy, MeteoBuoy, EcologBuoy.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface BuoySetting {
}
