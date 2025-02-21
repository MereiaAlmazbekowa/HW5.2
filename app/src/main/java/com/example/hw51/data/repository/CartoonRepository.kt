package com.example.hw51.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hw51.data.api.ApiService
import com.example.hw51.data.model.BaseResponse
import com.example.hw51.data.model.Character
import com.example.hw51.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CartoonRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getAllCharacters(): LiveData<Resource<List<Character>>> {
        val data = MutableLiveData<Resource<List<Character>>>()

        data.postValue(Resource.Loading())

        apiService.getCharacters().enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                data.postValue(Resource.Success(response.body()!!.characters))
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                data.postValue(Resource.Error(t.message ?: "Unknown Error"))
            }
        })
        return data
    }

    fun getCharactersById(id: Int): LiveData<Resource<Character>> {
        val data = MutableLiveData<Resource<Character>>()
        data.postValue(Resource.Loading())

        apiService.getCharacterById(id).enqueue(object : Callback<Character> {
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        data.postValue(Resource.Success(it))
                    } ?: data.postValue(Resource.Error("Empty response from server"))
                } else {
                    data.postValue(Resource.Error("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                data.postValue(Resource.Error(t.message ?: "Unknown Error"))
            }
        })
        return data
    }
}