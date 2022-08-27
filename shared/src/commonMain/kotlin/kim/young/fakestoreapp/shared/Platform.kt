package kim.young.fakestoreapp.shared

import org.koin.core.module.Module

expect fun platformModule(): Module


@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class Parcelize()

expect interface Parcelable