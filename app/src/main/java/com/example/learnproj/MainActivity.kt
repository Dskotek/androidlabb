package com.example.learnproj

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.learnproj.data.Games
import com.example.learnproj.ui.theme.LearnProjTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val queue = Volley.newRequestQueue(application)
        enableEdgeToEdge()
        setContent {
            var games by remember { mutableStateOf<List<Games?>>(emptyList()) }

                val APIKEY = ""
                val url = "https://api.rawg.io/api/games?key=$APIKEY&page_size=100"

            val jsonRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null, { response ->
                    val results = response.getJSONArray("results")
                    val gameList = mutableListOf<Games>()

                    for(i in 0 until results.length()) {
                        val obj = results.getJSONObject(i)
                        val id = obj.getInt("id")
                        val name = obj.getString("name")
                        val description = obj.getString("description")
                        val imageUrl = obj.getString("background_image")

                        gameList.add(Games(id, name, description, imageUrl))
                    }
                    games = gameList


                },
                {error ->
                    Log.e("Volley error", error.toString())
                }
            )
            queue.add(jsonRequest)

            LearnProjTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    contentColor = Color(0xFF121212)) { innerPadding ->
                    ShowGames(
                        games = games as List<Games>,
                        modifier = Modifier.padding(innerPadding)

                    )
                }
            }
        }
    }
}

@Composable
fun ShowGames(games: List<Games>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier
    ){
        items(games) { game ->
            Column(modifier = Modifier.padding(16.dp)){
                Text(text = game.name, style = MaterialTheme.typography.titleMedium)
                AsyncImage(
                    model = game.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Text(
                    text = game.description,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
                HorizontalDivider(Modifier, thickness = 4.dp, color = Color.Black)
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameListPreview() {

        val sampleGames = listOf(
            Games(
                id = 1,
                name = "Game 1",
                description = "This is game 1",
                imageUrl = "https://picsum.photos/200/300"
            ),
            Games(
                id = 2,
                name = "Game 2",
                description = "This is game 2",
                imageUrl = "https://picsum.photos/200/300"
            )
        )
    LearnProjTheme {
        ShowGames(games = sampleGames)
    }
    }
