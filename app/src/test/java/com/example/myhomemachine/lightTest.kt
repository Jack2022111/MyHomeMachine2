//package com.example.myhomemachine
//
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import okhttp3.mockwebserver.MockResponse
//import okhttp3.mockwebserver.MockWebServer
//import org.junit.*
//import org.junit.runner.RunWith
//import kotlinx.coroutines.runBlocking
//import org.mockito.Mockito
//import org.mockito.ArgumentMatchers.anyString
//import retrofit2.Response
//
//@RunWith(AndroidJUnit4::class)
//class LightsTest {
//
//    @get:Rule
//    val activityRule = ActivityScenarioRule(MainActivity::class.java)
//
//    private val mockServer = MockWebServer()
//    private lateinit var mockLifxApiService: LifxApiService
//
//    @Before
//    fun setUp() {
//        mockServer.start()
//
//        mockServer.enqueue(
//            MockResponse()
//                .setResponseCode(200)
//                .setBody("""{"result":"ok"}""")
//        )
//
//        mockLifxApiService = Mockito.mock(LifxApiService::class.java)
//
//        runBlocking {
//            Mockito.`when`(
//                mockLifxApiService.setLightState(
//                    anyString(),
//                    anyString(),
//                    Mockito.any(LightState::class.java)
//                )
//            ).thenReturn(Unit) // If setLightState returns Unit
//        }
//    }
//
//    @After
//    fun tearDown() {
//        mockServer.shutdown()
//    }
//
//    @Test
//    fun testSetBrightness_sendsCorrectRequest() = runBlocking {
//        val testRetrofitClient = object : RetrofitClient() {
//            override val instance: LifxApiService
//                get() = mockLifxApiService
//        }
//        activityRule.scenario.onActivity { activity: MainActivity ->
//            activity.retrofitClient = testRetrofitClient
//            activity.setBrightness(0.8f)
//        }
//        Mockito.verify(mockLifxApiService).setLightState(
//            Mockito.eq(LIFX_SELECTOR),
//            Mockito.eq("Bearer $LIFX_API_TOKEN"),
//            Mockito.argThat { lightState ->
//                lightState.power == "on" &&
//                        lightState.brightness == 0.8f &&
//                        lightState.color?.isNotEmpty() == true
//            }
//        )
//    }
//
//    companion object {
//        private const val LIFX_API_TOKEN = "c30381e0c360262972348a08fdda96e118d69ded53ec34bd1e06c24bd37fc247"
//        private const val LIFX_SELECTOR = "all"
//    }
//}
