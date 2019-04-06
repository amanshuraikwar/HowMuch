package io.github.amanshuraikwar.howmuch.protocol

import io.reactivex.Observable

interface CategoriesDataManager {

    fun getAllCategories(): Observable<Iterable<Category>>

    fun getCategoryById(id: String): Observable<Category>

    fun addCategory(category: Category): Observable<Category>

    fun updateCategory(category: Category): Observable<Category>

    fun deleteCategory(category: Category): Observable<Category>

}