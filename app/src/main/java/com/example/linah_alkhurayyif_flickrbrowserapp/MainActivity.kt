package com.example.linah_alkhurayyif_flickrbrowserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var images_List: ArrayList<FlickrImage>
    private lateinit var imageAdapter: ImageRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        images_List = arrayListOf()
        imageAdapter= ImageRVAdapter(this, images_List)
        recyclerView.adapter = imageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchbtn.setOnClickListener {
            if(search_et.text.isEmpty()){
                Toast.makeText(this, "Search field should not be empty", Toast.LENGTH_LONG).show()

            }else{
                if(images_List.isNotEmpty()){
                    images_List.clear()
                    imageAdapter.notifyDataSetChanged()
                }

                requestAPI()
            }
        }
        image.setOnClickListener {
            image.isVisible = false
            recyclerView.isVisible = true
            searchLayout.isVisible = true
        }
    }

    private fun requestAPI(){
        CoroutineScope(IO).launch {
            val data = async { getImages() }.await()
            if(data.isNotEmpty()){
                showPhotos(data)
            }else{
                Toast.makeText(this@MainActivity, "No Images Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getImages(): String{
        var response = ""
        try{
            response = URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&per_page=100&api_key=8b3fbe8baef12198dd01e01d138e548c&tags=${search_et.text}&format=json&nojsoncallback=1")
                .readText(Charsets.UTF_8)
        }catch(e: Exception){
            println("Error: $e")
        }
        return response
    }

    private suspend fun showPhotos(data: String){
        withContext(Main){
            val jsonObj = JSONObject(data)
            val photos = jsonObj.getJSONObject("photos").getJSONArray("photo")

            for(i in 0 until photos.length()){
                val Image_title = photos.getJSONObject(i).getString("title")
                val Image_farmID = photos.getJSONObject(i).getString("farm")
                val Image_serverID = photos.getJSONObject(i).getString("server")
                val Image_id = photos.getJSONObject(i).getString("id")
                val Image_secret = photos.getJSONObject(i).getString("secret")
                val Image_photoLink = "https://farm$Image_farmID.staticflickr.com/$Image_serverID/${Image_id}_$Image_secret.jpg"
                images_List.add(FlickrImage(Image_title, Image_photoLink))
            }
            imageAdapter.notifyDataSetChanged()
        }
    }

    fun openImg(link: String){
        Glide.with(this).load(link).into(image)
        image.isVisible = true
        recyclerView.isVisible = false
        searchLayout.isVisible = false
    }
}