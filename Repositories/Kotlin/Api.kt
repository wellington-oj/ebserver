import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

private const val FRAMEWORK = "JAVA"
private const val TEST_TYPE = "benchmarkGame"
private const val ACTIVITY = ".MainActivity"

interface Api {
    @GET("/what_now")
    suspend fun started(
        @Header("device") device: String,
        @Header("application_id") appId: String,
        @Header("test_type") currentTestType: String = TEST_TYPE,
        @Header("activity") activity: String = ACTIVITY,
        @Header("framework") framework: String = FRAMEWORK
    ): Response<String>

    @GET("/logdata")
    suspend fun logData(
        @Header("device") device: String,
        @Header("application_id") appId: String,
        @Header("test_type") currentTestType: String = TEST_TYPE,
        @Header("activity") activity: String = ACTIVITY,
        @Header("framework") framework: String = FRAMEWORK
    ): Response<String>

    @GET("/done")
    suspend fun done(
        @Header("device") device: String,
        @Header("application_id") appId: String,
        @Header("test_type") currentTestType: String = TEST_TYPE,
        @Header("activity") activity: String = ACTIVITY,
        @Header("framework") framework: String = FRAMEWORK
    ): Response<String>

    @GET("/")
    suspend fun test(): Response<String>
}