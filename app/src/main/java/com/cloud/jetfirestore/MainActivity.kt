package com.cloud.jetfirestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cloud.jetfirestore.data.Books
import com.cloud.jetfirestore.ui.theme.JetFirestoreTheme
import com.google.firebase.firestore.Query
import com.jet.firestore.JetFirestore
import com.jet.firestore.getObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetFirestoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    var bookList by remember {
                        mutableStateOf<List<Books>?>(null)
                    }

                    var books by remember {
                        mutableStateOf<Books?>(null)
                    }

                    JetFirestore(
                        path = { collection("books").document("6RaxAU1WrXadOHlBaMUk") },
                        queryOnCollection = { orderBy("author", Query.Direction.DESCENDING) },
                        onSingleTimeDocumentFetch = { values,  exception ->
                            books = values.getObject()
                        }
                    ) {
                        books?.let {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                elevation = 12.dp
                            ) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = it.name)
                                    Text(text = it.author)
                                }
                            }
                        } ?: Text(text = "Please wait...")
                    }
                }
            }
        }
    }
}
