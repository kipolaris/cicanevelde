package hu.bme.aut.android.cicanevelde.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import hu.bme.aut.android.cicanevelde.R
import hu.bme.aut.android.cicanevelde.domain.model.Cat
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CatStatsPopUp(
    cat: Cat,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(330.dp)
            .height(106.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.statspopup),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.offset(x = 24.dp, y = 10.dp)
        ) {
            Text(
                text = cat.name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A1F27)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Energy: ${cat.stats.energy}",
                fontSize = 26.sp,
                color = Color(0xFF3A1F27)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Hunger: ${cat.stats.hunger}",
                fontSize = 26.sp,
                color = Color(0xFF3A1F27)
            )
        }

        Column(
            modifier = Modifier.offset(x = 190.dp, y = 10.dp)
        ) {
            Text(
                text = "Bladder: ${cat.stats.bladder}",
                fontSize = 26.sp,
                color = Color(0xFF3A1F27)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Hygiene: ${cat.stats.hygiene}",
                fontSize = 26.sp,
                color = Color(0xFF3A1F27)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Happiness: ${cat.stats.happiness}",
                fontSize = 26.sp,
                color = Color(0xFF3A1F27)
            )
        }
    }
}