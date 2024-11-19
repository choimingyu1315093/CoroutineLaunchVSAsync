package com.example.coroutinelaunchvsasync

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coroutinelaunchvsasync.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //첫 번째 실험(suspend fun은 순서대로 실행할까?) - yes
        binding.btn1.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                getRandom1()
                getRandom2()
            }
        }

        //두 번째 실험(launch는 순서대로 실행할까?)
        binding.btn2.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                //이렇게 테스트 하면 순서대로 ok 1->2
//                launch {
//                    getRandom1()
//                    getRandom2()
//                }

                //이렇게 테스트 하면 순서대로 no 2->1
                launch {
                    getRandom1()
                }
                launch {
                    getRandom2()
                }
            }
        }

        binding.btn3.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                //이렇게 테스트 하면 순서대로 ok 1->2
//                val first = async {
//                    getRandom1()
//                    getRandom2()
//                }

                //이렇게 테스트 하면 순서대로 no 2->1
                val first = async {
                    getRandom1()
                }
                val second = async {
                    getRandom2()
                }
            }
        }
    }

    //결론은 하나의 코루틴 빌더(코루틴을 만드는 함수를 코루틴 빌더)에 코드를 넣으면 순서대로 작업한다.
    //코루틴 빌더를 여러개 만들면 순서대로 작업하지 않는다.

    suspend fun getRandom1(): Int {
        delay(3000L)
        Log.d(TAG, "getRandom1: 호출")
        return Random.nextInt(0, 500)
    }

    suspend fun getRandom2(): Int {
        delay(800L)
        Log.d(TAG, "getRandom2: 호출2")
        return Random.nextInt(0, 500)
    }
}