package com.example.learnproj

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

                val APIKEY = "LÄGG API-NYCKEL HÄR"
                val url = "https://api.rawg.io/api/games?key=$APIKEY&page_size=100"

            val jsonRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null, { response ->
                    val results = response.getJSONArray("results")
                    val gameList = mutableListOf<Games?>()

                    for(i in 0 until results.length()) {
                        val obj = results.getJSONObject(i)
                        val id = obj.getInt("id")
                        val name = obj.getString("name")
                        val description = obj.optString("metacritic")
                        val imageUrl = obj.getString("background_image")

                        gameList.add(Games(id, name, description, imageUrl))

                    }
                    games = gameList
                    Log.d("API Response", response.toString())


                },
                {error ->
                    Log.e("Volley error", error.toString())
                }
            )
            queue.add(jsonRequest)


            LearnProjTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier,
        contentPadding = PaddingValues(bottom = 60.dp, top = 35.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        items(games) { game ->
            Card (modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(Color(0xFFB8E7F4))
            ){
                Column(modifier = Modifier.padding(16.dp)){
                Text(text = game.name, style = MaterialTheme.typography.titleMedium)
                    //Spacer(modifier = Modifier.height(4.dp))
                AsyncImage(
                    model = game.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                    Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Metacritic rating: ${game.description}",
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
                //HorizontalDivider(Modifier, thickness = 4.dp, color = Color.Black)
                }
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
