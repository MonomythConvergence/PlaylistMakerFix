package com.example.playlistmaker

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
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
    private lateinit var recyclerResultsView: RecyclerView
    private lateinit var recyclerRecentView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter


    companion object {
        private const val USER_INPUT = "userInput"
        var recentAdapter = SearchAdapter(arrayListOf<Track>(), SearchActivity())
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


        recyclerResultsView = findViewById(R.id.searchResultsRecycler)
        searchAdapter = SearchAdapter(trackList, this)
        recyclerResultsView.adapter = searchAdapter
        recyclerResultsView.layoutManager = LinearLayoutManager(this)

        recyclerRecentView = findViewById(R.id.recentRecycler)
        recentAdapter = SearchAdapter(recentTracksList, this)
        recyclerRecentView.adapter = recentAdapter
        recyclerRecentView.layoutManager = LinearLayoutManager(this)


        val searchBarField = findViewById<EditText>(R.id.searchBarField)
        val searchBarClear = findViewById<ImageButton>(R.id.searchBarClear)
        val searchRefresh = findViewById<Button>(R.id.searchRefresh)
        val noConnectionError = findViewById<LinearLayout>(R.id.noConnectionError)
        val noResultsError = findViewById<LinearLayout>(R.id.noResultsError)


        val recentSearchFrame = findViewById<LinearLayout>(R.id.recentSearchFrame)
        val clearSearchHistory = findViewById<Button>(R.id.clearSearchHistory)



        if (savedInstanceState != null) {
            searchBarField.setText(userInputReserve)
        }

        fun setLayoutVisibility(
            recentSearchFrame: View,
            searchRecycler: View,
            noConnectionError: View,
            noResultsError: View,
            isSearchHistoryVisible: Boolean,
            isSearchVisible: Boolean,
            isNoConnectionErrorVisible: Boolean,
            isNoResultsErrorVisible: Boolean
        ) {
            recentSearchFrame.visibility = if (isSearchHistoryVisible) View.VISIBLE else View.GONE
            searchRecycler.visibility = if (isSearchVisible) View.VISIBLE else View.GONE
            noConnectionError.visibility =
                if (isNoConnectionErrorVisible) View.VISIBLE else View.GONE
            noResultsError.visibility = if (isNoResultsErrorVisible) View.VISIBLE else View.GONE
        }

        searchBarField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchBarField.text.isEmpty() && recentTracksList.isNotEmpty()) {
                setLayoutVisibility(
                    recentSearchFrame,
                    recyclerResultsView,
                    noConnectionError,
                    noResultsError,
                    true,
                    false,
                    false,
                    false
                )

            }

        }


        fun handleSearch() {
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

                            trackList.clear()

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
                                        setLayoutVisibility(
                                            recentSearchFrame,
                                            recyclerResultsView,
                                            noConnectionError,
                                            noResultsError,
                                            false,
                                            false,
                                            false,
                                            true
                                        )
                                    } else {
                                        for (result in searchResults) {

                                            trackList.add(
                                                Track(
                                                    result.trackName,
                                                    result.artistName,
                                                    SimpleDateFormat(
                                                        "mm:ss",
                                                        Locale.getDefault()
                                                    ).format(
                                                        result.trackTimeMillis
                                                    ),
                                                    result.artworkUrl100,
                                                    result.trackId,
                                                    result.collectionName,
                                                    if (result.releaseDate.length >= 4) {
                                                        result.releaseDate.substring(0, 4)
                                                    } else {""},
                                                    result.primaryGenreName,
                                                    result.country
                                                )
                                            )

                                        }
                                        Log.d("MyTag", "New tracklist filled. Display results")
                                        setLayoutVisibility(
                                            recentSearchFrame,
                                            recyclerResultsView,
                                            noConnectionError,
                                            noResultsError,
                                            false,
                                            true,
                                            false,
                                            false
                                        )
                                        searchAdapter.notifyDataSetChanged()

                                    }
                                } else {
                                    Log.d(
                                        "MyTag",
                                        "IF4 else. Display noResultsError, " +
                                                "but it's just a safety measure against null"
                                    )
                                    setLayoutVisibility(
                                        recentSearchFrame,
                                        recyclerResultsView,
                                        noConnectionError,
                                        noResultsError,
                                        false,
                                        false,
                                        false,
                                        true
                                    )
                                }
                            } else {
                                Log.d("MyTag", "IF3 else. Null results. Display noResultsError")
                                setLayoutVisibility(
                                    recentSearchFrame,
                                    recyclerResultsView,
                                    noConnectionError,
                                    noResultsError,
                                    false,
                                    false,
                                    false,
                                    true
                                )
                            }

                        } else {
                            Log.d("MyTag", "IF2 else. Code!=200. Display noConnectionError")
                            setLayoutVisibility(
                                recentSearchFrame,
                                recyclerResultsView,
                                noConnectionError,
                                noResultsError,
                                false,
                                false,
                                true,
                                false
                            )
                        }
                    } else {
                        Log.d("MyTag", "IF1 else. Unsuccessful response. Display noConnectionError")
                        setLayoutVisibility(
                            recentSearchFrame,
                            recyclerResultsView,
                            noConnectionError,
                            noResultsError,
                            false,
                            false,
                            true,
                            false
                        )
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("MyTag", "onFailure. Display noConnectionError")
                    setLayoutVisibility(
                        recentSearchFrame,
                        recyclerResultsView,
                        noConnectionError,
                        noResultsError,
                        false,
                        false,
                        true,
                        false
                    )
                }
            })
        }

        clearSearchHistory.setOnClickListener {
            recentTracksList.clear()
            SearchHistory(App.recentTracksSharedPreferences).encodeAndSave()
            setLayoutVisibility(
                recentSearchFrame,
                recyclerResultsView,
                noConnectionError,
                noResultsError,
                false,
                false,
                false,
                false
            )
            recentAdapter.notifyDataSetChanged()
        }

        searchBarField.doOnTextChanged { text, start, before, count ->
            if (text?.isNotEmpty() == true) {
                searchBarClear.visibility = View.VISIBLE
            } else {
                searchBarClear.visibility = View.INVISIBLE
                if (recentTracksList.isNotEmpty()) {
                    setLayoutVisibility(
                        recentSearchFrame,
                        recyclerResultsView,
                        noConnectionError,
                        noResultsError,
                        true,
                        false,
                        false,
                        false
                    )
                }
                if (recentTracksList.isEmpty()) {
                    setLayoutVisibility(
                        recentSearchFrame,
                        recyclerResultsView,
                        noConnectionError,
                        noResultsError,
                        false,
                        false,
                        false,
                        false
                    )
                }

            }
            userInputReserve = text.toString()
        }


        searchBarField.requestFocus()



        searchBarClear.setOnClickListener {
            searchBarField.text.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchBarField.windowToken, 0)
        }


        val backButton = findViewById<ImageButton>(R.id.backButton)

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
