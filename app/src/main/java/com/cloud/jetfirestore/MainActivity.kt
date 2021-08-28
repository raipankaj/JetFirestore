package com.cloud.jetfirestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cloud.jetfirestore.data.Books
import com.cloud.jetfirestore.ui.theme.JetFirestoreTheme
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jet.firestore.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetFirestoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    var booksList by remember {
                        mutableStateOf(listOf<Books>())
                    }

                    JetFirestore(
                        path = { collection("books") },
                        limitOnSingleTimeCollectionFetch = 2,
                        queryOnCollection = { orderBy("cost", Query.Direction.DESCENDING) },
                        onSingleTimeCollectionFetch = { values,  exception ->
                            //When all documents are fetched
                            //booksList = values.getListOfObjects()

                            //When documents are fetched based on limit
                            booksList = booksList + values.getListOfObjects()
                        }
                    ) { pagination ->
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            ListItem(booksList)

                            Row {
                                Button(onClick = { pagination.loadNextPage() }, modifier = Modifier.fillMaxWidth()) {
                                    Text("Next")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ListItem(booksList: List<Books>) {
        LazyColumn {
            items(booksList) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    elevation = 12.dp
                ) {
                    Column {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = it.name)
                            Text(text = it.author)
                        }
                        Text(text = it.cost.toString())
                    }
                }
            }
        }
    }
}
