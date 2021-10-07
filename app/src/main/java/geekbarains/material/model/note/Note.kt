package geekbarains.material.model.note

data class Note(
    var id: Int = 0,
    var someText: String = "Text",
    var description: String? = "Description",
    var priority: Int = 0,
    var planet: Int = 0
) 