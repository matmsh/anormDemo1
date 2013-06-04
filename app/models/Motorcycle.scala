package models

import play.api.db.DB

case class Motorcycle( make: String, model: String, engineCapacity: Int)

object Motorcycle {

  import anorm.SQL
  import anorm.SqlQuery

  val sql: SqlQuery = SQL("select * from motorcycles order by make,model asc")

  import play.api.Play.current

  def getAll: List[Motorcycle] = DB.withConnection {

    implicit connection =>

      sql().map(row =>

        Motorcycle(row[String]("make"), row[String]("model"),
          row[Int]("engineCapacity"))).toList
  }

  def insert(motorcycle: Motorcycle): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("""insert
            into motorcycles
            values ({make}, {model}, {engineCapacity})""").on(
            "make" -> motorcycle.make,
            "model" -> motorcycle.model,
            "engineCapacity" -> motorcycle.engineCapacity).executeUpdate()==1
    }
  }
  
  
  
  
  def findByMakeAndModel(motorcycle:Motorcycle) = DB.withConnection { implicit connection =>
    val sql = SQL("select * from motorcycles where make={make} and model={model}").
      on("make" -> motorcycle.make,
         "model" -> motorcycle.model
      )
      
     sql().map(row =>
        Motorcycle(row[String]("make"), row[String]("model"),
          row[Int]("engineCapacity"))).headOption
            
  }

}






