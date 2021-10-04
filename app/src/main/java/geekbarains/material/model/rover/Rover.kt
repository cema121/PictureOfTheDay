package geekbarains.material.model.rover

import java.util.*

data class Rover(
    val name: String,
    val dateStart: String,
    val dateEnd: String,
    val cameras: List<String>
)