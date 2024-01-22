package com.example.playlistmaker

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    private var userInputReserve = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter


    companion object {
        private const val USER_INPUT = "userInput"
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_INPUT, userInputReserve)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userInputReserve = savedInstanceState.getString(USER_INPUT, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.searchRecycler)
        adapter = SearchAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val searchBarField = findViewById<EditText>(R.id.searchBarField)
        val searchBarClear = findViewById<ImageButton>(R.id.searchBarClear)
        val searchRefresh = findViewById<Button>(R.id.searchRefresh)
        val noConnectionError = findViewById<LinearLayout>(R.id.noConnectionError)
        val noResultsError = findViewById<LinearLayout>(R.id.noResultsError)
        val searchRecycler = findViewById<RecyclerView>(R.id.searchRecycler)

        if (savedInstanceState != null) {
            searchBarField.setText(userInputReserve)
        }

        fun handleSearch() {

            fun setLayoutVisibility(searchRecycler: View, noConnectionError: View, noResultsError: View, isSearchVisible: Boolean, isNoConnectionErrorVisible: Boolean, isNoResultsErrorVisible: Boolean) {
                searchRecycler.visibility = if (isSearchVisible) View.VISIBLE else View.GONE
                noConnectionError.visibility = if (isNoConnectionErrorVisible) View.VISIBLE else View.GONE
                noResultsError.visibility = if (isNoResultsErrorVisible) View.VISIBLE else View.GONE
            }


            val gson = Gson()

            val rawQuery = searchBarField.text.toString()
            val entity = "song"

            val apiClient = ApiClient()
            val call = apiClient.apiService.searchQuery(rawQuery, entity)



            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            Track.trackList.clear()
                            if (response.body() != null) {
                                val parsedResponse = gson.fromJson(
                                    response.body(),
                                    searchServerResonse::class.java
                                )
                                val searchResults = parsedResponse?.results
                                if (searchResults != null) {
                                    if (parsedResponse.resultCount == 0) {
                                        Log.d(
                                            "MyTag",
                                            "0 results. Display noResultsError"
                                        )
                                        setLayoutVisibility(searchRecycler, noConnectionError, noResultsError, false, false, true)
                                    } else {
                                        for (result in searchResults) {
                                            Track.trackList.add(
                                                Track(
                                                    result.trackName,
                                                    result.artistName,
                                                    SimpleDateFormat(
                                                        "mm:ss",
                                                        Locale.getDefault()
                                                    ).format(
                                                        result.trackTimeMillis
                                                    ),
                                                    result.artworkUrl100
                                                )
                                            )
                                        }
                                        Log.d("MyTag", "New tracklist filled. Display results")
                                        setLayoutVisibility(searchRecycler, noConnectionError, noResultsError, true, false, false)
                                    }
                                } else {
                                    Log.d(
                                        "MyTag",
                                        "IF4 else. Display noResultsError, " +
                                                "but it's just a safety measure against null"
                                    )
                                    setLayoutVisibility(searchRecycler, noConnectionError, noResultsError, false, false, true)
                                }
                            } else {
                                Log.d("MyTag", "IF3 else. Null results. Display noResultsError")
                                setLayoutVisibility(searchRecycler, noConnectionError, noResultsError, false, false, true)
                            }
                            adapter.notifyDataSetChanged()
                        } else {
                            Log.d("MyTag", "IF2 else. Code!=200. Display noConnectionError")
                            setLayoutVisibility(searchRecycler, noConnectionError, noResultsError, false, true, false)
                        }
                    } else {
                        Log.d("MyTag", "IF1 else. Unsuccessful response. Display noConnectionError")
                        setLayoutVisibility(searchRecycler, noConnectionError, noResultsError, false, true, false)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("MyTag", "onFailure. Display noConnectionError")
                    setLayoutVisibility(searchRecycler, noConnectionError, noResultsError, false, true, false)
                }
            })
        }


        searchBarField.doOnTextChanged { text, start, before, count ->
            if (text?.isNotEmpty() == true) {
                searchBarClear.visibility = View.VISIBLE
            } else {
                searchBarClear.visibility = View.INVISIBLE
            }
            userInputReserve = text.toString()
        }

        searchBarClear.setOnClickListener {
            searchBarField.text.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchBarField.windowToken, 0)
        }

        val backButton = findViewById<ImageButton>(R.id.back_button)

        backButton.setOnClickListener {

            finish()
        }

        searchBarField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleSearch()
                true
            } else {
                false
            }


        }
        searchRefresh.setOnClickListener {
            handleSearch()
        }

    }


    }
