package amirhs.de.stage.aspect;

import amirhs.de.stage.annotation.CurrentUser;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CurrentUserAspect {

    @Before("@annotation(currentUser)")
    public void before(CurrentUser currentUser) {
        // This method is executed before any method annotated with @CurrentUser
        // Here we don't need to do much, but you could add additional logic if needed
    }
}