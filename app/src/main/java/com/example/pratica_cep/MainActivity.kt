package com.example.pratica_cep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pratica_cep.model.Endereco
import com.example.pratica_cep.model.EnderecoServiceIF
import com.example.pratica_cep.model.RetrofitClient
import com.example.pratica_cep.ui.theme.PraticacepTheme
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PraticacepTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CEPConsultationScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CEPConsultationScreen(modifier: Modifier = Modifier) {
    var cep by remember { mutableStateOf("58065000") } // Você pode deixar isso editável
    var endereco by remember { mutableStateOf<Endereco?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = cep,
            onValueChange = { cep = it },
            label = { Text("Digite o CEP") }
        )
        Button(onClick = {
            coroutineScope.launch {
                isLoading = true
                fetchEndereco(cep) { result ->
                    endereco = result
                    isLoading = false
                }
            }
        }) {
            Text("Buscar Endereço")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        } else {
            endereco?.let {
                Text("Logradouro: ${it.logradouro}")
                Text("Bairro: ${it.bairro}")
                Text("Cidade: ${it.localidade}")
                Text("UF: ${it.uf}")
            }
        }
    }
}

fun fetchEndereco(cep: String, callback: (Endereco?) -> Unit) {
    val service = RetrofitClient.getRetrofitInstance().create(EnderecoServiceIF::class.java)
    val call = service.buscarEndereco(cep)

    call.enqueue(object : Callback<Endereco> {
        override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
            if (response.isSuccessful) {
                callback(response.body())
            } else {
                callback(null)
            }
        }

        override fun onFailure(call: Call<Endereco>, t: Throwable) {
            callback(null)
        }
    })
}

@Preview(showBackground = true)
@Composable
fun CEPConsultationScreenPreview() {
    PraticacepTheme {
        CEPConsultationScreen()
    }
}
