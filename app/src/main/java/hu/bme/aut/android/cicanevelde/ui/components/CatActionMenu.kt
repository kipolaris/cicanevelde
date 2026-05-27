package hu.bme.aut.android.cicanevelde.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.cicanevelde.domain.model.enums.CatActionType

@Composable
fun CatActionMenu(
    actions: List<CatActionUi>,
    onActionClick: (CatActionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        actions.forEach { action ->
            PinkButton(
                text = action.label,
                onClick = { onActionClick(action.type) }
            )
        }
    }
}