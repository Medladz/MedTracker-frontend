import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.util.ArrayList

data class AgendaFeed(
    val data: ArrayList<Agendadata>
)

data class Agendadata (
    val id: Int,
    val type: String,
    val attributes: Attributes
)

data class Attributes (
    val name: String,
    val consumedAt: String
)

//User Deserializer
class AgendaDeserializer : ResponseDeserializable<AgendaFeed> {
    override fun deserialize(content: String) = Gson().fromJson(content, AgendaFeed::class.java)
}
class DrugFeed (val data: List<drugdata>)

class drugdata (val id: Int, val type: String, val attributes: attributes)

class attributes (val name: String, val thumbnailURL:String)
