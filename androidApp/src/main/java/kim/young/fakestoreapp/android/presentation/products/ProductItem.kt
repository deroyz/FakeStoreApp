package kim.young.fakestoreapp.android.presentation.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import kim.young.fakestoreapp.shared.domain.ProductDomainModel

@ExperimentalCoilApi
@Composable
fun ProductItem(
    product: ProductDomainModel,
//    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
//            .clickable(onClick = onClick)
    ) {
        Image(
            painter = rememberImagePainter(product.image),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 10.dp, start = 10.dp, bottom = 10.dp)
                .width(110.dp)
                .height(110.dp)
        )
        product.title?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}
