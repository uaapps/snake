package xyz.uaapps.snake.presentation.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.uaapps.snake.R
import xyz.uaapps.snake.data.cache.GameCache
import xyz.uaapps.snake.presentation.component.AppBar
import xyz.uaapps.snake.presentation.component.AppButton
import xyz.uaapps.snake.presentation.theme.border2dp
import xyz.uaapps.snake.presentation.theme.padding16dp

@Composable
fun SettingScreen(navController: NavHostController) {
    val dataStore = GameCache(LocalContext.current)
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    AppBar(
        title = stringResource(R.string.title_settings)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = padding16dp,
                    start = padding16dp,
                    end = padding16dp
                )
                .border(width = border2dp, color = MaterialTheme.colorScheme.onBackground),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            TextField(
                value = text,
                placeholder = { Text(stringResource(R.string.player_name)) },
                onValueChange = { text = it },
                keyboardActions = KeyboardActions(
                    onDone = { submit(scope, dataStore, text, context, navController) },
                ),
                singleLine = true,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Enter || keyEvent.key == Key.DirectionCenter) {
                            submit(scope, dataStore, text, context, navController)
                            true
                        } else false
                    }
                    .fillMaxWidth()
                    .padding(padding16dp)
                    .border(width = border2dp, color = MaterialTheme.colorScheme.onBackground)
            )
            AppButton(
                text = stringResource(R.string.save), modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding16dp, top = 0.dp, padding16dp, padding16dp)
            ) { submit(scope, dataStore, text, context, navController) }
        }
    }
}

private fun submit(
    scope: CoroutineScope,
    dataStore: GameCache,
    text: TextFieldValue,
    context: Context,
    navController: NavHostController
) {
    scope.launch {
        dataStore.savePlayerName(text.text.trim())
        Toast.makeText(context, R.string.player_name_updated, Toast.LENGTH_SHORT).show()
        navController.popBackStack()
    }
}
