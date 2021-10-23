package com.example.jsonpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.jsonpractice.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

     fun requestAPI(v:View){
        CoroutineScope(IO).launch {
            val data = async {
                getResponse()
            }.await()

            if(data.isNotEmpty()){

                if(binding.etNumber.text.isNotEmpty())
                getContacts(data,binding.etNumber.text.toString().toInt())
                else
                Toast.makeText(this@MainActivity, "Enter a number", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(this@MainActivity, "Unable to load data", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun getContacts(data:String, num:Int) {
        withContext(Main) {
            val contacts =JSONArray(data)
            if(num<contacts.length()) {
                binding.textView.text =
                    "Name: " + contacts.getJSONObject(num).getString("name") + "\nLocation:" +
                            " " + contacts.getJSONObject(num).getString("location") + "\nMobile:" +
                            " " + contacts.getJSONObject(num).getString("mobile") + "\nEmail:" +
                            " " + contacts.getJSONObject(num).getString("email")
            }else
                Toast.makeText(this@MainActivity, "index $num not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getResponse(): String{
        var response = ""
        try{
            response = URL("https://dojo-recipes.herokuapp.com/contacts/")
                .readText(Charsets.UTF_8)
        }catch(e: Exception){
            Toast.makeText(this@MainActivity, "$e", Toast.LENGTH_LONG).show()
        }
        return response
    }






}
