# JetFirestore
[![](https://jitpack.io/v/raipankaj/JetFirestore.svg)](https://jitpack.io/#raipankaj/JetFirestore)
<br>
Add Cloud Firestore to your Android apps built with Jetpack Compose

Now with Jetpack Compose you can easily add Cloud Firestore to your existing app with just a few lines of code.

To get started with JetFirestore just add the maven url and the dependency

<b>build.gradle (Project level)</b>
```groovy
allprojects {
    repositories {
    ...
    //Add this url
    maven { url 'https://jitpack.io' }
    }
}
```
If you are using Android Studio Arctic Fox and do not have allProjects in build.gradle then add following maven url in <b>settings.gradle</b> like below
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        //Add this url
        maven { url 'https://jitpack.io' }
        jcenter() // Warning: this repository is going to shut down soon
    }
}
```

Once you have added the maven url now add the Stories dependency in the <b>build.gradle (module level)</b>
```groovy
implementation 'com.github.raipankaj:JetFirestore:1.0.3'
```

Congratulations, you have successfully added the dependency. 
Now to get started with JetFirestore add the following code snippet
```kotlin

var booksList by remember { mutableStateOf(listOf<Books>()) }

JetFirestore(
    	path = { collection("books") },
	queryOnCollection = { orderBy("author", Query.Direction.DESCENDING) },
	onRealtimeCollectionFetch = { values,  exception ->
		booksList = values.getListOfObjects()
	}
) {
	Text(...)
}
```
<br>
Here are all the parameters accepted by JetFirestore composable.

```kotlin
@Composable
fun JetFirestore(
    path: FirebaseFirestore.() -> Any,
    limitOnSingleTimeCollectionFetch: Long = 0,
    queryOnCollection: (CollectionReference.() -> Query)? = null,
    onSingleTimeCollectionFetch: ((QuerySnapshot?, Exception?) -> Unit)? = null,
    onSingleTimeDocumentFetch: ((DocumentSnapshot?, Exception?) -> Unit)? = null,
    onRealtimeCollectionFetch: ((QuerySnapshot?, FirebaseFirestoreException?) -> Unit)? = null,
    onRealtimeDocumentFetch: ((DocumentSnapshot?, FirebaseFirestoreException?) -> Unit)? = null,
    content: @Composable (Pagination) -> Unit
)
```

</br>
Here is a code snippet to show list of documents based on pagination

```kotlin
@Composable
fun BooksInformation() {
    var booksList by remember { mutableStateOf(listOf<Books>()) }

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
```
<br>
Note: If you like this library, then please hit the star button! :smiley:

