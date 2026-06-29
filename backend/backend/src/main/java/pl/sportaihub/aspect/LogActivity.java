package pl.sportaihub.aspect;

import pl.sportaihub.enums.ActivityType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogActivity {

    ActivityType type();

    String message();
}