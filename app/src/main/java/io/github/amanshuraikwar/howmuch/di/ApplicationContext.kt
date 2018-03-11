package io.github.amanshuraikwar.howmuch.di

import java.lang.annotation.Documented
import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier

/**
 * Created by amanshuraikwar on 07/03/18.
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Qualifier @Retention annotation class ApplicationContext