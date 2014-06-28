package pomf.service

import java.util.UUID

class PostNotFoundException(val postId : UUID) extends Exception(s"Post $postId does not exist")

class FridgeNotFoundException(val fridgeId : UUID) extends Exception(s"Fridge $fridgeId does not exist")

class FridgeAlreadyExistsException(val fridgeId : UUID) extends Exception(s"Fridge $fridgeId already exists")

class ChatRoomNotFoundException(val fridgeId : UUID) extends Exception(s"ChatRoom $fridgeId does not exist")
