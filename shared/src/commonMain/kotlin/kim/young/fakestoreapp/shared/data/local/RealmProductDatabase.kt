package kim.young.fakestoreapp.shared.data.local

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.flow.Flow

interface ProductDatabase {
    suspend fun clearAndInsertProductList(products: List<ProductDatabaseModel>)
    suspend fun clearAllProductList()

    fun getProductList(): Flow<ResultsChange<ProductDatabaseModel>>

    fun getProductListObj(): RealmResults<ProductDatabaseModel>
}

class RealmProductDatabase(private val realm: Realm) : ProductDatabase {

    // Writing data into Realm database
    override suspend fun clearAndInsertProductList(products: List<ProductDatabaseModel>) {
        realm.writeBlocking {
            val queries = query<ProductDatabaseModel>().find()
            delete(queries)

            products.forEach { product ->
                copyToRealm(
                    product
                )
            }
        }
    }

    override suspend fun clearAllProductList() {
        realm.writeBlocking {
            val queries = query<ProductDatabaseModel>().find()
            delete(queries)
        }
    }

    // Fetching data from Realm database
    override fun getProductList() = realm.query<ProductDatabaseModel>().find().asFlow()

    override fun getProductListObj() = realm.query<ProductDatabaseModel>().find()


}

