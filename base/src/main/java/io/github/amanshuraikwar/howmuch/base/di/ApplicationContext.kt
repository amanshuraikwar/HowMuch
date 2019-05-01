package io.github.amanshuraikwar.howmuch.base.di

import javax.inject.Qualifier

/**
 * Created by Amanshu Raikwar on 07/03/18.
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Qualifier @Retention annotation class ApplicationContext