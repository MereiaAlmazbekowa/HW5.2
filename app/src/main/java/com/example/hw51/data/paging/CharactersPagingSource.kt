package com.example.hw51.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.hw51.data.api.ApiService
import com.example.hw51.data.model.Character
import javax.inject.Inject

const val STARTING_INDEX = 1

class CharactersPagingSource @Inject constructor(
    private val api: ApiService
): PagingSource<Int, com.example.hw51.data.model.Character>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.example.hw51.data.model.Character> {
        return try {
            val currentPosition = params.key ?: STARTING_INDEX
            val response = api.getCharactersPaging(page = currentPosition)

            if (response.isSuccessful && response.body() != null) {
                LoadResult.Page(
                    prevKey = if (currentPosition == STARTING_INDEX) null else currentPosition - 1,
                    nextKey = if (response.body()!!.info?.pages == currentPosition) null else currentPosition + 1,
                    data = response.body()!!.characters ?: emptyList()
                )
            } else {
                LoadResult.Error(throw Exception("Error"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}