package com.codev13.electrosign13back.validator;

import com.codev13.electrosign13back.validator.annotation.ExistsList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class ExistsListValidator implements ConstraintValidator<ExistsList, Collection<?>> {

    private final ApplicationContext applicationContext;
    private String field;
    private Class<?> entity;

    @Override
    public void initialize(ExistsList constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.entity = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(Collection<?> values, ConstraintValidatorContext context) {
        if (values == null || values.isEmpty()) {
            return false;
        }
        try {
            String repositoryName = entity.getSimpleName() + "Repository";
            String packageName = entity.getPackage().getName();
            packageName = packageName.substring(0, packageName.lastIndexOf(".")) + ".repository";
            Class<?> repositoryClass = Class.forName(packageName + "." + repositoryName);

            JpaRepository<?, ?> repository = (JpaRepository<?, ?>) applicationContext.getBean(repositoryClass);

            String methodName = "existsBy" + field.substring(0, 1).toUpperCase() + field.substring(1);
            Method existsMethod = repositoryClass.getMethod(methodName, values.iterator().next().getClass());

            for (Object value : values) {
                if (!(Boolean) existsMethod.invoke(repository, value)) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la validation d'existence de la liste", e);
        }
    }
}

