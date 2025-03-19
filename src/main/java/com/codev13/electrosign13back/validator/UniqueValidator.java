package com.codev13.electrosign13back.validator;


import com.codev13.electrosign13back.validator.annotation.Unique;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    private final ApplicationContext applicationContext;

    private String field;
    private Class<?> entity;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.entity = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            String repositoryName = entity.getSimpleName() + "Repository";
            String packageName = entity.getPackage().getName();
            packageName = packageName.substring(0, packageName.lastIndexOf(".")) + ".repository";
            Class<?> repositoryClass = Class.forName(packageName + "." + repositoryName);
            JpaRepository<?, ?> repository = (JpaRepository<?, ?>) applicationContext.getBean(repositoryClass);
            System.out.println("ICI3");
            String methodName = "existsBy" + field.substring(0, 1).toUpperCase() + field.substring(1);
            Method existsMethod = repositoryClass.getMethod(methodName, value.getClass());
            System.out.println("ICI 3");
            return !(Boolean) existsMethod.invoke(repository, value);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la validation d'unicité", e);
        }
    }
}
