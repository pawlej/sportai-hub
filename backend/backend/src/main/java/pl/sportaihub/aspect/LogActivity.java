package pl.sportaihub.aspect;

import pl.sportaihub.enums.ActivityType;

import java.lang.annotation.*;
/**
 * Adnotacja oznaczająca operację biznesową, która powinna zostać
 * automatycznie zapisana w historii aktywności.
 *
 * <p>Adnotacja jest obsługiwana przez {@link ActivityLogAspect}.</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogActivity {

    ActivityType type();

    String message();
}