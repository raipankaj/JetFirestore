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
implementation 'com.github.raipankaj:JetFirestore:1.0.2'
```

Congratulations, you have successfully added the dependency. 
Now to get started with JetFirestore add the following code snippet
```kotlin
JetFirestore(
    	path = { collection("books").document("6RaxAU1WrXadOHlBaMUk") },
	queryOnCollection = { orderBy("author", Query.Direction.DESCENDING) },
	onSingleTimeDocumentFetch = { values,  exception ->
		books = values.getObject()
	}
) {
	Text(books.author)
}
```
<br>
Here are all the parameters accepted by JetFirestore composable.

```kotlin
@Composable
fun JetFirestore(
    path: FirebaseFirestore.() -> Any,
    queryOnCollection: (CollectionReference.() -> Query)? = null,
    onSingleTimeCollectionFetch: ((QuerySnapshot?, Exception?) -> Unit)? = null,
    onSingleTimeDocumentFetch: ((DocumentSnapshot?, Exception?) -> Unit)? = null,
    onRealtimeCollectionFetch: ((QuerySnapshot?, FirebaseFirestoreException?) -> Unit)? = null,
    onRealtimeDocumentFetch: ((DocumentSnapshot?, FirebaseFirestoreException?) -> Unit)? = null,
    content: @Composable () -> Unit
)
```

</br>
Here is a code snippet to show default toast

```kotlin
@Composable
fun DefaultToast() {
    var showToast by remember {
        mutableStateOf(false)
    }

    Column(Modifier.height(80.dp)) {

        Text("Default Toast", Modifier.fillMaxWidth().clickable {
                showToast = true
        }, textAlign = TextAlign.Center)
        Toast(
            message = "Simple Toast",
            showToast = showToast,
            afterToastShown = { showToast = it },
        )
    }
}
```

<br>
You can also show toast like a snackbar

```kotlin
Surface(color = MaterialTheme.colors.background) {

  var books by remember {
        mutableStateOf<Books?>(null)
  }

  JetFirestore(path = { collection("books") },
              queryOnCollection = { orderBy("author", Query.Direction.DESCENDING) },
              onRealtimeCollectionFetch = { values,  exception ->
                            books = values.getObject()
              }
  ) {
        books?.let {
                Card(modifier = Modifier.fillMaxWidth().padding(12.dp), elevation = 12.dp) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = it.name)
                        Text(text = it.author)
                    }
                }
        } ?: Text(text = "Please wait...")
    }
}
```
<br>
Note: If you like this library, then please hit the star button! :smiley:

