package kim.young.fakestoreapp.android.util

sealed interface ConnectionState {
    object Available: ConnectionState
    object Unavailable: ConnectionState
}