# Fake Server
Imagine during the development of your app being able to configure the response of your endpoints quickly, without having to worry about setting up software like Mockoon or Flipper. 

Fake Server is a library for Android development that facilitates creating a fake server for testing and development purposes. It allows you to register fake responses for different URLs and return these responses during testing or development.

### Installation
Add the JitPack repository to your build.gradle (at the project level):
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your build.gradle (at the module level):
```gradle
dependencies {
  implementation 'com.github.rebecaalbuquerque:android-fake-server:$version'
}
```

## Usage

### Initialize FakeServer
Preferably, initialize it in the Application for a global configuration.
```kotlin
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        FakeServer.initialize(this)
    }
}
```

### (Optional) Register a fake response:
```kotlin
FakeServer.registerResponse(
  endpoint = "path-to-endpoint",
  response = """
      {
          "id": 1,
          "name": "test"
      }
  """
)
```

### Create your service layer
```kotlin
interface MyApiService {

  @GET("fake-server/lorem-ipsum")
  fun getFakeLoremIpsum(
      @Header(FakeServer.HEADER_DELAY) delay: Int? = null,
      @Header(FakeServer.HEADER_STATUS_CODE) statusCode: String? = null
  ): Single<ResponseBody>

  @GET("lorem-ipsum")
  fun getLoremIpsum(): Single<ResponseBody>
}
```

### Set up the Activity/Fragment
To use Fake Server, you need to add the FakeServerInterceptor to your OkHttp client. `delay` and `statusCode` are optional.
```kotlin
class MainActivity : AppCompatActivity() {

  private val apiService: MyApiService by lazy {
      val client = OkHttpClient.Builder()
        .addInterceptor(FakeServerInterceptor())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
  
      val retrofit = Retrofit.Builder()
          .baseUrl("https://your-base-url/")
          .cliet(client)
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .build()

      retrofit.create(MyApiService::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      binding = ActivityMainBinding.inflate(layoutInflater)
      setContentView(binding?.root)

      fetchFakeData()
      fetchData()
  }

  private fun fetchFakeData() {
      apiService.getFakeLoremIpsum(delay = 2500, statusCode = 123)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({ response ->
              // Handle the response
          }, { throwable ->
              // Handle the failure
          })
  }

  private fun fetchData() {
      apiService.getLoremIpsum()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({ response ->
              // Handle the response
          }, { throwable ->
              // Handle the failure
          })
  } 
}
```


### Screenshots/GIF
<img src="https://github.com/rebecaalbuquerque/android-fake-server/assets/41158713/210bbbb6-8169-4508-855b-73010f09c298" width="350" />
