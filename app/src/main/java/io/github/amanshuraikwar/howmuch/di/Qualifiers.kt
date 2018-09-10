package io.github.amanshuraikwar.howmuch.di

import javax.inject.Qualifier

/**
 * Used all over the app to define qualifiers
 * These qualifiers are used to differentiate between instances of same class used for different purposes
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 10/09/18.
 */
interface Qualifiers {

    @Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
    @Qualifier
    @Retention annotation class CuratedPhotosListing
}