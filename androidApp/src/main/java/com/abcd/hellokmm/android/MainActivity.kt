package com.abcd.hellokmm.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abcd.hellokmm.DatabaseDriverFactory
import com.abcd.hellokmm.L
import com.abcd.hellokmm.Links
import com.abcd.hellokmm.Rocket
import com.abcd.hellokmm.RocketLaunch
import com.abcd.hellokmm.SpaceXSDK
import com.abcd.hellokmm.title
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val sdk = SpaceXSDK(DatabaseDriverFactory(this))
    private val mainScope = MainScope()
    private val launchesState = mutableStateListOf<RocketLaunch>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen(launchesState)
                }
            }
        }
        mainScope.launch {
            kotlin.runCatching {
                sdk.getLaunches(true)
            }.onSuccess {
                launchesState.addAll(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}

@Composable
fun HomeScreen(state: SnapshotStateList<RocketLaunch>) {
    Column(
        modifier = Modifier.fillMaxSize().background(
            Brush.linearGradient(
                listOf(
                    Color.DarkGray,
                    Color.Gray
                )
            )
        )
    ) {
        if (state.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize(), color = Color.Green)
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = L.general.home.title(),
                    fontSize = 26.sp,
                    color = Color.White
                )
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(state) { item: RocketLaunch ->
                        EntityRow(rocket = item)
                    }
                }
            }
        }
    }
}

@Composable
fun EntityRow(rocket: RocketLaunch) {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Name: ${rocket.rocket.name}")
        Text(text = "Status: ${rocket.launchSuccess}")
        Text(text = "Year: ${rocket.launchYear}")
        Text(text = "Details: ${rocket.details}")
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        val state: SnapshotStateList<RocketLaunch> = remember {
            mutableStateListOf<RocketLaunch>(
                RocketLaunch(
                    10,
                    "Test mission",
                    2022,
                    "",
                    Rocket("", "Houston rockets", ""),
                    "Houston rockets used to be good, but then harden left...don't know why, but he did :(",
                    false,
                    Links(null, null)
                ),
                RocketLaunch(
                    10,
                    "Test mission",
                    2022,
                    "",
                    Rocket("", "Houston rockets", ""),
                    "Houston rockets used to be good, but then harden left...don't know why, but he did :(",
                    false,
                    Links(null, null)
                )
            )
        }
        HomeScreen(state)
    }
}

@Preview
@Composable
fun RowPreview() {
    EntityRow(
        rocket = RocketLaunch(
            10,
            "Test mission",
            2022,
            "",
            Rocket("", "Houston rockets", ""),
            "Houston rockets used to be good, but then harden left...don't know why, but he did :(",
            false,
            Links(null, null)
        )
    )
}
