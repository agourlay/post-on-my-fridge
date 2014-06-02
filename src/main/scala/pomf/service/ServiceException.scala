package pomf.service

class PostNotFoundException(val postId : Long) extends Exception(s"Post $postId does not exist")

class FridgeAlreadyExistsException(val fridgeName : String) extends Exception(s"Fridge $fridgeName already exists")