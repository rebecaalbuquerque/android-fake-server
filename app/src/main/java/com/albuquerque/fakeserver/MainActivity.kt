package com.albuquerque.fakeserver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.albuquerque.fakeserver.databinding.ActivityMainBinding
import com.albuquerque.fakeserver.service.MyApiService
import com.albuquerque.fakeserver.service.MyRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val service: MyApiService by inject()
    private val repository = MyRepository(service)
    private val disposable = CompositeDisposable()

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        fetchData()
    }

    private fun fetchData() {
        disposable.add(
            repository.fetchFakeLoremIpsum(delay = 2500)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { binding?.fakeServerResponse?.text = getString(R.string.loading) }
                .subscribe({ response ->
                    binding?.fakeServerResponse?.text = formatJson(response.string())
                }, { throwable ->
                    binding?.fakeServerResponse?.text = throwable.message
                })
        )

        disposable.add(
            repository.fetchLoremIpsum()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { binding?.fakeServerResponse?.text = getString(R.string.loading) }
                .subscribe({ response ->
                    binding?.realServerResponse?.text = formatJson(response.string())
                }, { throwable ->
                    binding?.realServerResponse?.text = throwable.message
                })
        )
    }

    private fun formatJson(string: String): String {
        return try {
            JSONObject(string).toString(4)
        } catch (e: Exception) {
            string
        }
    }
}